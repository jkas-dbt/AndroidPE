package jkas.androidpe.projectUtils.utils;

import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;

/**
 * @author JKas
 */
public class ValuesTools {

    public static class PathController {

        public static boolean isCodeFile(String path) {
            String pattern =
                    (DataRefManager.getInstance().P.getAbsolutePath()
                                    + "/.*/src/main/(java|kotlin|cpp).*")
                            .replace("/", "\\/");
            if (path.matches(pattern)) return true;
            return false;
        }

        public static boolean isResFile(String path) {
            String patternRes =
                    (DataRefManager.getInstance().P.getAbsolutePath()
                                    + "/.*"
                                    + ProjectsPathUtils.RES_PATH
                                    + ".*")
                            .replace("/", "\\/");
            if (path.matches(patternRes)) return true;
            return false;
        }

        public static boolean isDrawableFile(String path) {
            String patternXml = null;
            { // check if it a xml drawable path
                patternXml =
                        (DataRefManager.getInstance().P.getAbsolutePath()
                                        + "/.*"
                                        + ProjectsPathUtils.DRAWABLE_PATH.intern()
                                        + ".*")
                                .replace("/", "\\/");
                if (path.matches(patternXml)) return true;
            }

            { // check if it a xml mipmap path
                patternXml =
                        (DataRefManager.getInstance().P.getAbsolutePath()
                                        + "/.*"
                                        + ProjectsPathUtils.MIPMAP_PATH
                                        + ".*")
                                .replace("/", "\\/");

                if (path.matches(patternXml)) return true;
            }
            return false;
        }

        public static boolean isLayoutFile(String path) {
            String patternXml =
                    (DataRefManager.getInstance().P.getAbsolutePath()
                                    + "/.*"
                                    + ProjectsPathUtils.LAYOUT_PATH
                                    + ".*")
                            .replace("/", "\\/");

            if (path.matches(patternXml)) return true;
            return false;
        }

        public static boolean isMenuFile(String path) {
            String patternXml =
                    (DataRefManager.getInstance().P.getAbsolutePath()
                                    + "/.*"
                                    + ProjectsPathUtils.MENU_PATH
                                    + ".*")
                            .replace("/", "\\/");

            if (path.matches(patternXml)) return true;

            return false;
        }

        public static boolean isValuesFile(String path) {
            String patternXml =
                    (DataRefManager.getInstance().P.getAbsolutePath()
                                    + "/.*"
                                    + ProjectsPathUtils.VALUES_PATH
                                    + ".*")
                            .replace("/", "\\/");

            if (path.matches(patternXml) || path.endsWith(ProjectsPathUtils.VALUES_PATH))
                return true;
            return false;
        }

        public static boolean isGradleFile(String path) {

            if (path.toLowerCase().endsWith(ProjectsPathUtils.gradlePath)
                    || path.endsWith(ProjectsPathUtils.gradlePath + ".kts")) return true;

            return false;
        }
    }
}
