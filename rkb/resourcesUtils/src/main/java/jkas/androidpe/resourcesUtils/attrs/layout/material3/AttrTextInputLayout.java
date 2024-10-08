package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_TINT_MODE;

/**
 * @author JKas
 */
public class AttrTextInputLayout {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:hint", new String[] {"@string"});
        attrs.put("android:maxEms", null);
        attrs.put("android:minEms", null);
        attrs.put("android:maxWidth", null);
        attrs.put("android:minWidth", null);
        attrs.put("app:boxStrokeErrorColor", new String[] {"@color"});
        attrs.put("app:boxStrokeWidth", new String[] {"@dimen"});
        attrs.put("app:counterEnabled", null);
        attrs.put("app:counterMaxLength", null);
        attrs.put("app:counterOverflowTextAppearance", null);
        attrs.put("app:counterOverflowTextColor", new String[] {"@color"});
        attrs.put("app:counterTextColor", new String[] {"@color"});
        attrs.put("app:cursorColor", new String[] {"@color"});
        attrs.put("app:cursorErrorColor", new String[] {"@color"});
        attrs.put("app:endIconCheckable", VALUES_BOOLEAN);
        attrs.put("app:endIconContentDescription", new String[] {"@string"});
        attrs.put("app:endIconDrawable", null);
        attrs.put("app:endIconMode", null);
        attrs.put("app:endIconTintMode", VALUES_TINT_MODE);
        attrs.put("app:errorContentDescription", new String[] {"@string"});
        attrs.put("app:errorEnabled", VALUES_BOOLEAN);
        attrs.put("app:errorIconDrawable", new String[] {"@drawable"});
        attrs.put("app:errorIconTint", new String[] {"@color"});
        attrs.put("app:errorIconTintMode", VALUES_TINT_MODE);
        attrs.put("app:helperTextEnabled", VALUES_BOOLEAN);
        attrs.put("app:hintEnabled", VALUES_BOOLEAN);
        attrs.put("app:hintTextColor", new String[] {"@color"});
        attrs.put("app:passwordToggleContentDescription", null);
        attrs.put("app:passwordToggleDrawable", null);
        attrs.put("app:passwordToggleEnabled", VALUES_BOOLEAN);
        attrs.put("app:passwordToggleTint", new String[] {"@color"});
        attrs.put("app:passwordToggleTintMode", VALUES_TINT_MODE);
        attrs.put("app:placeholderTextColor", new String[] {"@color"});
        attrs.put("app:prefixTextColor", new String[] {"@color"});
        attrs.put("app:startIconCheckable", VALUES_BOOLEAN);
        attrs.put("app:startIconContentDescription", null);
        attrs.put("app:startIconDrawable", null);
        attrs.put("app:startIconTint", new String[] {"@color"});
        attrs.put("app:startIconTintMode", VALUES_TINT_MODE);
        attrs.put("app:suffixTextColor", new String[] {"@color"});
    }
}
