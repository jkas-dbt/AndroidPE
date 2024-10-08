package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import jkas.androidpe.resourcesUtils.attrs.layout.widgets.AttrTextView;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.*;

/**
 * @author JKas
 */
public class AttrMaterialButton {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrTextView.attrs);
        init();
    }

    private static void init() {
        attrs.put("android:checkable", VALUES_BOOLEAN);
        attrs.put("app:insetBottom", VALUES_DIMEN);
        attrs.put("app:insetTop", VALUES_DIMEN);
        attrs.put("app:cornerRadius", VALUES_DIMEN);
        attrs.put("app:icon", VALUES_DRAWABLE);
        attrs.put("app:iconGravity", VALUES_GRAVITY);
        attrs.put("app:iconPadding", VALUES_DIMEN);
        attrs.put("app:iconSize", VALUES_DIMEN);
        attrs.put("app:iconTint", VALUES_COLOR);
        attrs.put("app:iconTintMode", VALUES_TINT_MODE);
        attrs.put("app:rippleColor", VALUES_COLOR);
        attrs.put("app:strokeColor", VALUES_COLOR);
        attrs.put("app:strokeWidth", VALUES_DIMEN);
    }
}
