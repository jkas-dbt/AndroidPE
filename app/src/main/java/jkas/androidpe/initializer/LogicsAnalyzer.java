package jkas.androidpe.initializer;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.project.AndroidModule;
import jkas.androidpe.projectUtils.utils.ProjectsUtils;
import jkas.androidpe.resources.R;
import jkas.androidpe.project.Project;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
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
        P = DataRefManager.getInstance().P;
    }

    public void iniData() {
        Logger.info(SRC, C.getString(R.string.searching_for_modules));
        searchDataInfoFromSettingsGradleFile();
        checkIfAllModulesExists();
        setDefaultCurrentAndroidModule();
    }

    private void setDefaultCurrentAndroidModule() {
        boolean verif = false;
        for (AndroidModule am : DataRefManager.getInstance().listAndroidModule) {
            verif = true;
            if (am.getPath().equals(":app")) {
                DataRefManager.getInstance().currentAndroidModule = am;
                DataRefManager.getInstance().setCurrentModuleRes(":app");
                return;
            }
        }
        if (DataRefManager.getInstance().listAndroidModule.size() > 0) {
            DataRefManager.getInstance().currentAndroidModule =
                    DataRefManager.getInstance().listAndroidModule.get(0);
        }
    }

    private void checkIfAllModulesExists() {
        DataRefManager.getInstance().listAndroidModule.clear();
        for (final String s : includes) {
            String pathModule = P.getAbsolutePath() + s.replace(":", "/");
            String pathBuildGradle = pathModule + "/build.gradle";

            boolean found = false;
            if (Files.isDirectory(pathModule)) {
                found = Files.isFile(pathBuildGradle);
                if (!found) {
                    pathBuildGradle += ".kts";
                    found = Files.isFile(pathBuildGradle);
                }
            }

            if (!found) {
                Logger.error(
                        "Modules Manager",
                        C.getString(R.string.module)
                                + " (\""
                                + s
                                + "\") "
                                + C.getString(R.string.not_found));
                continue;
            }

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

            DataRefManager.getInstance().listAndroidModule.add(am);
        }
    }

    private void searchDataInfoFromSettingsGradleFile() {
        String path = P.getAbsolutePath() + "/settings.gradle";
        if (!Files.isFile(path) && !Files.isFile(path + ".kts")) {
            Logger.error(SRC, C.getString(R.string.not_found) + "(.kts)", path);
            return;
        } else if (Files.isFile(path + ".kts")) path = path + ".kts";

        includes = ProjectsUtils.Gradle.getModulesIncluded(path);
        if (includes.size() > 0) Logger.info(SRC, C.getString(R.string.modules_found));
        else Logger.info(SRC, C.getString(R.string.no_modules_found));

        if (includes.size() == 0) {
            if (Files.isDirectory(P.getAbsolutePath() + "/app")) {
                includes.add(":app");
                Logger.warn(
                        SRC,
                        ":app" + C.getString(R.string.added),
                        "But a problem was detected. "
                                + "your Gradle file is not properly configured or contains errors."
                                + " please check your configuration file at the project root.");
            }
        }
    }
}
