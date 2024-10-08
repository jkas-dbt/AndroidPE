package jkas.androidpe.resourcesUtils.attrs.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jkas.androidpe.resourcesUtils.attrs.AllAttrBase;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.*;

public class AttrGroup {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.putAll(AttrBase.attrs);
        init();
    }

    public static ArrayList<String> getAttrs() {
        return AllAttrBase.getAttrs(attrs);
    }

    public static String[] getValuesOfAttr(String attrName) {
        return AllAttrBase.getValuesOfAttr(attrs, attrName);
    }

    private static void init() {
        attrs.put("android:checkableBehavior", new String[] {"none", "all", "single"});
    }
}
