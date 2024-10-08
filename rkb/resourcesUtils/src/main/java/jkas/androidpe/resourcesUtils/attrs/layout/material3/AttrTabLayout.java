package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_GRAVITY;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrTabLayout {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:tabBackground", new String[] {"@drawable"});
        attrs.put("app:tabContentStart", null); // not yet
        attrs.put("app:tabGravity", VALUES_GRAVITY);
        attrs.put("app:tabIndicatorAnimationMode", null); // not yet
        attrs.put("app:tabIndicatorColor", new String[] {"@color"});
        attrs.put("app:tabIndicatorFullWidth", VALUES_BOOLEAN);
        attrs.put("app:tabIndicatorGravity", VALUES_GRAVITY);
        attrs.put("app:tabIndicatorHeight", VALUES_BOOLEAN);
        attrs.put("app:tabInlineLabel", null); // not yet
        attrs.put("app:tabMaxWidth", null); // not yet
        attrs.put("app:tabMinWidth", null); // not yet
        attrs.put("app:tabMode", new String[] {"fixed", "scrollable"});
        attrs.put("app:tabPadding", new String[] {"@dimen"});
        attrs.put("app:tabPaddingBottom", new String[] {"@dimen"});
        attrs.put("app:tabPaddingEnd", new String[] {"@dimen"});
        attrs.put("app:tabPaddingStart", new String[] {"@dimen"});
        attrs.put("app:tabPaddingTop", new String[] {"@dimen"});
        attrs.put("app:tabRippleColor", new String[] {"@color"});
        attrs.put("app:tabSelectedTextColor", new String[] {"@color"});
        attrs.put("app:tabTextColor", new String[] {"@color"});
    }
}
