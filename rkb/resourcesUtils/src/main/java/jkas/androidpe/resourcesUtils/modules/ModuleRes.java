package jkas.androidpe.resourcesUtils.modules;

import android.content.Context;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import jkas.codeUtil.XmlManager;
import jkas.codeUtil.Files;
import org.w3c.dom.Element;

/**
 * This is the class that contains module's resources. This class represents a module. subsequently,
 * the ResDataInitializer class will create several depending on the module detected in the project.
 */

/**
 * @author JKas
 */
public class ModuleRes {
    private Context C;
    private String path; // :moduleName |:subFolder:moduleName | ...

    public Map<String, String> drawables = new HashMap<>();
    public Map<String, String> mipmaps = new HashMap<>();
    public Map<String, String> layouts = new HashMap<>();
    public Map<String, String> menus = new HashMap<>();
    public Map<String, String> raws = new HashMap<>();

    public ArrayList<String> values = new ArrayList<>();

    public Map<String, String> valuesStrings = new HashMap<>();
    public Map<String, String> valuesColors = new HashMap<>();
    public Map<String, String> valuesBools = new HashMap<>();
    public Map<String, String> valuesDimens = new HashMap<>();
    public Map<String, String> valuesIntegers = new HashMap<>();
    public Map<String, String> valuesStyles = new HashMap<>();
    public Map<String, ArrayList<String>> valuesArrays = new HashMap<>();

    public ModuleRes(Context c, String path) {
        this.C = c;
        this.path = path;

        init();
    }

    public void init() {
        valuesStrings.clear();
        valuesColors.clear();
        valuesBools.clear();
        valuesDimens.clear();
        valuesIntegers.clear();
        valuesArrays.clear();
        valuesStyles.clear();
    }

    public void initValues() {
        init();
        XmlManager xml = new XmlManager(C);
        for (String path : values) {
            if (Files.getParentNameFromAbsolutePath(path).contains("-")) continue;
            if (!xml.initializeFromPath(path)) continue;
            // init String
            for (final Element e : xml.getElementsByTagName("string"))
                if (!valuesStrings.containsKey(e.getAttribute("name").intern()))
                    valuesStrings.put(e.getAttribute("name").intern(), e.getTextContent());

            // init Color
            for (final Element e : xml.getElementsByTagName("color"))
                if (!valuesColors.containsKey(e.getAttribute("name").intern()))
                    valuesColors.put(e.getAttribute("name").intern(), e.getTextContent());

            // init Boolean
            for (final Element e : xml.getElementsByTagName("bool"))
                if (!valuesBools.containsKey(e.getAttribute("name").intern()))
                    valuesBools.put(e.getAttribute("name").intern(), e.getTextContent());

            // init Dimen
            for (final Element e : xml.getElementsByTagName("dimen"))
                if (!valuesDimens.containsKey(e.getAttribute("name").intern()))
                    valuesDimens.put(e.getAttribute("name").intern(), e.getTextContent());

            // init Integer
            for (final Element e : xml.getElementsByTagName("integer"))
                if (!valuesIntegers.containsKey(e.getAttribute("name").intern()))
                    valuesIntegers.put(e.getAttribute("name").intern(), e.getTextContent());

            // init Style
            for (final Element e : xml.getElementsByTagName("style"))
                if (!valuesStyles.containsKey(e.getAttribute("name").intern()))
                    valuesStyles.put(e.getAttribute("name").intern(), e.getTextContent());
        }
    }

    public String getPath() {
        return path;
    }
}
