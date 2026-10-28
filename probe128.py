"""128.0 上游连通性与鉴权探针（多变量矩阵）"""
import base64
import hashlib
import hmac
import json
import os
import sys
import time
import urllib.parse

import requests

KID = "36ccfe00-78fc-4cab-9c5b-5460b0c78513"
PKG = "starnest.aichat.aichatbot.assistant"
VALIDITY = "90"
PROXY = os.environ.get("AICHAT_PROXY", "http://127.0.0.1:10808")
BASES = {
    "aichat": "https://api-aichat.starnestsolution.com",
    "akeyboard": "https://api-akeyboard.starnestsolution.com",
    "widget": "https://widget.startnest.uk",
}


def b64(b):
    return base64.b64encode(b).decode()


def sig(ts=None):
    ts = ts or int(time.time())
    v = hashlib.sha256((KID + str(ts) + VALIDITY).encode()).hexdigest()
    pairs = [("kid", KID), ("algorithm", "sha256"), ("timestamp", str(ts)),
             ("validity", VALIDITY), ("userId", ""), ("value", v)]
    return "&".join(f"{urllib.parse.quote(k)}={urllib.parse.quote(x)}" for k, x in pairs)


def jwt(tok):
    now = int(time.time())
    h = b64(json.dumps({"alg": "HS256", "typ": "JWT"}, separators=(",", ":")).encode())
    b = b64(json.dumps({"token": tok, "iat": now, "exp": now + 90},
                       separators=(",", ":")).encode())
    si = f"{h}.{b}"
    return si + "." + b64(hmac.new(KID.encode(), si.encode(), hashlib.sha256).digest())


def tok_from_der(p):
    return b64(hashlib.sha256(open(p, "rb").read()).digest())


def sess():
    s = requests.Session()
    s.trust_env = False
    s.proxies = {"http": PROXY, "https": PROXY}
    return s


def hdrs(tok, dev="0123456789abcdef", vip="true", cm="gpt_4o_mini"):
    return {
        "Authorization": "Signature " + sig(),
        "app-token": "Bearer " + jwt(tok),
        "package-id": PKG,
        "device-id": dev,
        "isUserInAdMode": "false",
        "isVip": vip,
        "chatModel": cm,
        "Accept-Language": "en",
        "messageRequest": "0",
        "totalMessageRequest": "0",
        "Content-Type": "application/json; charset=UTF-8",
        "User-Agent": "okhttp/4.9.0",
    }


BODY = {
    "isVip": True,
    "max_tokens": 200,
    "messages": [{"content": [{"text": "Reply with exactly: PONG", "type": "text"}], "role": "user"}],
    "stream": False,
    "model": "gpt_4o_mini",
}


def show(tag, r):
    body = r.text.replace("\n", " ")[:220]
    print(f"[{tag}] {r.status_code} {body}")


def main():
    toks = {}
    for name in ("cert_0.der", "..\\ai-chat\\official_cert.der"):
        if os.path.exists(name):
            toks[name] = tok_from_der(name)
    print("tokens:", {k: v for k, v in toks.items()})
    s = sess()
    for bname, base in BASES.items():
        try:
            r = s.get(base + "/", timeout=20)
            show(f"health:{bname}", r)
        except Exception as e:
            print(f"[health:{bname}] ERR {e}")
    tok_mod = toks.get("cert_0.der")
    tok_off = toks.get("..\\ai-chat\\official_cert.der")
    cases = []
    for base_name in ("aichat", "akeyboard"):
        for ver in ("v2", "v3"):
            for tok in ([("mod", tok_mod)] + ([("off", tok_off)] if tok_off else [])):
                cases.append((f"{base_name}/{ver}/{tok[0]}", BASES[base_name],
                              f"/api/{ver}/completions/v2", tok[1]))
    for tag, base, path, tok in cases:
        try:
            r = s.post(base + path, headers=hdrs(tok), data=json.dumps(BODY), timeout=30)
            show(tag, r)
        except Exception as e:
            print(f"[{tag}] ERR {e}")
    # 流式
    try:
        r = s.post(BASES["aichat"] + "/api/v2/completions/v3/stream",
                   headers=hdrs(tok_mod), data=json.dumps({**BODY, "stream": True}),
                   timeout=30, stream=True)
        print("[stream/aichat/v2/mod]", r.status_code, r.raw.read(200))
    except Exception as e:
        print("[stream] ERR", e)


if __name__ == "__main__":
    main()
