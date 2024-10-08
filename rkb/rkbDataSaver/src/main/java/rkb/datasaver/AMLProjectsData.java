package rkb.datasaver;

import android.content.Context;
import android.widget.Toast;
import java.util.ArrayList;
import jkas.androidpe.projectUtils.current.Environment;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class AMLProjectsData {
    private static final String pathToAMLData = Environment.DEFAULT_ROOT + "/RKB/AnnyBL.xml";

    private static Context C;
    private static XmlManager xmlF;
    private static Element ropProject; // recently opened project (only one)

    public static void init(Context c) {
        C = c;
        initXmlFile();
    }

    public static void initXmlFile() {
        xmlF = new XmlManager(C);
        xmlF.initializeFromPath(pathToAMLData);
        if (xmlF.isInitialized) return;

        String rkbCode =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + """
<aml>
    <rop></rop> <!-- recently opened project -->
</aml>""";
        Files.writeFile(pathToAMLData, rkbCode);
        xmlF.initializeFromPath(pathToAMLData);
        if (!xmlF.isInitialized)
            Toast.makeText(C, "AML Error : fatal must bu fix", Toast.LENGTH_SHORT).show();
    }

    public static void appendNewOpenedFile(String path) {
        if (path == null) return;
        for (Element e /*file tag*/ : XmlManager.getAllChildFromElement(ropProject))
            if (e.getTextContent().equals(path)) return;

        Element eFile = xmlF.getDocument().createElement("file");
        eFile.setTextContent(path);
        ropProject.appendChild(eFile);
        xmlF.saveAllModif();

        return;
    }

    public static void fileClosed(String path) {
        for (Element e : XmlManager.getAllChildFromElement(ropProject))
            if (e.getTextContent().equals(path)) {
                e.getParentNode().removeChild(e);
                xmlF.saveAllModif();
                break;
            }
    }

    public static ArrayList<String> getOpenedFiles() {
        ArrayList<String> list = new ArrayList<>();
        for (Element e : XmlManager.getAllChildFromElement(ropProject))
            list.add(e.getTextContent());
        return list;
    }

    public static void addProjectIfNotAvialable(String p) {
        for (Element e : xmlF.getElementsByTagName("project"))
            if (e.getAttribute("name").equals(p)) {
                ropProject = e;
                return;
            }

        Element project = xmlF.getDocument().createElement("project");
        project.setAttribute("name", p);
        xmlF.getElement("rop", 0).appendChild(project);
        xmlF.saveAllModif();
        ropProject = project;
    }

    public String getLastProjectOpened() {
        Element el = null;
        for (Element e : XmlManager.getAllChildFromElement(xmlF.getElement("rop", 0))) el = e;
        if (el != null) return el.getTextContent();
        return null;
    }
}
