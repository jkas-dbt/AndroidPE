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
    private static ModuleProject mProject;

    public static synchronized void init(Context c) {
        C = c;
        DataRefManager.getInstance().listModuleRes.clear();
        for (ModuleProject mp : DataRefManager.getInstance().listModuleProject) {
            mRes = new ModuleRes(C, mp.getPath());
            mProject = mp;
            initModule();
            mRes.initValues();
            DataRefManager.getInstance().listModuleRes.add(mRes);
        }

        if (DataRefManager.getInstance().currentModuleRes != null) {
            DataRefManager.getInstance()
                    .setCurrentModuleRes(DataRefManager.getInstance().currentModuleRes.getPath());
        }
    }

    private static void initModule() {
        loadDrawables();
        loadLayouts();
        loadMenus();
        loadRaws();
        loadValues();
    }

    private static void loadValues() {
        mRes.values.clear();
        for (String path : mProject.getValues())
            for (String file : Files.listFile(path)) mRes.values.add(file);
    }

    private static void loadRaws() {
        mRes.raws.clear();
        mRes.listRaws.clear();
        for (String path : mProject.getRaws()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.raws.put(name.intern(), file);
                mRes.listRaws.add("@raw/" + name.intern());
            }
        }
    }

    private static void loadMenus() {
        mRes.menus.clear();
        mRes.listMenus.clear();
        for (String path : mProject.getMenus()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.menus.put(name.intern(), file);
                mRes.listMenus.add("@menu/" + name.intern());
            }
        }
    }

    private static void loadLayouts() {
        mRes.layouts.clear();
        mRes.listLayouts.clear();
        for (String path : mProject.getLayouts()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.layouts.put(name.intern(), file);
                mRes.listLayouts.add("@layout/" + name.intern());
            }
        }
    }

    private static void loadDrawables() {
        mRes.drawables.clear();
        mRes.listDrawables.clear();
        for (String path : mProject.getDrawables()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.drawables.put(name.intern(), file);
                mRes.listDrawables.add("@drawable/" + name.intern());
            }
        }

        // loading mipmap data
        mRes.mipmaps.clear();
        for (String path : mProject.getMipmaps()) {
            for (String file : Files.listFile(path)) {
                String name = Files.getNameFromAbsolutePath(file);
                name = name.substring(0, name.lastIndexOf("."));
                mRes.mipmaps.put(name.intern(), file);
                mRes.listDrawables.add("@mipmap/" + name.intern());
            }
        }
    }
}
