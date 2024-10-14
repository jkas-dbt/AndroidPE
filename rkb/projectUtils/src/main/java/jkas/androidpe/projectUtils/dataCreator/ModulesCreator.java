package jkas.androidpe.projectUtils.dataCreator;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import jkas.androidpe.projectUtils.current.Environment;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.codeUtil.Files;
import net.lingala.zip4j.ZipFile;

/**
 * @author JKas
 */
public class ModulesCreator {
    public static final int APP_TYPE = 0;
    public static final int LIB_TYPE = 1;

    private Context C;
    private int language = 0;
    private int script = 0;
    private int codeTemplateType;
    private int type;
    private int rootProjectType;

    private String folderName, packageName, rootProjectPath, sdkMin, sdkTarget;
    // all data files ref for FilesRef class. must be edit to match project infos
    private Map<String, String> listFiles = new HashMap<>();
    // same thing with listFiles but just a simple extact data
    private Map<String, String> listArchivePackages = new HashMap<>();

    public ModulesCreator(Context c) {
        C = c;

        script = ProjectsCreator.SCRIPT_GROOVY;
        language = ProjectsCreator.JAVA_LANGUAGE;
        type = ProjectsCreator.PROJECT_TYPE_ANDROIDX;

        listFiles.clear();
    }

    public void writeModule() {
        for (Map.Entry<String, String> entry : listFiles.entrySet())
            Files.writeFile(rootProjectPath + "/" + entry.getKey(), entry.getValue());

        for (Map.Entry<String, String> entry : listArchivePackages.entrySet())
            try {
                Files.copyFileFromAssetsToDir(
                        C,
                        entry.getValue(),
                        Environment.DEFAULT_ANDROIDPE_TMP_DATA + "/archive.zip");
                new ZipFile(Environment.DEFAULT_ANDROIDPE_TMP_DATA + "/archive.zip")
                        .extractAll(rootProjectPath + "/" + entry.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        Files.writeFile(rootProjectPath + "/" + folderName + "/.gitignore", "/build/");
        addModuleToSettingsFile();
    }

    private void addModuleToSettingsFile() {
        String moduleName = folderName;
        int lastIndex = DataRefManager.getInstance().P.getAbsolutePath().length();
        if (rootProjectPath.length() > lastIndex)
            moduleName = "/" + rootProjectPath.substring(lastIndex) + "/" + folderName;
        moduleName = ("/" + moduleName).replace("//", "/").replace("/", ":").replace("::", ":");

        String pathSettingsGradle =
                DataRefManager.getInstance().P.getAbsolutePath() + "/settings.gradle";
        if (!Files.isFile(pathSettingsGradle)) pathSettingsGradle += ".kts";
        if (!Files.isFile(pathSettingsGradle)) {
            DialogBuilder.getDialogBuilder(
                    C,
                    C.getString(R.string.warning),
                    "settings.gradle / settings.gradle.kts : " + C.getString(R.string.not_found));
            return;
        }

        String gradleCode = Files.readFile(pathSettingsGradle);
        gradleCode +=
                "\ninclude"
                        + (pathSettingsGradle.endsWith(".kts")
                                ? "(\"" + moduleName + "\")"
                                : " '" + moduleName + "'");

        Files.writeFile(pathSettingsGradle, gradleCode);
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setRootProjectPath(String rootProjectPath) {
        this.rootProjectPath = rootProjectPath;
    }

    public void setSdkMin(int sdkMin) {
        this.sdkMin = String.valueOf(sdkMin);
    }

    public void setSdkTarget(int sdkTarget) {
        this.sdkTarget = String.valueOf(sdkTarget);
    }

    public void setLanguage(int language) {
        this.language = language;
        if (rootProjectType == ProjectsCreator.PROJECT_TYPE_ANDROIDX) {
            if (language == ProjectsCreator.JAVA_LANGUAGE) {
                listFiles.put(
                        folderName
                                + "/src/main/java/"
                                + packageName.replace(".", "/")
                                + "/MainActivity.java",
                        FilesRef.SourceCode.Java.CODEactivity.replace("$PACKAGE_NAME$", packageName)
                                .replace("$CLASS_NAME$", "MainActivity")
                                .replace("$LAYOUT_NAME$", "activity_main"));
            } else {
                listFiles.put(
                        folderName
                                + "/src/main/java/"
                                + packageName.replace(".", "/")
                                + "/MainActivity.kt",
                        FilesRef.SourceCode.Kotlin.CODEactivity.replace(
                                        "$PACKAGE_NAME$", packageName)
                                .replace("$LAYOUT_NAME$", "activity_main")
                                .replace("$CLASS_NAME$", "MainActivity"));
            }
        } else {
            listFiles.put(
                    folderName
                            + "/src/main/java/"
                            + packageName.replace(".", "/")
                            + "/MainActivity.kt",
                    FilesRef.SourceCode.Kotlin.CODEcompose.replace("$PACKAGE_NAME$", packageName)
                            .replace("$CLASS_NAME$", "MainActivity"));
            listFiles.put(
                    folderName
                            + "/src/main/java/"
                            + packageName.replace(".", "/")
                            + "/ui/theme/Color.kt",
                    FilesRef.SourceCode.Kotlin.CODEcolor.replace("$PACKAGE_NAME$", packageName));
            listFiles.put(
                    folderName
                            + "/src/main/java/"
                            + packageName.replace(".", "/")
                            + "/ui/theme/Theme.kt",
                    FilesRef.SourceCode.Kotlin.CODEtheme.replace("$PACKAGE_NAME$", packageName));
            listFiles.put(
                    folderName
                            + "/src/main/java/"
                            + packageName.replace(".", "/")
                            + "/ui/theme/Type.kt",
                    FilesRef.SourceCode.Kotlin.CODEtype.replace("$PACKAGE_NAME$", packageName));
        }
    }

    public void setModuleGradleScript(int type) {
        this.script = type;
    }

    public void setType(int type) {
        this.type = type;
        switch (type) {
            case APP_TYPE:
                if (rootProjectType == ProjectsCreator.PROJECT_TYPE_ANDROIDX) {
                    if (script == ProjectsCreator.SCRIPT_KOTLIN) {
                        if (language == ProjectsCreator.KOTLIN_LANGUAGE) {
                            listFiles.put(
                                    folderName + "/build.gradle.kts",
                                    FilesRef.Module.CODEAppLangKtsBuildGradleKts.replace(
                                                    "$PACKAGE_NAME$", packageName)
                                            .replace("$MIN_SDK$", sdkMin)
                                            .replace("$TARGET_SDK$", sdkTarget));
                        } else {
                            listFiles.put(
                                    folderName + "/build.gradle.kts",
                                    FilesRef.Module.CODEAppBuildGradleKts.replace(
                                                    "$PACKAGE_NAME$", packageName)
                                            .replace("$MIN_SDK$", sdkMin)
                                            .replace("$TARGET_SDK$", sdkTarget));
                        }
                    } else {
                        if (language == ProjectsCreator.KOTLIN_LANGUAGE) {
                            listFiles.put(
                                    folderName + "/build.gradle",
                                    FilesRef.Module.CODEAppLangKtsBuildGradle.replace(
                                                    "$PACKAGE_NAME$", packageName)
                                            .replace("$MIN_SDK$", sdkMin)
                                            .replace("$TARGET_SDK$", sdkTarget));
                        } else {
                            listFiles.put(
                                    folderName + "/build.gradle",
                                    FilesRef.Module.CODEAppBuildGradle.replace(
                                                    "$PACKAGE_NAME$", packageName)
                                            .replace("$MIN_SDK$", sdkMin)
                                            .replace("$TARGET_SDK$", sdkTarget));
                        }
                    }
                } else {
                    if (script == ProjectsCreator.SCRIPT_KOTLIN) {
                        listFiles.put(
                                folderName + "/build.gradle.kts",
                                FilesRef.Module.CODEAppComposeBuildGradleKts.replace(
                                                "$PACKAGE_NAME$", packageName)
                                        .replace("$MIN_SDK$", sdkMin)
                                        .replace("$TARGET_SDK$", sdkTarget));
                    } else {
                        listFiles.put(
                                folderName + "/build.gradle",
                                FilesRef.Module.CODEAppComposeBuildGradle.replace(
                                                "$PACKAGE_NAME$", packageName)
                                        .replace("$MIN_SDK$", sdkMin)
                                        .replace("$TARGET_SDK$", sdkTarget));
                    }
                }
                break;
            case LIB_TYPE:
                if (script == ProjectsCreator.SCRIPT_KOTLIN)
                    listFiles.put(
                            folderName + "/build.gradle.kts",
                            FilesRef.Module.CODELibBuildGradleKts.replace(
                                            "$PACKAGE_NAME$", packageName)
                                    .replace("$MIN_SDK$", sdkMin)
                                    .replace("$TARGET_SDK$", sdkTarget));
                else
                    listFiles.put(
                            folderName + "/build.gradle",
                            FilesRef.Module.CODELibBuildGradle.replace(
                                            "$PACKAGE_NAME$", packageName)
                                    .replace("$MIN_SDK$", sdkMin)
                                    .replace("$TARGET_SDK$", sdkTarget));
                break;
        }
        listFiles.put(folderName + "/proguard-rules.pro", FilesRef.Module.CODEproguardRulesPro);
    }

    public void setRootProjectType(int rootProjectType) {
        this.rootProjectType = rootProjectType;
        if (rootProjectType == ProjectsCreator.PROJECT_TYPE_ANDROIDX) {
            listFiles.put(
                    folderName + "/src/main/res/layout/activity_main.xml",
                    FilesRef.Resources.Layout.layoutXml);
        }
        listArchivePackages.put(
                folderName + "/src/main/res",
                FilesRef.Resources.Drawable.PATHmipmapXxxHdpiImagePng);
        listFiles.put(
                folderName + "/src/main/res/values/colors.xml",
                FilesRef.Resources.Values.CODEcolorsXml);
        listFiles.put(
                folderName + "/src/main/res/values/strings.xml",
                FilesRef.Resources.Values.CODEstringsXml.replace("$APP_NAME$", folderName));
        listFiles.put(
                folderName + "/src/main/res/values/themes.xml",
                FilesRef.Resources.Values.CODEthemesXml);
        listFiles.put(
                folderName + ProjectsPathUtils.ANDROID_MANIFEST_PATH,
                FilesRef.Resources.CODEandroidManifestXml);
    }
}
