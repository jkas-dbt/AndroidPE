package jkas.androidpe.initializer;

import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.projectUtils.current.Environment;
import jkas.androidpe.projectUtils.dataCreator.FilesRef;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class ProjectInitializer {
    private final String SRC = "Project Initializer";
    private String pathTmp = Environment.DEFAULT_ANDROIDPE_TMP_DATA;
    private AppCompatActivity C;
    private LogicsAnalyzer logicsAnalyzer;
    private ExecutorService exe = Executors.newSingleThreadExecutor();

    public ProjectInitializer(AppCompatActivity C) {
        this.C = C;
        logicsAnalyzer = new LogicsAnalyzer(C);
    }

    public void iniProject() {
        try {
            exe.submit(
                    () -> {
                        gradleWrapper();
                        settingsGradle();
                        requiredFiles();
                        initializeProject();
                    });
            exe.shutdown();
            exe.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Logger.error(SRC, "Fatal Error : Can't init Project", e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeProject() {
        initData();
    }

    private void initData() {
        /* This method is called first to attempt to load modules without Gradle.
         * That said: this is only for the editor for support.
         */
        logicsAnalyzer.iniData();
    }

    public LogicsAnalyzer getRKBLogicsAnalyzer() {
        return logicsAnalyzer;
    }

    private void gradleWrapper() {
        try {
            if (!Files.isDirectory(
                    DataRefManager.getInstance().P.getAbsolutePath() + "/gradle/wrapper")) {
                Logger.warn(
                        SRC,
                        C.getString(R.string.missing_file)
                                + "...\"/gradle/wrapper/<b>gradle-wrapper.jar\"",
                        "...\"/gradle/wrapper/<b>gradle-wrapper.properties\"");
                Logger.info(
                        SRC,
                        C.getString(R.string.adding_missing_file)
                                + "...\"/gradle/wrapper/<b>gradle-wrapper.jar\"",
                        "...\"/gradle/wrapper/<b>gradle-wrapper.properties\"");

                Files.copyFileFromAssetsToDir(
                        C,
                        FilesRef.ProjectRoot.PATHgradleWrapperGradleWrapperJar,
                        DataRefManager.getInstance().P.getAbsolutePath()
                                + "/gradle/wrapper/gradle-wrapper.jar");
                Files.writeFile(
                        DataRefManager.getInstance().P.getAbsolutePath()
                                + "/gradle/wrapper/gradle-wrapper.properties",
                        FilesRef.ProjectRoot.CODEgradleWrapperGradleWrapperProperties);

                Logger.success(
                        SRC,
                        "2 : " + C.getString(R.string.file_has_been_added),
                        "...\"/gradle/wrapper/gradle-wrapper.jar\"",
                        "...\"/gradle/wrapper/gradle-wrapper.properties\"");
            }
        } catch (Exception e) {
            Logger.error(SRC, C.getString(R.string.cant_add_files), e.getMessage());
        }

        try {
            if (!Files.isFile(
                    DataRefManager.getInstance().P.getAbsolutePath()
                            + "/gradle/wrapper/gradle-wrapper.jar")) {
                Logger.warn(
                        SRC,
                        C.getString(R.string.missing_file)
                                + "...\"/gradle/wrapper/<b>gradle-wrapper.jar\"");
                Logger.info(
                        SRC,
                        C.getString(R.string.adding_missing_file)
                                + "...\"/gradle/wrapper/<b>gradle-wrapper.jar\"");

                Files.copyFileFromAssetsToDir(
                        C,
                        FilesRef.ProjectRoot.PATHgradleWrapperGradleWrapperJar,
                        DataRefManager.getInstance().P.getAbsolutePath()
                                + "/gradle/wrapper/gradle-wrapper.jar");
                Logger.success(
                        SRC,
                        C.getString(R.string.file_has_been_added),
                        "...\"/gradle/wrapper/gradle-wrapper.jar\"");
            }
        } catch (Exception e) {
            Logger.error(SRC, C.getString(R.string.cant_add_files), e.getMessage());
        }

        try {
            if (!Files.isFile(
                    DataRefManager.getInstance().P.getAbsolutePath()
                            + "/gradle/wrapper/gradle-wrapper.properties")) {
                Logger.warn(
                        SRC,
                        C.getString(R.string.missing_file),
                        "...\"/gradle/wrapper/gradle-wrapper.properties\"");
                Logger.info(
                        SRC,
                        C.getString(R.string.adding_missing_file),
                        "...\"/gradle/wrapper/gradle-wrapper.properties\"");

                Files.writeFile(
                        DataRefManager.getInstance().P.getAbsolutePath()
                                + "/gradle/wrapper/gradle-wrapper.properties",
                        FilesRef.ProjectRoot.CODEgradleWrapperGradleWrapperProperties);

                Logger.success(
                        SRC,
                        "2 : " + C.getString(R.string.file_has_been_added),
                        "...\"/gradle/wrapper/gradle-wrapper.properties\"");
            }
        } catch (Exception e) {
            Logger.error(SRC, C.getString(R.string.cant_add_files), e.getMessage());
        }
    }

    private void settingsGradle() {
        try {
            if (!Files.isFile(
                    DataRefManager.getInstance().P.getAbsolutePath() + "/settings.gradle"))
                if (!Files.isFile(
                        DataRefManager.getInstance().P.getAbsolutePath()
                                + "/settings.gradle.kts")) {
                    Logger.error(
                            SRC,
                            C.getString(R.string.cant_find_file),
                            "ROOT/settings.gradle | settings.gradle.kts");
                    Logger.info(SRC, C.getString(R.string.adding_missing_file));

                    String code = FilesRef.ProjectRoot.CODEsettingsGradle;
                    code =
                            code.replace(
                                    "$PROJECT_NAME$",
                                    DataRefManager.getInstance().P.getFolderName().trim());
                    Files.writeFile(
                            DataRefManager.getInstance().P.getAbsolutePath() + "/settings.gradle",
                            code);

                    Logger.success(SRC, C.getString(R.string.added), "ROOT/settings.gradle");
                }
        } catch (Exception e) {
            Logger.error(SRC, C.getString(R.string.cant_add_files), e.getMessage());
        }
    }

    private void requiredFiles() {
        try {
            String missing;
            missing = "";
            if (!Files.isFile(DataRefManager.getInstance().P.getAbsolutePath() + "/gradlew")) {
                Files.writeFile(
                        DataRefManager.getInstance().P.getAbsolutePath() + "/gradlew",
                        FilesRef.ProjectRoot.CODEgradlew);
                missing += DataRefManager.getInstance().P.getAbsolutePath() + "/gradlew\n";
            }

            if (!Files.isFile(DataRefManager.getInstance().P.getAbsolutePath() + "/gradlew.bat")) {
                Files.writeFile(
                        DataRefManager.getInstance().P.getAbsolutePath() + "/gradlew.bat",
                        FilesRef.ProjectRoot.CODEgradlewBat);
                missing += DataRefManager.getInstance().P.getAbsolutePath() + "/gradlew.bat\n";
            }

            if (!Files.isFile(
                    DataRefManager.getInstance().P.getAbsolutePath() + "/gradle.properties")) {
                Files.writeFile(
                        DataRefManager.getInstance().P.getAbsolutePath() + "/gradle.properties",
                        FilesRef.ProjectRoot.CODEgradleProperties);
                missing += DataRefManager.getInstance().P.getAbsolutePath() + "/gradle.properties";
            }

            if (!missing.isEmpty()) Logger.info(SRC, C.getString(R.string.added), missing);
        } catch (Exception e) {
            Logger.error(SRC, C.getString(R.string.cant_add_files), e.getMessage());
        }
    }
}
