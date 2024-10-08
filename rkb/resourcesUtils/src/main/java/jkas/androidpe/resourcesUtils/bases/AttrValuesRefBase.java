package jkas.androidpe.resourcesUtils.bases;

import java.lang.reflect.Field;
import java.util.ArrayList;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;

/** All data in this class is reserved for value autocompletion */
/**
 * @author JKas
 */
public class AttrValuesRefBase {
    public static ArrayList<String> listRefAndroid = new ArrayList<>();
    public static ArrayList<String> listRefAndroidX = new ArrayList<>();
    public static ArrayList<String> listRefMaterial3 = new ArrayList<>();

    static {
        initAndroid();
    }

    public static void initBase() {
        initAndroidX();
        initMaterial3();
    }

    private static void initAndroid() {
        listRefAndroid.clear();

        Class clazz = null;

        clazz = android.R.anim.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:anim/" + field.getName());

        clazz = android.R.array.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:array/" + field.getName());

        clazz = android.R.attr.class;
        for (Field field : clazz.getFields()) {
            listRefAndroid.add("?" + field.getName());
            listRefAndroid.add("?attr/" + field.getName());
            listRefAndroid.add("?attr/android:" + field.getName());
            listRefAndroid.add("?android:" + field.getName());
            listRefAndroid.add("?android:attr/" + field.getName());
            listRefAndroid.add("@android:attr/" + field.getName());
        }

        clazz = android.R.bool.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:bool/" + field.getName());

        clazz = android.R.color.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:color/" + field.getName());

        clazz = android.R.dimen.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:dimen/" + field.getName());

        clazz = android.R.drawable.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:drawable/" + field.getName());

        clazz = android.R.integer.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:integer/" + field.getName());

        clazz = android.R.layout.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:layout/" + field.getName());

        clazz = android.R.menu.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:menu/" + field.getName());

        clazz = android.R.mipmap.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:mipmap/" + field.getName());

        clazz = android.R.raw.class;
        for (Field field : clazz.getFields()) listRefAndroid.add("@android:raw/" + field.getName());

        clazz = android.R.string.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:string/" + field.getName());

        clazz = android.R.style.class;
        for (Field field : clazz.getFields())
            listRefAndroid.add("@android:style/" + field.getName());
    }

    private static void initAndroidX() {
        listRefAndroidX.clear();

        Class clazz = ResourcesValuesFixer.AndroidX.getAnimClass();
        for (Field field : clazz.getFields()) listRefAndroidX.add("@anim/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getAttrClass();
        for (Field field : clazz.getFields()) {
            listRefAndroidX.add("?" + field.getName());
            listRefAndroidX.add("?attr/" + field.getName());
            listRefAndroidX.add("@attr/" + field.getName());
        }

        clazz = ResourcesValuesFixer.AndroidX.getBoolClass();
        for (Field field : clazz.getFields()) listRefAndroidX.add("@bool/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getColorClass();
        for (Field field : clazz.getFields()) listRefAndroidX.add("@color/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getDimenClass();
        for (Field field : clazz.getFields()) listRefAndroidX.add("@dimen/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getDrawableClass();
        for (Field field : clazz.getFields()) listRefAndroidX.add("@drawable/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getIntegerClass();
        for (Field field : clazz.getFields()) listRefAndroidX.add("@integer/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getLayoutClass();
        for (Field field : clazz.getFields()) listRefAndroidX.add("@layout/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getStringClass();
        for (Field field : clazz.getFields()) listRefAndroidX.add("@string/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getStyleClass();
        for (Field field : clazz.getFields()) listRefAndroidX.add("@style/" + field.getName());
    }

    private static void initMaterial3() {
        listRefMaterial3.clear();

        Class clazz = ResourcesValuesFixer.MaterialR.getAnimClass();
        for (Field field : clazz.getFields()) listRefMaterial3.add("@anim/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getAttrClass();
        for (Field field : clazz.getFields()) {
            listRefMaterial3.add("?" + field.getName());
            listRefMaterial3.add("?attr/" + field.getName());
            listRefMaterial3.add("@attr/" + field.getName());
        }

        clazz = ResourcesValuesFixer.MaterialR.getBoolClass();
        for (Field field : clazz.getFields()) listRefMaterial3.add("@bool/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getColorClass();
        for (Field field : clazz.getFields()) listRefMaterial3.add("@color/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getDimenClass();
        for (Field field : clazz.getFields()) listRefMaterial3.add("@dimen/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getDrawableClass();
        for (Field field : clazz.getFields()) listRefMaterial3.add("@drawable/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getIntegerClass();
        for (Field field : clazz.getFields()) listRefMaterial3.add("@integer/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getLayoutClass();
        for (Field field : clazz.getFields()) listRefMaterial3.add("@layout/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getStringClass();
        for (Field field : clazz.getFields()) listRefMaterial3.add("@string/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getStyleClass();
        for (Field field : clazz.getFields()) listRefMaterial3.add("@style/" + field.getName());
    }
}
