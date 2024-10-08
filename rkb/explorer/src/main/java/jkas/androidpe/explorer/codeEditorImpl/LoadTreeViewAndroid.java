package jkas.androidpe.explorer.codeEditorImpl;

import android.content.Context;

/**
 * @author JKas
 */
public class LoadTreeViewAndroid {
    private OnFileClickListener eventOnClick;
    private Context C;
    private String path;

    public LoadTreeViewAndroid(Context C, String path) {
        this.C = C;
        this.path = path;
    }

    public void loadRoot() {}

    public void setOnFileClickListener(OnFileClickListener eventOnClick) {
        this.eventOnClick = eventOnClick;
    }

    public interface OnFileClickListener {
        public void onClick(String path);
    }
}
