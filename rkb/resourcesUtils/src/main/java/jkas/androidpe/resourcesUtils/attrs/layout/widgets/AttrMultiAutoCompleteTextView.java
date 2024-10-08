package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrMultiAutoCompleteTextView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrTextView.attrs);
        attrs.putAll(AttrAutoCompleteTextView.attrs);
        init();
    }

    private static void init() {
        // nothing else;
    }
}
