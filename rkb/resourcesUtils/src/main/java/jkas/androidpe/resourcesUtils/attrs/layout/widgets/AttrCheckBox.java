package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrCheckBox {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrTextView.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:checked", VALUES_BOOLEAN);
    }
}
