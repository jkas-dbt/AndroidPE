package jkas.androidpe.resourcesUtils.attrs.layout.layouts;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrGridLayout {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:alignmentMode", new String[] {"alignBounds", "alignMargins"});
        attrs.put("android:columnCount", null);
        attrs.put("android:columnOrderPreserved", VALUES_BOOLEAN);
        attrs.put("android:orientation", new String[] {"vertical", "horizontal"});
        attrs.put("android:rowCount", null);
        attrs.put("android:rowOrderPreserved", VALUES_BOOLEAN);
        attrs.put("android:useDefaultMargins", VALUES_BOOLEAN);
    }
}
