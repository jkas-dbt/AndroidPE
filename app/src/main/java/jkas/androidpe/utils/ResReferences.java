package jkas.androidpe.utils;

import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import jkas.androidpe.project.AndroidModule;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dataInitializer.DataResInitializer;
import jkas.androidpe.resourcesUtils.modules.ModuleProject;

/**
 * @author JKas
 */
public class ResReferences {
    private static Context C;

    public static void init(Context c) {
        C = c;
        ExecutorService exe = Executors.newSingleThreadExecutor();
        exe.execute(() -> initGlobal());
        exe.shutdown();
        try {
            exe.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void initGlobal() {
        loadModules();
        DataRefManager.getInstance().initData();
        DataResInitializer.init(C);
    }

    /** Load module first */
    private static void loadModules() {
        DataRefManager.getInstance().listModuleProject.clear();
        for (AndroidModule am : DataRefManager.getInstance().listAndroidModule) {
            ModuleProject mp = new ModuleProject();
            mp.setPath(am.getPath());
            mp.setAbsolutePath(am.getProjectAbsolutePath());
            mp.setName(am.getName());
            mp.addModuleToRef(am.getRefToOthersModules());
            DataRefManager.getInstance().listModuleProject.add(mp);
        }
    }
}
