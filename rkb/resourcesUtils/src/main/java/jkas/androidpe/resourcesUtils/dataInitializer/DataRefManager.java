package jkas.androidpe.resourcesUtils.dataInitializer;

import java.util.ArrayList;
import jkas.androidpe.resourcesUtils.modules.ModuleProject;
import jkas.androidpe.resourcesUtils.modules.ModuleRes;
import jkas.androidpe.resourcesUtils.requests.ProjectDataRequested;

/**
 * @author JKas
 */
public class DataRefManager {
    private static DataRefManager INSTANCE;

    public ModuleRes currentModuleRes;
    public ArrayList<ModuleRes> listModuleRes = new ArrayList<>();
    public ArrayList<ModuleProject> listModuleProject = new ArrayList<>();

    private DataRefManager() {
        listModuleProject.clear();
        listModuleRes.clear();
    }

    public static DataRefManager getInstance() {
        if (INSTANCE == null) INSTANCE = new DataRefManager();
        return INSTANCE;
    }

    public void reload() {
        initData();
    }

    public void setCurrentModuleRes(String currentFilePath) {
        for (ModuleProject mp : listModuleProject)
            if (currentFilePath.startsWith(mp.getAbsolutePath()))
                for (ModuleRes mr : listModuleRes)
                    if (mp.getPath().equals(mr.getPath())) {
                        currentModuleRes = mr;
                        return;
                    }
    }

    public void initData() {
        for (ModuleProject mp : listModuleProject) mp.initData();
    }

    /**
     * the following series of methods are intended for the interface which will be implemented in
     * the activity where the code will be edited
     */
    public static String getProjectAbsolutePath() {
        return ProjectDataRequested.getInstance().listener.onProjectAbsolutePathNeeded();
    }
}
