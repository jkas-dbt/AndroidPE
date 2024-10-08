package jkas.androidpe.resourcesUtils.dataInitializer;

import android.content.Context;
import jkas.androidpe.resourcesUtils.modules.ModuleProject;
import jkas.androidpe.resourcesUtils.modules.ModuleRes;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class DataResInitializer {
    private static Context C;
    private static ModuleRes mRes;
    private static ModuleProject module;

    public static synchronized void init(Context c) {
        C = c;
        DataRefManager.getInstance().listModuleRes.clear();
        for (ModuleProject mp : DataRefManager.getInstance().listModuleProject) {
            mRes = new ModuleRes(C, mp.getPath());
            module = mp;
            initModule();
            mRes.initValues();
            DataRefManager.getInstance().listModuleRes.add(mRes);
        }
    }

    private static void initModule() {
        loadDrawables();
        loadMipmaps();
        loadLayouts();
        loadMenus();
        loadRaws();
        loadValues();
    }

    private static void loadValues() {
        mRes.values.clear();
        for (String path : module.getValues())
            for (String file : Files.listFile(path)) mRes.values.add(file);
    }

    private static void loadRaws() {
        mRes.raws.clear();
        for (String path : module.getRaws()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.raws.put(name.intern(), file);
            }
        }
    }

    private static void loadMenus() {
        mRes.menus.clear();
        for (String path : module.getMenus()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.menus.put(name.intern(), file);
            }
        }
    }

    private static void loadLayouts() {
        mRes.layouts.clear();
        for (String path : module.getLayouts()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.layouts.put(name.intern(), file);
            }
        }
    }

    private static void loadMipmaps() {
        mRes.mipmaps.clear();
        for (String path : module.getMipmaps()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.mipmaps.put(name.intern(), file);
            }
        }
    }

    private static void loadDrawables() {
        mRes.drawables.clear();
        for (String path : module.getDrawables()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.drawables.put(name.intern(), file);
            }
        }
    }
}
