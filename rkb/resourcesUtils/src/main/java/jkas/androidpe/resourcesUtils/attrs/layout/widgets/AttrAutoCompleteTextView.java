package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrAutoCompleteTextView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrTextView.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:dropDownHeight", new String[] {"@dimen"});
        attrs.put("android:dropDownWidth", new String[] {"@dimen"});
        attrs.put("android:dropDownSelector", new String[] {"@color"});
        attrs.put("android:dropDownVerticalOffset", new String[] {"@dimen"});
        attrs.put("android:dropDownHorizontalOffset", new String[] {"@dimen"});
        attrs.put("android:popupBackground", new String[] {"@color"});
    }
}
