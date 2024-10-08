package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.*;
import jkas.androidpe.resourcesUtils.attrs.layout.widgets.AttrImageView;

/**
 * @author JKas
 */
public class AttrFloatingActionButton {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrImageView.attrs);
        init();
    }

    private static void init() {
        attrs.put("app:elevation", VALUES_DIMEN);
        attrs.put("app:ensureMinTouchTargetSize", null);
        attrs.put("app:fabCustomSize", VALUES_DIMEN);
        attrs.put("app:fabSize", new String[] {"auto", "mini", "normal"});
        attrs.put("app:hideMotionSpec", null);
        attrs.put("app:hoveredFocusedTranslationZ", null);
        attrs.put("app:maxImageSize", null);
        attrs.put("app:pressedTranslationZ", null);
        attrs.put("app:rippleColor", VALUES_COLOR);
        attrs.put("app:showMotionSpec", null);
        attrs.put("app:useCompatPadding", VALUES_BOOLEAN);
        attrs.put("app:srcCompat", new String[] {"@drawable"});
        attrs.put("app:borderWidth", new String[] {"@dimen"});
    }
}
