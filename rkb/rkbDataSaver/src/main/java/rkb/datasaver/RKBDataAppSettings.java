package rkb.datasaver;

import android.content.Context;
import android.widget.Toast;
import jkas.androidpe.projectUtils.current.Environment;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class RKBDataAppSettings {
    private static final String pathToRKBData = Environment.DEFAULT_ROOT + "/RKB/RosetteKB.xml";

    private static Context C;
    public static XmlManager xmlF;

    public static final String MATERIAL_ENABLED = "true";
    public static final String MATERIAL_DISABLED = "false";
    public static final String APP_THEME_FOLLOW_SYSTEM = "system";
    public static final String APP_THEME_LIGHT = "light";
    public static final String APP_THEME_DARK = "dark";
    public static final String AUTO_SAVE_NONE = "none";
    public static final String AUTO_SAVE_10_SEC = "10000";
    public static final String AUTO_SAVE_15_SEC = "15000";
    public static final String AUTO_SAVE_20_SEC = "20000";
    public static final String AUTO_SAVE_30_SEC = "30000";

    public static void init(Context c) {
        C = c;
        initXmlFile();
    }

    private static void initXmlFile() {
        xmlF = new XmlManager(C);
        xmlF.initializeFromPath(pathToRKBData);
        if (xmlF.isInitialized) return;

        String rkbCode =
                """
<?xml version="1.0" encoding="utf-8"?>
<rkb>
    <material>false</material>
    <theme>system</theme>
    <language>default</language>
    <poa>true</poa> <!-- Project opening Assistance -->
    <olp>false</olp> <!-- open last project directly -->
    <pnp>my.company.</pnp> <!-- package name prefix -->
    <cas>none</cas><!-- code auto saved -->
    <p2p> <!-- START : paths to projects -->
        <path>AndroidPEProjects</path>
        <path>AndroidIDEProjects</path>
        <path>AppProjects</path>
    </p2p> <!-- END : paths to projects -->
</rkb>
""";
        Files.writeFile(pathToRKBData, rkbCode);
        initXmlFile();
    }

    public static String getCodeAutoSavedTime() {
        return xmlF.getElement("cas", 0).getTextContent();
    }

    public static void setCodeAutoSavedTime(String lang) {
        xmlF.getElement("cas", 0).setTextContent(lang);
        xmlF.saveAllModif();
    }

    public static String getAppLanguage() {
        return xmlF.getElement("language", 0).getTextContent();
    }

    public static void setAppLanguage(String lang) {
        xmlF.getElement("language", 0).setTextContent(lang);
        xmlF.saveAllModif();
    }

    public static boolean isAppMaterialEnabled() {
        return xmlF.getElement("material", 0).getTextContent().equals("true");
    }

    public static void setAppMaterialEnabled(String materialYou) {
        xmlF.getElement("material", 0).setTextContent(materialYou);
        xmlF.saveAllModif();
    }

    public static String getAppTheme() {
        return xmlF.getElement("theme", 0).getTextContent();
    }

    public static void setAppTheme(String theme) {
        xmlF.getElement("theme", 0).setTextContent(theme);
        xmlF.saveAllModif();
    }

    public static String getAppPnp() {
        return xmlF.getElement("pnp", 0).getTextContent();
    }

    public static void setAppPnp(String pnp) {
        xmlF.getElement("pnp", 0).setTextContent(pnp);
        xmlF.saveAllModif();
    }

    public static String getAppOlp() {
        return xmlF.getElement("olp", 0).getTextContent();
    }

    public static void setAppOlp(String olp) {
        xmlF.getElement("olp", 0).setTextContent(olp);
        xmlF.saveAllModif();
    }

    public static String getAppPoa() {
        return xmlF.getElement("poa", 0).getTextContent();
    }

    public static void setAppPoa(String poa) {
        xmlF.getElement("poa", 0).setTextContent(poa);
        xmlF.saveAllModif();
    }
}
