package jkas.androidpe.resourcesUtils.dataInitializer;

import android.widget.Toast;
import java.util.ArrayList;
import jkas.androidpe.project.AndroidModule;
import jkas.androidpe.project.Project;
import jkas.androidpe.resourcesUtils.modules.ModuleProject;
import jkas.androidpe.resourcesUtils.modules.ModuleRes;
import jkas.androidpe.resourcesUtils.requests.ProjectDataRequested;

/**
 * @author JKas
 */
public class DataRefManager {
    private static DataRefManager INSTANCE;

    public String filePathSelected;
    public Project P;
    public ModuleRes currentModuleRes;
    public ModuleProject currentModuleProject;
    public AndroidModule currentAndroidModule;
    public ArrayList<ModuleRes> listModuleRes = new ArrayList<>();
    public ArrayList<ModuleProject> listModuleProject = new ArrayList<>();
    public ArrayList<AndroidModule> listAndroidModule = new ArrayList<>();

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

    public void setCurrentModuleRes(String path) {
        for (ModuleProject mp : listModuleProject) {
            if (path.equals(mp.getPath())) {
                currentModuleProject = mp;
                break;
            }
        }
        for (ModuleRes mr : listModuleRes) {
            if (path.equals(mr.getPath())) {
                currentModuleRes = mr;
                break;
            }
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
