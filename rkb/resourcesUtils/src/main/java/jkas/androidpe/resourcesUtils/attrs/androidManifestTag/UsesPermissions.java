package jkas.androidpe.resourcesUtils.attrs.androidManifestTag;

/**
 * @author JKas
 */
public class UsesPermissions {
    private static String[] DATA = null;

    public static String[] getAllDataAsArray() {
        if (DATA != null) return DATA;
        String[] data = new String[Permissions.values().length];
        int i = 0;
        for (Permissions p : Permissions.values()) {
            data[i] = p.toString();
            i++;
        }
        DATA = data;
        return DATA;
    }

    public enum Permissions {
        ACCESS_CHECKIN_PROPERTIES,
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION,
        ACCESS_LOCATION_EXTRA_COMMANDS,
        ACCESS_MOCK_LOCATION,
        ACCESS_NETWORK_STATE,
        ACCESS_SURFACE_FLINGER,
        ACCESS_WIFI_STATE,
        ACCOUNT_MANAGER,
        AUTHENTICATE_ACCOUNTS,
        BATTERY_STATS,
        BIND_APPWIDGET,
        BIND_ACCESSIBILITY_SERVICE,
        BIND_DEVICE_ADMIN,
        BIND_INPUT_METHOD,
        BIND_REMOTEVIEWS,
        BIND_TEXT_SERVICE,
        BIND_VPN_SERVICE,
        BIND_WALLPAPER,
        BLUETOOTH,
        BLUETOOTH_ADMIN,
        BRICK,
        BROADCAST_PACKAGE_REMOVED,
        BROADCAST_SMS,
        BROADCAST_STICKY,
        BROADCAST_WAP_PUSH,
        CALL_PHONE,
        CALL_PRIVILEGED,
        CAMERA,
        CHANGE_COMPONENT_ENABLED_STATE,
        CHANGE_CONFIGURATION,
        CHANGE_NETWORK_STATE,
        CHANGE_WIFI_MULTICAST_STATE,
        CHANGE_WIFI_STATE,
        CLEAR_APP_CACHE,
        CLEAR_APP_USER_DATA,
        CONTROL_LOCATION_UPDATES,
        DELETE_CACHE_FILES,
        DELETE_PACKAGES,
        DEVICE_POWER,
        DIAGNOSTIC,
        DISABLE_KEYGUARD,
        DUMP,
        EXPAND_STATUS_BAR,
        FACTORY_TEST,
        FLASHLIGHT,
        FORCE_BACK,
        GET_ACCOUNTS,
        GET_PACKAGE_SIZE,
        GET_TASKS,
        GLOBAL_SEARCH,
        HARDWARE_TEST,
        INJECT_EVENTS,
        INSTALL_LOCATION_PROVIDER,
        INSTALL_PACKAGES,
        INTERNAL_SYSTEM_WINDOW,
        INTERNET,
        KILL_BACKGROUND_PROCESSES,
        MANAGE_ACCOUNTS,
        MANAGE_APP_TOKENS,
        MANAGE_EXTERNAL_STORAGE,
        MASTER_CLEAR,
        MODIFY_AUDIO_SETTINGS,
        MODIFY_PHONE_STATE,
        MOUNT_FORMAT_FILESYSTEMS,
        MOUNT_UNMOUNT_FILESYSTEMS,
        NFC,
        PERSISTENT_ACTIVITY,
        PROCESS_OUTGOING_CALLS,
        POST_NOTIFICATIONS,
        RECEIVE_NOTIFICATIONS,
        READ_CALENDAR,
        READ_CALL_LOG,
        READ_CONTACTS,
        READ_EXTERNAL_STORAGE,
        READ_FRAME_BUFFER,
        READ_HISTORY_BOOKMARKS,
        READ_PROFILE,
        READ_SOCIAL_STREAM,
        READ_USER_DICTIONARY,
        SET_ALARM,
        SET_PREFERRED_APPLICATIONS,
        USE_BIOMETRIC,
        VIBRATE,
        WRITE_CALL_LOG,
        WRITE_EXTERNAL_STORAGE,
        WRITE_HISTORY_BOOKMARKS,
        WRITE_PROFILE,
        WRITE_SOCIAL_STREAM,
        WRITE_USER_DICTIONARY
    }
}
