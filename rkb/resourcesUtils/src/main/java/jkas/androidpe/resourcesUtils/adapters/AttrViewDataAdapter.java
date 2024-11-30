package jkas.androidpe.resourcesUtils.adapters;

import java.util.Collections;
import java.util.Map;
import jkas.androidpe.resourcesUtils.bases.AttrValuesRefBase;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.modules.ModuleRes;
import java.util.ArrayList;

/**
 * @author JKas
 */
public class AttrViewDataAdapter {
    public static ArrayList<String> getAllData(String valueType) {
        final ArrayList<String> listAdapter = new ArrayList<>();
        listAdapter.addAll(getModuleList(valueType));
        listAdapter.addAll(getDefaultList(valueType));
        return listAdapter;
    }

    public static ArrayList<String> getModuleList(String valueType) {
        final ArrayList<String> listAdapter = new ArrayList<>();
        ModuleRes mr = DataRefManager.getInstance().currentModuleRes;
        listAdapter.clear();
        if (valueType.equals("@string")) listAdapter.addAll(mr.listStrings);
        else if (valueType.equals("@array")) listAdapter.addAll(mr.listArrays);
        else if (valueType.equals("@bool")) {
            listAdapter.add("true");
            listAdapter.add("false");
            listAdapter.addAll(mr.listBools);
        } else if (valueType.equals("@dimen")) listAdapter.addAll(mr.listDimens);
        else if (valueType.equals("@integer")) listAdapter.addAll(mr.listIntegers);
        else if (valueType.equals("@menu")) listAdapter.addAll(mr.listMenus);
        else if (valueType.equals("@layout")) listAdapter.addAll(mr.listLayouts);
        else if (valueType.startsWith("@raw")) listAdapter.addAll(mr.listRaws);
        else if (valueType.equals("@style")) listAdapter.addAll(mr.listStyles);
        else if (valueType.equals("@color") || valueType.contains("color")) {
            listAdapter.addAll(mr.listColors);
        }

        if (valueType.equals("@drawable") || valueType.contains("drawable")) {
            if (valueType.equals("@drawable")) listAdapter.clear();
            listAdapter.addAll(mr.listDrawables);
        }

        if (valueType.contains("color")
                || valueType.contains("style")
                || valueType.contains("dimen")) {
            listAdapter.addAll(AttrValuesRefBase.listAttrs);
        }

        return listAdapter;
    }

    public static ArrayList<String> getDefaultList(String valueType) {
        if (valueType == null) return new ArrayList<>();
        final ArrayList<String> listAdapter = new ArrayList<>();
        listAdapter.clear();
        if (valueType.equals("@string")) listAdapter.addAll(AttrValuesRefBase.listStrings);
        else if (valueType.equals("@array")) listAdapter.addAll(AttrValuesRefBase.listArrays);
        else if (valueType.equals("@bool")) listAdapter.addAll(AttrValuesRefBase.listBools);
        else if (valueType.equals("@dimen")) listAdapter.addAll(AttrValuesRefBase.listDimens);
        else if (valueType.equals("@integer")) listAdapter.addAll(AttrValuesRefBase.listIntegers);
        else if (valueType.equals("@menu")) listAdapter.addAll(AttrValuesRefBase.listMenus);
        else if (valueType.equals("@layout")) listAdapter.addAll(AttrValuesRefBase.listLayouts);
        else if (valueType.startsWith("@raw")) listAdapter.addAll(AttrValuesRefBase.listRaws);
        else if (valueType.equals("@style")) listAdapter.addAll(AttrValuesRefBase.listStyles);
        else if (valueType.equals("@color") || valueType.contains("color"))
            listAdapter.addAll(AttrValuesRefBase.listColors);

        if (valueType.equals("@drawable") || valueType.contains("drawable")) {
            if (valueType.equals("@drawable")) listAdapter.clear();
            listAdapter.addAll(AttrValuesRefBase.listDrawables);
        }

        if (valueType.contains("color")
                || valueType.contains("style")
                || valueType.contains("dimen")) {
            listAdapter.addAll(AttrValuesRefBase.listAttrs);
        }

        return listAdapter;
    }
}
