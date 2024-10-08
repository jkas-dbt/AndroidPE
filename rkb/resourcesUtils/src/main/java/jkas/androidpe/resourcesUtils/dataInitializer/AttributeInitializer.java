package jkas.androidpe.resourcesUtils.dataInitializer;

import android.content.Context;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jkas.androidpe.resourcesUtils.utils.Constants;

public class AttributeInitializer {
    /*private Context context;
    private HashMap<View, AttributeMap> viewAttributeMap = new HashMap<>();
    private HashMap<String, List<HashMap<String, Object>>> attributes;
    private HashMap<String, List<HashMap<String, Object>>> parentAttributes;

    public AttributeInitializer(
            Context context,
            HashMap<String, List<HashMap<String, Object>>> attributes,
            HashMap<String, List<HashMap<String, Object>>> parentAttributes) {
        this.context = context;
        this.attributes = attributes;
        this.parentAttributes = parentAttributes;
    }

    public AttributeInitializer(
            Context context,
            HashMap<View, AttributeMap> viewAttributeMap,
            HashMap<String, List<HashMap<String, Object>>> attributes,
            HashMap<String, List<HashMap<String, Object>>> parentAttributes) {
        this.viewAttributeMap = viewAttributeMap;
        this.context = context;
        this.attributes = attributes;
        this.parentAttributes = parentAttributes;
    }

    public void applyDefaultAttributes(View target, Map<String, String> defaultAttrs) {
        List<HashMap<String, Object>> allAttrs = getAllAttributesForView(target);

        for (String key : defaultAttrs.keySet()) {
            for (HashMap<String, Object> map : allAttrs) {
                if (map.get(Constants.KEY_ATTRIBUTE_NAME).toString().equals(key)) {
                    applyAttribute(target, defaultAttrs.get(key), map);
                    break;
                }
            }
        }
    }

    public void applyAttribute(View target, String value, HashMap<String, Object> attribute) {
        String methodName = attribute.get(Constants.KEY_METHOD_NAME).toString();
        String className = attribute.get(Constants.KEY_CLASS_NAME).toString();
        String attributeName = attribute.get(Constants.KEY_ATTRIBUTE_NAME).toString();

        if (value.startsWith("@+id/") && viewAttributeMap.get(target).containsKey("android:id")) {
            for (View view : viewAttributeMap.keySet()) {
                HashMap<String, String> map = viewAttributeMap.get(view);

                for (String k : map.keySet()) {
                    String mValue = map.get(k);

                    if (mValue.startsWith("@id/")
                            && mValue.equals(
                                    viewAttributeMap
                                            .get(target)
                                            .get("android:id")
                                            .replace("+", ""))) {
                        map.put(k, value.replace("+", ""));
                    }
                }
            }
        }

        if (attributeName != null) {
            viewAttributeMap.get(target).put(attributeName, value);
        }
        if (methodName != null) {
            if (className != null) {
                invokeMethod(methodName, className, target, value, context);
            }
        }
    }

    public List<HashMap<String, Object>> getAvailableAttributesForView(View target) {
        List<String> keys = new ArrayList<>(viewAttributeMap.get(target).keySet());
        List<HashMap<String, Object>> allAttrs = getAllAttributesForView(target);

        for (int i = allAttrs.size() - 1; i >= 0; i--) {
            for (String key : keys) {
                if (key.equals(allAttrs.get(i).get(Constants.KEY_ATTRIBUTE_NAME).toString())) {
                    allAttrs.remove(i);
                    break;
                }
            }
        }

        return allAttrs;
    }

    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> getAllAttributesForView(View target) {
        List<HashMap<String, Object>> allAttrs = new ArrayList<>();

        Class<?> cls = target.getClass();
        Class<?> viewParentCls = View.class.getSuperclass();

        while (cls != viewParentCls) {
            if (attributes.containsKey(cls.getName()))
                allAttrs.addAll(0, attributes.get(cls.getName()));

            cls = cls.getSuperclass();
        }

        if (target.getParent() != null && target.getParent().getClass() != DesignEditor.class) {
            cls = target.getParent().getClass();

            while (cls != viewParentCls) {
                if (parentAttributes.containsKey(cls.getName()))
                    allAttrs.addAll(parentAttributes.get(cls.getName()));

                cls = cls.getSuperclass();
            }
        }

        return allAttrs;
    }

    public HashMap<String, Object> getAttributeFromKey(
            String key, List<HashMap<String, Object>> list) {
        for (HashMap<String, Object> map : list) {
            if (map.get(Constants.KEY_ATTRIBUTE_NAME).equals(key)) return map;
        }
        return null;
    }*/
}
