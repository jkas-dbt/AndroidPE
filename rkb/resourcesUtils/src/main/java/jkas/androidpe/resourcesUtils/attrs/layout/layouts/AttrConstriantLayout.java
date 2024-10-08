package jkas.androidpe.resourcesUtils.attrs.layout.layouts;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_GRAVITY;

/**
 * @author JKas
 */
public class AttrConstriantLayout {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put(
                "layout_constraintVertical_chainStyle",
                new String[] {"spread", "spread_inside", "packed"});
        attrs.put(
                "layout_constraintHorizontal_chainStyle",
                new String[] {"spread", "spread_inside", "packed"});
    }
}
