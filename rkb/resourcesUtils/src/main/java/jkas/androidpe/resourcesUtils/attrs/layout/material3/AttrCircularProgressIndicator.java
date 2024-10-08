package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrCircularProgressIndicator {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:indicatorInset", new String[] {"@dimen"});
        attrs.put("app:indicatorSize", new String[] {"@dimen"});
    }
}
