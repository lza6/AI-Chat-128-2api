"""AI Chat 2API demo server — OpenAI 兼容 (/v1/chat/completions)  ·  目标 APK 版本 128.0

模式:
  AICHAT_MODE=mock      (默认) 本地假回复, 用于 UI/压测联调
  AICHAT_MODE=upstream  真实转发 https://api-aichat.starnestsolution.com

环境变量:
  AICHAT_PORT       监听端口, 默认 8933
  AICHAT_MODE       mock | upstream
  AICHAT_PROXY      上游代理, 如 http://127.0.0.1:10808
  AICHAT_CERT_HEX   APK 签名证书 DER 的 hex (upstream 模式必需, 或放 cert_0.der)
  MOCK_LATENCY_MS   mock 模式模拟首字延迟, 默认 40
"""
from __future__ import annotations

import json
import os
import random
import threading
import time
import uuid
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

import aichat_client as ac

APP_VERSION = "128.0"
PORT = int(os.environ.get("AICHAT_PORT", "8933"))
MODE = os.environ.get("AICHAT_MODE", "mock").lower()
MOCK_LATENCY_MS = int(os.environ.get("MOCK_LATENCY_MS", "40"))

STATS_LOCK = threading.Lock()
STATS = {"requests": 0, "ok": 0, "errors": 0, "sse_chunks": 0,
         "latencies": [], "by_status": {}, "started": time.time()}
CLIENT: ac.AiChatClient | None = None


def stat(**kw):
    with STATS_LOCK:
        for k, v in kw.items():
            if k == "latency":
                STATS["latencies"].append(v)
                if len(STATS["latencies"]) > 5000:
                    STATS["latencies"] = STATS["latencies"][-5000:]
            elif k == "status":
                STATS["by_status"][str(v)] = STATS["by_status"].get(str(v), 0) + 1
            else:
                STATS[k] += v


def pct(vals, p):
    if not vals:
        return 0
    s = sorted(vals)
    return round(s[min(len(s) - 1, int(len(s) * p))], 1)


class Handler(BaseHTTPRequestHandler):
    protocol_version = "HTTP/1.1"

    def log_message(self, fmt, *args):
        pass

    # ---------- helpers ----------
    def _json(self, code: int, obj: dict):
        raw = json.dumps(obj, ensure_ascii=False).encode()
        self.send_response(code)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(raw)))
        self.send_header("Access-Control-Allow-Origin", "*")
        self.end_headers()
        self.wfile.write(raw)

    def _err(self, code: int, msg: str, etype: str = "upstream_error"):
        self._json(code, {"error": {"message": msg, "type": etype, "code": code}})

    def _sse_open(self):
        self.send_response(200)
        self.send_header("Content-Type", "text/event-stream; charset=utf-8")
        self.send_header("Cache-Control", "no-cache")
        # 必须显式关闭连接: 无 Content-Length 的 1.1 keep-alive 响应会让浏览器
        # 的 fetch reader 永远读不到 EOF, 前端 finally 不执行 -> 按钮卡死。
        self.send_header("Connection", "close")
        self.send_header("Access-Control-Allow-Origin", "*")
        self.end_headers()
        self.close_connection = True

    def _sse_write(self, obj) -> None:
        payload = obj if isinstance(obj, str) else json.dumps(obj, ensure_ascii=False)
        self.wfile.write(f"data: {payload}\n\n".encode())
        self.wfile.flush()
        stat(sse_chunks=1)

    def do_OPTIONS(self):
        self.send_response(204)
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Headers", "*")
        self.send_header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
        self.send_header("Content-Length", "0")
        self.end_headers()

    # ---------- routes ----------
    def do_GET(self):
        p = self.path.split("?")[0]
        if p == "/health":
            return self._json(200, {"status": "ok", "mode": MODE, "app_version": APP_VERSION,
                                    "upstream": ac.BASE, "uptime_s":
                                    round(time.time() - STATS["started"], 1)})
        if p == "/stats":
            with STATS_LOCK:
                lat = STATS["latencies"]
                return self._json(200, {
                    "requests": STATS["requests"], "ok": STATS["ok"],
                    "errors": STATS["errors"], "sse_chunks": STATS["sse_chunks"],
                    "by_status": STATS["by_status"],
                    "latency_ms": {"p50": pct(lat, .5), "p90": pct(lat, .9),
                                   "p99": pct(lat, .99), "n": len(lat)},
                })
        if p == "/protocol":
            return self._json(200, {
                "app_version": APP_VERSION,
                "upstream": ac.BASE, "fallback": ac.BASE_ALT,
                "package_id": ac.PACKAGE_ID, "kid": ac.KID,
                "endpoints": {
                    "chat": "POST /api/v2/completions/v2",
                    "chat_stream": "POST /api/v2/completions/v3/stream",
                    "suggest_story_youtube_ask": "POST /api/v2/completions/v3",
                    "pdf": "POST /api/v2/files/completions[/stream]",
                    "embeddings": "POST /api/v2/embeddings",
                    "image": "POST /api/v2/image-generator | GET /api/v2/image-generator/{id}",
                    "search": "POST /api/v2/search",
                    "youtube": "POST /api/v2/youtube/transcript",
                    "upload": "POST /api/v2/upload (multipart)",
                },
                "auth": {
                    "Authorization": "Signature kid=&algorithm=sha256&timestamp=&validity=90&userId=&value=sha256hex(kid+ts+validity)",
                    "app-token": "Bearer JWT(HS256, key=kid, payload={token: b64(sha256(certDER)), iat, exp})",
                    "headers": ["package-id", "device-id", "isUserInAdMode", "isVip",
                                "chatModel", "Accept-Language", "messageRequest",
                                "totalMessageRequest"],
                },
                "models": ac.MODELS,
                "chat_model_header": ac.CHAT_MODEL_HEADER,
                "daily_free": ac.DAILY_FREE,
            })
        if p == "/v1/models":
            now = int(time.time())
            return self._json(200, {"object": "list", "data": [
                {"id": k, "object": "model", "created": now, "owned_by": "starnest"}
                for k in ac.MODELS]})
        if p in ("/", "/demo"):
            return self._file("demo.html", "text/html; charset=utf-8")
        return self._err(404, "not found", "not_found")

    def _file(self, name: str, ctype: str):
        path = os.path.join(os.path.dirname(os.path.abspath(__file__)), name)
        if not os.path.exists(path):
            return self._err(404, f"{name} missing", "not_found")
        raw = open(path, "rb").read()
        self.send_response(200)
        self.send_header("Content-Type", ctype)
        self.send_header("Content-Length", str(len(raw)))
        self.end_headers()
        self.wfile.write(raw)

    def do_POST(self):
        p = self.path.split("?")[0]
        if p != "/v1/chat/completions":
            return self._err(404, "not found", "not_found")
        try:
            n = int(self.headers.get("Content-Length") or 0)
            req = json.loads(self.rfile.read(n) or b"{}")
        except Exception as e:
            return self._err(400, f"invalid json: {e}", "invalid_request_error")

        messages = req.get("messages") or []
        if not messages:
            return self._err(400, "messages is required", "invalid_request_error")
        model = req.get("model") or "gpt-4o-mini"
        stream = bool(req.get("stream"))
        max_tokens = int(req.get("max_tokens") or 2000)
        rid = "chatcmpl-" + uuid.uuid4().hex[:24]
        t0 = time.time()
        stat(requests=1)

        try:
            if MODE == "upstream":
                self._handle_upstream(rid, messages, model, stream, max_tokens, t0)
            else:
                self._handle_mock(rid, model, messages, stream, t0)
        except BrokenPipeError:
            pass
        except Exception as e:
            stat(errors=1, latency=(time.time() - t0) * 1000)
            try:
                self._err(502, f"upstream exception: {e}")
            except Exception:
                pass

    def _handle_mock(self, rid, model, messages, stream, t0):
        last = messages[-1].get("content", "")
        if isinstance(last, list):
            last = " ".join(p.get("text", "") for p in last if isinstance(p, dict))
        text = (f"[MOCK 128.0] upstream protocol ready. got {len(messages)} message(s), "
                f"model={model}. last_user='{str(last)[:60]}'")
        latency = max(0, MOCK_LATENCY_MS) / 1000.0
        time.sleep(latency * random.uniform(0.5, 1.5))
        if not stream:
            stat(ok=1, latency=(time.time() - t0) * 1000)
            return self._json(200, {
                "id": rid, "object": "chat.completion", "created": int(t0),
                "model": model,
                "choices": [{"index": 0, "message": {"role": "assistant", "content": text},
                             "finish_reason": "stop"}],
                "usage": {"prompt_tokens": len(str(messages)) // 4,
                          "completion_tokens": len(text) // 4,
                          "total_tokens": (len(str(messages)) + len(text)) // 4},
            })
        self._sse_open()
        for w in text.split(" "):
            self._sse_write({"id": rid, "object": "chat.completion.chunk",
                             "created": int(t0), "model": model,
                             "choices": [{"index": 0, "delta": {"content": w + " "},
                                          "finish_reason": None}]})
            time.sleep(0.01)
        self._sse_write({"id": rid, "object": "chat.completion.chunk", "created": int(t0),
                         "model": model,
                         "choices": [{"index": 0, "delta": {}, "finish_reason": "stop"}]})
        self._sse_write("[DONE]")
        stat(ok=1, latency=(time.time() - t0) * 1000)

    def _handle_upstream(self, rid, messages, model, stream, max_tokens, t0):
        global CLIENT
        if CLIENT is None:
            CLIENT = ac.AiChatClient()
        r = CLIENT.chat(messages, model=model, stream=stream, max_tokens=max_tokens)
        stat(status=r.status_code)
        if r.status_code != 200:
            body = r.text[:400]
            stat(errors=1, latency=(time.time() - t0) * 1000)
            return self._err(502 if r.status_code >= 500 else 502,
                             f"upstream {r.status_code}: {body}", "upstream_error")
        if not stream:
            try:
                data = r.json()
            except Exception:
                stat(errors=1)
                return self._err(502, f"upstream non-json: {r.text[:300]}", "upstream_error")
            content = ac.extract_text(data) or json.dumps(data)[:400]
            stat(ok=1, latency=(time.time() - t0) * 1000)
            return self._json(200, {
                "id": rid, "object": "chat.completion", "created": int(t0), "model": model,
                "choices": [{"index": 0, "message": {"role": "assistant", "content": content},
                             "finish_reason": "stop"}],
                "usage": {"prompt_tokens": 0, "completion_tokens": 0, "total_tokens": 0},
            })
        self._sse_open()
        for payload in ac.iter_sse(r):
            if payload.strip() == "[DONE]":
                break
            try:
                chunk = json.loads(payload)
            except Exception:
                continue
            text = ac.extract_text(chunk)
            if text:
                self._sse_write({"id": rid, "object": "chat.completion.chunk",
                                 "created": int(t0), "model": model,
                                 "choices": [{"index": 0, "delta": {"content": text},
                                              "finish_reason": None}]})
        self._sse_write({"id": rid, "object": "chat.completion.chunk", "created": int(t0),
                         "model": model,
                         "choices": [{"index": 0, "delta": {}, "finish_reason": "stop"}]})
        self._sse_write("[DONE]")
        stat(ok=1, latency=(time.time() - t0) * 1000)


def main():
    srv = ThreadingHTTPServer(("0.0.0.0", PORT), Handler)
    print(f"[demo] AI Chat 2API (APK {APP_VERSION}) on :{PORT} mode={MODE} upstream={ac.BASE}", flush=True)
    srv.serve_forever()


if __name__ == "__main__":
    main()
