package Nc;

import Eh.ljwx.KokYQQy;
import N9.DGy.YlYqIZPyuBqo;
import androidx.media3.common.tWfl.oIRsptWMCIkqSR;
import com.onegravity.rteditor.converter.tagsoup.HTMLModels;
import com.starnest.keyboard.R$drawable;
import com.starnest.keyboard.R$string;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p214n1.rfG.elWebisTriBrO;
import p340v6.AbstractC3917j5;
import p340v6.E4;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'GPT_5_NANO' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:485)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByField(EnumVisitor.java:399)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByWrappedInsn(EnumVisitor.java:364)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:349)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:284)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInvoke(EnumVisitor.java:315)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:288)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:160)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:102)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX INFO: renamed from: Nc.l, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes2.dex */
public final class EnumC1015l {
    private static final /* synthetic */ Lg.a $ENTRIES;
    private static final /* synthetic */ EnumC1015l[] $VALUES;
    public static final EnumC1015l CLAUDE_HAIKU_4_5;
    public static final a Companion;
    public static final EnumC1015l DEEP_SEEK_R1;
    public static final EnumC1015l DEEP_SEEK_V3;
    public static final EnumC1015l GEMINI;
    public static final EnumC1015l GEMINI_3_1_FLASH;
    public static final EnumC1015l GEMINI_3_FLASH;
    public static final EnumC1015l GPT_3_5_Turbo = new EnumC1015l("GPT_3_5_Turbo", 0, "gpt_3_5", "gpt-4o-mini-2024-07-18", HTMLModels.M_INLINE, R$string.chat_model_gpt_4o_mini_description, R$drawable.ic_home_chat_ai, false, 0, 64, null);
    public static final EnumC1015l GPT_4;
    public static final EnumC1015l GPT_4_1;
    public static final EnumC1015l GPT_5;
    public static final EnumC1015l GPT_5_2;
    public static final EnumC1015l GPT_5_4;
    public static final EnumC1015l GPT_5_6;
    public static final EnumC1015l GPT_5_MINI;
    public static final EnumC1015l GPT_5_NANO;
    public static final EnumC1015l GPT_O3;
    public static final EnumC1015l GROK_3_MINI;
    public static final EnumC1015l GROK_4_3;
    public static final String MODEL_4_MINI_PATH = "_4o_mini";
    private final int descriptionId;
    private final int iconId;
    private final boolean isPro;
    private final int limited;
    private final int maxTokens;
    private final String model;
    private final String value;

    /* JADX INFO: renamed from: Nc.l$a */
    /* JADX INFO: loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(kotlin.jvm.internal.f fVar) {
            this();
        }

        public final ArrayList<EnumC1015l> getListModels() {
            return Fg.m.a(EnumC1015l.GPT_5_MINI, EnumC1015l.GPT_5_NANO, EnumC1015l.GPT_3_5_Turbo, EnumC1015l.GEMINI_3_1_FLASH, EnumC1015l.GROK_3_MINI, EnumC1015l.DEEP_SEEK_V3, EnumC1015l.GPT_5_6, EnumC1015l.GPT_5_2, EnumC1015l.GPT_5, EnumC1015l.GPT_4, EnumC1015l.GEMINI_3_FLASH, EnumC1015l.GPT_O3, EnumC1015l.GROK_4_3, EnumC1015l.DEEP_SEEK_R1, EnumC1015l.GPT_4_1, EnumC1015l.GPT_5_4, EnumC1015l.CLAUDE_HAIKU_4_5);
        }
    }

    private static final /* synthetic */ EnumC1015l[] $values() {
        return new EnumC1015l[]{GPT_3_5_Turbo, GPT_5_NANO, GPT_5_MINI, GPT_4_1, GPT_5, GPT_5_2, GPT_5_4, GPT_5_6, GPT_4, GPT_O3, GEMINI, GEMINI_3_1_FLASH, GEMINI_3_FLASH, GROK_3_MINI, GROK_4_3, DEEP_SEEK_R1, DEEP_SEEK_V3, CLAUDE_HAIKU_4_5};
    }

    /* JADX WARN: Multi-variable type inference failed */
    static {
        Object[] objArr = 0 == true ? 1 : 0;
        GPT_5_NANO = new EnumC1015l("GPT_5_NANO", 1, "gpt_5_nano", "gpt_5_nano", HTMLModels.M_INLINE, R$string.ultra_light_ai_for_quick_replies_summaries_and_basic_text_tasks_at_low_cost, R$drawable.ic_home_chat_ai, objArr, 0, 64, null);
        GPT_5_MINI = new EnumC1015l("GPT_5_MINI", 2, "gpt_5_mini", "gpt_5_mini", HTMLModels.M_INLINE, R$string.fast_and_lightweight_ai_for_writing_translation_and_q_a_in_daily_assistant_tasks, R$drawable.ic_home_chat_ai, 0 == true ? 1 : 0, 0, 64, null);
        int i2 = R$string.excels_in_programming_and_performance_optimization_for_complex_tasks;
        int i10 = R$drawable.ic_home_chat_ai;
        C1026u.a aVar = C1026u.a.INSTANCE;
        GPT_4_1 = new EnumC1015l("GPT_4_1", 3, "gpt_4_1", "gpt-4.1", HTMLModels.M_INLINE, i2, i10, 0 == true ? 1 : 0, aVar.getModel4oLimit(), 32, null);
        GPT_5 = new EnumC1015l("GPT_5", 4, "gpt_5", "gpt_5", HTMLModels.M_INLINE, R$string.advanced_ai_for_nlp_and_coding_with_strong_skills_in_data_analysis_research_and_high_quality_content_creation, R$drawable.ic_home_chat_ai, false, aVar.getModel5Limit(), 32, null);
        GPT_5_2 = new EnumC1015l("GPT_5_2", 5, "gpt_5_2", "gpt_5_2", HTMLModels.M_INLINE, R$string.the_most_advanced_agentic_coding_model_for_professional_software_engineering_and_defensive_cybersecurity, R$drawable.ic_home_chat_ai, false, aVar.getModel5_2Limit(), 32, null);
        GPT_5_4 = new EnumC1015l("GPT_5_4", 6, "gpt_5_4", "gpt_5_4", HTMLModels.M_INLINE, R$string.advanced_ai_model_specialized_in_reasoning_coding_content_creation_and_complex_problem_solving_with_higher_accuracy, R$drawable.ic_home_chat_ai, false, aVar.getModel5_4Limit(), 32, null);
        GPT_5_6 = new EnumC1015l("GPT_5_6", 7, "gpt_5_6_luna", "gpt_5_6_luna", HTMLModels.M_INLINE, R$string.chat_model_gpt_5_6_luna_description, R$drawable.ic_home_chat_ai, false, aVar.getModel5_6Limit(), 32, null);
        GPT_4 = new EnumC1015l("GPT_4", 8, "gpt_4", oIRsptWMCIkqSR.wSfmUYwHmiMidc, HTMLModels.M_INLINE, R$string.chat_model_gpt_4o_description, R$drawable.ic_home_chat_ai, false, aVar.getModel4oLimit(), 32, null);
        GPT_O3 = new EnumC1015l(KokYQQy.jXjecWRIy, 9, "o3", "o3", HTMLModels.M_INLINE, R$string.chat_model_gpt_o3_description, R$drawable.ic_home_chat_ai, false, aVar.getModelO3Limit(), 32, null);
        GEMINI = new EnumC1015l("GEMINI", 10, "gemini_lite", "gemini_lite", HTMLModels.M_INLINE, R$string.chat_model_gemini_description, R$drawable.ic_model_gemini_circle, false, 0, 64, null);
        GEMINI_3_1_FLASH = new EnumC1015l("GEMINI_3_1_FLASH", 11, "gemini_3_1_flash", "gemini_3_1_flash", HTMLModels.M_INLINE, R$string.lightweight_and_cost_efficient_model_optimized_for_high_volume_tasks_like_classification_translation_and_fast_responses, R$drawable.ic_model_gemini_circle, 0 == true ? 1 : 0, 0, 64, null);
        GEMINI_3_FLASH = new EnumC1015l("GEMINI_3_FLASH", 12, "gemini_3_flash", "gemini_3_flash", HTMLModels.M_INLINE, R$string.balanced_multimodal_model_with_strong_reasoning_speed_and_support_for_diverse_tasks_across_text_and_media, R$drawable.ic_model_gemini_circle, 0 == true ? 1 : 0, aVar.getModelGemini3FlashLimit(), 32, null);
        GROK_3_MINI = new EnumC1015l("GROK_3_MINI", 13, "grok_3_mini", "grok_3_mini", HTMLModels.M_INLINE, R$string.fast_lightweight_ai_for_logic_light_coding_and_clear_reasoning_steps, R$drawable.ic_chat_model_grok, false, 0, 64, null);
        GROK_4_3 = new EnumC1015l("GROK_4_3", 14, "grok_4_3", "grok_4_3", HTMLModels.M_INLINE, R$string.advanced_reasoning_model_for_complex_analysis_long_context_understanding_and_high_level_problem_solving, R$drawable.ic_chat_model_grok, false, aVar.getModelGrok4_3Limit(), 32, null);
        DEEP_SEEK_R1 = new EnumC1015l("DEEP_SEEK_R1", 15, "deepseek_reasoner", "deepseek_reasoner", HTMLModels.M_INLINE, R$string.chat_model_deep_seek_r1_new_description, R$drawable.ic_model_deep_seek, false, aVar.getModelDeepSeekLimit(), 32, null);
        DEEP_SEEK_V3 = new EnumC1015l("DEEP_SEEK_V3", 16, "deepseek_chat", "deepseek_chat", HTMLModels.M_INLINE, R$string.chat_model_deep_seek_v3_description, R$drawable.ic_model_deep_seek, false, aVar.getModelDeepSeekV3Limit());
        CLAUDE_HAIKU_4_5 = new EnumC1015l("CLAUDE_HAIKU_4_5", 17, "claude_haiku", "claude_haiku", HTMLModels.M_INLINE, R$string.specialized_in_lightning_fast_real_time_tasks_including_data_categorization_instant_support_and_brief_text_summaries, R$drawable.ic_home_chat_ai, false, aVar.getModelClaudeLimit(), 32, null);
        EnumC1015l[] enumC1015lArr$values = $values();
        $VALUES = enumC1015lArr$values;
        $ENTRIES = AbstractC3917j5.a(enumC1015lArr$values);
        Companion = new a(null);
    }

    private EnumC1015l(String str, int i2, String str2, String str3, int i10, int i11, int i12, boolean z6, int i13) {
        super(str, i2);
        this.value = str2;
        this.model = str3;
        this.maxTokens = i10;
        this.descriptionId = i11;
        this.iconId = i12;
        this.isPro = z6;
        this.limited = i13;
    }

    public /* synthetic */ EnumC1015l(String str, int i2, String str2, String str3, int i10, int i11, int i12, boolean z6, int i13, int i14, kotlin.jvm.internal.f fVar) {
        this(str, i2, str2, str3, i10, i11, i12, (i14 & 32) != 0 ? true : z6, (i14 & 64) != 0 ? Integer.MAX_VALUE : i13);
    }

    public static Lg.a getEntries() {
        return $ENTRIES;
    }

    /* JADX WARN: Code duplicated, block: B:11:0x0023  */
    /* JADX WARN: Code duplicated, block: B:15:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    private final List<EnumC1015l> reorderOrders(List<? extends EnumC1015l> list) {
        Iterator it = list.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            if (kotlin.jvm.internal.l.a(((EnumC1015l) it.next()).model, this.model)) {
                if (i2 >= 0) {
                    return Fg.l.J(list.subList(i2, list.size()), list.subList(0, i2));
                }
                return list;
            }
            i2++;
        }
        i2 = -1;
        if (i2 >= 0) {
            return Fg.l.J(list.subList(i2, list.size()), list.subList(0, i2));
        }
        return list;
    }

    public static EnumC1015l valueOf(String str) {
        return (EnumC1015l) Enum.valueOf(EnumC1015l.class, str);
    }

    public static EnumC1015l[] values() {
        return (EnumC1015l[]) $VALUES.clone();
    }

    public final int getAvatarDrawable() {
        return R$drawable.ic_home_chat_ai;
    }

    public final String getChatModel() {
        switch (AbstractC1017m.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
                return "gpt_4o_mini";
            case 2:
                return "gpt_4o";
            case 3:
                return "gpt_4_1";
            case 4:
                return "o3";
            case 5:
                return "gpt_5_nano";
            case 6:
                return "gpt_5_mini";
            case 7:
                return "gpt_5";
            case 8:
                return "gpt_5_2";
            case 9:
                return "gpt_5_4";
            case 10:
                return "gpt_5_6_luna";
            case 11:
                return "gemini_lite";
            case 12:
                return "gemini_3_flash_lite_preview";
            case 13:
                return "gemini_3_flash";
            case 14:
                return "grok_3_mini";
            case 15:
                return "grok_4_3";
            case 16:
                return "deepseek_reasoner";
            case 17:
                return "deepseek_chat";
            case 18:
                return "claude_haiku";
            default:
                throw new Eg.e();
        }
    }

    public final EnumC1015l getChatModelLimited() {
        return this.isPro ? GPT_3_5_Turbo : this;
    }

    public final int getDescriptionId() {
        return this.descriptionId;
    }

    public final String getDisplayName() {
        switch (AbstractC1017m.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
                return "GPT-4o mini";
            case 2:
                return "GPT-4o";
            case 3:
                return "GPT-4.1";
            case 4:
                return "GPT-o3";
            case 5:
                return "GPT-5 nano";
            case 6:
                return "GPT-5 mini";
            case 7:
                return elWebisTriBrO.jcmBlfpgT;
            case 8:
                return "GPT-5.2";
            case 9:
                return "GPT-5.4";
            case 10:
                return "GPT 5.6 Luna";
            case 11:
                return "Gemini 2.0 Flash-Lite";
            case 12:
                return "Gemini 3.1 Flash Lite";
            case 13:
                return "Gemini 3 Flash";
            case 14:
                return "Grok-3 mini";
            case 15:
                return "Grok 4.3";
            case 16:
                return "DeepSeek-R1";
            case 17:
                return "DeepSeek-V3";
            case 18:
                return "Claude Haiku 4.5";
            default:
                throw new Eg.e();
        }
    }

    public final String getEventName() {
        switch (AbstractC1017m.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
                return "4O_MINI";
            case 2:
                return YlYqIZPyuBqo.UEw;
            case 3:
                return "4_1";
            case 4:
                return "GPT_o3";
            case 5:
                return "GPT5_NANO";
            case 6:
                return "GPT5_MINI";
            case 7:
                return "GPT5";
            case 8:
                return "GPT5_2";
            case 9:
                return "GPT5_4";
            case 10:
                return "GPT5_6";
            case 11:
                return "GEMI_LITE";
            case 12:
                return "GEMI_3_1_FL";
            case 13:
                return "GEMI_3_FL";
            case 14:
                return "GROK_3_MINI";
            case 15:
                return "GROK_4_3";
            case 16:
                return "R1";
            case 17:
                return "V3";
            case 18:
                return "CLAU_HAIKU_4_5";
            default:
                throw new Eg.e();
        }
    }

    public final String getHomeChatEvent() {
        return p335v.r.d("HOME_CHAT_MODEL_", getEventName());
    }

    public final String getHomeEvent() {
        return p335v.r.d("HOME_MODEL_", getEventName());
    }

    public final int getIconId() {
        return this.iconId;
    }

    public final int getLimited() {
        return this.limited;
    }

    public final List<EnumC1015l> getListChatModelLimited() {
        Eg.l lVar;
        ArrayList arrayListA = Fg.m.a(GPT_5, GPT_5_2, GPT_5_4, GPT_5_6, GPT_O3, GPT_4_1, GPT_4);
        ArrayList arrayListA2 = Fg.m.a(GEMINI_3_FLASH);
        ArrayList arrayListA3 = Fg.m.a(GROK_4_3);
        ArrayList arrayListA4 = Fg.m.a(DEEP_SEEK_R1);
        if (arrayListA.contains(this)) {
            lVar = new Eg.l(GPT_5_MINI, arrayListA);
        } else if (arrayListA2.contains(this)) {
            lVar = new Eg.l(GEMINI_3_1_FLASH, arrayListA2);
        } else if (arrayListA3.contains(this)) {
            lVar = new Eg.l(GROK_3_MINI, arrayListA3);
        } else {
            lVar = arrayListA4.contains(this) ? new Eg.l(DEEP_SEEK_V3, arrayListA4) : new Eg.l(this, new ArrayList());
        }
        EnumC1015l enumC1015l = (EnumC1015l) lVar.f3255n;
        ArrayList arrayListB = E4.b(reorderOrders((ArrayList) lVar.q));
        arrayListB.add(enumC1015l);
        return arrayListB;
    }

    public final int getMaxTokens() {
        return this.maxTokens;
    }

    public final String getModel() {
        return this.model;
    }

    public final String getModelGroupName() {
        String strS = p016ah.n.S(name(), "_");
        switch (strS.hashCode()) {
            case 70795:
                return !strS.equals("GPT") ? "Other Models" : "GPT Models";
            case 2094316:
                return !strS.equals("DEEP") ? "Other Models" : "DeepSeek Models";
            case 2196487:
                return !strS.equals("GROK") ? "Other Models" : "Grok Models";
            case 1990360990:
                return !strS.equals("CLAUDE") ? "Other Models" : "Claude Models";
            case 2098759221:
                return !strS.equals("GEMINI") ? "Other Models" : "Gemini Models";
            default:
                return "Other Models";
        }
    }

    public final String getValue() {
        return this.value;
    }

    public final boolean isDeepSeek() {
        return Fg.m.a(DEEP_SEEK_R1, DEEP_SEEK_V3).contains(this);
    }

    public final boolean isPro() {
        return this.isPro;
    }

    public final boolean isProModel() {
        return this == GPT_4;
    }
}
