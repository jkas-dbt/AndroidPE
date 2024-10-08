package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrNavigationRailView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:strokeColor", new String[] {"@color"});
        attrs.put("app:strokeWidth", null);
        attrs.put("app:itemMinHeight", new String[] {"@dimen"});
        attrs.put("app:elevation", new String[] {"@dimen"});
        attrs.put("app:menuGravity", null);
        attrs.put("app:menu", new String[] {"@menu"});
    }
}
