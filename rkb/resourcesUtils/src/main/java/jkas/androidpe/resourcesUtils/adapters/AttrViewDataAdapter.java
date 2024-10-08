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
    public static ArrayList<String> getListAssist(String valueType) {
        if (valueType == null) return new ArrayList<>();
        return loadData(valueType);
    }

    private static ArrayList<String> loadData(String valueType) {
        ArrayList<String> listAdapter = new ArrayList<>();
        ModuleRes mr = DataRefManager.getInstance().currentModuleRes;
        listAdapter.clear();
        if (valueType.equals("@string")) {
            for (Map.Entry entry : mr.valuesStrings.entrySet())
                listAdapter.add("@string/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:string/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@string/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@string/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
        } else if (valueType.equals("@array")) {
            for (Map.Entry entry : mr.valuesArrays.entrySet())
                listAdapter.add("@array/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:array/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@array/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@array/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
        } else if (valueType.equals("@bool")) {
            listAdapter.add("true");
            listAdapter.add("false");
            for (Map.Entry entry : mr.valuesBools.entrySet())
                listAdapter.add("@bool/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:bool/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@bool/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@bool/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
        } else if (valueType.equals("@dimen")) {
            for (Map.Entry entry : mr.valuesDimens.entrySet())
                listAdapter.add("@dimen/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:dimen/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@dimen/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@dimen/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
        } else if (valueType.equals("@integer")) {
            for (Map.Entry entry : mr.valuesIntegers.entrySet())
                listAdapter.add("@integer/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:integer/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@integer/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@integer/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
        } else if (valueType.equals("@menu")) {
            for (Map.Entry entry : mr.menus.entrySet())
                listAdapter.add("@menu/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:menu/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@menu/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@menu/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
        } else if (valueType.equals("@layout")) {
            for (Map.Entry entry : mr.layouts.entrySet())
                listAdapter.add("@layout/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:layout/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@layout/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@layout/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
        } else if (valueType.startsWith("@raw")) {
            for (Map.Entry entry : mr.raws.entrySet())
                listAdapter.add("@raw/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:raw/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@raw/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@raw/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
        } else if (valueType.equals("@style")) {
            for (Map.Entry entry : mr.valuesStyles.entrySet())
                listAdapter.add("@style/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:style/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@style/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@style/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
        } else if (valueType.equals("@color") || valueType.contains("color")) {
            for (Map.Entry entry : mr.valuesColors.entrySet())
                listAdapter.add("@color/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:color/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@color/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@color/")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
        }

        if (valueType.equals("@drawable") || valueType.contains("drawable")) {
            if (valueType.equals("@drawable")) listAdapter.clear();
            for (Map.Entry entry : mr.drawables.entrySet())
                listAdapter.add("@drawable/" + entry.getKey().toString());
            for (Map.Entry entry : mr.mipmaps.entrySet())
                listAdapter.add("@mipmap/" + entry.getKey().toString());
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.startsWith("@android:drawable/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.startsWith("@drawable/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.startsWith("@drawable/"))
                    if (!listAdapter.contains(ref)) listAdapter.add(ref);
        }

        if (valueType.contains("color")
                || valueType.contains("style")
                || valueType.contains("dimen")) {
            for (String ref : AttrValuesRefBase.listRefAndroid)
                if (ref.matches("\\?.*")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefAndroidX)
                if (ref.matches("\\?.*")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
            for (String ref : AttrValuesRefBase.listRefMaterial3)
                if (ref.matches("\\?.*")) if (!listAdapter.contains(ref)) listAdapter.add(ref);
        }

        Collections.sort(listAdapter);
        return listAdapter;
    }
}
