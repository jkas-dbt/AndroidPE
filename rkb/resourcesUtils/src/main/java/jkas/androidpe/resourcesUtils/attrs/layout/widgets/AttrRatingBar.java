package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrRatingBar {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrProgressBar.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:isIndicator", VALUES_BOOLEAN);
        attrs.put("android:numStars", null);
        attrs.put("android:rating", null);
        attrs.put("android:stepSize", null);
    }
}
