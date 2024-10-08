package jkas.androidpe.projectUtils.dataCreator;

import android.content.Context;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class FilesRef {
    private static Context C;

    public static void initData(Context c) {
        C = c;
        init();
    }

    private static void init() {
        // Project class init()
        ProjectRoot.CODEbuildGradle =
                Files.readFileFromAssets(C, "projects/android/gradle/build.gradle");
        ProjectRoot.CODEsettingsGradle =
                Files.readFileFromAssets(C, "projects/android/gradle/settings.gradle");
        ProjectRoot.CODEbuildGradleKts =
                Files.readFileFromAssets(C, "projects/android/gradle/build.gradle.kts");
        ProjectRoot.CODEsettingsGradleKts =
                Files.readFileFromAssets(C, "projects/android/gradle/settings.gradle.kts");
        ProjectRoot.CODEgradleProperties =
                Files.readFileFromAssets(C, "projects/android/gradle/gradle.properties");
        ProjectRoot.CODEgradlew = Files.readFileFromAssets(C, "projects/android/gradle/gradlew");
        ProjectRoot.CODEgradlewBat =
                Files.readFileFromAssets(C, "projects/android/gradle/gradlew.bat");
        ProjectRoot.CODEgradleWrapperGradleWrapperProperties =
                Files.readFileFromAssets(
                        C, "projects/android/gradle/gradle/wrapper/gradle-wrapper.properties");

        // Modules
        Module.CODEAppBuildGradle =
                Files.readFileFromAssets(C, "projects/android/gradle/module/app.build.gradle");
        Module.CODEAppBuildGradleKts =
                Files.readFileFromAssets(C, "projects/android/gradle/module/app.build.gradle.kts");
        Module.CODEAppLangKtsBuildGradle =
                Files.readFileFromAssets(
                        C, "projects/android/gradle/module/app.langKts.build.gradle");
        Module.CODEAppLangKtsBuildGradleKts =
                Files.readFileFromAssets(
                        C, "projects/android/gradle/module/app.langKts.build.gradle.kts");
        Module.CODEAppComposeBuildGradle =
                Files.readFileFromAssets(
                        C, "projects/android/gradle/module/app.compose.build.gradle");
        Module.CODEAppComposeBuildGradleKts =
                Files.readFileFromAssets(
                        C, "projects/android/gradle/module/app.compose.build.gradle.kts");
        Module.CODELibBuildGradle =
                Files.readFileFromAssets(C, "projects/android/gradle/module/lib.build.gradle");
        Module.CODELibBuildGradleKts =
                Files.readFileFromAssets(C, "projects/android/gradle/module/lib.build.gradle.kts");
        Module.CODEproguardRulesPro =
                Files.readFileFromAssets(C, "projects/android/gradle/module/proguard-rules.pro");

        // AndroidManifest.xml
        Resources.CODEandroidManifestXml =
                Files.readFileFromAssets(C, "projects/android/xml/AndroidManifest.xml");

        // Drawable
        Resources.Drawable.CODEViewXml =
                Files.readFileFromAssets(C, "projects/android/xml/drawable/drawable.xml");

        // Layout
        Resources.Layout.layoutXml =
                Files.readFileFromAssets(C, "projects/android/xml/layout/layout.xml");

        // Menu
        Resources.Menu.menuXml = Files.readFileFromAssets(C, "projects/android/xml/menu/menu.xml");

        // Values
        Resources.Values.CODEcolorsXml =
                Files.readFileFromAssets(C, "projects/android/xml/values/colors.xml");
        Resources.Values.CODEstringsXml =
                Files.readFileFromAssets(C, "projects/android/xml/values/strings.xml");
        Resources.Values.CODEthemesXml =
                Files.readFileFromAssets(C, "projects/android/xml/values/themes.xml");

        // Xml
        Resources.Xml.CODEbackupRulesXml =
                Files.readFileFromAssets(C, "projects/android/xml/xml/backup_rules.xml");
        Resources.Xml.CODEdataExtractionRulesXml =
                Files.readFileFromAssets(C, "projects/android/xml/xml/data_extraction_rules.xml");

        // Java
        SourceCode.Java.CODEclass =
                Files.readFileFromAssets(C, "projects/android/src/java/class.java");
        SourceCode.Java.CODEactivity =
                Files.readFileFromAssets(C, "projects/android/src/java/activity.java");
        SourceCode.Java.CODEFragment =
                Files.readFileFromAssets(C, "projects/android/src/java/fragment.java");
        SourceCode.Java.CODEenum =
                Files.readFileFromAssets(C, "projects/android/src/java/enum.java");
        SourceCode.Java.CODEinterface =
                Files.readFileFromAssets(C, "projects/android/src/java/interface.java");
        SourceCode.Java.CODEprovider =
                Files.readFileFromAssets(C, "projects/android/src/java/provider.java");
        SourceCode.Java.CODEreceiver =
                Files.readFileFromAssets(C, "projects/android/src/java/receiver.java");
        SourceCode.Java.CODEservice =
                Files.readFileFromAssets(C, "projects/android/src/java/service.java");

        // Kotlin
        SourceCode.Kotlin.CODEclass =
                Files.readFileFromAssets(C, "projects/android/src/kotlin/class.kt");
        SourceCode.Kotlin.CODEactivity =
                Files.readFileFromAssets(C, "projects/android/src/kotlin/activity.kt");
        SourceCode.Kotlin.CODEFragment =
                Files.readFileFromAssets(C, "projects/android/src/kotlin/fragment.kt");
        SourceCode.Kotlin.CODEcompose =
                Files.readFileFromAssets(C, "projects/android/src/kotlin/compose.kt");
        SourceCode.Kotlin.CODEtheme =
                Files.readFileFromAssets(C, "projects/android/src/kotlin/theme.kt");
        SourceCode.Kotlin.CODEcolor =
                Files.readFileFromAssets(C, "projects/android/src/kotlin/color.kt");
        SourceCode.Kotlin.CODEtype =
                Files.readFileFromAssets(C, "projects/android/src/kotlin/type.kt");
        SourceCode.Kotlin.CODEprovider =
                Files.readFileFromAssets(C, "projects/android/src/java/provider.java");
        SourceCode.Kotlin.CODEreceiver =
                Files.readFileFromAssets(C, "projects/android/src/java/receiver.java");
        SourceCode.Kotlin.CODEservice =
                Files.readFileFromAssets(C, "projects/android/src/java/service.java");
    }

    public static class ProjectRoot {
        public static String CODEbuildGradle;
        public static String CODEsettingsGradle;
        public static String CODEbuildGradleKts;
        public static String CODEsettingsGradleKts;
        public static String CODEgradleProperties;
        public static String CODEgradlew;
        public static String CODEgradlewBat;
        public static String CODEgradleWrapperGradleWrapperProperties;
        public static String PATHgradleWrapperGradleWrapperJar =
                "projects/android/gradle/gradle/wrapper/gradle-wrapper.jar";
    }

    public static class Module {
        public static String CODEAppBuildGradle;
        public static String CODEAppBuildGradleKts;
        public static String CODEAppLangKtsBuildGradle;
        public static String CODEAppLangKtsBuildGradleKts;
        public static String CODEAppComposeBuildGradle;
        public static String CODEAppComposeBuildGradleKts;
        public static String CODELibBuildGradle;
        public static String CODELibBuildGradleKts;
        public static String CODEproguardRulesPro;
    }

    public static class Resources {
        public static String CODEandroidManifestXml;

        public static class Drawable {
            public static String CODEViewXml;
            public static String PATHmipmapXxxHdpiImagePng =
                    "projects/android/xml/drawable/drawable.zip";
        }

        public static class Layout {
            public static String layoutXml;
        }

        public static class Menu {
            public static String menuXml;
        }

        public static class Values {
            public static String CODEcolorsXml;
            public static String CODEstringsXml;
            public static String CODEthemesXml;
        }

        public static class Xml {
            public static String CODEbackupRulesXml;
            public static String CODEdataExtractionRulesXml;
        }
    }

    public static class SourceCode {
        public static class Java {
            public static String CODEclass;
            public static String CODEactivity;
            public static String CODEFragment;
            public static String CODEenum;
            public static String CODEinterface;
            public static String CODEprovider;
            public static String CODEreceiver;
            public static String CODEservice;
        }

        public static class Kotlin {
            public static String CODEclass;
            public static String CODEactivity;
            public static String CODEFragment;
            public static String CODEcompose;
            public static String CODEcolor;
            public static String CODEtheme;
            public static String CODEtype;
            public static String CODEprovider;
            public static String CODEreceiver;
            public static String CODEservice;
        }

        /*public static class Cpp {
            public static String CODEclass;
            public static String CODEactivity;
            public static String CODEFragment;
        }*/
    }

    public static class Fonts {
        public static String PATHjetbrains_mono_ttf = "fonts/jetbrains-mono.ttf";
        public static String PATHjosefin_sans_ttf = "fonts/josefin-sans.ttf";
        public static String PATHquicksand_ttf = "fonts/quicksand.ttf";
    }
}
