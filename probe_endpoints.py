"""128.0 全端点守卫探测：区分 'guard 统一拒绝' 与 '仅 completions 拒绝'"""
import json
import os
import sys

from probe128 import BASES, hdrs, sess, tok_from_der, show

tok = tok_from_der("cert_0.der")
s = sess()
B = BASES["aichat"]

CASES = [
    ("POST", "/api/v2/completions/v2", {"isVip": True, "max_tokens": 200, "stream": False,
                                        "model": "gpt_4o_mini",
                                        "messages": [{"content": "hi", "role": "user"}]}),
    ("POST", "/api/v2/completions/v3", {"isVip": True, "max_tokens": 200, "stream": False,
                                        "model": "gpt_4o_mini",
                                        "messages": [{"content": "hi", "role": "user"}]}),
    ("POST", "/api/v2/completions/v3/stream", {"isVip": True, "max_tokens": 200, "stream": True,
                                               "model": "gpt_4o_mini",
                                               "messages": [{"content": "hi", "role": "user"}]}),
    ("POST", "/api/v2/search", {"q": "hello"}),
    ("POST", "/api/v1/search", {"q": "hello"}),
    ("GET", "/api/v1/trending_terms", None),
    ("POST", "/api/v2/youtube/transcript", {"url": "https://www.youtube.com/watch?v=dQw4w9WgXcQ"}),
    ("POST", "/api/v2/image-generator", {"prompt": "a cat", "size": {"width": 1024, "height": 1024},
                                         "responseFormat": "b64_json"}),
    ("GET", "/api/v2/image-generator/abc123", None),
    ("POST", "/api/v2/embeddings", {"input": "hello"}),
    ("POST", "/api/v2/files/get", {"url": "https://example.com/a.pdf"}),
    ("POST", "/api/v2/files/completions", {"isVip": True, "max_tokens": 100, "stream": False,
                                           "model": "gpt_4o_mini",
                                           "messages": [{"content": "hi", "role": "user"}]}),
    ("POST", "/api/v2/upload", {}),
]


def main():
    for method, path, body in CASES:
        try:
            if method == "GET":
                r = s.get(B + path, headers=hdrs(tok), timeout=30)
            else:
                r = s.post(B + path, headers=hdrs(tok),
                           data=json.dumps(body) if body is not None else None, timeout=30)
            show(f"{method} {path}", r)
        except Exception as e:
            print(f"[{method} {path}] ERR {e}")


if __name__ == "__main__":
    main()
