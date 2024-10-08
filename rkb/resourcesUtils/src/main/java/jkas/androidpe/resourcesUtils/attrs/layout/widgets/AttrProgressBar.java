package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_TINT_MODE;

/**
 * @author JKas
 */
public class AttrProgressBar {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:indeterminate", VALUES_BOOLEAN);
        attrs.put("android:indeterminateBehavior", new String[] {"cycle", "repeat"});
        attrs.put("android:indeterminateDrawable", new String[] {"@drawable"});
        attrs.put("android:indeterminateDuration", null);
        attrs.put("android:indeterminateOnly", VALUES_BOOLEAN);
        attrs.put("android:indeterminateTint", new String[] {"@color"});
        attrs.put("android:indeterminateTintMode", VALUES_TINT_MODE);
        attrs.put("android:max", null);
        attrs.put("android:min", null);
        attrs.put("android:maxHeight", new String[] {"@dimen"});
        attrs.put("android:maxWidth", new String[] {"@dimen"});
        attrs.put("android:mirrorForRtl", VALUES_BOOLEAN);
        attrs.put("android:progress", null);
        attrs.put("android:progressBackgroundTint", new String[] {"@color"});
        attrs.put("android:progressBackgroundTintMode", VALUES_TINT_MODE);
        attrs.put("android:progressTint", new String[] {"@color"});
        attrs.put("android:progressTintMode", VALUES_TINT_MODE);
        attrs.put("android:econdaryProgress", null);
        attrs.put("android:secondaryProgressTint", new String[] {"@color"});
        attrs.put("android:secondaryProgressTintMode", VALUES_TINT_MODE);
    }
}
