package jkas.androidpe.initializer;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.projectUtils.current.ProjectsModules;
import jkas.androidpe.project.AndroidModule;
import jkas.androidpe.projectUtils.utils.ProjectsUtils;
import jkas.androidpe.resources.R;
import jkas.androidpe.project.Project;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class LogicsAnalyzer {
    private final String SRC = "Module Parser";
    private AppCompatActivity C;
    private Project P;
    private ArrayList<String> includes = new ArrayList<>();

    public LogicsAnalyzer(AppCompatActivity c) {
        C = c;
        P = ProjectsModules.getInstance().P;
    }

    public void iniData() {
        Logger.info(SRC, C.getString(R.string.searching_for_modules));
        searchDataInfoFromSettingsGradleFile();
        checkIfAllModulesExists();
        setDefaultCurrentAndroidModule();
    }

    private void setDefaultCurrentAndroidModule() {
        boolean verif = false;
        for (AndroidModule am : ProjectsModules.getInstance().listOfAllAndroidModule) {
            verif = true;
            if (am.getPath().equals(":app")) {
                ProjectsModules.getInstance().currentAndroidModule = am;
                break;
            }
        }
        if (verif && ProjectsModules.getInstance().currentAndroidModule == null)
            ProjectsModules.getInstance().currentAndroidModule =
                    ProjectsModules.getInstance().listOfAllAndroidModule.get(0);

        if (ProjectsModules.getInstance().currentAndroidModule == null)
            if (ProjectsModules.getInstance().listOfAllAndroidModule.size() > 0)
                ProjectsModules.getInstance().currentAndroidModule =
                        ProjectsModules.getInstance().listOfAllAndroidModule.get(0);
    }

    private void checkIfAllModulesExists() {
        ProjectsModules.getInstance().listOfAllAndroidModule.clear();
        if (includes != null)
            for (final String s : includes) {
                String pathModule = P.getAbsolutePath() + s.replace(":", "/");
                String pathBuildGradle = pathModule + "/build.gradle";

                if (!Files.isDirectory(pathModule)) continue;
                if (!Files.isFile(pathBuildGradle))
                    if (!Files.isFile(pathBuildGradle + ".kts")) continue;
                    else pathBuildGradle += ".kts";
                if (!Files.isFile(pathBuildGradle)) continue;
                final AndroidModule am =
                        new AndroidModule(Files.getNameFromAbsolutePath(pathModule), s, pathModule);

                final ArrayList<String> modules = new ArrayList<>();
                String code = Files.readFile(pathBuildGradle).replace(".", ":").trim();
                code = ResCodeUtils.removeJavaComment(code);

                for (String r : includes)
                    if (code.contains(r) && !r.equals(am.getPath()))
                        if (code.contains(r + "')")
                                || code.contains(r + "\")")
                                || code.contains(r + ")")) modules.add(r);
                am.setRefToOthersModules(modules);

                ProjectsModules.getInstance().listOfAllAndroidModule.add(am);
            }
    }

    private void searchDataInfoFromSettingsGradleFile() {
        String path = P.getAbsolutePath() + "/settings.gradle";
        if (!Files.isFile(path) && !Files.isFile(path + ".kts")) {
            Logger.error(SRC, C.getString(R.string.not_found) + "(.kts)", path);
            return;
        } else if (Files.isFile(path + ".kts")) path = path + ".kts";

        includes = ProjectsUtils.Gradle.getModulesIncluded(path);
        if (includes != null) Logger.info(SRC, C.getString(R.string.modules_found));
        else Logger.info(SRC, C.getString(R.string.no_modules_found));

        if (includes.size() == 0) {
            if (Files.isDirectory(P.getAbsolutePath() + "/app")) {
                includes.add(":app");
                Logger.warn(
                        SRC,
                        ":app" + C.getString(R.string.added),
                        "But a problem was detected. "
                                + "your Gradle file is not properly configured or contains errors."
                                + " lease check your configuration file at the project root.");
            }
        }
    }
}
