package Wc;

import A5.q;
import Eg.l;
import Fg.A;
import Fg.k;
import Z9.C1261b;
import android.content.Context;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.util.Base64;
import com.facebook.AccessToken;
import com.facebook.AuthenticationTokenClaims;
import com.facebook.gamingservices.cloudgaming.internal.SDKConstants;
import com.google.gson.j;
import com.starnest.keyboard.model.remote.ChatGPTService;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import org.json.JSONException;
import org.json.JSONObject;
import p016ah.n;
import p368wh.C4118f;
import p368wh.D;
import p368wh.E;
import p368wh.G;
import p368wh.N;
import p368wh.y;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static i f15706b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Context f15707a;

    public i(Context context) {
        this.f15707a = context;
    }

    public final ChatGPTService a(String str) {
        final Context context = this.f15707a;
        final String deviceId = com.starnest.keyboard.model.model.c.getConfig(context).getDeviceId();
        D d3 = new D();
        d3.f45205d.add(new y() { // from class: Wc.e
            @Override // p368wh.y
            public final N a(Bh.g gVar) throws JSONException, NoSuchAlgorithmException, InvalidKeyException {
                long epochSecond = Instant.now().getEpochSecond();
                String str2 = "36ccfe00-78fc-4cab-9c5b-5460b0c78513" + epochSecond + com.tom_roush.pdfbox.pdmodel.documentinterchange.taggedpdf.d.f35200S1;
                l lVar = new l("kid", "36ccfe00-78fc-4cab-9c5b-5460b0c78513");
                l lVar2 = new l("algorithm", "sha256");
                l lVar3 = new l(SDKConstants.PARAM_DEBUG_MESSAGE_TIMESTAMP, String.valueOf(epochSecond));
                l lVar4 = new l("validity", String.valueOf(90));
                String strM = "";
                l lVar5 = new l("userId", "");
                MessageDigest messageDigest = MessageDigest.getInstance("sha256");
                Charset UTF_8 = StandardCharsets.UTF_8;
                kotlin.jvm.internal.l.e(UTF_8, "UTF_8");
                byte[] bytes = str2.getBytes(UTF_8);
                kotlin.jvm.internal.l.e(bytes, "getBytes(...)");
                byte[] bArrDigest = messageDigest.digest(bytes);
                kotlin.jvm.internal.l.c(bArrDigest);
                String strE = Fg.l.E(A.f(lVar, lVar2, lVar3, lVar4, lVar5, new l("value", k.r(bArrDigest, "", new Y9.b(19), 30))).entrySet(), "&", null, null, new Y9.b(20), 30);
                Context context2 = context;
                SigningInfo signingInfo = context2.getPackageManager().getPackageInfo(context2.getPackageName(), 134217728).signingInfo;
                Signature[] apkContentsSigners = signingInfo != null ? signingInfo.getApkContentsSigners() : null;
                String strS = apkContentsSigners != null ? k.s(apkContentsSigners, new Y9.b(18), 30) : null;
                if (strS == null) {
                    strS = "";
                }
                if (!n.D(strS)) {
                    long epochSecond2 = Instant.now().getEpochSecond();
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("alg", "HS256");
                    jSONObject.put("typ", "JWT");
                    String string = jSONObject.toString();
                    kotlin.jvm.internal.l.e(string, "toString(...)");
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put(AccessToken.TOKEN_KEY, strS);
                    jSONObject2.put(AuthenticationTokenClaims.JSON_KEY_IAT, epochSecond2);
                    jSONObject2.put(AuthenticationTokenClaims.JSON_KEY_EXP, epochSecond2 + ((long) 90));
                    String string2 = jSONObject2.toString();
                    kotlin.jvm.internal.l.e(string2, "toString(...)");
                    byte[] bytes2 = string.getBytes(UTF_8);
                    kotlin.jvm.internal.l.e(bytes2, "getBytes(...)");
                    String strEncodeToString = Base64.encodeToString(bytes2, 11);
                    kotlin.jvm.internal.l.e(strEncodeToString, "encodeToString(...)");
                    byte[] bytes3 = string2.getBytes(UTF_8);
                    kotlin.jvm.internal.l.e(bytes3, "getBytes(...)");
                    String strEncodeToString2 = Base64.encodeToString(bytes3, 11);
                    kotlin.jvm.internal.l.e(strEncodeToString2, "encodeToString(...)");
                    String strM2 = q.m(strEncodeToString, ".", strEncodeToString2);
                    Mac mac = Mac.getInstance("HmacSHA256");
                    byte[] bytes4 = "36ccfe00-78fc-4cab-9c5b-5460b0c78513".getBytes(UTF_8);
                    kotlin.jvm.internal.l.e(bytes4, "getBytes(...)");
                    mac.init(new SecretKeySpec(bytes4, "HmacSHA256"));
                    byte[] bytes5 = strM2.getBytes(UTF_8);
                    kotlin.jvm.internal.l.e(bytes5, "getBytes(...)");
                    byte[] bArrDoFinal = mac.doFinal(bytes5);
                    kotlin.jvm.internal.l.e(bArrDoFinal, "doFinal(...)");
                    String strEncodeToString3 = Base64.encodeToString(bArrDoFinal, 11);
                    kotlin.jvm.internal.l.e(strEncodeToString3, "encodeToString(...)");
                    strM = q.m(strM2, ".", strEncodeToString3);
                }
                G gA = gVar.f1746f.a();
                gA.a("Authorization", "Signature " + strE);
                gA.a("app-token", "Bearer " + strM);
                gA.a("package-id", "starnest.aichat.aichatbot.assistant");
                gA.a("device-id", deviceId);
                gA.a("isUserInAdMode", String.valueOf(com.starnest.keyboard.model.model.c.isUserInAdMode(com.starnest.keyboard.model.model.c.getConfig(context2))));
                return gVar.b(gA.b());
            }
        });
        File cacheDir = context.getCacheDir();
        kotlin.jvm.internal.l.e(cacheDir, "getCacheDir(...)");
        d3.f45211k = new C4118f(cacheDir, 10485760L);
        TimeUnit unit = TimeUnit.SECONDS;
        kotlin.jvm.internal.l.f(unit, "unit");
        d3.f45222w = p381xh.a.b(120L);
        kotlin.jvm.internal.l.f(unit, "unit");
        d3.f45223x = p381xh.a.b(120L);
        kotlin.jvm.internal.l.f(unit, "unit");
        d3.f45221v = p381xh.a.b(120L);
        f fVar = new f(0);
        SSLContext sSLContext = SSLContext.getInstance("SSL");
        sSLContext.init(null, new X509TrustManager[]{fVar}, new SecureRandom());
        SSLSocketFactory socketFactory = sSLContext.getSocketFactory();
        kotlin.jvm.internal.l.e(socketFactory, "getSocketFactory(...)");
        d3.a(socketFactory, fVar);
        d3.f45218s = new C1261b();
        d3.f45204c.add(new Bh.a(2, context));
        E e2 = new E(d3);
        j jVar = new j();
        jVar.f32636k = true;
        Object objCreate = new Retrofit.Builder().baseUrl("https://api-aichat.starnestsolution.com/").client(e2).addConverterFactory(GsonConverterFactory.create(jVar.a())).build().create(ChatGPTService.class);
        kotlin.jvm.internal.l.e(objCreate, "create(...)");
        return (ChatGPTService) objCreate;
    }
}
