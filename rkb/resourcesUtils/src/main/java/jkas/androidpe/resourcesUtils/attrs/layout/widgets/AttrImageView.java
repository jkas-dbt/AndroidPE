package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.*;

/**
 * @author JKas
 */
public class AttrImageView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:adjustViewBounds", VALUES_BOOLEAN);
        attrs.put("android:baseline", VALUES_DIMEN);
        attrs.put("android:baselineAlignBottom", VALUES_BOOLEAN);
        attrs.put("android:cropToPadding", VALUES_BOOLEAN);
        attrs.put("android:maxHeight", VALUES_DIMEN);
        attrs.put("android:maxWidth", VALUES_DIMEN);
        attrs.put("android:src", VALUES_DRAWABLE);
        attrs.put("android:tint", VALUES_COLOR);
        attrs.put("android:tintMode", VALUES_TINT_MODE);
        attrs.put(
                "android:scaleType",
                new String[] {
                    "center",
                    "centerCrop",
                    "centerInside",
                    "fitCenter",
                    "fitEnd",
                    "fitStart",
                    "fitXY",
                    "matrix"
                });
    }
}
