package jkas.androidpe.constants;

import android.util.Pair;
import java.util.ArrayList;

/**
 * @author JKas
 */
public class LanguagesPreferences {
    public static ArrayList<Pair<String, String>> list = new ArrayList<>();
    public static String[] name, code;

    static {
        init();
        initNameCode();
    }

    private static void initNameCode() {
        name = new String[list.size()];
        code = new String[list.size()];

        int i = 0;
        for (var pair : list) {
            name[i] = pair.first;
            code[i] = pair.second;
            i++;
        }
    }

    private static void init() {
        list.clear();
        list.add(new Pair<String, String>("Default (English)", "default"));
        list.add(new Pair<String, String>("Français", "fr"));
        list.add(new Pair<String, String>("Español", "es"));
        list.add(new Pair<String, String>("Indonesia", "in"));
        list.add(new Pair<String, String>("Italiano", "it"));
        list.add(new Pair<String, String>("Português", "pt"));
        list.add(new Pair<String, String>("हिन्दी", "hi"));
        list.add(new Pair<String, String>("বাংলা", "bn"));
    }
}
