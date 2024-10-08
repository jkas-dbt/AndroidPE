package jkas.androidpe.resourcesUtils.attrs.layout.layouts;

import java.util.HashMap;
import java.util.Map;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_GRAVITY;

/**
 * @author JKas
 */
public class AttrLinearLayout {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:baselineAligned", VALUES_BOOLEAN);
        attrs.put("android:baselineAlignedChildIndex", new String[] {"@id"});
        attrs.put("android:divider", new String[] {"@drawable"});
        attrs.put("android:gravity", VALUES_GRAVITY);
        attrs.put("android:measureWithLargestChild", VALUES_BOOLEAN);
        attrs.put("android:orientation", new String[] {"vertical", "horizontal"});
        attrs.put("android:weightSum", null);
    }
}
