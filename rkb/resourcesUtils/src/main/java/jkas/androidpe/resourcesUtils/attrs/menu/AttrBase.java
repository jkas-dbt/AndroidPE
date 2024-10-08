package jkas.androidpe.resourcesUtils.attrs.menu;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import jkas.androidpe.resourcesUtils.attrs.AllAttrBase;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.*;

public class AttrBase {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        initBase();
    }

    private static void initBase() {
        attrs.clear();
        attrs.put("android:id", VALUES_STRING);
        attrs.put("android:visible", VALUES_STRING);
        attrs.put("android:enabled", VALUES_BOOLEAN);
        attrs.put(
                "android:menuCategory",
                new String[] {"container", "system", "secondary", "alternative"});
        attrs.put("android:orderInCategory", VALUES_INTEGER);
    }
}
