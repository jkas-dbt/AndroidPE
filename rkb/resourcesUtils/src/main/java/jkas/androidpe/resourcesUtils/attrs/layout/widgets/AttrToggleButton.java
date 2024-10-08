package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrToggleButton {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrTextView.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:textOn", new String[] {"@string"});
        attrs.put("android:textOff", new String[] {"@string"});
        attrs.put("android:disabledAlpha", null);
        
    }
}
