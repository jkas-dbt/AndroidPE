package jkas.androidpe.resourcesUtils.attrs.layout.material3;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.VALUES_BOOLEAN;

/**
 * @author JKas
 */
public class AttrMaterialButtonToggleGroup {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("app:checkedButton", new String[] {"@id"});
        attrs.put("app:selectionRequired", VALUES_BOOLEAN);
        attrs.put("app:singleSelection", VALUES_BOOLEAN);
    }
}
