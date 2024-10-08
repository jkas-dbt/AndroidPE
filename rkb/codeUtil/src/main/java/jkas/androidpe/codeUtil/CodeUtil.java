package jkas.codeUtil;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @auther JKas
 */
public class CodeUtil {

    // utils
    public static boolean isColor(String color) {
        return color.matches("^#([0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$");
    }

    public static String getTimeHMS() {
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = timeFormat.format(currentDate);
        return formattedTime;
    }

    public static float getOnlyNumber(String chaine) {
        String patternChiffres = "\\\\d+";
        Pattern pattern = Pattern.compile(patternChiffres);
        Matcher matcher = pattern.matcher(chaine);
        StringBuilder chiffres = new StringBuilder();
        while (matcher.find()) {
            chiffres.append(matcher.group());
        }
        return Float.parseFloat(chiffres.toString());
    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        String firstLetter = str.substring(0, 1).toUpperCase();
        String restOfString = str.substring(1).toLowerCase();
        return firstLetter + restOfString;
    }

    public static String toUpperCaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        String firstLetter = str.substring(0, 1).toUpperCase();
        String restOfString = str.substring(1);
        return firstLetter + restOfString;
    }

    public static String toLowerCaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        String firstLetter = str.substring(0, 1).toLowerCase();
        String restOfString = str.substring(1);
        return firstLetter + restOfString;
    }

    /*



    */
    // OS
    public static void sleepThread(long tmp) {
        try {
            Thread.sleep(tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startActivity(Context context, Class<?> cl) {
        context.startActivity(new Intent(context, cl));
    }

    public static void setNotificationBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window w = activity.getWindow();
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(color);
        }
    }

    public static void setNavigationBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(color);
        }
    }

    public static void copyTextToClipBoard(Context context, String text) {
        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
                .setPrimaryClip(ClipData.newPlainText("clipboard", text));
    }

    public static String getTextFromClipboard(Context context) {
        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
    }

    public static ClipData getClipboard(Context context) {
        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboardManager.getPrimaryClip();
    }

    public static boolean isScreenLandscape(Context context) {
        int screenOrientation = context.getResources().getConfiguration().orientation;
        return (screenOrientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    public static boolean isTheSystemThemeDark(Context context) {
        boolean verif = false;
        switch (context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                verif = true;
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                verif = false;
        }

        return verif;
    }

    public static void showApplicationSettingsDetails(Context context) {
        context.startActivity(
                new Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.getPackageName(), null)));
    }

    public static void showApplicationSettingsDetails(Context context, String appPkgName) {
        context.startActivity(
                new Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", appPkgName, null)));
    }

    /*



    */
    // storage requests
    public static boolean checkIfPermissionAccessStorageGranted(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                                context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) return false;
        return true;
    }

    public static boolean checkIfPermissionAccessStorageManagerGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return Environment.isExternalStorageManager();
        return true;
    }

    public static void requestPermission(Activity activity, int codeReq) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                codeReq);
    }

    public static void requestPermissionManager(Activity activity, int codeReq) {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(
                    Uri.parse(String.format("package:%s", ((Context) activity).getPackageName())));
            activity.startActivityForResult(intent, codeReq);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            activity.startActivityForResult(intent, codeReq);
        }
    }

    /*



    */
    // layout params
    public static LayoutParams getLayoutParamsMW(int margins) {
        final LayoutParams lp =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(margins, margins, margins, margins);
        return lp;
    }

    public static LayoutParams getLayoutParamsWW(int margins) {
        final LayoutParams lp =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(margins, margins, margins, margins);
        return lp;
    }

    public static LayoutParams getLayoutParamsWM(int margins) {
        final LayoutParams lp =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(margins, margins, margins, margins);
        return lp;
    }

    public static LayoutParams getLayoutParamsMM(int margins) {
        final LayoutParams lp =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(margins, margins, margins, margins);
        return lp;
    }

    /*



    */
    // converter
    public static ArrayList<Pair<?, ?>> convertMapToPair(Map<?, ?> map) {
        final ArrayList<Pair<?, ?>> listPair = new ArrayList<>();
        for (Map.Entry entry : map.entrySet()) {
            listPair.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return listPair;
    }

    public static ArrayList<Pair<?, ?>> convertLinkedHashMapToPair(LinkedHashMap<?, ?> map) {
        final ArrayList<Pair<?, ?>> listPair = new ArrayList<>();
        for (Map.Entry entry : map.entrySet()) {
            listPair.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return listPair;
    }

    public static String toIntString(String str) {
        if (str.contains(".")) return str.substring(0, str.lastIndexOf("."));
        return str;
    }

    /*



    */
    // dimen manage
    public static int getDisplayWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getDimenValue(Context C, String unitString) {
        return Float.parseFloat((parseUnitString2Px(C, unitString)));
    }

    public static String parseUnitString2Px(Context C, String unitString) {
        String[] suffixs = {"sp", "dp", "dip", "pt", "px", "mm", "in"};
        unitString = unitString.trim();
        int lastIndex = -1;
        for (String suffix : suffixs) {
            if (unitString.endsWith(suffix)) {
                lastIndex = unitString.length() - suffix.length();
                try {
                    float v = Float.parseFloat(unitString.substring(0, lastIndex));
                    switch (suffix) {
                        case "sp":
                            return String.valueOf(
                                    TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_SP,
                                            v,
                                            C.getResources().getDisplayMetrics()));
                        case "dp":
                        case "dip":
                            return String.valueOf(
                                    TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            v,
                                            C.getResources().getDisplayMetrics()));
                        case "pt":
                            return String.valueOf(
                                    TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_PT,
                                            v,
                                            C.getResources().getDisplayMetrics()));
                        case "px":
                            return String.valueOf(v);
                        case "mm":
                            return String.valueOf(
                                    TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_MM,
                                            v,
                                            C.getResources().getDisplayMetrics()));
                        case "in":
                            return String.valueOf(
                                    TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_IN,
                                            v,
                                            C.getResources().getDisplayMetrics()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return "-1";
    }

    public static boolean isDimenValue(String unitString) {
        Pattern pattern = Pattern.compile("^[0-9]+(sp|dp|dip|pt|px|mm|in)$");
        return pattern.matcher(unitString).matches();
    }

    public static int getMeasuredWidth(final View view) {
        return measureView(view)[0];
    }

    public static int getMeasuredHeight(final View view) {
        return measureView(view)[1];
    }

    public static int[] measureView(final View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null)
            lp =
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        int heightSpec;
        if (lpHeight > 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(widthSpec, heightSpec);
        return new int[] {view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(final float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /*




    */
    // reflect
    public static Object invoke(Object v, String name, Class<?>[] types, Object... params) {
        try {
            Class<?> clazz = v.getClass();
            Method method = getMethod(clazz, name, types);
            if (method == null) return null;
            method.setAccessible(true);
            return method.invoke(v, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasMethod(Object v, String name, Class... types) {
        for (Class<?> superClass = v.getClass();
                superClass != Object.class;
                superClass = superClass.getSuperclass()) {
            try {
                superClass.getDeclaredMethod(name, types);
                return true;
            } catch (NoSuchMethodException e) {
            }
        }
        return false;
    }

    public static Object getFiledValueFromClass(Class<?> o, String name) {
        Field f = getField(o, name);
        try {
            return f.get(o);
        } catch (Exception e) {

        }

        return null;
    }

    public static Object getFiledValue(Object o, String name) {
        return getFiledValueFromClass(o.getClass(), name);
    }

    public static void setField(Object o, String name, Object value) {
        try {
            Class<?> clazz = o.getClass();
            Field field = getField(clazz, name);
            if (field == null) return;
            field.setAccessible(true);
            field.set(o, value);
        } catch (Exception e) {
        }
    }

    public static Field getField(Class<?> clazz, String name) {
        for (Class<?> superClass = clazz;
                superClass != Object.class;
                superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(name);
            } catch (Exception e) {

            }
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... types) {
        for (Class<?> superClass = clazz;
                superClass != Object.class;
                superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(name, types);
            } catch (Exception e) {

            }
        }
        return null;
    }

    public static int getSystemResourceId(Class clazz, String name) {
        try {
            Field field = clazz.getField(name);
            return field.getInt(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Object getFieldValueFromClass(Class<?> o, String name) {
        Field f = getField(o, name);
        try {
            return f.get(o);
        } catch (Exception e) {

        }

        return null;
    }

    public static Object getFieldValue(Object o, String name) {
        return getFieldValueFromClass(o.getClass(), name);
    }

    public static boolean isClass(String pkg) {
        try {
            Class<?> clazz = Class.forName(pkg);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
