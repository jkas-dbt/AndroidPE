package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_GRAVITY;

/**
 * @author JKas
 */
public class AttrGridView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:columnWidth", new String[] {"@dimen"});
        attrs.put("android:gravity", VALUES_GRAVITY);
        attrs.put("android:gravity", new String[] {"@dimen"});
        attrs.put("android:numColumns", null);
        attrs.put(
                "android:stretchMode",
                new String[] {"colulmWidth", "none", "spacingWidth", "spacingWidthUniform"});
        attrs.put("android:verticalSpacing", new String[] {"@dimen"});
    }
}
