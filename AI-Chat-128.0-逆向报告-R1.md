# AI Chat (Starnest) 128.0 逆向报告 · R1

- 分析日期：2026-10-29
- 目标：`AI_Chat_Chatbot_&__istant_128.0__ded_by_Platinum_s.apk`（Platinum Mods 修改版）
- 工作目录：`D:/apk-analysis/aichat128/`
- 前序：`D:/apk-analysis/ai-chat/AI-Chat-逆向报告-R1.md`（v133 mod + v142 官方）
- **结论**：协议再次 100% 还原并与 v133 逐字节一致；本地 demo / 11 场景压测 / 真实浏览器链路全通（已验证）；**上游依旧全端点 401**，128.0 未带来任何可用性突破（已验证，见 §6）

---

## 1. 应用画像（本轮实测）

| 项 | 值 | 证据 |
|---|---|---|
| 包名 | `starnest.aichat.aichatbot.assistant` | `aapt2 dump badging` |
| versionCode / versionName | `128` / `128.0` | 同上 |
| minSdk / targetSdk | 28 / 36（compileSdk 36, Android 16） | 同上 |
| 体积 / dex | 123.7 MB / 9 个 dex（官方 142 为 5 个） | `unzip -l` |
| 签名证书 | 自签名 264 字节，`sha256_b64=Zqxu7cm9nRhxtdlb4kC82SabYcdxsJ2Z3bx25IqgLAk=` | `apkcert.py` |
| native | 仅 `libjni_latinime.so` 等 4 个，**无 libpairipcore.so**（即仅 Java 层 PairIP 字符串混淆 `com.pairip.StartupLauncher`） | `unzip -l` |
| 修改内容 | ApkSignatureKillerEx（`bin.mt.signature.KillerApplication`）+ 远程 banner JSON（`raw.githubusercontent.com/platinummodsofficial-cyber/Ai-Chat-115/…/128.0`，内容是一段"更新提示"文案，**不含任何协议/域名改动**） | 静态 + 实测抓取 |
| TLS | 客户端自建 `SSLContext("SSL")` + 全信任 `X509TrustManager`（`Wc/i.java:140-144`）→ mitmproxy 可直接解密，无需 root | 静态 |

---

## 2. 上游端点（128.0 全量，`ChatGPTService` 注解逐一核出）

主站 `https://api-aichat.starnestsolution.com`（NestJS）。失败自动切 `https://api-akeyboard.starnestsolution.com`（`Bh/a.java:538-543`，OkHttp 拦截器对所有 IO 异常重试一次，换主机不改路径）。

| 用途 | 方法与路径 |
|---|---|
| 对话（非流式） | `POST /api/v2/completions/v2`（`h` 与 `i` 两个 body 重载） |
| 对话（流式 SSE） | `POST /api/v2/completions/v3/stream`（`@Streaming`） |
| 建议 / 故事 / YouTube 提问 | `POST /api/v2/completions/v3` |
| PDF 问答 | `POST /api/v2/files/completions`、`POST /api/v2/files/completions/stream` |
| 向量 | `POST /api/v2/embeddings` |
| 文生图 | `POST /api/v2/image-generator`；`GET /api/v2/image-generator/{generationId}` |
| 联网搜索 | `POST /api/v2/search` |
| YouTube 字幕 | `POST /api/v2/youtube/transcript` |
| 上传 | `POST /api/v2/upload`、`POST /api/v2/files/upload`（multipart） |
| 文件取回 | `POST /api/v2/files/get` |

**128.0 全部落在 `/api/v2/*`** —— 与 v133 相同，官方 v142 才是 `/api/v3/*`。

---

## 3. 鉴权协议（128.0 源码逐行核对，与 v133 完全一致）

拦截器：`Wc/i.java:56-153`（`a(String)` 内联 anonymous `y`）。

### 3.1 `Authorization`

```
Signature kid=36ccfe00-78fc-4cab-9c5b-5460b0c78513
        &algorithm=sha256
        &timestamp=<epochSecond>
        &validity=90
        &userId=
        &value=<hex(sha256(kid + timestamp + "90"))>
```

关键常量逐一定位（这些是最容易复刻错的地方）：

| 片段 | 源码证据 |
|---|---|
| 被摘要串 = `KID + ts + "90"` | `Wc/i.java:64`，`"90"` 来自 `com.tom_roush.pdfbox...taggedpdf.d.f35200S1 = "90"`（`d.java:124`） |
| value = 小写 hex 的 sha256 | `Y9/b.java:234-237` `String.format("%02x", byte)` |
| 拼接 = `URLEncoder.encode(k) + "=" + URLEncoder.encode(v)`，`&` 连接 | `Y9/b.java:238-243` |
| 前缀字面量 | `"Signature "`（`Wc/i.java:121`） |
| 键序 | kid, algorithm, timestamp, validity, userId, value（`Wc/i.java:65-78`） |
| `userId` | 恒为空串（游客模式，App 内无账号体系） |

### 3.2 `app-token`

```
Bearer <JWT HS256>
  header  {"alg":"HS256","typ":"JWT"}
  payload {"token": "<Base64.NO_WRAP(sha256(APK 签名证书 DER))>", "iat": now, "exp": now+90}
  HMAC key = "36ccfe00-78fc-4cab-9c5b-5460b0c78513"   ← 与 kid 同一字面量
```

证据：`Wc/i.java:80-119`（`getApkContentsSigners()` → `Y9/b:230-233` `Base64.encodeToString(sha256(certBytes), 2)`，flag `2 = NO_WRAP`；`Mac("HmacSHA256")` key 直接是该 UUID）。

### 3.3 拦截器一共只加 6 个头

`Authorization`、`app-token`、`package-id`、`device-id`、`isUserInAdMode`（`Wc/i.java:121-125`）；业务层再补 `isVip` / `chatModel` / `Accept-Language`（`Nc/…/c.java:createAdsHeader`）。剩余 `messageRequest` / `totalMessageRequest` 是本地每日计数（`Y9/p.java:556-561`）。

**没有任何 Play Integrity / attesting / 设备指纹签名头** —— 服务端拒绝的原因不可能在客户端头上找。

---

## 4. 模型表与免费额度（128.0 新增，与前序版本有差异）

来源：`Nc/EnumC1015l`（枚举 `value` / `model` / `getChatModel()` 三处）+ `Nc/C1026u`（额度常量）。

| 枚举 | body.model | 请求头 chatModel | 每日免费 |
|---|---|---|---|
| GPT_3_5_Turbo | `gpt-4o-mini-2024-07-18` | `gpt_4o_mini` | 50 |
| GPT_4 | `gpt-4o-2024-08-06` | `gpt_4o` | 50 |
| GPT_4_1 | `gpt-4.1` | `gpt_4_1` | 50 |
| GPT_5 | `gpt_5` | `gpt_5` | 50 |
| GPT_5_2 | `gpt_5_2` | `gpt_5_2` | 20 |
| GPT_5_4 | `gpt_5_4` | `gpt_5_4` | 5 |
| GPT_5_6 | `gpt_5_6_luna` | `gpt_5_6_luna` | 20 |
| GPT_5_MINI | `gpt_5_mini` | `gpt_5_mini` | （不限/未列） |
| GPT_5_NANO | `gpt_5_nano` | `gpt_5_nano` | （不限/未列） |
| GPT_O3 | `o3` | `o3` | 20 |
| GEMINI | `gemini_lite` | `gemini_lite` | — |
| GEMINI_3_1_FLASH | `gemini_3_1_flash` | **`gemini_3_flash_lite_preview`** | — |
| GEMINI_3_FLASH | `gemini_3_flash` | `gemini_3_flash` | 20 |
| GROK_3_MINI | `grok_3_mini` | `grok_3_mini` | 10 |
| GROK_4_3 | `grok_4_3` | `grok_4_3` | 5 |
| DEEP_SEEK_R1 | `deepseek_reasoner` | `deepseek_reasoner` | 50 |
| DEEP_SEEK_V3 | `deepseek_chat` | `deepseek_chat` | 100 |
| CLAUDE_HAIKU_4_5 | `claude_haiku` | `claude_haiku` | 20 |

> 陷阱：`body.model` 与请求头 `chatModel` **不同名**（如 `gpt-4o-2024-08-06` vs `gpt_4o`，`gemini_3_1_flash` vs `gemini_3_flash_lite_preview`）。只发一个的实现在服务端风控/额度判定上属于"非法组合"，客户端已按表分别映射（`CHAT_MODEL_HEADER`）。

---

## 5. 128.0 vs v133 vs v142 差量

| 维度 | 128.0（本轮） | v133（前序，mod） | v142（前序，官方） |
|---|---|---|---|
| 路径前缀 | `/api/v2/*` | `/api/v2/*` | `/api/v3/*` |
| 鉴权（kid / 签名 / JWT key） | 完全相同 | 相同 | 相同 |
| 拦截器类 | `Wc/i` | `Fc/k` | `Lc/b` |
| 请求头集合 | 6 + 3（业务） | 同左 | 同左 |
| chatModel 取值 | 见表（含 `gpt_5_6_luna`、`gemini_3_flash_lite_preview`） | 表中子集 | 同 142 口径 |
| 模型数 | 18（17 可用 + GEMINI 老档） | 略少 | 略多 |
| dex / 加固 | 9 dex，纯 Java PairIP 字符串混淆 | 9 dex + killPM | 5 dex |
| 结论 | **无协议增量** | 401 | 401 |

结论：**128.0 是"老版本 + 换皮 mod"，协议面没有任何新东西**，因此前序 401 定案原样继承。

---

## 6. 上游 401 定案（本轮新增证据）

`probe128.py` + `probe_endpoints.py`，出口走本地 v2ray `http://127.0.0.1:10808`，证书用 **本 APK 自己的签名证书**（`cert_0.der`）：

```
GET  /                                           404 {"message":"Cannot GET /"}          ← 服务在线
POST /api/v2/completions/v2                     401 localization.Unauthorized
POST /api/v2/completions/v3                     401
POST /api/v2/completions/v3/stream              401
POST /api/v2/search                             401
POST /api/v2/youtube/transcript                 401
POST /api/v2/image-generator                    401
GET  /api/v2/image-generator/abc123             401
POST /api/v2/embeddings                         401
POST /api/v2/files/completions                  401
POST /api/v2/files/get                          401
POST /api/v1/search                             404 PAGE_NOT_FOUND_ERR   ← 路由不存在
GET  /api/v1/trending_terms                     404 PAGE_NOT_FOUND_ERR
```

交叉变量（`probe128.py` 矩阵）：域名 `api-aichat` / `api-akeyboard` × 路径 `v2` / `v3` × 证书 `本 APK 自签` / `官方 142 证书` = **8 种组合全部 401**。

三点推论：

1. **不是端点选择问题**：8 个真实业务端点全部 401，不是单接口下线。
2. **不是证书/版本问题**：自签与官方证书、v2 与 v3 路径全 401。
3. **不是复刻问题**：签名/JWT/头/UA 与 `Wc/i.java` 逐字节一致（§3），仍 401 ⇒ 拒绝发生在 controller guard，服务端对"游客态 + 该客户端"整体关门。

补充：本轮上游链路本身可用（`GET /` 200/404 正常应答），首次 POST 曾出现一次 `SSL: UNEXPECTED_EOF_WHILE_READING`，随后同一请求连续 3 次稳定复现 401 —— 属链路抖动，不是判定依据。

---

## 7. 本地交付与实测证据

| 文件 | 说明 |
|---|---|
| `aichat_client.py` | 协议客户端：签名/JWT/设备池(8)/模型双映射/`AICHAT_API_VER` 切换/SSE 解析 |
| `demo_server.py` | OpenAI 兼容服务（默认 mock，可 `AICHAT_MODE=upstream`）；`/v1/chat/completions`、`/v1/models`、`/health`、`/stats`、`/protocol`、`/`(demo 页)，端口 8933 |
| `demo.html` | 聊天 UI + 实时统计 + 协议摘要，`?auto=<text>` 支持自动冒烟 |
| `stress_test.py` | 11 场景压测（端点/非流式/流式/4 边界/并发） |
| `verify_browser.py` | 真实 headless Chrome 链路验证（自动发送 + 手动点击 + 截图） |
| `probe128.py` / `probe_endpoints.py` | 上游守卫探测矩阵 |
| `apkcert.py` | APK Signing Block v2/v3 证书提取 |
| `jadx-out/` | 128.0 全量反编译（18404 个 .java，95 个反编译告警） |

### 7.1 压测（已实际运行）

```
$ python stress_test.py --base http://127.0.0.1:8933 --conc 8  --n 10
11/11 PASS | 并发 8x10=80/80 ok, 0 err, wall=0.62s, p50=47ms, rps=129.4

$ python stress_test.py --base http://127.0.0.1:8933 --conc 20 --n 10
11/11 PASS | 并发 20x10=200/200 ok, 0 err, wall=0.69s, p50=43ms, rps=289.1
```

场景明细见 `stress_report.json`：`/health`、`/stats`、`/protocol`、`/v1/models`、非流式、流式(11 chunk)、空 messages→400、缺 messages→400、未知路径→404、非法 JSON→400、并发批次。

### 7.2 真实浏览器（已实际运行，Playwright + 本机 Chrome headless）

```
$ python verify_browser.py
→ #log .msg = 4 条（引导/用户/助手/耗时），统计面板 req=1 ok=1 err=0 chunk=12 p50=146ms
→ 手动 fill + click #send → 消息数 4 → 7
BROWSER_OK
截图：browser_128_auto.png / browser_128_manual.png
```

### 7.3 本轮修复的真实缺陷（P1，浏览器验证发现）

**流式响应永不结束导致前端按钮卡死**：`demo_server._sse_open()` 原用 `Connection: keep-alive` 且无 `Content-Length`，浏览器 `fetch` reader 读不到 EOF → `finally { btn.disabled = false }` 不执行，`#send` 永久 disabled。
修复：SSE 响应改 `Connection: close` + `self.close_connection = True`（`demo_server.py:_sse_open`）。修复后 11/11 与浏览器双验证均 PASS。
（此 bug 是 R1 遗留物；R1 的"浏览器链路通过"结论在该实现下不成立——已在本轮修正。）

### 7.4 upstream 模式实测

```
$ AICHAT_MODE=upstream AICHAT_PROXY=http://127.0.0.1:10808 python demo_server.py
POST /v1/chat/completions -> 502
{"error":{"message":"upstream 401: localization.Unauthorized", ...}}
/stats -> {"requests":1,"ok":0,"errors":1,"p50":1915.5}
```

即：本地链路没有任何问题，**失败点 100% 在上游**。

---

## 8. 结论与剩余风险

- 协议面：**已验证**（静态逐行 + 8 组合实弹探测）。
- 本地链路：**已验证**（压测 + 真实浏览器）。
- 上游可用性：**已验证不可用（401）**，与前序版本一致，128.0 无突破。
- 未验证项：
  - 真机（非模拟器）+ 住宅 IP 重放（R1 未做，本轮同样未做）——唯一还可能翻盘的环境变量。
  - 服务端是否已整体关闭游客通道（无法从客户端证伪）。
- 剩余风险：若后续要用该渠道接入网关，必须先拿到一次真实 200；在此之前任何"接入完成"的说法都是伪实现。

## 9. 下一步（按性价比）

1. **真机 + 住宅 IP 定案**：装 128.0 到真机（自带全信任 TM，mitm 免 root），若真机 200 则差异在设备/出口环境，采集真实头 + device-id 复刻；若真机也 401 → 服务端已关游客通道，本渠道作废。
2. 若拿到 200：立刻 `AICHAT_MODE=upstream AICHAT_API_VER=v2` 跑通 SSE，再按 new-api `TwoAPIChannelTypes` 模式接入。
3. 归档闭环：把 demo/压测/报告作为"死渠道样本"留档，不占用网关开发时间。
