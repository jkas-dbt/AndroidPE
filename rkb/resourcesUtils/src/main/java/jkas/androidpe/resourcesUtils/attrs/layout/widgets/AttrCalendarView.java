package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import jkas.androidpe.resourcesUtils.attrs.layout.layouts.AttrFrameLayout;

/**
 * @author JKas
 */
public class AttrCalendarView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrFrameLayout.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:dateTextAppearance", new String[] {"@style"});
        attrs.put("android:firstDayOfWeek", null);
        attrs.put("android:maxDate", null);
        attrs.put("android:minDate", null);
    }
}
