package jkas.androidpe.resourcesUtils.attrs.layout.layouts;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_GRAVITY;

/**
 * @author JKas
 */
public class AttrRelativeLayout {
    public static Map<String, String[]> attrs = new HashMap<>();
    
    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:gravity", VALUES_GRAVITY);
        attrs.put(
                "android:ignoreGravity",
                new String[] {
                    "above",
                    "align_baseline",
                    "align_bottom",
                    "align_end",
                    "align_left",
                    "align_parent_bottom",
                    "align_parent_end",
                    "align_parent_left",
                    "align_parent_right",
                    "align_parent_start",
                    "align_parent_top",
                    "align_right",
                    "align_start",
                    "align_top",
                    "align_below",
                    "center_horizontal",
                    "center_vertical",
                    "center_in_parent",
                    "end_of",
                    "left_of",
                    "right_of",
                    "start_of",
                    "true"
                });
    }
}
