package jkas.androidpe.resourcesUtils.modules;

import android.content.Context;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
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

    // for auto-comp... assist
    public ArrayList<String> listStrings = new ArrayList<>();
    public ArrayList<String> listArrays = new ArrayList<>();
    public ArrayList<String> listBools = new ArrayList<>();
    public ArrayList<String> listDimens = new ArrayList<>();
    public ArrayList<String> listIntegers = new ArrayList<>();
    public ArrayList<String> listMenus = new ArrayList<>();
    public ArrayList<String> listLayouts = new ArrayList<>();
    public ArrayList<String> listRaws = new ArrayList<>();
    public ArrayList<String> listStyles = new ArrayList<>();
    public ArrayList<String> listColors = new ArrayList<>();
    public ArrayList<String> listDrawables = new ArrayList<>();

    public ModuleRes(Context c, String path) {
        this.C = c;
        this.path = path;
        init();
    }

    public void init() {
        valuesArrays.clear();
        valuesStrings.clear();
        valuesColors.clear();
        valuesBools.clear();
        valuesDimens.clear();
        valuesIntegers.clear();
        valuesStyles.clear();

        listArrays.clear();
        listStrings.clear();
        listColors.clear();
        listBools.clear();
        listDimens.clear();
        listIntegers.clear();
        listStyles.clear();
    }

    public boolean exists(String ref) {
        String name = ResourcesValuesFixer.parseReferName(ref);
        if (drawables.containsKey(name.intern())) return true;
        else if (mipmaps.containsKey(name.intern())) return true;
        else if (layouts.containsKey(name.intern())) return true;
        else if (menus.containsKey(name.intern())) return true;
        else if (raws.containsKey(name.intern())) return true;
        else if (valuesStrings.containsKey(name.intern())) return true;
        else if (valuesColors.containsKey(name.intern())) return true;
        else if (valuesBools.containsKey(name.intern())) return true;
        else if (valuesDimens.containsKey(name.intern())) return true;
        else if (valuesIntegers.containsKey(name.intern())) return true;
        else if (valuesStyles.containsKey(name.intern())) return true;
        else if (valuesArrays.containsKey(name.intern())) return true;
        return false;
    }

    public void initValues() {
        init();
        XmlManager xml = new XmlManager(C);
        for (String path : values) {
            if (Files.getParentNameFromAbsolutePath(path).contains("-")) continue;
            if (!xml.initializeFromPath(path)) continue;
            // init String
            for (final Element e : xml.getElementsByTagName("string")) {
                if (!valuesStrings.containsKey(e.getAttribute("name").intern()))
                    valuesStrings.put(e.getAttribute("name").intern(), e.getTextContent());
                if (!listStrings.contains(e.getAttribute("name").intern()))
                    listStrings.add("@string/" + e.getAttribute("name").intern());
            }

            // init Color
            for (final Element e : xml.getElementsByTagName("color")) {
                if (!valuesColors.containsKey(e.getAttribute("name").intern()))
                    valuesColors.put(e.getAttribute("name").intern(), e.getTextContent());
                if (!listColors.contains(e.getAttribute("name").intern()))
                    listColors.add("@color/" + e.getAttribute("name").intern());
            }

            // init Boolean
            for (final Element e : xml.getElementsByTagName("bool")) {
                if (!valuesBools.containsKey(e.getAttribute("name").intern()))
                    valuesBools.put(e.getAttribute("name").intern(), e.getTextContent());
                if (!listBools.contains(e.getAttribute("name").intern()))
                    listBools.add("@bool/" + e.getAttribute("name").intern());
            }

            // init Dimen
            for (final Element e : xml.getElementsByTagName("dimen")) {
                if (!valuesDimens.containsKey(e.getAttribute("name").intern()))
                    valuesDimens.put(e.getAttribute("name").intern(), e.getTextContent());
                if (!listDimens.contains(e.getAttribute("name").intern()))
                    listDimens.add("@dimen/" + e.getAttribute("name").intern());
            }

            // init Integer
            for (final Element e : xml.getElementsByTagName("integer")) {
                if (!valuesIntegers.containsKey(e.getAttribute("name").intern()))
                    valuesIntegers.put(e.getAttribute("name").intern(), e.getTextContent());
                if (!listIntegers.contains(e.getAttribute("name").intern()))
                    listIntegers.add("@integer/" + e.getAttribute("name").intern());
            }

            // init Style
            for (final Element e : xml.getElementsByTagName("style")) {
                if (!valuesStyles.containsKey(e.getAttribute("name").intern()))
                    valuesStyles.put(e.getAttribute("name").intern(), e.getTextContent());
                if (!listStyles.contains(e.getAttribute("name").intern()))
                    listStyles.add("@style/" + e.getAttribute("name").intern());
            }
        }
    }

    public String getPath() {
        return path;
    }
}
