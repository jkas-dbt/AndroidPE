package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrExtendedFloatingActionButton {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrMaterialButton.attrs);
        init();
    }

    private static void init() {
        attrs.put("app:extendMotionSpec", null);
        attrs.put("app:hideMotionSpec", null);
        attrs.put("app:showMotionSpec", null);
        attrs.put("app:shrinkMotionSpec", null);
    }
}
