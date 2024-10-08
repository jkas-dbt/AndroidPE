package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrTextClock {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrTextView.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:format12Hour", null);
        attrs.put("android:format24Hour", null);
        attrs.put("android:timeZone", null);
    }
}
