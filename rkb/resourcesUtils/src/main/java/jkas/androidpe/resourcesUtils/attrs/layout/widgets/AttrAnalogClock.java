package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_TINT_MODE;

/**
 * @author JKas
 */
public class AttrAnalogClock {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:dial", null);
        attrs.put("android:dialTint", new String[] {"@color"});
        attrs.put("android:dialTintMode", VALUES_TINT_MODE);
        attrs.put("android:hand_hour", null);
        attrs.put("android:hand_hourTint", new String[] {"@color"});
        attrs.put("android:hand_hourTintMode", VALUES_TINT_MODE);
        attrs.put("android:hand_minute", null);
        attrs.put("android:hand_minuteTint", new String[] {"@color"});
        attrs.put("android:hand_minuteTintMode", VALUES_TINT_MODE);
        attrs.put("android:firstDayOfWeek", null);
        attrs.put("android:hand_second", null);
        attrs.put("android:hand_secondTint", new String[] {"@style"});
        attrs.put("android:hand_secondTintMode", VALUES_TINT_MODE);
        attrs.put("android:timeZone", new String[] {"@string"});
    }
}
