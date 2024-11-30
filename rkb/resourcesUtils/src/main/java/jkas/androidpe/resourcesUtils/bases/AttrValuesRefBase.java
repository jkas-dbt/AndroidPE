package jkas.androidpe.resourcesUtils.bases;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;

/** All data in this class is reserved for value autocompletion */
/**
 * @author JKas
 */
public class AttrValuesRefBase {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    public static ArrayList<String> listStrings = new ArrayList<>();
    public static ArrayList<String> listAnims = new ArrayList<>();
    public static ArrayList<String> listAttrs = new ArrayList<>();
    public static ArrayList<String> listArrays = new ArrayList<>();
    public static ArrayList<String> listBools = new ArrayList<>();
    public static ArrayList<String> listDimens = new ArrayList<>();
    public static ArrayList<String> listIntegers = new ArrayList<>();
    public static ArrayList<String> listMenus = new ArrayList<>();
    public static ArrayList<String> listLayouts = new ArrayList<>();
    public static ArrayList<String> listRaws = new ArrayList<>();
    public static ArrayList<String> listStyles = new ArrayList<>();
    public static ArrayList<String> listColors = new ArrayList<>();
    public static ArrayList<String> listDrawables = new ArrayList<>();

    // will be called one time only
    public static void initRef() {
        listStrings.clear();
        listAnims.clear();
        listAttrs.clear();
        listArrays.clear();
        listBools.clear();
        listDimens.clear();
        listIntegers.clear();
        listMenus.clear();
        listLayouts.clear();
        listRaws.clear();
        listStyles.clear();
        listColors.clear();
        listDrawables.clear();

        exec.execute(
                () -> {
                    initAndroid();
                    initAndroidX();
                    initMaterial3();
                });
    }

    private static void initAndroid() {
        Class clazz = null;

        clazz = android.R.anim.class;
        for (Field field : clazz.getFields())
            if (!listAnims.contains(field.getName()))
                listAnims.add("@android:anim/" + field.getName());

        clazz = android.R.array.class;
        for (Field field : clazz.getFields())
            if (!listArrays.contains(field.getName()))
                listArrays.add("@android:array/" + field.getName());

        clazz = android.R.attr.class;
        for (Field field : clazz.getFields()) {
            if (!listAttrs.contains("?" + field.getName())) listAttrs.add("?" + field.getName());

            if (!listAttrs.contains("?attr/" + field.getName()))
                listAttrs.add("?attr/" + field.getName());

            if (!listAttrs.contains("?attr/android:" + field.getName()))
                listAttrs.add("?attr/android:" + field.getName());

            if (!listAttrs.contains("?android:" + field.getName()))
                listAttrs.add("?android:" + field.getName());

            if (!listAttrs.contains("?android:attr/" + field.getName()))
                listAttrs.add("?android:attr/" + field.getName());

            if (!listAttrs.contains("@android:attr/" + field.getName()))
                listAttrs.add("@android:attr/" + field.getName());
        }

        clazz = android.R.bool.class;
        listBools.add("true");
        listBools.add("false");
        for (Field field : clazz.getFields())
            if (!listBools.contains(field.getName()))
                listBools.add("@android:bool/" + field.getName());

        clazz = android.R.color.class;
        for (Field field : clazz.getFields())
            if (!listColors.contains(field.getName()))
                listColors.add("@android:color/" + field.getName());

        clazz = android.R.dimen.class;
        for (Field field : clazz.getFields())
            if (!listDimens.contains(field.getName()))
                listDimens.add("@android:dimen/" + field.getName());

        clazz = android.R.drawable.class;
        for (Field field : clazz.getFields())
            if (!listDrawables.contains(field.getName()))
                listDrawables.add("@android:drawable/" + field.getName());

        clazz = android.R.integer.class;
        for (Field field : clazz.getFields())
            if (!listIntegers.contains(field.getName()))
                listIntegers.add("@android:integer/" + field.getName());

        clazz = android.R.layout.class;
        for (Field field : clazz.getFields())
            if (!listLayouts.contains(field.getName()))
                listLayouts.add("@android:layout/" + field.getName());

        clazz = android.R.menu.class;
        for (Field field : clazz.getFields())
            if (!listMenus.contains(field.getName()))
                listMenus.add("@android:menu/" + field.getName());

        clazz = android.R.mipmap.class;
        for (Field field : clazz.getFields())
            if (!listArrays.contains(field.getName()))
                listDrawables.add("@android:mipmap/" + field.getName());

        clazz = android.R.raw.class;
        for (Field field : clazz.getFields())
            if (!listArrays.contains(field.getName()))
                listRaws.add("@android:raw/" + field.getName());

        clazz = android.R.string.class;
        for (Field field : clazz.getFields())
            if (!listArrays.contains(field.getName()))
                listStrings.add("@android:string/" + field.getName());

        clazz = android.R.style.class;
        for (Field field : clazz.getFields())
            if (!listArrays.contains(field.getName()))
                listStyles.add("@android:style/" + field.getName());
    }

    private static void initAndroidX() {
        Class clazz = ResourcesValuesFixer.AndroidX.getAnimClass();
        for (Field field : clazz.getFields())
            if (!listAnims.contains(field.getName())) listAnims.add("@anim/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getAttrClass();
        for (Field field : clazz.getFields()) {
            if (!listArrays.contains("?" + field.getName())) listAttrs.add("?" + field.getName());

            if (!listArrays.contains("?attr/" + field.getName()))
                listAttrs.add("?attr/" + field.getName());

            if (!listArrays.contains("@attr/" + field.getName()))
                listAttrs.add("@attr/" + field.getName());
        }

        clazz = ResourcesValuesFixer.AndroidX.getBoolClass();
        for (Field field : clazz.getFields())
            if (!listBools.contains(field.getName())) listBools.add("@bool/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getColorClass();
        for (Field field : clazz.getFields())
            if (!listColors.contains(field.getName())) listColors.add("@color/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getDimenClass();
        for (Field field : clazz.getFields())
            if (!listDimens.contains(field.getName())) listDimens.add("@dimen/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getDrawableClass();
        for (Field field : clazz.getFields())
            if (!listDrawables.contains(field.getName()))
                listDrawables.add("@drawable/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getIntegerClass();
        for (Field field : clazz.getFields())
            if (!listIntegers.contains(field.getName()))
                listIntegers.add("@integer/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getLayoutClass();
        for (Field field : clazz.getFields())
            if (!listLayouts.contains(field.getName()))
                listLayouts.add("@layout/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getStringClass();
        for (Field field : clazz.getFields())
            if (!listStrings.contains(field.getName()))
                listStrings.add("@string/" + field.getName());

        clazz = ResourcesValuesFixer.AndroidX.getStyleClass();
        for (Field field : clazz.getFields())
            if (!listStyles.contains(field.getName())) listStyles.add("@style/" + field.getName());
    }

    private static void initMaterial3() {
        Class clazz = ResourcesValuesFixer.MaterialR.getAnimClass();
        for (Field field : clazz.getFields())
            if (!listAnims.contains(field.getName())) listAnims.add("@anim/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getAttrClass();
        for (Field field : clazz.getFields()) {
            if (!listAttrs.contains("?" + field.getName())) listAttrs.add("?" + field.getName());

            if (!listAttrs.contains("?attr/" + field.getName()))
                listAttrs.add("?attr/" + field.getName());

            if (!listAttrs.contains("@attr/" + field.getName()))
                listAttrs.add("@attr/" + field.getName());
        }

        clazz = ResourcesValuesFixer.MaterialR.getBoolClass();
        for (Field field : clazz.getFields())
            if (!listBools.contains(field.getName())) listBools.add("@bool/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getColorClass();
        for (Field field : clazz.getFields())
            if (!listColors.contains(field.getName())) listColors.add("@color/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getDimenClass();
        for (Field field : clazz.getFields())
            if (!listDimens.contains(field.getName())) listDimens.add("@dimen/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getDrawableClass();
        for (Field field : clazz.getFields())
            if (!listDrawables.contains(field.getName()))
                listDrawables.add("@drawable/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getIntegerClass();
        for (Field field : clazz.getFields())
            if (!listIntegers.contains(field.getName()))
                listIntegers.add("@integer/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getLayoutClass();
        for (Field field : clazz.getFields())
            if (!listLayouts.contains(field.getName()))
                listLayouts.add("@layout/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getStringClass();
        for (Field field : clazz.getFields())
            if (!listStrings.contains(field.getName()))
                listStrings.add("@string/" + field.getName());

        clazz = ResourcesValuesFixer.MaterialR.getStyleClass();
        for (Field field : clazz.getFields())
            if (!listStyles.contains(field.getName())) listStyles.add("@style/" + field.getName());
    }
}
