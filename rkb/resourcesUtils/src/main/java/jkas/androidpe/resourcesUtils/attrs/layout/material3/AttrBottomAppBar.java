package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrBottomAppBar {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:fabAlignmentMode", new String[] {"center", "end"});
        attrs.put("app:fabAnchorMode", new String[] {"cradle", "embed"});
        attrs.put("app:fabAnimationMode", new String[] {"scale", "slide"});
        attrs.put("app:fabCradleMargin", new String[] {"@dimen"});
        attrs.put("app:fabCradleRoundedCornerRadius", new String[] {"@dimen"});
        attrs.put("app:fabCradleVerticalOffset", new String[] {"@dimen"});
        attrs.put("app:hideOnScroll", null); // not yet
        attrs.put("app:paddingBottomSystemWindowInsets", VALUES_BOOLEAN);
        attrs.put("app:menu", new String[] {"@menu"});
        attrs.put("app:layout_anchor", null); // not yet
        attrs.put("app:menuAlignmentMode", new String[] {"auto", "start"});
        attrs.put("app:navigationIconTint", new String[] {"@color"});
        attrs.put("app:fabAlignmentModeEndMargin", new String[] {"@dimen"}); // not yet
    }
}
