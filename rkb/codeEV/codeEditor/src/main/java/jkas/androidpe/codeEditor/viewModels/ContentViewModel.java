package jkas.androidpe.codeEditor.viewModels;

import androidx.core.util.Pair;
import io.github.rosemoe.sora.text.Content;
import java.util.ArrayList;

public class ContentViewModel {
    private static ContentViewModel instance;
    private ArrayList<Pair<String, Content>> list = new ArrayList<>();

    public static ContentViewModel getInstance() {
        if (instance == null) instance = new ContentViewModel();
        return instance;
    }

    public ContentViewModel() {
        super();
        list.clear();
    }

    public static void clear() {
        instance = null;
    }

    public void addData(String path, Content content) {
        list.add(new Pair<>(path, content));
    }

    public Content getData(String path) {
        for (var pair : list) {
            if (pair.first.equals(path)) {
                list.remove(pair);
                return pair.second;
            }
        }
        return null;
    }
}
