package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrNavigationView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:itemBackground", null);
        attrs.put("app:itemHorizontalPadding", new String[] {"@dimen"});
        attrs.put("app:itemIconPadding", new String[] {"@dimen"});
        attrs.put("app:itemIconSize", new String[] {"@dimen"});
        attrs.put("app:itemIconTint", new String[] {"@color"});
        attrs.put("app:itemIconTint", new String[] {"@color"});
        attrs.put("app:itemMaxLines", null);
        attrs.put("app:itemTextAppearance", null);
        attrs.put("app:itemTextAppearanceActiveBoldEnabled", VALUES_BOOLEAN);
        attrs.put("app:itemTextColor", new String[] {"@color"});
        attrs.put("app:itemVerticalPadding", new String[] {"@dimen"});
    }
}
