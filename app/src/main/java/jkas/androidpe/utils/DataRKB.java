package jkas.androidpe.utils;

import android.content.Context;
import rkb.datasaver.BackupDataInitializer;

/**
 * @author JKas
 */
public class DataRKB {
    public void init(Context c) {
        BackupDataInitializer.initialize(c);
    }
}
