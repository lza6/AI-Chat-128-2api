package Nc;

import java.util.ArrayList;

/* JADX INFO: renamed from: Nc.u, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C1026u {
    public static final String AI_KEYBOARD_S3_URL = "https://keyboard.startnest.uk/aikeyboard";
    public static final long DEFAULT_CLICK_TIME = 300;
    public static final long DEFAULT_SEARCH_DELAY_TIME = 300;
    public static final String EmojiPath = "data/emoji.txt";
    public static final int FREE_ADS_IMAGE_GENERATOR_ADVANCE_TOOL = 1;
    public static final int FREE_ADS_PREMIUM_ADVANCE_TOOL = 1;
    public static final int LIMIT_FREE_ADS_REQUEST = 3;
    public static final int NORMAL_CHAT_LIMIT_FREE_ADS_REQUEST = 4;
    public static final int NUMBER_ADS_PER_IMAGE = 2;
    public static final String S3_URL = "https://widget.startnest.uk";
    public static final String VNTelexPath = "data/telex.json";
    public static final C1026u INSTANCE = new C1026u();
    private static boolean isEnableABTest = true;
    private static final ArrayList<String> NO_ADS_COUNTRY_CODES = Fg.m.a("sw", "ha", "am", "om", "so", "fa", "ps", "si", "ne", "bn", "lo", "km", "ht", "ti", "ny", "rn", "sn", "tn", "wo", "dz", "tet");
    private static final ArrayList<String> supportOutputLanguages = Fg.m.a("pt", "es", "fr", "de", "en", "en-au", "en-gb", "vi", "ar", "th", "in", "pt-pt", "pt-br", "ms");

    /* JADX INFO: renamed from: Nc.u$a */
    public static final class a {
        public static final a INSTANCE = new a();
        private static final int model5Limit = 50;
        private static final int model5_2Limit = 20;
        private static final int model5_4Limit = 5;
        private static final int model5_6Limit = 20;
        private static final int model4oLimit = 50;
        private static final int modelO3Limit = 20;
        private static final int modelGemini3FlashLimit = 20;
        private static final int modelGrok4_20Limit = 10;
        private static final int modelGrok4_3Limit = 5;
        private static final int modelDeepSeekLimit = 50;
        private static final int modelDeepSeekV3Limit = 100;
        private static final int modelClaudeLimit = 20;

        private a() {
        }

        public final int getModel4oLimit() {
            return model4oLimit;
        }

        public final int getModel5Limit() {
            return model5Limit;
        }

        public final int getModel5_2Limit() {
            return model5_2Limit;
        }

        public final int getModel5_4Limit() {
            return model5_4Limit;
        }

        public final int getModel5_6Limit() {
            return model5_6Limit;
        }

        public final int getModelClaudeLimit() {
            return modelClaudeLimit;
        }

        public final int getModelDeepSeekLimit() {
            return modelDeepSeekLimit;
        }

        public final int getModelDeepSeekV3Limit() {
            return modelDeepSeekV3Limit;
        }

        public final int getModelGemini3FlashLimit() {
            return modelGemini3FlashLimit;
        }

        public final int getModelGrok4_20Limit() {
            return modelGrok4_20Limit;
        }

        public final int getModelGrok4_3Limit() {
            return modelGrok4_3Limit;
        }

        public final int getModelO3Limit() {
            return modelO3Limit;
        }
    }

    private C1026u() {
    }

    public final ArrayList<String> getNO_ADS_COUNTRY_CODES() {
        return NO_ADS_COUNTRY_CODES;
    }

    public final ArrayList<String> getSupportOutputLanguages() {
        return supportOutputLanguages;
    }

    public final boolean isEnableABTest() {
        return isEnableABTest;
    }

    public final void setEnableABTest(boolean z6) {
        isEnableABTest = z6;
    }
}
