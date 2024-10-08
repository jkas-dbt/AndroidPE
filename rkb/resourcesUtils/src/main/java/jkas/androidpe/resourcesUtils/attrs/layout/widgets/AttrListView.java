package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrListView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:divider", null);
        attrs.put("android:dividerHeight", new String[] {"@dimen"});
        attrs.put("android:footerDividersEnable", VALUES_BOOLEAN);
        attrs.put("android:headerDividersEnabled", VALUES_BOOLEAN);
    }
}
