package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import jkas.androidpe.resourcesUtils.attrs.layout.widgets.AttrRadioButton;

/**
 * @author JKas
 */
public class AttrSwitchMaterial {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        attrs.putAll(AttrRadioButton.attrs);
        init();
    }

    private static void init() {
        //nothing for the moment.
    }
}
