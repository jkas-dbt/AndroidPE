package jkas.androidpe.projectUtils.dataCreator;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class ProjectsCreator {
    private Context C;

    public static final int PROJECT_TYPE_ANDROIDX = 0;
    public static final int PROJECT_TYPE_COMPOSE = 1;

    public static final int SCRIPT_GROOVY = 0;
    public static final int SCRIPT_KOTLIN = 1;

    public static final int JAVA_LANGUAGE = 0;
    public static final int KOTLIN_LANGUAGE = 1;

    private String projectAbsolutePath;
    private String folderName, packageName, prefixPathToProject;
    private int language, script, projectType, sdkMin, sdkTarget;

    // all data files ref for FilesRef class. must be edit to match project infos
    private Map<String, String> listFiles = new HashMap<>();
    // same thing with listFiles but just a simple copy;
    private Map<String, String> listDataPackages = new HashMap<>();

    private ModulesCreator module;

    public ProjectsCreator(Context c) {
        C = c;

        listFiles.clear();

        script = SCRIPT_GROOVY;
        language = JAVA_LANGUAGE;
        projectType = PROJECT_TYPE_ANDROIDX;

        module = new ModulesCreator(C);
    }

    public void writeProjects() {
        for (Map.Entry<String, String> entry : listFiles.entrySet())
            Files.writeFile(projectAbsolutePath + "/" + entry.getKey(), entry.getValue());

        for (Map.Entry<String, String> entry : listDataPackages.entrySet())
            Files.copyFileFromAssetsToDir(
                    C, entry.getKey(), projectAbsolutePath + "/" + entry.getValue());

        module.setFolderName("app");
        module.setPackageName(packageName);
        module.setRootProjectPath(projectAbsolutePath);
        module.setSdkMin(21 + sdkMin);
        module.setSdkTarget(21 + sdkTarget);
        module.setModuleGradleScript(script);
        module.setRootProjectType(projectType);
        module.setType(ModulesCreator.APP_TYPE);
        module.setLanguage(language);
        module.writeModule();

        Files.writeFile(projectAbsolutePath + "/" + folderName + "/.gitignore", "/build/");
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setGradleScript(int script) {
        this.script = script;
        if (script == SCRIPT_GROOVY) {
            listFiles.put("build.gradle", FilesRef.ProjectRoot.CODEbuildGradle);
            listFiles.put(
                    "settings.gradle",
                    FilesRef.ProjectRoot.CODEsettingsGradle.replace("$PROJECT_NAME$", folderName));
        } else {
            listFiles.put("build.gradle.kts", FilesRef.ProjectRoot.CODEbuildGradleKts);
            listFiles.put(
                    "settings.gradle.kts",
                    FilesRef.ProjectRoot.CODEsettingsGradleKts.replace(
                            "$PROJECT_NAME$", folderName));
        }
        listFiles.put("gradle.properties", FilesRef.ProjectRoot.CODEgradleProperties);
        listFiles.put("gradlew", FilesRef.ProjectRoot.CODEgradlew);
        listFiles.put("gradlew.bat", FilesRef.ProjectRoot.CODEgradlewBat);
        listFiles.put(
                "gradle/wrapper/gradle-wrapper.properties",
                FilesRef.ProjectRoot.CODEgradleWrapperGradleWrapperProperties);
        listDataPackages.put(
                FilesRef.ProjectRoot.PATHgradleWrapperGradleWrapperJar,
                "gradle/wrapper/gradle-wrapper.jar");
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
        projectAbsolutePath = Files.getExternalStorageDir() + "/" + prefixPathToProject;
        projectAbsolutePath += "/" + folderName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setPrefixPathToProject(String prefixPathToProject) {
        this.prefixPathToProject = prefixPathToProject;
    }

    public void setSdkMin(int sdkMin) {
        this.sdkMin = sdkMin;
    }

    public void setSdkTarget(int sdkTarget) {
        this.sdkTarget = sdkTarget;
    }

    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }
}
