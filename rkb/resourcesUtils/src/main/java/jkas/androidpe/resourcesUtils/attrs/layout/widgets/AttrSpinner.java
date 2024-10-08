package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_GRAVITY;

/**
 * @author JKas
 */
public class AttrSpinner {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:dropDownHeight", new String[] {"@dimen"});
        attrs.put("android:dropDownWidth", new String[] {"@dimen"});
        attrs.put("android:dropDownSelector", new String[] {"@color"});
        attrs.put("android:dropDownVerticalOffset", new String[] {"@dimen"});
        attrs.put("android:dropDownHorizontalOffset", new String[] {"@dimen"});
        attrs.put("android:gravity", VALUES_GRAVITY);
        attrs.put("android:spinnerMode", new String[] {"dialog", "dropdown"});
    }
}
