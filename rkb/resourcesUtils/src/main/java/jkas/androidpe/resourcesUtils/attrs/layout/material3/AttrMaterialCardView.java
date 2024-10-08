package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrMaterialCardView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:android_checkable", VALUES_BOOLEAN);
        attrs.put("app:cardForegroundColor", new String[] {"@color"});
        attrs.put("app:cardElevation", new String[] {"@dimen"});
        attrs.put("app:checkedIcon", VALUES_BOOLEAN);
        attrs.put("app:checkedIconMargin", new String[] {"@dimen"});
        attrs.put("app:checkedIconSize", null);
        attrs.put("app:checkedIconTint", new String[] {"@color"});
        attrs.put("app:rippleColor", null);
    }
}
