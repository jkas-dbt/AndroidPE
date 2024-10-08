package jkas.androidpe.resourcesUtils.attrs.layout.layouts;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrNestedScrollView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:fillViewport", VALUES_BOOLEAN);
    }
}
