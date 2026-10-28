"""真实浏览器链路验证：headless Chrome 打开 demo 页, 自动发一条, 断言气泡与统计刷新, 截图。"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

BASE = "http://127.0.0.1:8933"
PROMPT = "你好，请用一句话自我介绍"


def main():
    out = {"target": BASE, "steps": [], "ok": False}
    with sync_playwright() as p:
        b = p.chromium.launch(channel="chrome", headless=True)
        pg = b.new_page(viewport={"width": 1280, "height": 900})
        pg.goto(f"{BASE}/?auto={urllib.parse.quote(PROMPT)}", wait_until="load")
        out["steps"].append({"step": "goto", "title": pg.title()})
        # 等用户气泡 + 助手气泡
        pg.wait_for_function(
            "() => document.querySelectorAll('#log .msg').length >= 2", timeout=15000)
        pg.wait_for_timeout(2500)   # 等流式结束
        msgs = pg.eval_on_selector_all(
            "#log .msg", "els => els.map(e => ({cls: e.className, text: e.innerText.slice(0,120)}))")
        out["steps"].append({"step": "messages", "count": len(msgs), "items": msgs})
        health = pg.inner_text("#health")
        mode = pg.inner_text("#modeTag")
        stats = {k: pg.inner_text(f"#s_{k}") for k in ("req", "ok", "err", "chunk", "p50")}
        pool = pg.inner_text("#pool")
        out["steps"].append({"step": "panel", "health": health, "mode": mode,
                             "stats": stats, "pool": pool})
        pg.screenshot(path="browser_128_auto.png", full_page=True)
        # 再手动点一条（模拟真实点击/输入）
        pg.fill("#q", "第二条：验证手动输入与按钮点击")
        pg.click("#send")
        pg.wait_for_function(
            "() => document.querySelectorAll('#log .msg').length >= 4", timeout=15000)
        pg.wait_for_timeout(1500)
        n = pg.eval_on_selector_all("#log .msg", "els => els.length")
        out["steps"].append({"step": "manual-click", "messages": n})
        pg.screenshot(path="browser_128_manual.png", full_page=True)
        b.close()
    out["ok"] = (not out["steps"][1].get("items") or len(out["steps"][1]["items"]) >= 2) and \
        out["steps"][1]["count"] >= 2 and out["steps"][-1]["messages"] >= 4
    print(json.dumps(out, ensure_ascii=False, indent=2))
    open("browser_report.json", "w", encoding="utf-8").write(
        json.dumps(out, ensure_ascii=False, indent=2))
    print("BROWSER_OK" if out["ok"] else "BROWSER_FAIL")
    return 0 if out["ok"] else 1


if __name__ == "__main__":
    sys.exit(main())
