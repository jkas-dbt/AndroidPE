package jkas.androidpe.projectUtils.current;

import java.util.ArrayList;
import jkas.androidpe.project.AndroidModule;
import jkas.androidpe.project.Project;

/**
 * @author JKas
 */
public class ProjectsModules {
    private static ProjectsModules instance;

    public Project P;
    public AndroidModule currentAndroidModule;
    public ArrayList<AndroidModule> listOfAllAndroidModule = new ArrayList<>();
    public String filePathSelected;
    
    public static ProjectsModules getInstance() {
        if (instance == null) newInstance();
        return instance;
    }

    private ProjectsModules() {
        listOfAllAndroidModule.clear();
        currentAndroidModule = null;
    }

    public static void newInstance() {
        instance = new ProjectsModules();
    }
}
