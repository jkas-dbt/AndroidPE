package jkas.androidpe.projectUtils.utils;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jkas.androidpe.project.Project;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class ProjectsUtils {
    public static final int NAP = 0; // not an protect
    public static final int ANDROID_PROJECT_BASED_ON_GRADLE = 1;
    public static final int GRADLE_PROJECT = 2;

    public static final int TYPE_JAVA = 0;
    public static final int TYPE_KOTLIN = 1;
    public static final int TYPE_COMPOSE = 2;

    public static int getModuleType() {
        String path = DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath();
        path += ProjectsPathUtils.gradlePath;
        if (Files.isFile(path + ".kts"))
            if (Files.readFile(path + ".kts").contains("compose")) return TYPE_COMPOSE;
            else return TYPE_KOTLIN;
        return TYPE_JAVA;
    }

    public static int getProjectStatus(Project P) {
        if (checkIfIsAnAndroidProjectBasedOnGradle(P)) return ANDROID_PROJECT_BASED_ON_GRADLE;
        if (checkIfIsAnGradleProject(P)) return GRADLE_PROJECT;
        return NAP;
    }

    private static boolean checkIfIsAnAndroidProjectBasedOnGradle(Project P) {
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.gradlePath)) return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.settingsPath)) return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.gradlePath + ".kts")) return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.settingsPath + ".kts"))
            return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.gradlewPath)) return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.gradlewBatPath)) return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.gradlePropertiesPath)) return true;
        return false;
    }

    private static boolean checkIfIsAnGradleProject(Project P) {
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.gradlePath)) return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.settingsPath)) return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.gradlePath + ".kts")) return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.settingsPath + ".kts"))
            return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.gradlewPath)) return true;
        if (Files.isFile(P.getAbsolutePath() + ProjectsPathUtils.gradlewBatPath)) return true;
        return false;
    }

    public static class Gradle {
        public static ArrayList<String> getModulesIncluded(@NonNull String path) {
            final ArrayList<String> includes = new ArrayList<>();
            String settingsGradleCode =
                    ResCodeUtils.removeJavaComment(Files.readFile(path)).replace("(", "");
            StringBuilder sb = new StringBuilder();
            String[] lines = settingsGradleCode.split("\\r?\\n");
            for (String line : lines) {
                sb.append(line.trim());
                if (line.endsWith(",") || line.endsWith("(")) continue;
                Matcher matcher =
                        Pattern.compile("include\\s*\\(?[\'\"]([\\w\\-\\:\\(\\)]+)[\'\"]\\)?")
                                .matcher(line);
                if (matcher.find()) includes.add(matcher.group(1));
                sb.setLength(0);
            }
            if (sb.length() > 0) {
                Matcher matcher =
                        Pattern.compile("include\\s*\\(?[\'\"]([\\w\\-\\:\\(\\)]+)[\'\"]\\)?")
                                .matcher(sb.toString());
                if (matcher.find()) includes.add(matcher.group(1));
            }

            try {
                if (includes.size() == 0) { // try another way, but may can get error
                    String[] group = settingsGradleCode.split("include");
                    for (int i = 1; i < group.length; i++) { // ignore first part
                        String input = group[i];
                        Pattern pattern = Pattern.compile("[\'\"](.*?)[\'\"]");
                        Matcher matcher = pattern.matcher(input);
                        while (matcher.find()) {
                            String txt = matcher.group(1);
                            if (txt.length() > 1) includes.add(txt);
                        }
                    }
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            return includes;
        }
    }
}
