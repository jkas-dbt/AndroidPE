package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_GRAVITY;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrBottomNavigationView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:menu", new String[] {"@menu"});
        attrs.put("app:layout_anchor", null);
        attrs.put("app:itemHorizontalTranslationEnabled", VALUES_BOOLEAN);
        attrs.put("app:layout_anchorGravity", VALUES_GRAVITY);
        attrs.put(
                "app:layout_insetEdge",
                new String[] {"left", "top", "right", "bottom", "start", "end", "none"});
        attrs.put("app:layout_behavior", new String[] {"@string"});
    }
}
