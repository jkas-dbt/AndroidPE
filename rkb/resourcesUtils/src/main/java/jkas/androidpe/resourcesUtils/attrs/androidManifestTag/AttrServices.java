package jkas.androidpe.resourcesUtils.attrs.androidManifestTag;

/**
 * @author JKas
 */
public class AttrServices {
    private static String[] DATA = null;

    public static String[] getAllDataAsArray() {
        if (DATA != null) return DATA;
        String[] data = new String[Attr.values().length];
        int i = 0;
        for (Attr p : Attr.values()) {
            data[i] = p.toString().replace("_",".");
            i++;
        }
        DATA = data;
        return DATA;
    }

    public enum Attr {
        android_name,
        android_exported,
        android_enabled,
        android_icon,
        android_label,
        android_permission,
        android_process,
        android_stopWithTask
    }
}
