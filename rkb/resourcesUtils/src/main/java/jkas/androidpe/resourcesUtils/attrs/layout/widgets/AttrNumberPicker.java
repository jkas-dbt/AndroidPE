package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import jkas.androidpe.resourcesUtils.attrs.layout.layouts.AttrLinearLayout;

/**
 * @author JKas
 */
public class AttrNumberPicker {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrLinearLayout.attrs);
        init();
    }

    private static void init() {
        // nothing else.
    }
}
