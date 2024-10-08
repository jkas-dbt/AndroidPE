package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import jkas.androidpe.resourcesUtils.attrs.layout.widgets.AttrAutoCompleteTextView;

/**
 * @author JKas
 */
public class AttrMaterialAutoCompleteTextView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrAutoCompleteTextView.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:dropDownBackgroundTint", new String[] {"@color"});
        attrs.put("app:simpleItemSelectedColor", new String[] {"@color"});
        attrs.put("app:simpleItemSelectedRippleColor", new String[] {"@color"});
        attrs.put("android:popupElevation", new String[] {"@dimen"});
        attrs.put("android:popupBackground", new String[] {"@color"});
    }
}
