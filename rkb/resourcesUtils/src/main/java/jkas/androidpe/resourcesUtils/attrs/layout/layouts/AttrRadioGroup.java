package jkas.androidpe.resourcesUtils.attrs.layout.layouts;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrRadioGroup {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrLinearLayout.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:checkedButton", new String[] {"@id"});
    }
}
