package com.starnest.keyboard.model.model;

import Ab.j;
import Ba.C0130l0;
import Ba.D;
import Fg.m;
import Fg.q;
import Nc.EnumC1015l;
import android.content.Context;
import com.facebook.GraphRequest;
import com.starnest.core.app.AbstractApplication;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.l;
import p016ah.n;
import p340v6.E4;

/* JADX INFO: loaded from: classes.dex */
public abstract class c {
    public static final void addCurrentClipBoard(b bVar, String currentClipBoard) {
        l.f(bVar, "<this>");
        l.f(currentClipBoard, "currentClipBoard");
        if (bVar.getCurrentClipBoards().contains(currentClipBoard)) {
            return;
        }
        ArrayList arrayListB = E4.b(bVar.getCurrentClipBoards());
        arrayListB.add(currentClipBoard);
        if (arrayListB.size() > 10) {
            q.t(arrayListB);
        }
        bVar.setCurrentClipBoards(arrayListB);
    }

    public static final void addRecentSearch(b bVar, String newRecent) {
        l.f(bVar, "<this>");
        l.f(newRecent, "newRecent");
        ArrayList arrayListT = Fg.l.T(bVar.getRecentSearch());
        arrayListT.remove(newRecent);
        arrayListT.add(0, newRecent);
        bVar.setRecentSearch(Fg.l.O(arrayListT, 7));
    }

    public static final boolean checkShouldShowGift(b bVar) {
        l.f(bVar, "<this>");
        Context context = bVar.getContext();
        AbstractApplication abstractApplication = context instanceof AbstractApplication ? (AbstractApplication) context : null;
        if (abstractApplication != null ? abstractApplication.d() : false) {
            return false;
        }
        if (bVar.getInstallTime() == 0) {
            return true;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -2);
        return bVar.getInstallTime() > calendar.getTimeInMillis();
    }

    public static final HashMap<String, String> createAdsHeader(b bVar) {
        l.f(bVar, "<this>");
        Context context = bVar.getContext();
        AbstractApplication abstractApplication = context instanceof AbstractApplication ? (AbstractApplication) context : null;
        boolean zD = abstractApplication != null ? abstractApplication.d() : false;
        HashMap<String, String> map = new HashMap<>();
        map.put("isVip", String.valueOf(zD));
        map.put("chatModel", (isUserInAdMode(bVar) ? EnumC1015l.GPT_3_5_Turbo : EnumC1015l.GPT_3_5_Turbo).getChatModel());
        map.put(GraphRequest.ACCEPT_LANGUAGE_HEADER, bVar.getCurrentLanguage());
        return map;
    }

    public static final long getActiveDay(b bVar) {
        l.f(bVar, "<this>");
        return TimeUnit.DAYS.convert(Calendar.getInstance().getTimeInMillis() - bVar.getInstallTime(), TimeUnit.MILLISECONDS) + 1;
    }

    @p403z7.a
    public static final List<TypeAiMenu> getAiTypeMenus(b bVar) {
        l.f(bVar, "<this>");
        List<TypeAiMenu> menus = bVar.getMenus();
        ArrayList arrayList = new ArrayList();
        for (Object obj : menus) {
            String botId = ((TypeAiMenu) obj).getBotId();
            if (botId != null && !n.D(botId)) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        boolean z6 = false;
        int i2 = 0;
        while (i2 < size) {
            Object obj2 = arrayList.get(i2);
            i2++;
            if (z6) {
                arrayList2.add(obj2);
            } else if (((TypeAiMenu) obj2).getReplyBot(bVar.getContext()) != null) {
                arrayList2.add(obj2);
                z6 = true;
            }
        }
        ArrayList arrayListT = Fg.l.T(menus);
        arrayListT.removeIf(new D(new C0130l0(2, arrayList2), 10));
        bVar.setMenus(arrayListT);
        return bVar.getMenus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean getAiTypeMenus$lambda$2$0(List list, TypeAiMenu menu) {
        l.f(menu, "menu");
        String botId = menu.getBotId();
        if (botId == null || n.D(botId)) {
            return false;
        }
        ArrayList arrayList = new ArrayList(m.c(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(((TypeAiMenu) it.next()).getBotId());
        }
        return !arrayList.contains(menu.getBotId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean getAiTypeMenus$lambda$2$1(Sg.c cVar, Object obj) {
        return ((Boolean) cVar.invoke(obj)).booleanValue();
    }

    public static final b getConfig(Context context) {
        l.f(context, "<this>");
        b.a aVar = b.Companion;
        Context applicationContext = context.getApplicationContext();
        l.e(applicationContext, "getApplicationContext(...)");
        return aVar.newInstance(Gc.c.r(applicationContext));
    }

    public static final ArrayList<KeyboardSettingItem> getKeyboardFeatureMenus(b bVar) {
        List<KeyboardSettingItem> defaults;
        l.f(bVar, "<this>");
        if (!bVar.getKeyboardFeatureMenus().isEmpty() || bVar.isFirstTimeCustomToolbar()) {
            List<KeyboardSettingItem> keyboardFeatureMenus = bVar.getKeyboardFeatureMenus();
            Iterator<T> it = keyboardFeatureMenus.iterator();
            while (it.hasNext()) {
                ((KeyboardSettingItem) it.next()).setConfigValue(getConfig(bVar.getContext()));
            }
            defaults = keyboardFeatureMenus;
        } else {
            defaults = KeyboardSettingItem.INSTANCE.getDefaults(bVar.getContext());
        }
        return E4.b(defaults);
    }

    public static final ArrayList<KeyboardSettingItem> getKeyboardFeatureMenusNotSelected(b bVar, List<KeyboardSettingItem> selectedToolbar) {
        l.f(bVar, "<this>");
        l.f(selectedToolbar, "selectedToolbar");
        ArrayList<KeyboardSettingItem> arrayListB = E4.b((!bVar.getKeyboardFeatureMenus().isEmpty() || bVar.isFirstTimeCustomToolbar()) ? bVar.getKeyboardFeatureMenus() : KeyboardSettingItem.INSTANCE.getDefaults(bVar.getContext()));
        int size = arrayListB.size();
        int i2 = 0;
        while (i2 < size) {
            KeyboardSettingItem keyboardSettingItem = arrayListB.get(i2);
            i2++;
            keyboardSettingItem.setSelected(false);
        }
        arrayListB.removeIf(new D(new j(26, selectedToolbar), 9));
        return arrayListB;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:10:0x0024 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:12:0x0026 A[RETURN] */
    public static final boolean getKeyboardFeatureMenusNotSelected$lambda$1$0(List list, KeyboardSettingItem item) {
        l.f(item, "item");
        for (Object obj : list) {
            if (((KeyboardSettingItem) obj).getType() == item.getType()) {
                if (obj != null) {
                    return true;
                }
                return false;
            }
        }
        obj = null;
        if (obj != null) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean getKeyboardFeatureMenusNotSelected$lambda$1$1(Sg.c cVar, Object obj) {
        return ((Boolean) cVar.invoke(obj)).booleanValue();
    }

    public static final ArrayList<KeyboardSettingItem> getKeyboardToolbarMenus(b bVar) {
        List<KeyboardSettingItem> keyboardToolbarDefault;
        l.f(bVar, "<this>");
        if (!bVar.getKeyboardToolbarMenus().isEmpty() || bVar.isFirstTimeCustomToolbar()) {
            List<KeyboardSettingItem> keyboardToolbarMenus = bVar.getKeyboardToolbarMenus();
            Iterator<T> it = keyboardToolbarMenus.iterator();
            while (it.hasNext()) {
                ((KeyboardSettingItem) it.next()).setConfigValue(getConfig(bVar.getContext()));
            }
            keyboardToolbarDefault = keyboardToolbarMenus;
        } else {
            keyboardToolbarDefault = KeyboardSettingItem.INSTANCE.getKeyboardToolbarDefault(bVar.getContext());
        }
        return E4.b(keyboardToolbarDefault);
    }

    public static final int getRemainingMessage(b bVar) {
        l.f(bVar, "<this>");
        return Math.max(bVar.getKeyboardAdditionalMessages() + bVar.getRemainingMessage() + (4 - bVar.getTotalKeyboardMessage()), 0);
    }

    public static final boolean isAutoCheckUnLimitedReached(b bVar) {
        l.f(bVar, "<this>");
        Context context = bVar.getContext();
        AbstractApplication abstractApplication = context instanceof AbstractApplication ? (AbstractApplication) context : null;
        return (abstractApplication != null ? abstractApplication.d() : false) || !bVar.isGrammarViewShownFirstTime() || bVar.getCountOfAutoGrammarError() <= 7;
    }

    public static final boolean isCanSentMessage(b bVar) {
        l.f(bVar, "<this>");
        Context context = bVar.getContext();
        AbstractApplication abstractApplication = context instanceof AbstractApplication ? (AbstractApplication) context : null;
        return (abstractApplication != null ? abstractApplication.d() : false) || getRemainingMessage(bVar) > 0;
    }

    public static final boolean isUserInAdMode(b bVar) {
        l.f(bVar, "<this>");
        Context context = bVar.getContext();
        AbstractApplication abstractApplication = context instanceof AbstractApplication ? (AbstractApplication) context : null;
        return bVar.isEnableAdMode() && !(abstractApplication != null ? abstractApplication.d() : false);
    }

    public static final void migrateKeyboardToolbar(b bVar) {
        l.f(bVar, "<this>");
        if (bVar.getKeyboardSettingVersion() != 12) {
            bVar.setKeyboardSettingVersion(12);
            if (bVar.getKeyboardFeatureMenus().isEmpty()) {
                return;
            }
            bVar.setKeyboardFeatureMenus(KeyboardSettingItem.INSTANCE.migrate(E4.b(bVar.getKeyboardFeatureMenus()), bVar.getContext()));
        }
    }

    public static final void removeAllClipBoard(b bVar) {
        l.f(bVar, "<this>");
        bVar.setCurrentClipBoards(new ArrayList());
    }

    public static final void removeCurrentClipBoard(b bVar, List<String> clipBoards) {
        l.f(bVar, "<this>");
        l.f(clipBoards, "clipBoards");
        ArrayList arrayListB = E4.b(bVar.getCurrentClipBoards());
        arrayListB.removeAll(Fg.l.W(clipBoards));
        bVar.setCurrentClipBoards(arrayListB);
    }

    public static final boolean shouldShowRating(b bVar) {
        l.f(bVar, "<this>");
        p030bd.e.Companion.getClass();
        if (p030bd.c.a().f20400X == bVar.getResponseTimes()) {
            return false;
        }
        p030bd.c.a().f20400X = bVar.getResponseTimes();
        return !bVar.isUserRated() && m.a(1, 4, 7).contains(Integer.valueOf(bVar.getResponseTimes()));
    }

    public static final void updateToTalKeyboardMessage(b bVar) {
        l.f(bVar, "<this>");
        if (bVar.getTotalKeyboardMessage() < bVar.getKeyboardAdditionalMessages() + bVar.getRemainingMessage() + 4) {
            bVar.setTotalKeyboardMessage(bVar.getTotalKeyboardMessage() + 1);
        }
    }
}
