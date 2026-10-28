# AI Chat 2API（Starnest 128.0）— 逆向 + 本地 demo / 压测

目标 APK：`AI_Chat_Chatbot_&__istant_128.0__ded_by_Platinum_s.apk`
包名 `starnest.aichat.aichatbot.assistant`（versionCode 128 / versionName 128.0）

> **状态**：协议 100% 还原，本地链路（demo / 压测 / 真实浏览器）全通；
> **上游全端点 401**，与 v133/v142 一致 —— 本仓库目前是"协议实现 + 死渠道样本"，不可用于生产。
> 详见 `AI-Chat-128.0-逆向报告-R1.md`。

## 快速开始

```powershell
# 1) mock 模式（默认，联调用，不碰上游）
python demo_server.py                     # http://127.0.0.1:8933/
# 浏览器打开 http://127.0.0.1:8933/ ，或自动冒烟：
#   http://127.0.0.1:8933/?auto=你好

# 2) upstream 模式（真实打上游，需要签名证书 + 代理）
$env:AICHAT_MODE="upstream"
$env:AICHAT_PROXY="http://127.0.0.1:10808"
python demo_server.py

# 3) 压测（11 场景）
python stress_test.py --base http://127.0.0.1:8933 --conc 20 --n 10

# 4) 真实浏览器链路验证（需要 playwright + 本机 Chrome）
python verify_browser.py

# 5) 上游守卫探测
python probe128.py            # 域名 x 路径 x 证书 矩阵
python probe_endpoints.py     # 12 个端点逐个探
```

## 文件

| 文件 | 作用 |
|---|---|
| `aichat_client.py` | 协议客户端（签名 / app-token JWT / 设备池 / 模型双映射 / SSE 解析） |
| `demo_server.py` | OpenAI 兼容网关（`/v1/chat/completions`、`/v1/models`、`/health`、`/stats`、`/protocol`） |
| `demo.html` | 聊天 UI + 实时统计 |
| `stress_test.py` | 11 场景压测 |
| `verify_browser.py` | Playwright 真实浏览器验证（截图 `browser_128_*.png`） |
| `probe128.py` / `probe_endpoints.py` | 上游探测矩阵 |
| `apkcert.py` | 从 APK Signing Block 提证书 |
| `cert_0.der` | 本 APK 签名证书（`app-token` 的 token 字段来源） |
| `sources/` | 关键反编译源码（拦截器 / 接口 / 模型枚举 / 额度常量 / 失败重试） |
| `jadx-out/` | 全量反编译（18404 个 .java，本地生成，未入库） |
| `AI-Chat-128.0-逆向报告-R1.md` | 完整报告 |

## 协议速查

```
Authorization: Signature kid=36ccfe00-78fc-4cab-9c5b-5460b0c78513
   &algorithm=sha256&timestamp=<epoch>&validity=90&userId=
   &value=hex(sha256(kid+timestamp+"90"))
app-token: Bearer JWT(HS256, key=<kid>, payload={token:b64(sha256(certDER)),iat,exp=iat+90})
package-id / device-id(16hex) / isUserInAdMode / isVip / chatModel / Accept-Language
POST /api/v2/completions/v2            非流式
POST /api/v2/completions/v3/stream     流式 SSE
```

## 环境变量

| 变量 | 说明 |
|---|---|
| `AICHAT_PORT` | demo 端口，默认 8933 |
| `AICHAT_MODE` | `mock`（默认）/ `upstream` |
| `AICHAT_PROXY` | 上游代理，如 `http://127.0.0.1:10808` |
| `AICHAT_CERT_HEX` | 覆盖证书 DER hex（默认读 `cert_0.der`） |
| `AICHAT_API_VER` | `v2`（默认，128.0/v133 口径）/ `v3`（142 口径） |
| `MOCK_LATENCY_MS` | mock 首字延迟，默认 40 |
