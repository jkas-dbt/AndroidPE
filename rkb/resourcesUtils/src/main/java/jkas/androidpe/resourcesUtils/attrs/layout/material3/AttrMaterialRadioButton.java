package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_TINT_MODE;
import jkas.androidpe.resourcesUtils.attrs.layout.widgets.AttrTextView;

/**
 * @author JKas
 */
public class AttrMaterialRadioButton {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrTextView.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:checked", VALUES_BOOLEAN);
        attrs.put("app:buttonIcon", new String[] {"@drawable"});
        attrs.put("app:buttonIconTint", new String[] {"@color"});
        attrs.put("app:buttonIconTintMode", VALUES_TINT_MODE);
    }
}
