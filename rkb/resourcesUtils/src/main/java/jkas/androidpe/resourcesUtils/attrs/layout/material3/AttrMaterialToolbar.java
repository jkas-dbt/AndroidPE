package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrMaterialToolbar {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:title", new String[] {"@string"});
        attrs.put("app:subTitle", new String[] {"@string"});
        attrs.put("app:menu", new String[] {"@menu"});
    }
}
