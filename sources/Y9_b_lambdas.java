package Y9;

import Eg.C;
import Nc.N0;
import android.content.pm.Signature;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodSubtype;
import com.android.inputmethod.event.Combiner;
import com.android.inputmethod.event.CombinerChain;
import com.android.inputmethod.keyboard.KeyboardId;
import com.android.inputmethod.keyboard.internal.keyboard_parser.JsonKeyboardParserKt;
import com.android.inputmethod.keyboard.internal.keyboard_parser.LocaleKeyboardInfosKt;
import com.android.inputmethod.latin.DictionaryFactoryKt;
import com.android.inputmethod.latin.InputView;
import com.android.inputmethod.latin.utils.PopupKeysUtilsKt;
import com.android.inputmethod.latin.utils.SubtypeSettingsKt;
import com.android.inputmethod.latin.utils.SubtypeUtilsKt;
import com.android.inputmethod.latin.utils.ToolbarKey;
import com.android.inputmethod.latin.utils.ToolbarUtilsKt;
import com.starnest.aichat.keyboard.App;
import com.starnest.aichat.keyboard.model.database.entity.Message;
import com.starnest.aichat.keyboard.ui.assistant.activity.OutputActivity;
import com.starnest.aichat.keyboard.ui.setting.activity.SettingActivity;
import com.starnest.core.di.adapter.BooleanAdapter;
import com.starnest.core.di.adapter.DoubleAdapter;
import com.starnest.core.di.adapter.IntegerAdapter;
import com.starnest.keyboard.model.model.TypeAiMenu;
import com.starnest.keyboard.model.model.YoutubeTranscriptData;
import com.starnest.keyboard.model.replybotsdatabase.entity.ReplyBot;
import com.starnest.keyboard.view.typeai.result.ResultView;
import com.starnest.keyboard.view.widget.AppEditText;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import p068dh.AbstractC3224u;
import p324u6.AbstractC3764t;
import p340v6.J4;

/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class b implements Sg.c {

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public final /* synthetic */ int f16459n;

    public /* synthetic */ b(int i2) {
        this.f16459n = i2;
    }

    @Override // Sg.c
    public final Object invoke(Object obj) throws Exception {
        ArrayList arrayList;
        Date dateH;
        Date dateH2;
        Date dateH3;
        int i2 = this.f16459n;
        C c5 = C.f3246a;
        switch (i2) {
            case 0:
                YoutubeTranscriptData it = (YoutubeTranscriptData) obj;
                kotlin.jvm.internal.l.f(it, "it");
                return it.getText();
            case 1:
                p090f3.c cVarL0 = ((p090f3.a) obj).l0("select * from `ReplyBot` where isVisibleInKeyboard = 1 and deletedAt IS NULL order by isPin DESC, createdAt DESC");
                try {
                    int iD = AbstractC3764t.d(cVarL0, "id");
                    int iD2 = AbstractC3764t.d(cVarL0, "category");
                    int iD3 = AbstractC3764t.d(cVarL0, "name");
                    int iD4 = AbstractC3764t.d(cVarL0, "imageFileName");
                    int iD5 = AbstractC3764t.d(cVarL0, "isCustomImage");
                    int iD6 = AbstractC3764t.d(cVarL0, "prompt");
                    int iD7 = AbstractC3764t.d(cVarL0, "sources");
                    int iD8 = AbstractC3764t.d(cVarL0, "isVisibleInKeyboard");
                    int iD9 = AbstractC3764t.d(cVarL0, "isPin");
                    int iD10 = AbstractC3764t.d(cVarL0, "createdAt");
                    int iD11 = AbstractC3764t.d(cVarL0, "updatedAt");
                    int iD12 = AbstractC3764t.d(cVarL0, "deletedAt");
                    ArrayList arrayList2 = new ArrayList();
                    while (cVarL0.h0()) {
                        ReplyBot replyBot = new ReplyBot();
                        String strR = cVarL0.isNull(iD) ? null : cVarL0.R(iD);
                        UUID uuidFromString = strR == null ? null : UUID.fromString(strR);
                        kotlin.jvm.internal.l.f(uuidFromString, "<set-?>");
                        replyBot.f34837n = uuidFromString;
                        String strR2 = cVarL0.isNull(iD2) ? null : cVarL0.R(iD2);
                        kotlin.jvm.internal.l.f(strR2, "<set-?>");
                        replyBot.q = strR2;
                        String strR3 = cVarL0.isNull(iD3) ? null : cVarL0.R(iD3);
                        kotlin.jvm.internal.l.f(strR3, "<set-?>");
                        replyBot.f34834X = strR3;
                        String strR4 = cVarL0.isNull(iD4) ? null : cVarL0.R(iD4);
                        kotlin.jvm.internal.l.f(strR4, "<set-?>");
                        replyBot.f34835Y = strR4;
                        int i10 = iD2;
                        replyBot.f34836Z = ((int) cVarL0.getLong(iD5)) != 0;
                        String strR5 = cVarL0.isNull(iD6) ? null : cVarL0.R(iD6);
                        kotlin.jvm.internal.l.f(strR5, "<set-?>");
                        replyBot.f34838p0 = strR5;
                        String strR6 = cVarL0.isNull(iD7) ? null : cVarL0.R(iD7);
                        if (strR6 == null) {
                            arrayList = null;
                        } else {
                            Type type = new Bc.a().getType();
                            BooleanAdapter booleanAdapter = new BooleanAdapter();
                            IntegerAdapter integerAdapter = new IntegerAdapter();
                            DoubleAdapter doubleAdapter = new DoubleAdapter();
                            com.google.gson.j jVar = new com.google.gson.j();
                            jVar.b(Boolean.TYPE, booleanAdapter);
                            jVar.b(Integer.TYPE, integerAdapter);
                            jVar.b(Double.TYPE, doubleAdapter);
                            jVar.f32633g = "MMM dd, yyyy HH:mm:ss";
                            arrayList = (ArrayList) jVar.a().d(strR6, type);
                        }
                        kotlin.jvm.internal.l.f(arrayList, "<set-?>");
                        replyBot.q0 = arrayList;
                        replyBot.f34839r0 = ((int) cVarL0.getLong(iD8)) != 0;
                        replyBot.f34840s0 = ((int) cVarL0.getLong(iD9)) != 0;
                        String strR7 = cVarL0.isNull(iD10) ? null : cVarL0.R(iD10);
                        if (strR7 != null) {
                            try {
                                dateH = J4.h(strR7, "yyyy-MM-dd HH:mm:ss.SSS Z");
                            } catch (Exception unused) {
                                dateH = J4.h(strR7, "yyyy-MM-dd HH:mm:ss");
                            }
                        } else {
                            dateH = null;
                        }
                        kotlin.jvm.internal.l.f(dateH, "<set-?>");
                        replyBot.f34841t0 = dateH;
                        String strR8 = cVarL0.isNull(iD11) ? null : cVarL0.R(iD11);
                        if (strR8 != null) {
                            try {
                                dateH2 = J4.h(strR8, "yyyy-MM-dd HH:mm:ss.SSS Z");
                            } catch (Exception unused2) {
                                dateH2 = J4.h(strR8, "yyyy-MM-dd HH:mm:ss");
                            }
                        } else {
                            dateH2 = null;
                        }
                        kotlin.jvm.internal.l.f(dateH2, "<set-?>");
                        replyBot.f34842u0 = dateH2;
                        String strR9 = cVarL0.isNull(iD12) ? null : cVarL0.R(iD12);
                        if (strR9 != null) {
                            try {
                                dateH3 = J4.h(strR9, "yyyy-MM-dd HH:mm:ss.SSS Z");
                            } catch (Exception unused3) {
                                dateH3 = J4.h(strR9, "yyyy-MM-dd HH:mm:ss");
                            }
                        } else {
                            dateH3 = null;
                        }
                        replyBot.v0 = dateH3;
                        arrayList2.add(replyBot);
                        iD = iD;
                        iD2 = i10;
                        iD3 = iD3;
                        iD4 = iD4;
                        iD5 = iD5;
                        iD6 = iD6;
                        iD7 = iD7;
                        break;
                    }
                    cVarL0.close();
                    return arrayList2;
                } catch (Throwable th2) {
                    cVarL0.close();
                    throw th2;
                }
            case 2:
                return Boolean.valueOf(obj == null);
            case 3:
                int i11 = OutputActivity.f33011A0;
                kotlin.jvm.internal.l.f((View) obj, "it");
                App.Companion.getClass();
                G9.g.a().B();
                return c5;
            case 4:
                int i12 = ResultView.f34940s0;
                kotlin.jvm.internal.l.f((View) obj, "it");
                p030bd.e.Companion.getClass();
                p030bd.c.a();
                App.Companion.getClass();
                G9.g.a().B();
                return c5;
            case 5:
                return Boolean.valueOf(CombinerChain._set_isHangul_$lambda$0((Combiner) obj));
            case 6:
                return DictionaryFactoryKt.createMainDictionary$lambda$5$0((String) obj);
            case 7:
                return InputView.loadMenus$lambda$0$0$0((TypeAiMenu) obj);
            case 8:
                return InputView.loadMenus$lambda$0$0$1((TypeAiMenu) obj);
            case 9:
                return SubtypeUtilsKt.locale((InputMethodSubtype) obj);
            case 10:
                return PopupKeysUtilsKt.reorderPopupKeysDialog$lambda$1$0((Eg.l) obj);
            case 11:
                return SubtypeSettingsKt.getMatchingLayoutSetNameForLocale$lambda$0((InputMethodSubtype) obj);
            case 12:
                return SubtypeSettingsKt.getDefaultEnabledSubtypes$lambda$0$0((Locale) obj);
            case 13:
                return ToolbarUtilsKt.toToolbarKeyString$lambda$0((ToolbarKey) obj);
            case 14:
                TypeAiMenu it2 = (TypeAiMenu) obj;
                kotlin.jvm.internal.l.f(it2, "it");
                return Boolean.valueOf(it2.getType() == N0.REPLY_BOT);
            case 15:
                Ig.i iVar = (Ig.i) obj;
                if (iVar instanceof AbstractC3224u) {
                    return (AbstractC3224u) iVar;
                }
                return null;
            case 16:
                Eg.l it3 = (Eg.l) obj;
                kotlin.jvm.internal.l.f(it3, "it");
                return (CharSequence) it3.q;
            case 17:
                Eg.l it4 = (Eg.l) obj;
                kotlin.jvm.internal.l.f(it4, "it");
                return (CharSequence) it4.q;
            case 18:
                String strEncodeToString = Base64.encodeToString(MessageDigest.getInstance("SHA-256").digest(((Signature) obj).toByteArray()), 2);
                kotlin.jvm.internal.l.e(strEncodeToString, "encodeToString(...)");
                return strEncodeToString;
            case 19:
                Byte b10 = (Byte) obj;
                b10.byteValue();
                return String.format("%02x", Arrays.copyOf(new Object[]{b10}, 1));
            case 20:
                Map.Entry it5 = (Map.Entry) obj;
                kotlin.jvm.internal.l.f(it5, "it");
                String str = (String) it5.getKey();
                Charset charset = StandardCharsets.UTF_8;
                return A5.q.m(URLEncoder.encode(str, charset.toString()), "=", URLEncoder.encode((String) it5.getValue(), charset.toString()));
            case 21:
                p016ah.i it6 = (p016ah.i) obj;
                kotlin.jvm.internal.l.f(it6, "it");
                String str2 = (String) ((Fg.C) it6.a()).get(1);
                W2.x.a(16);
                char[] chars = Character.toChars(Integer.parseInt(str2, 16));
                kotlin.jvm.internal.l.e(chars, "toChars(...)");
                return new String(chars);
            case KeyboardId.ELEMENT_EMOJI_CATEGORY12 /* 22 */:
                return JsonKeyboardParserKt.florisJsonConfig$lambda$0$0$0$0((String) obj);
            case KeyboardId.ELEMENT_EMOJI_CATEGORY13 /* 23 */:
                return JsonKeyboardParserKt.florisJsonConfig$lambda$0$0$1$0((String) obj);
            case KeyboardId.ELEMENT_EMOJI_CATEGORY14 /* 24 */:
                return JsonKeyboardParserKt.florisJsonConfig$lambda$0((p300sh.f) obj);
            case 25:
                return LocaleKeyboardInfosKt.addLocaleKeyTextsToParams$lambda$0((Locale) obj);
            case KeyboardId.ELEMENT_EMOJI_CATEGORY16 /* 26 */:
                int i13 = AppEditText.q;
                kotlin.jvm.internal.l.f((View) obj, "it");
                p030bd.e.Companion.getClass();
                p030bd.c.a().d();
                p030bd.c.a().onStartInputViewInternal(new EditorInfo(), true);
                return c5;
            case 27:
                ((Boolean) obj).getClass();
                int i14 = SettingActivity.f34255y0;
                return c5;
            case KeyboardId.ELEMENT_NUMPAD /* 28 */:
                Message it7 = (Message) obj;
                kotlin.jvm.internal.l.f(it7, "it");
                return Boolean.valueOf(it7.f32842B0);
            default:
                Message it8 = (Message) obj;
                kotlin.jvm.internal.l.f(it8, "it");
                S9.d dVar = it8.f32846F0;
                return Boolean.valueOf(dVar != null && dVar.a());
        }
    }
}
