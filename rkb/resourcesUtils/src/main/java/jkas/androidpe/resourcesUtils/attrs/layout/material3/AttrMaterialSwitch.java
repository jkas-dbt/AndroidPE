package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_TINT_MODE;
import jkas.androidpe.resourcesUtils.attrs.layout.widgets.AttrRadioButton;

/**
 * @author JKas
 */
public class AttrMaterialSwitch {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrRadioButton.attrs);
        init();
    }

    private static void init() {
        attrs.put("app:thumbIcon", null);
        attrs.put("app:thumbIconSize", null);
        attrs.put("app:thumbIconTint", new String[]{"@color"});
        attrs.put("app:thumbIconTintMode", VALUES_TINT_MODE);
        attrs.put("app:trackDecoration", null);
        attrs.put("app:trackDecorationTint", new String[]{"color"});
        attrs.put("app:trackDecorationTintMode", VALUES_TINT_MODE);
    }
}
