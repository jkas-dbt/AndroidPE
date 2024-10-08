package jkas.androidpe.resourcesUtils.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.codeUtil.Files;

/**
 * This class save all references available in the targeted module including the modules on which it
 * depends.
 */

/*
 * @author JKas
 */
public class ModuleProject {
    private String path;
    private String absolutePath;
    private String name;

    private ArrayList<String> refToOtherModule = new ArrayList<>();

    private ArrayList<String> drawables = new ArrayList<>();
    private ArrayList<String> mipmaps = new ArrayList<>();
    private ArrayList<String> layouts = new ArrayList<>();
    private ArrayList<String> menus = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();
    private ArrayList<String> raws = new ArrayList<>();

    public ModuleProject() {
        refToOtherModule.clear();

        drawables.clear();
        mipmaps.clear();
        layouts.clear();
        menus.clear();
        values.clear();
        raws.clear();
    }

    /**
     * For all modules to which the currently selected module refers, their path to the "res"
     * directory are/will be added in the "listPathRes" list. So, for each directory type, each list
     * will contain all the parent paths for which they refer
     */
    public void initData() {
        final ArrayList<String> listPathRes = new ArrayList<>();
        listPathRes.clear();
        // add current module first
        listPathRes.add(getAbsolutePath() + ProjectsPathUtils.RES_PATH);

        // add the res path of ModuleRef
        for (String modulePath : getRefToOtherModule())
            for (ModuleProject mp : DataRefManager.getInstance().listModuleProject)
                if (modulePath.equals(mp.getPath()))
                    listPathRes.add(mp.getAbsolutePath() + ProjectsPathUtils.RES_PATH);

        for (String resPath : listPathRes) {
            for (String path : Files.listDir(resPath)) {
                String folderName = Files.getNameFromAbsolutePath(path);
                if (folderName.startsWith("drawable")) this.drawables.add(path);
                else if (folderName.startsWith("mipmap")) this.mipmaps.add(path);
                else if (folderName.startsWith("layout")) this.layouts.add(path);
                else if (folderName.startsWith("menu")) this.menus.add(path);
                else if (folderName.startsWith("values")) this.values.add(path);
                else if (folderName.startsWith("raw")) this.raws.add(path);
            }
        }
    }

    public ArrayList<String> getDrawables() {
        return this.drawables;
    }

    public ArrayList<String> getMipmaps() {
        return this.mipmaps;
    }

    public ArrayList<String> getLayouts() {
        return this.layouts;
    }

    public ArrayList<String> getMenus() {
        return this.menus;
    }

    public ArrayList<String> getRaws() {
        return this.raws;
    }

    public ArrayList<String> getValues() {
        return this.values;
    }

    /** Base code */
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAbsolutePath() {
        return this.absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getRefToOtherModule() {
        return this.refToOtherModule;
    }

    public void addModuleToRef(ArrayList<String> listModulePath) {
        this.refToOtherModule.addAll(listModulePath);
    }
}
