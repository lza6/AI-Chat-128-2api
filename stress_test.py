"""AI Chat 2API demo server 压测 (本地)

用法:
  python stress_test.py                 # 默认 http://127.0.0.1:8933
  python stress_test.py --conc 20 --n 100 --stream
场景:
  1) /health /stats /protocol /v1/models 可用性
  2) 非流式 / 流式 chat 单请求
  3) 并发压测 (默认 10 并发 x 20 轮)
  4) 边界: 空 messages、坏 JSON、未知路径
"""
from __future__ import annotations

import argparse
import json
import statistics
import threading
import time
import urllib.error
import urllib.request

BASE = "http://127.0.0.1:8933"
RESULTS = []
LOCK = threading.Lock()


def rec(name, ok, detail, ms):
    with LOCK:
        RESULTS.append({"scenario": name, "ok": ok, "detail": detail, "ms": round(ms, 1)})
    print(f"  [{'PASS' if ok else 'FAIL'}] {name}: {detail} ({ms:.0f}ms)")


def http(path, method="GET", body=None, timeout=120):
    url = BASE + path
    data = json.dumps(body).encode() if body is not None else None
    req = urllib.request.Request(url, data=data, method=method,
                                 headers={"Content-Type": "application/json"})
    t0 = time.time()
    try:
        with urllib.request.urlopen(req, timeout=timeout) as r:
            return r.status, r.read(), (time.time() - t0) * 1000
    except urllib.error.HTTPError as e:
        return e.code, e.read(), (time.time() - t0) * 1000
    except Exception as e:
        return 0, str(e).encode(), (time.time() - t0) * 1000


def sse_chat(prompt, model="gpt-4o-mini", timeout=120):
    """流式请求, 返回 (status, 拼接文本, chunk 数)"""
    url = BASE + "/v1/chat/completions"
    body = json.dumps({"model": model, "stream": True,
                       "messages": [{"role": "user", "content": prompt}]}).encode()
    req = urllib.request.Request(url, data=body, method="POST",
                                 headers={"Content-Type": "application/json"})
    out, chunks = [], 0
    try:
        with urllib.request.urlopen(req, timeout=timeout) as r:
            for raw in r:
                line = raw.decode("utf-8", "ignore").strip()
                if not line.startswith("data:"):
                    continue
                p = line[5:].strip()
                if p == "[DONE]":
                    break
                try:
                    j = json.loads(p)
                    d = j.get("choices", [{}])[0].get("delta", {}).get("content")
                    if d:
                        out.append(d)
                        chunks += 1
                except Exception:
                    pass
            return r.status, "".join(out), chunks
    except urllib.error.HTTPError as e:
        return e.code, e.read().decode("utf-8", "ignore"), 0


def main():
    global BASE
    ap = argparse.ArgumentParser()
    ap.add_argument("--base", default=BASE)
    ap.add_argument("--conc", type=int, default=10)
    ap.add_argument("--n", type=int, default=20)
    ap.add_argument("--stream", action="store_true", help="并发用流式请求")
    ap.add_argument("--out", default="stress_report.json")
    a = ap.parse_args()
    BASE = a.base.rstrip("/")
    print(f"== 目标 {BASE} (并发 {a.conc} x {a.n}) ==")

    # 1. 基础端点
    for p, key in [("/health", "status"), ("/stats", "requests"), ("/protocol", "upstream")]:
        st, raw, ms = http(p)
        ok = st == 200 and key in raw.decode("utf-8", "ignore")
        rec(f"GET {p}", ok, f"{st}", ms)

    st, raw, ms = http("/v1/models")
    try:
        ok = st == 200 and len(json.loads(raw)["data"]) > 0
    except Exception:
        ok = False
    rec("GET /v1/models", ok, f"{st}", ms)

    # 2. 单请求
    st, raw, ms = http("/v1/chat/completions", "POST",
                       {"model": "gpt-4o-mini", "messages": [{"role": "user", "content": "ping"}]})
    try:
        j = json.loads(raw)
        txt = j.get("choices", [{}])[0].get("message", {}).get("content", "")
        err = j.get("error")
    except Exception:
        txt, err = raw[:120].decode("utf-8", "ignore"), None
    rec("POST chat (non-stream)", st == 200 and not err,
        (err.get("message", "")[:120] if isinstance(err, dict) else txt[:90]), ms)

    st, txt, chunks = sse_chat("ping stream")
    rec("POST chat (stream)", st == 200 and chunks > 0, f"{st} chunks={chunks} text={txt[:60]}", 0)

    # 3. 边界
    st, raw, ms = http("/v1/chat/completions", "POST", {"messages": []})
    rec("边界: 空 messages -> 400", st == 400, f"{st}", ms)
    st, raw, ms = http("/v1/chat/completions", "POST", {"model": "x"})
    rec("边界: 缺 messages -> 400", st == 400, f"{st}", ms)
    st, raw, ms = http("/nope")
    rec("边界: 未知路径 -> 404", st == 404, f"{st}", ms)
    req = urllib.request.Request(BASE + "/v1/chat/completions", data=b"{bad json",
                                 method="POST", headers={"Content-Type": "application/json"})
    try:
        urllib.request.urlopen(req, timeout=30)
        st = 200
    except urllib.error.HTTPError as e:
        st = e.code
    except Exception:
        st = 0
    rec("边界: 坏 JSON -> 400", st == 400, f"{st}", 0)

    # 4. 并发
    lat, ok_n, err_n = [], 0, 0
    lock = threading.Lock()

    def worker(i):
        nonlocal ok_n, err_n
        t0 = time.time()
        if a.stream:
            st, txt, ch = sse_chat(f"load {i}")
            good = st == 200 and ch > 0
        else:
            st, raw, _ = http("/v1/chat/completions", "POST",
                              {"model": "gpt-4o-mini",
                               "messages": [{"role": "user", "content": f"load {i}"}]})
            good = st == 200 and b"error" not in raw[:80]
        ms = (time.time() - t0) * 1000
        with lock:
            lat.append(ms)
            if good:
                ok_n += 1
            else:
                err_n += 1

    total = a.conc * a.n
    t0 = time.time()
    for r in range(a.n):
        ths = [threading.Thread(target=worker, args=(r * a.conc + k,)) for k in range(a.conc)]
        [t.start() for t in ths]
        [t.join() for t in ths]
    wall = time.time() - t0
    rec(f"并发 {a.conc}x{a.n}", err_n == 0,
        f"{ok_n}/{total} ok, {err_n} err, wall={wall:.2f}s, "
        f"p50={statistics.median(lat):.0f}ms, rps={total / wall:.1f}", wall * 1000)

    st, raw, _ = http("/stats")
    try:
        stats = json.loads(raw)
    except Exception:
        stats = {}

    passed = sum(1 for r in RESULTS if r["ok"])
    report = {"target": BASE, "total_scenarios": len(RESULTS), "passed": passed,
              "failed": len(RESULTS) - passed,
              "concurrency": a.conc, "rounds": a.n, "stream_load": a.stream,
              "results": RESULTS, "server_stats": stats,
              "timestamp": time.strftime("%Y-%m-%dT%H:%M:%S")}
    open(a.out, "w", encoding="utf-8").write(json.dumps(report, ensure_ascii=False, indent=2))
    print(f"\n== 结果: {passed}/{len(RESULTS)} PASS -> {a.out} ==")
    return 0 if passed == len(RESULTS) else 1


if __name__ == "__main__":
    raise SystemExit(main())
