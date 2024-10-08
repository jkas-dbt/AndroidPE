package jkas.androidpe.resourcesUtils.attrs.layout.layouts;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrViewFlipper {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrFrameLayout.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:autoStart", VALUES_BOOLEAN);
        attrs.put("android:flipInterval", null); // not yet
    }
}
