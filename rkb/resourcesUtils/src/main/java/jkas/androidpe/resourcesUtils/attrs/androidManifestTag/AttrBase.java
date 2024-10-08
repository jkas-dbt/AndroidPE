package jkas.androidpe.resourcesUtils.attrs.androidManifestTag;

import java.util.Map;
import java.util.HashMap;

/**
 * @author JKas
 */
public class AttrBase {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        init();
    }


    private static void init() {
        attrs.clear();
        attrs.put("android:name", new String[] {"@string"});
        attrs.put("android:permission", new String[] {"@string"});
        attrs.put("android:exported", new String[] {"true", "false"});
    }
}
