package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrRangeSlider {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:minSeparation", null);
        attrs.put("android:stepSize", null);
        attrs.put("android:values", null);
        attrs.put("android:valueFrom", null);
        attrs.put("android:valueTo", null);
        attrs.put("app:haloRadius", null);
        attrs.put("app:haloColor", new String[] {"@string"});
        attrs.put("app:labelBehavior", null);
        attrs.put("app:thumbColor", new String[] {"@color"});
        attrs.put("app:thumbElevation", null);
        attrs.put("app:thumbHeight", null);
        attrs.put("app:thumbStrokeColor", null);
        attrs.put("app:thumbStrokeWidth", null);
        attrs.put("app:thumbTrackGapSize", null);
        attrs.put("app:thumbWidth", null);
        attrs.put("app:tickColor", new String[] {"color"});
        attrs.put("app:tickColorActive", new String[] {"color"});
        attrs.put("app:tickColorInactive", new String[] {"color"});
        attrs.put("app:tickVisible", VALUES_BOOLEAN);
        attrs.put("app:trackColor", new String[] {"color"});
        attrs.put("app:trackColorActive", new String[] {"color"});
        attrs.put("app:trackColorInactive", new String[] {"color"});
    }
}
