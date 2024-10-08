package rkb.datasaver;

import android.content.Context;

/**
 * @author JKas
 */
public class BackupDataInitializer {
    private static Context C;
    public static void initialize(Context c) {
        C = c;
        RKBDataAppSettings.init(C);
        AMLProjectsData.init(C);
    }
}
