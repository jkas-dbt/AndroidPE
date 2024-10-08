package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import jkas.androidpe.resourcesUtils.attrs.layout.widgets.AttrImageView;
/**
 * @author JKas
 */
public class AttrShapeableImageView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrImageView.attrs);
        init();
    }

    private static void init() {
        attrs.put("app:strokeColor", new String[]{"@color"});
        attrs.put("app:strokeWidth", new String[]{"@dimen"});
    }
}