"""AI Chat (starnest.aichat.aichatbot.assistant) 上游协议客户端 — 版本 128.0

协议来源: APK 128.0 静态还原 (Wc/i.java 拦截器 + ChatGPTService 接口) + 与 v133/v142 交叉核对
  Authorization: Signature kid=<KID>&algorithm=sha256&timestamp=<epoch秒>&validity=90&userId=&value=<sha256hex(KID+ts+"90")>
  app-token:     Bearer <JWT HS256(payload={token: b64(sha256(APK 签名证书 DER)), iat, exp=iat+90})>
  其余头:        package-id / device-id(16hex) / isUserInAdMode / isVip / chatModel / Accept-Language

128.0 端点全部落在 /api/v2/*（与 v133 相同；官方 142 才是 /api/v3/*）。
"""
from __future__ import annotations

import base64
import hashlib
import hmac
import json
import os
import random
import threading
import time
import urllib.parse
import uuid
from typing import Any, Iterable

import requests

BASE = os.environ.get("AICHAT_BASE", "https://api-aichat.starnestsolution.com")
BASE_ALT = "https://api-akeyboard.starnestsolution.com"
# 128.0 客户端全部用 /api/v2/*（官方 142 才是 /api/v3/*，同上游同鉴权）
API_VER = os.environ.get("AICHAT_API_VER", "v2")
KID = "36ccfe00-78fc-4cab-9c5b-5460b0c78513"
PACKAGE_ID = "starnest.aichat.aichatbot.assistant"
VALIDITY = "90"
UA = "okhttp/4.9.0"
HERE = os.path.dirname(os.path.abspath(__file__))

# 模型表来自 128.0 `Nc/EnumC1015l`（value / model / getChatModel 三源交叉）
#   公开名 -> (上游 body.model, 请求头 chatModel, 每日免费次数)
MODELS: dict[str, str] = {
    "gpt-3.5-turbo": "gpt-4o-mini-2024-07-18",
    "gpt_3_5": "gpt-4o-mini-2024-07-18",
    "gpt-4o-mini": "gpt-4o-mini-2024-07-18",
    "gpt-4o": "gpt-4o-2024-08-06",
    "gpt-5-nano": "gpt_5_nano",
    "gpt_5_nano": "gpt_5_nano",
    "gpt-5-mini": "gpt_5_mini",
    "gpt_5_mini": "gpt_5_mini",
    "gpt-4.1": "gpt-4.1",
    "gpt_4_1": "gpt-4.1",
    "gpt-5": "gpt_5",
    "gpt-5.2": "gpt_5_2",
    "gpt-5.4": "gpt_5_4",
    "gpt-5.6": "gpt_5_6_luna",
    "gpt-4": "gpt-4o-2024-08-06",
    "o3": "o3",
    "gpt-o3": "o3",
    "gemini": "gemini_lite",
    "gemini-3.1-flash": "gemini_3_1_flash",
    "gemini-3-flash": "gemini_3_flash",
    "grok-3-mini": "grok_3_mini",
    "grok-4.3": "grok_4_3",
    "deepseek-r1": "deepseek_reasoner",
    "deepseek-v3": "deepseek_chat",
    "claude-haiku-4.5": "claude_haiku",
}

# 请求头 chatModel 与 body.model 不同名，必须分开映射（取自 getChatModel() 的 switch）
CHAT_MODEL_HEADER: dict[str, str] = {
    "gpt-4o-mini-2024-07-18": "gpt_4o_mini",
    "gpt-4o-2024-08-06": "gpt_4o",
    "gpt-4.1": "gpt_4_1",
    "gpt_5_nano": "gpt_5_nano",
    "gpt_5_mini": "gpt_5_mini",
    "gpt_5": "gpt_5",
    "gpt_5_2": "gpt_5_2",
    "gpt_5_4": "gpt_5_4",
    "gpt_5_6_luna": "gpt_5_6_luna",
    "o3": "o3",
    "gemini_lite": "gemini_lite",
    "gemini_3_1_flash": "gemini_3_flash_lite_preview",
    "gemini_3_flash": "gemini_3_flash",
    "grok_3_mini": "grok_3_mini",
    "grok_4_3": "grok_4_3",
    "deepseek_reasoner": "deepseek_reasoner",
    "deepseek_chat": "deepseek_chat",
    "claude_haiku": "claude_haiku",
}

# 每日免费额度（128.0 `Nc/C1026u` 常量）
DAILY_FREE = {
    "gpt-4o-mini-2024-07-18": 50, "gpt-4o-2024-08-06": 50, "gpt-4.1": 50,
    "gpt_5": 50, "gpt_5_2": 20, "gpt_5_4": 5, "gpt_5_6_luna": 20,
    "o3": 20, "gemini_3_flash": 20, "grok_3_mini": 10, "grok_4_3": 5,
    "deepseek_reasoner": 50, "deepseek_chat": 100, "claude_haiku": 20,
}


def header_model(model: str) -> str:
    """公开名或上游 model 名 -> 请求头 chatModel 值"""
    upstream = MODELS.get(model, model)
    return CHAT_MODEL_HEADER.get(upstream, upstream)


def _b64(b: bytes) -> str:
    return base64.b64encode(b).decode()


def _cert_der_from_env() -> bytes | None:
    if os.environ.get("AICHAT_CERT_HEX"):
        return bytes.fromhex(os.environ["AICHAT_CERT_HEX"])
    p = os.path.join(HERE, "cert_0.der")          # 本版本 APK 签名证书（apkcert.py 提取）
    if os.path.exists(p):
        return open(p, "rb").read()
    for name in ("official_cert.der", "orig_cert.der"):
        q = os.path.join(HERE, name)
        if os.path.exists(q):
            return open(q, "rb").read()
    return None


def app_token_value(cert_der: bytes) -> str:
    """app-token JWT 的 token 字段 = base64(sha256(证书 DER))"""
    return _b64(hashlib.sha256(cert_der).digest())


def signature(kid: str = KID, ts: int | None = None) -> str:
    ts = ts or int(time.time())
    value = hashlib.sha256((kid + str(ts) + VALIDITY).encode()).hexdigest()
    pairs = [("kid", kid), ("algorithm", "sha256"), ("timestamp", str(ts)),
             ("validity", VALIDITY), ("userId", ""), ("value", value)]
    return "&".join(f"{urllib.parse.quote(k)}={urllib.parse.quote(v)}" for k, v in pairs)


def jwt_headers(token_value: str, kid: str = KID) -> str:
    now = int(time.time())
    head = _b64(json.dumps({"alg": "HS256", "typ": "JWT"}, separators=(",", ":")).encode())
    body = _b64(json.dumps({"token": token_value, "iat": now, "exp": now + 90},
                           separators=(",", ":")).encode())
    si = f"{head}.{body}"
    return si + "." + _b64(hmac.new(kid.encode(), si.encode(), hashlib.sha256).digest())


class DevicePool:
    """固定小池, 避免每请求随机设备 (防风控画像)。device-id 为 16 位 hex。"""

    def __init__(self, size: int = 8):
        self._lock = threading.Lock()
        self._devices = [uuid.uuid4().hex[:16] for _ in range(max(1, size))]
        self._idx = 0

    def next(self) -> str:
        with self._lock:
            d = self._devices[self._idx % len(self._devices)]
            self._idx += 1
            return d

    def size(self) -> int:
        return len(self._devices)


class AiChatClient:
    def __init__(self, cert_der: bytes | None = None, pool_size: int = 8,
                 proxy: str | None = None):
        if cert_der is None:
            cert_der = _cert_der_from_env()
        if cert_der is None:
            raise RuntimeError("缺少 APK 签名证书 (AICHAT_CERT_HEX / cert_0.der)")
        self.token_value = app_token_value(cert_der)
        self.pool = DevicePool(pool_size)
        self.proxy = proxy if proxy is not None else os.environ.get("AICHAT_PROXY", "")
        self.base = BASE

    # ---------- 传输 ----------
    def _session(self) -> requests.Session:
        s = requests.Session()
        s.trust_env = False
        if self.proxy:
            s.proxies = {"http": self.proxy, "https": self.proxy}
        return s

    def headers(self, device_id: str, chat_model: str = "gpt_4o_mini",
                stream: bool = True) -> dict[str, str]:
        return {
            "Authorization": "Signature " + signature(),
            "app-token": "Bearer " + jwt_headers(self.token_value),
            "package-id": PACKAGE_ID,
            "device-id": device_id,
            "isUserInAdMode": "false",
            "isVip": "true",
            "chatModel": chat_model,
            "Accept-Language": "en",
            "messageRequest": "0",
            "totalMessageRequest": "0",
            "Content-Type": "application/json; charset=UTF-8",
            "User-Agent": UA,
        }

    @staticmethod
    def body(messages: list[dict], model: str, stream: bool, max_tokens: int = 2000) -> dict:
        msgs = []
        for m in messages:
            c = m.get("content", "")
            if isinstance(c, list):
                msgs.append({"role": m.get("role", "user"),
                             "content": [{"type": "text", "text": p.get("text", "")}
                                         for p in c if p.get("type") == "text"]})
            else:
                msgs.append({"role": m.get("role", "user"), "content": str(c)})
        return {"isVip": True, "max_tokens": max_tokens, "messages": msgs,
                "stream": stream, "model": MODELS.get(model, model)}

    # ---------- 调用 ----------
    def chat(self, messages: list[dict], model: str = "gpt-4o-mini",
             stream: bool = False, max_tokens: int = 2000, timeout: int = 120,
             chat_model_header: str | None = None) -> requests.Response:
        path = f"/api/{API_VER}/completions/v3/stream" if stream else f"/api/{API_VER}/completions/v2"
        dev = self.pool.next()
        return self._session().post(
            self.base + path,
            headers=self.headers(dev, chat_model_header or header_model(model), stream),
            data=json.dumps(self.body(messages, model, stream, max_tokens)),
            timeout=timeout, stream=stream)

    def health(self) -> tuple[int, str]:
        r = self._session().get(self.base + "/", timeout=20)
        return r.status_code, r.text[:200]


def iter_sse(resp: requests.Response) -> Iterable[str]:
    """逐行产出 SSE 的 data 载荷 (去掉 'data: ' 前缀)"""
    for raw in resp.iter_lines(decode_unicode=True):
        if not raw:
            continue
        line = raw.strip()
        if line.startswith("data:"):
            line = line[5:].strip()
        if line:
            yield line


def extract_text(chunk: dict) -> str:
    """从上游 chunk 中尽量提取增量文本 (兼容 OpenAI 风格与自定义嵌套)"""
    if not isinstance(chunk, dict):
        return ""
    for key in ("choices",):
        ch = chunk.get(key)
        if isinstance(ch, list) and ch:
            c0 = ch[0]
            if isinstance(c0, dict):
                d = c0.get("delta") or {}
                if isinstance(d, dict) and d.get("content"):
                    return str(d["content"])
                if c0.get("text"):
                    return str(c0["text"])
                msg = c0.get("message") or {}
                if isinstance(msg, dict) and msg.get("content"):
                    return str(msg["content"])
    data = chunk.get("data")
    if isinstance(data, dict):
        t = data.get("content") or data.get("text") or data.get("delta")
        if isinstance(t, str):
            return t
        if isinstance(t, dict) and t.get("content"):
            return str(t["content"])
        return extract_text(data)
    if isinstance(data, str):
        return data
    for k in ("content", "text", "delta", "message"):
        v = chunk.get(k)
        if isinstance(v, str):
            return v
    return ""


if __name__ == "__main__":
    c = AiChatClient()
    print("token_value:", c.token_value, "pool:", c.pool.size())
    code, txt = c.health()
    print("health:", code, txt)
    r = c.chat([{"role": "user", "content": "Reply with exactly: PONG"}])
    print("chat:", r.status_code, r.text[:500])
