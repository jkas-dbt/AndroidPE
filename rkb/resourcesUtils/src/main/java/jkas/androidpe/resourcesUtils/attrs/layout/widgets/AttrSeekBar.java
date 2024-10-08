package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrSeekBar {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrProgressBar.attrs);
        init();
    }

    private static void init() {
        // nothing else.
    }
}
