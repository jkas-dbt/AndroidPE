package jkas.androidpe.projectUtils.dataCreator;

import android.content.Context;
import android.widget.Toast;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class ActivitiesCreator {
    private String SRC = "Activity Creator";
    private Context C;
    private OnCreateListener listener;

    public static final int TYPE_JAVA = 0;
    public static final int TYPE_KOTLIN = 1;
    public static final int TYPE_COMPOSE = 2;

    private int type;
    private String className, layoutName;
    private String codeClass, codeLayout;
    private String pathCodeFile, pathLayoutFile;
    private String pkg;

    public ActivitiesCreator(Context c, String pkg, String className, String layoutName, int type) {
        this.C = c;
        this.className = className;
        this.layoutName = layoutName;
        this.type = type;
        this.pkg = pkg;
        init();
    }

    public boolean create() {
        if (!writeInAMSuccessed()) {
            Logger.error(SRC, C.getString(R.string.msg_error_activity_creating));
            listener.onCreate(false);
            Toast.makeText(C, "error", Toast.LENGTH_SHORT).show();
            return false;
        }
        Files.writeFile(pathCodeFile, codeClass);
        if (type != TYPE_COMPOSE) Files.writeFile(pathLayoutFile, codeLayout);
        Logger.success(
                SRC,
                "Activity : " + className + C.getString(R.string.added),
                C.getString(R.string.module)
                        + " : "
                        + DataRefManager.getInstance().currentAndroidModule.getPath(),
                C.getString(R.string.package_name) + " : " + pkg);
        listener.onCreate(true);
        return true;
    }

    private boolean writeInAMSuccessed() {
        XmlManager AM = new XmlManager(C);
        String path2Manifest =
                DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath()
                        + ProjectsPathUtils.ANDROID_MANIFEST_PATH;
        if (!Files.isFile(path2Manifest)) {
            Logger.error(SRC, "AndroidManifest.xml : " + C.getString(R.string.not_found));
            Files.writeFile(path2Manifest, FilesRef.Resources.CODEandroidManifestXml);
            Logger.info(SRC, "AndroidManifest.xml : " + C.getString(R.string.created));
            AM.initializeFromPath(path2Manifest);
            for (Element e : AM.getElementsByTagName("application")) {
                e.removeAttribute("android:icon");
                e.removeAttribute("android:label");
                e.removeAttribute("android:roundIcon");
                e.removeAttribute("android:theme");
            }
            for (Element e : AM.getElementsByTagName("activity")) e.getParentNode().removeChild(e);
            AM.saveAllModif();
        }
        AM.initializeFromPath(path2Manifest);
        // check if all is OK
        Element manifest = AM.getElement("manifest", 0);
        if (manifest == null) {
            String code =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                            + "<manifest  =\"http://schemas.android.com/apk/res/android\">"
                            + "</manifest>";
            Files.writeFile(path2Manifest, code);
            AM.initializeFromPath(path2Manifest);
        }

        manifest = AM.getElement("manifest", 0);
        manifest.setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android");
        AM.saveAllModif();

        Element app = AM.getElement("application", 0);
        boolean searchForAppTag = false;
        if (app == null) {
            Element newApp = AM.getDocument().createElement("application");
            manifest.appendChild(newApp);
            AM.saveAllModif();
        }

        app = AM.getElement("application", 0);
        Element activity = AM.getDocument().createElement("activity");
        activity.setAttribute("android:name", pkg + "." + className);
        app.appendChild(activity);

        AM.saveAllModif();

        Logger.info(
                SRC, "AndroidManifest.xml : Activity " + className, C.getString(R.string.added));
        return true;
    }

    private void init() {
        pathCodeFile = DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath();
        if (Files.isDirectory(pathCodeFile + ProjectsPathUtils.KOTLIN_PATH))
            pathCodeFile += ProjectsPathUtils.KOTLIN_PATH;
        else pathCodeFile += ProjectsPathUtils.JAVA_PATH;

        pathCodeFile += "/" + pkg.replace(".", "/");
        pathCodeFile += "/" + className;
        pathCodeFile = pathCodeFile.replace("//", "/");
        switch (type) {
            case TYPE_JAVA:
                pathCodeFile += ".java";
                codeClass = FilesRef.SourceCode.Java.CODEactivity;
                break;
            case TYPE_KOTLIN:
                pathCodeFile += ".kt";
                codeClass = FilesRef.SourceCode.Kotlin.CODEactivity;
                break;
            case TYPE_COMPOSE:
                pathCodeFile += ".kt";
                codeClass = FilesRef.SourceCode.Kotlin.CODEcompose;
                break;
        }

        pathLayoutFile =
                DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath()
                        + ProjectsPathUtils.LAYOUT_PATH;
        pathLayoutFile += "/" + layoutName + ".xml";
        codeLayout = FilesRef.Resources.Layout.layoutXml;

        codeClass = codeClass.replace("$PACKAGE_NAME$", pkg);
        codeClass = codeClass.replace("$CLASS_NAME$", className);
        codeClass = codeClass.replace("$LAYOUT_NAME$", layoutName);
    }

    // interface
    public void setOnCreateListener(OnCreateListener listener) {
        this.listener = listener;
    }

    public interface OnCreateListener {
        public void onCreate(boolean load);
    }
}
