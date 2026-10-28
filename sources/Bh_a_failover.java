package Bh;

import A6.S;
import Ah.m;
import Ah.o;
import Dh.C0253a;
import Fg.t;
import Kh.p;
import android.content.Context;
import com.android.inputmethod.latin.spellcheck.AndroidSpellCheckerService;
import com.facebook.GraphRequest;
import com.google.android.gms.internal.ads.Rz;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.regex.Pattern;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import kotlin.jvm.internal.l;
import p324u6.AbstractC3792x3;
import p340v6.H4;
import p368wh.C4113a;
import p368wh.C4114b;
import p368wh.C4123k;
import p368wh.E;
import p368wh.G;
import p368wh.H;
import p368wh.L;
import p368wh.M;
import p368wh.N;
import p368wh.Q;
import p368wh.u;
import p368wh.v;
import p368wh.w;
import p368wh.x;
import p368wh.y;
import p368wh.z;

/* JADX INFO: loaded from: classes4.dex */
public final class a implements y {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1735a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Object f1736b;

    public /* synthetic */ a(int i2, Object obj) {
        this.f1735a = i2;
        this.f1736b = obj;
    }

    public a(C4114b cookieJar) {
        this.f1735a = 0;
        l.f(cookieJar, "cookieJar");
        this.f1736b = cookieJar;
    }

    public static int d(N n2, int i2) {
        String strB = N.b("Retry-After", n2);
        if (strB == null) {
            return i2;
        }
        Pattern patternCompile = Pattern.compile("\\d+");
        l.e(patternCompile, "compile(...)");
        if (!patternCompile.matcher(strB).matches()) {
            return Integer.MAX_VALUE;
        }
        Integer numValueOf = Integer.valueOf(strB);
        l.e(numValueOf, "Integer.valueOf(header)");
        return numValueOf.intValue();
    }

    @Override // p368wh.y
    public final N a(g gVar) {
        Q q;
        N nB;
        boolean z6;
        SSLSocketFactory sSLSocketFactory;
        HostnameVerifier hostnameVerifier;
        C4123k c4123k;
        N nE;
        switch (this.f1735a) {
            case 0:
                C4114b c4114b = (C4114b) this.f1736b;
                H h = gVar.f1746f;
                G gA = h.a();
                v vVar = h.f45262d;
                x url = h.f45260b;
                L l6 = h.f45263e;
                if (l6 != null) {
                    z zVarContentType = l6.contentType();
                    if (zVarContentType != null) {
                        gA.c(GraphRequest.CONTENT_TYPE_HEADER, zVarContentType.f45420a);
                    }
                    long jContentLength = l6.contentLength();
                    if (jContentLength != -1) {
                        gA.c("Content-Length", String.valueOf(jContentLength));
                        gA.f45256c.f("Transfer-Encoding");
                    } else {
                        gA.c("Transfer-Encoding", "chunked");
                        gA.f45256c.f("Content-Length");
                    }
                }
                boolean z8 = false;
                if (vVar.c("Host") == null) {
                    gA.c("Host", p381xh.a.t(url, false));
                }
                if (vVar.c("Connection") == null) {
                    gA.c("Connection", "Keep-Alive");
                }
                if (vVar.c("Accept-Encoding") == null && vVar.c("Range") == null) {
                    gA.c("Accept-Encoding", "gzip");
                    z8 = true;
                }
                c4114b.getClass();
                l.f(url, "url");
                if (vVar.c(GraphRequest.USER_AGENT_HEADER) == null) {
                    gA.c(GraphRequest.USER_AGENT_HEADER, "okhttp/4.9.0");
                }
                N nB2 = gVar.b(gA.b());
                v vVar2 = nB2.q0;
                f.b(c4114b, url, vVar2);
                M mQ = nB2.q();
                mQ.f45272a = h;
                if (z8 && "gzip".equalsIgnoreCase(N.b(GraphRequest.CONTENT_ENCODING_HEADER, nB2)) && f.a(nB2) && (q = nB2.f45289r0) != null) {
                    p pVar = new p(q.source());
                    u uVarF = vVar2.f();
                    uVarF.f(GraphRequest.CONTENT_ENCODING_HEADER);
                    uVarF.f("Content-Length");
                    mQ.c(uVarF.d());
                    mQ.f45278g = new h(N.b(GraphRequest.CONTENT_TYPE_HEADER, nB2), -1L, H4.b(pVar));
                }
                return mQ.a();
            case 1:
                H h10 = gVar.f1746f;
                Ah.i iVar = gVar.f1742b;
                List listK = t.f3876n;
                N n2 = null;
                int i2 = 0;
                H request = h10;
                while (true) {
                    boolean z10 = true;
                    while (true) {
                        l.f(request, "request");
                        if (iVar.f935r0 != null) {
                            throw new IllegalStateException("Check failed.");
                        }
                        synchronized (iVar) {
                            try {
                                if (iVar.f937t0) {
                                    throw new IllegalStateException("cannot make a new request because the previous response is still open: please call response.close()");
                                }
                                if (iVar.f936s0) {
                                    throw new IllegalStateException("Check failed.");
                                }
                            } catch (Throwable th2) {
                                throw th2;
                            }
                        }
                        if (z10) {
                            S s2 = iVar.f933n;
                            x xVar = request.f45260b;
                            E e2 = iVar.f941y0;
                            if (xVar.f45409a) {
                                SSLSocketFactory sSLSocketFactory2 = e2.f45247z0;
                                if (sSLSocketFactory2 == null) {
                                    throw new IllegalStateException("CLEARTEXT-only client");
                                }
                                HostnameVerifier hostnameVerifier2 = e2.f45229D0;
                                c4123k = e2.f45230E0;
                                sSLSocketFactory = sSLSocketFactory2;
                                hostnameVerifier = hostnameVerifier2;
                            } else {
                                sSLSocketFactory = null;
                                hostnameVerifier = null;
                                c4123k = null;
                            }
                            iVar.f932Z = new Ah.e(s2, new C4113a(xVar.f45413e, xVar.f45414f, e2.v0, e2.f45246y0, sSLSocketFactory, hostnameVerifier, c4123k, e2.f45245x0, e2.f45228C0, e2.f45227B0, e2.f45244w0), iVar);
                        }
                        try {
                            if (iVar.v0) {
                                throw new IOException("Canceled");
                            }
                            try {
                                nB = gVar.b(request);
                            } catch (o e4) {
                                if (!c(e4.f963n, iVar, request, false)) {
                                    IOException iOException = e4.q;
                                    p381xh.a.x(iOException, listK);
                                    throw iOException;
                                }
                                listK = Fg.l.K(listK, e4.q);
                                iVar.f(true);
                                z10 = false;
                            } catch (IOException e9) {
                                if (!c(e9, iVar, request, !(e9 instanceof C0253a))) {
                                    p381xh.a.x(e9, listK);
                                    throw e9;
                                }
                                listK = Fg.l.K(listK, e9);
                                iVar.f(true);
                                z10 = false;
                            }
                        } catch (Throwable th3) {
                            iVar.f(true);
                            throw th3;
                        }
                        break;
                    }
                    if (n2 != null) {
                        M mQ2 = nB.q();
                        M mQ3 = n2.q();
                        mQ3.f45278g = null;
                        N nA = mQ3.a();
                        if (nA.f45289r0 != null) {
                            throw new IllegalArgumentException("priorResponse.body != null");
                        }
                        mQ2.f45280j = nA;
                        nB = mQ2.a();
                    }
                    n2 = nB;
                    Ah.d dVar = iVar.f935r0;
                    request = b(n2, dVar);
                    if (request == null) {
                        if (dVar != null && dVar.f913a) {
                            if (iVar.q0) {
                                throw new IllegalStateException("Check failed.");
                            }
                            iVar.q0 = true;
                            iVar.q.i();
                        }
                        z6 = false;
                    } else {
                        z6 = false;
                        L l10 = request.f45263e;
                        if (l10 == null || !l10.isOneShot()) {
                            Q q2 = n2.f45289r0;
                            if (q2 != null) {
                                p381xh.a.c(q2);
                            }
                            i2++;
                            if (i2 > 20) {
                                throw new ProtocolException("Too many follow-up requests: " + i2);
                            }
                            iVar.f(true);
                        }
                    }
                    iVar.f(z6);
                    return n2;
                }
            default:
                H h11 = gVar.f1746f;
                try {
                    nE = gVar.b(h11);
                    try {
                        if (!nE.d()) {
                            nE.close();
                            try {
                                return e(gVar, h11);
                            } catch (Exception unused) {
                            }
                        }
                    } catch (IOException unused2) {
                        if (nE != null) {
                            nE.close();
                        }
                        nE = e(gVar, h11);
                    }
                } catch (IOException unused3) {
                    nE = null;
                }
                return nE;
        }
    }

    /* JADX WARN: Code duplicated, block: B:103:0x013b A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:104:0x013d  */
    /* JADX WARN: Code duplicated, block: B:107:0x0144  */
    /* JADX WARN: Code duplicated, block: B:110:0x0161  */
    /* JADX WARN: Code duplicated, block: B:76:0x00da  */
    /* JADX WARN: Code duplicated, block: B:79:0x00e6  */
    /* JADX WARN: Code duplicated, block: B:81:0x00f0  */
    /* JADX WARN: Code duplicated, block: B:83:0x00fb  */
    /* JADX WARN: Code duplicated, block: B:84:0x0100  */
    /* JADX WARN: Code duplicated, block: B:93:0x011e  */
    /* JADX WARN: Code duplicated, block: B:97:0x012a  */
    public H b(N n2, Ah.d dVar) throws ProtocolException {
        E e2;
        String strB;
        H h;
        w wVarF;
        x xVarA;
        G gA;
        boolean z6;
        L l6;
        N n10;
        m mVar;
        p368wh.S s2 = (dVar == null || (mVar = (m) dVar.f914b) == null) ? null : mVar.q;
        int i2 = n2.f45286Z;
        H h10 = n2.q;
        String str = h10.f45261c;
        if (i2 == 307 || i2 == 308) {
            e2 = (E) this.f1736b;
            if (e2.f45240r0) {
                strB = N.b("Location", n2);
                h = n2.q;
                if (strB != null) {
                    x xVar = h.f45260b;
                    xVar.getClass();
                    wVarF = xVar.f(strB);
                    if (wVarF != null) {
                        xVarA = wVarF.a();
                    } else {
                        xVarA = null;
                    }
                    if (xVarA != null && (l.a(xVarA.f45410b, h.f45260b.f45410b) || e2.f45241s0)) {
                        gA = h.a();
                        if (AbstractC3792x3.b(str)) {
                            int i10 = n2.f45286Z;
                            z6 = !str.equals("PROPFIND") || i10 == 308 || i10 == 307;
                            if (!str.equals("PROPFIND") || i10 == 308 || i10 == 307) {
                                gA.d(str, z6 ? h.f45263e : null);
                            } else {
                                gA.d("GET", null);
                            }
                            if (!z6) {
                                gA.f45256c.f("Transfer-Encoding");
                                gA.f45256c.f("Content-Length");
                                gA.f45256c.f(GraphRequest.CONTENT_TYPE_HEADER);
                            }
                        }
                        if (!p381xh.a.a(h.f45260b, xVarA)) {
                            gA.f45256c.f("Authorization");
                        }
                        gA.f45254a = xVarA;
                        return gA.b();
                    }
                }
            }
        } else {
            if (i2 == 401) {
                ((E) this.f1736b).q0.getClass();
                return null;
            }
            if (i2 == 421) {
                L l10 = h10.f45263e;
                if ((l10 == null || !l10.isOneShot()) && dVar != null && !l.a(((Ah.e) dVar.f916d).h.f45306a.f45413e, ((m) dVar.f914b).q.f45298a.f45306a.f45413e)) {
                    m mVar2 = (m) dVar.f914b;
                    synchronized (mVar2) {
                        mVar2.f954j = true;
                    }
                    return n2.q;
                }
            } else if (i2 == 503) {
                N n11 = n2.f45292u0;
                if ((n11 == null || n11.f45286Z != 503) && d(n2, Integer.MAX_VALUE) == 0) {
                    return n2.q;
                }
            } else {
                if (i2 == 407) {
                    l.c(s2);
                    if (s2.f45299b.type() != Proxy.Type.HTTP) {
                        throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                    }
                    ((E) this.f1736b).f45245x0.getClass();
                    return null;
                }
                if (i2 != 408) {
                    switch (i2) {
                        case 300:
                        case AndroidSpellCheckerService.SPELLCHECKER_DUMMY_KEYBOARD_HEIGHT /* 301 */:
                        case 302:
                        case 303:
                            e2 = (E) this.f1736b;
                            if (e2.f45240r0) {
                                strB = N.b("Location", n2);
                                h = n2.q;
                                if (strB != null) {
                                    x xVar2 = h.f45260b;
                                    xVar2.getClass();
                                    wVarF = xVar2.f(strB);
                                    if (wVarF != null) {
                                        xVarA = wVarF.a();
                                    } else {
                                        xVarA = null;
                                    }
                                    if (xVarA != null) {
                                        gA = h.a();
                                        if (AbstractC3792x3.b(str)) {
                                            int i11 = n2.f45286Z;
                                            if (str.equals("PROPFIND")) {
                                            }
                                            if (str.equals("PROPFIND")) {
                                                gA.d(str, z6 ? h.f45263e : null);
                                            } else {
                                                gA.d(str, z6 ? h.f45263e : null);
                                            }
                                            if (!z6) {
                                                gA.f45256c.f("Transfer-Encoding");
                                                gA.f45256c.f("Content-Length");
                                                gA.f45256c.f(GraphRequest.CONTENT_TYPE_HEADER);
                                            }
                                        }
                                        if (!p381xh.a.a(h.f45260b, xVarA)) {
                                            gA.f45256c.f("Authorization");
                                        }
                                        gA.f45254a = xVarA;
                                        return gA.b();
                                    }
                                }
                            }
                        default:
                            return null;
                    }
                } else if (((E) this.f1736b).f45239p0 && (((l6 = h10.f45263e) == null || !l6.isOneShot()) && (((n10 = n2.f45292u0) == null || n10.f45286Z != 408) && d(n2, 0) <= 0))) {
                    return n2.q;
                }
            }
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:37:0x0053  */
    /* JADX WARN: Code duplicated, block: B:40:0x0058  */
    /* JADX WARN: Code duplicated, block: B:42:0x005b  */
    /* JADX WARN: Code duplicated, block: B:52:0x006f A[ADDED_TO_REGION, DONT_GENERATE, REMOVE] */
    /* JADX WARN: Code duplicated, block: B:54:0x0071 A[Catch: all -> 0x0087, TRY_ENTER, TRY_LEAVE, TryCatch #0 {, blocks: (B:50:0x006b, B:54:0x0071, B:58:0x0083), top: B:78:0x006b }] */
    /* JADX WARN: Code duplicated, block: B:65:0x008c  */
    /* JADX WARN: Code duplicated, block: B:66:0x008e  */
    /* JADX WARN: Code duplicated, block: B:67:0x0090  */
    /* JADX WARN: Code duplicated, block: B:69:0x0094  */
    /* JADX WARN: Code duplicated, block: B:72:0x009b  */
    /* JADX WARN: Code duplicated, block: B:77:0x00a6 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:78:0x006b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public boolean c(IOException iOException, Ah.i iVar, H h, boolean z6) {
        Ah.e eVar;
        int i2;
        boolean zG;
        p368wh.S s2;
        Ah.p pVar;
        Rz rz;
        m mVar;
        L l6;
        if (!((E) this.f1736b).f45239p0 || ((z6 && (((l6 = h.f45263e) != null && l6.isOneShot()) || (iOException instanceof FileNotFoundException))) || (iOException instanceof ProtocolException))) {
            return false;
        }
        if (!(iOException instanceof InterruptedIOException)) {
            if (((iOException instanceof SSLHandshakeException) && (iOException.getCause() instanceof CertificateException)) || (iOException instanceof SSLPeerUnverifiedException)) {
                return false;
            }
            eVar = iVar.f932Z;
            l.c(eVar);
            i2 = eVar.f920c;
            if (i2 != 0) {
                if (eVar.f923f != null) {
                    zG = true;
                } else {
                    s2 = null;
                    if (i2 <= 1) {
                        synchronized (mVar) {
                            if (mVar.f955k != 0) {
                                s2 = mVar.q;
                            }
                        }
                    }
                    if (s2 != null) {
                        eVar.f923f = s2;
                    } else {
                        pVar = eVar.f918a;
                        zG = pVar != null ? rz.g() : rz.g();
                    }
                    zG = true;
                }
            } else if (eVar.f923f != null) {
                zG = true;
            } else {
                s2 = null;
                if (i2 <= 1) {
                    synchronized (mVar) {
                        if (mVar.f955k != 0) {
                            s2 = mVar.q;
                        }
                    }
                }
                if (s2 != null) {
                    eVar.f923f = s2;
                } else {
                    pVar = eVar.f918a;
                    if (pVar != null) {
                    }
                }
                zG = true;
            }
            if (!zG) {
                return true;
            }
        } else if ((iOException instanceof SocketTimeoutException) && !z6) {
            eVar = iVar.f932Z;
            l.c(eVar);
            i2 = eVar.f920c;
            if (i2 != 0 && eVar.f921d == 0 && eVar.f922e == 0) {
                zG = false;
            } else if (eVar.f923f != null) {
                zG = true;
            } else {
                s2 = null;
                if (i2 <= 1 && eVar.f921d <= 1 && eVar.f922e <= 0 && (mVar = eVar.f925i.f934p0) != null) {
                    synchronized (mVar) {
                        if (mVar.f955k != 0 && p381xh.a.a(mVar.q.f45298a.f45306a, eVar.h.f45306a)) {
                            s2 = mVar.q;
                        }
                    }
                }
                if (s2 != null) {
                    eVar.f923f = s2;
                } else {
                    pVar = eVar.f918a;
                    if ((pVar != null || !pVar.b()) && (rz = eVar.f919b) != null) {
                    }
                }
                zG = true;
            }
            if (!zG) {
                return true;
            }
        }
        return false;
    }

    public N e(g gVar, H h) {
        b3.i.s(Gc.c.b((Context) this.f1736b), "REQUEST_FAILED_ON_NEW_SERVER");
        String strL = p016ah.u.l(h.f45260b.f45417j, "https://api-aichat.starnestsolution.com/", "https://api-akeyboard.starnestsolution.com/", false);
        G gA = h.a();
        gA.f(strL);
        return gVar.b(gA.b());
    }
}
