package jkas.androidpe.resourcesUtils.attrs.androidManifestTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jkas.androidpe.resourcesUtils.attrs.AllAttrBase;

/**
 * @author JKas
 */
public class AttrActivities {
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
        attrs.put("android:allowEmbedded", new String[] {"true", "false"});
        attrs.put("android:allowTaskReparenting", new String[] {"true", "false"});
        attrs.put("android:alwaysRetainTaskState", new String[] {"true", "false"});
        attrs.put("android:autoRemoveFromRecents", new String[] {"true", "false"});
        attrs.put("android:banner", new String[] {"@drawable"});
        attrs.put("android:clearTaskOnLaunch", new String[] {"true", "false"});
        attrs.put("android:colorMode", new String[] {"hdr", "wideColorGamut"});
        attrs.put(
                "android:configChanges",
                new String[] {
                    "mcc",
                    "mnc",
                    "locale",
                    "touchscreen",
                    "keyboard",
                    "keyboardHidden",
                    "navigation",
                    "screenLayout",
                    "fontScale",
                    "uiMode",
                    "orientation",
                    "density",
                    "screenSize",
                    "smallestScreenSize"
                });
        attrs.put("android:directBootAware", new String[] {"true", "false"});
        attrs.put(
                "android:documentLaunchMode",
                new String[] {"intoExisting", "always", "none", "never"});
        attrs.put("android:enabled", new String[] {"true", "false"});
        attrs.put("android:enabledOnBackInvokedCallback", new String[] {"true", "false"});
        attrs.put("android:excludeFromRecents", new String[] {"hdr", "wideColorGamut"});
        attrs.put("android:finishOnTaskLaunch", new String[] {"true", "false"});
        attrs.put("android:hardwareAccelerated", new String[] {"true", "false"});
        attrs.put("android:icon", new String[] {"@drawable"});

        attrs.put("android:immersive", new String[] {"true", "false"});
        attrs.put("android:label", new String[] {"@string"});
        attrs.put(
                "android:launchMode", new String[] {"normal", "never", "if_whitelisted", "always"});
        attrs.put(
                "android:lockTaskMode",
                new String[] {"normal", "never", "if_whitelisted", "always"});

        attrs.put("android:maxRecents", new String[] {"@integer"});
        attrs.put("android:maxAspectRatio", new String[] {"@float"});
        attrs.put("android:multiprocess", new String[] {"true", "false"});
        attrs.put("android:noHistory", new String[] {"true", "false"});
        attrs.put("android:parentActivityName", new String[] {"@string"});
        attrs.put(
                "android:persistableMode",
                new String[] {"persistRootOnly", "persistAcrossReboots", "persistNever"});
        attrs.put("android:process", new String[] {"@string"});
        attrs.put("android:relinquishTaskIdentity", new String[] {"true", "false"});
        attrs.put("android:resizeableActivity", new String[] {"true", "false"});
        attrs.put(
                "android:screenOrientation",
                new String[] {
                    "unspecified",
                    "behind",
                    "landscape",
                    "portrait",
                    "reverseLandscape",
                    "reversePortrait",
                    "sensorLandscape",
                    "sensorPortrait",
                    "userLandscape",
                    "userPortrait",
                    "sensor",
                    "fullSensor",
                    "nosensor",
                    "user",
                    "fullUser",
                    "locked"
                });
        attrs.put("android:showForAllUsers", new String[] {"true", "false"});
        attrs.put("android:stateNotNeeded", new String[] {"true", "false"});
        attrs.put("android:supportsPictureInPicture", new String[] {"true", "false"});
        attrs.put("android:taskAffinity", new String[] {"@string"});
        attrs.put("android:theme", new String[] {"@style"});
        attrs.put("android:uiOptions", new String[] {"none", "splitActionBarWhenNarrow"});
        attrs.put(
                "android:windowSoftInputMode",
                new String[] {
                    "stateUnspecified",
                    "stateUnchanged",
                    "stateHidden",
                    "stateAlwaysHidden",
                    "stateVisible",
                    "stateAlwaysVisible",
                    "adjustUnspecified",
                    "adjustResize",
                    "adjustPan"
                });
    }
}
