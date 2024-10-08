package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrMaterialDiviser {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:dividerColor", new String[] {"@color"});
        attrs.put("app:dividerInsetEnd", null);
        attrs.put("app:dividerInsetStart", null);
        attrs.put("app:dividerThickness", null);
    }
}
