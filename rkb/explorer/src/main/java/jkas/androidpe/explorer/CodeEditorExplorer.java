package jkas.androidpe.explorer;

import android.content.Context;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import jkas.androidpe.explorer.codeEditorImpl.LoadTreeViewAndroid;
import jkas.androidpe.explorer.codeEditorImpl.LoadTreeViewProject;
import jkas.androidpe.explorer.databinding.CodeEditorExplorerBinding;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;

/**
 * @author JKas
 */
public class CodeEditorExplorer {
    public static final int TYPE_ANDROID = 0;
    public static final int TYPE_PROJECT = 1;

    private OnAnyEventListener listener;
    private Context C;
    private CodeEditorExplorerBinding binding;
    private LoadTreeViewAndroid LEA;
    private LoadTreeViewProject LEP;

    private int type_explorer = TYPE_PROJECT;

    public CodeEditorExplorer(Context C) {
        this.C = C;

        init();
        listener();
        events();
        loadData();
    }

    private void loadData() {
        loadDataExplorer();
    }

    private void events() {
        binding.btnAndroid.setOnClickListener((v) -> loadDataAndroid());
        binding.btnProject.setOnClickListener((v) -> loadDataExplorer());
    }

    private void listener() {
        LEA.setOnFileClickListener(path -> {});
        LEP.setOnAnyEventListener(
                new LoadTreeViewProject.OnAnyEventListener() {

                    @Override
                    public void onClick(String path) {
                        listener.onClick(path);
                    }

                    @Override
                    public void onRename(String oldPath, String newPath) {
                        listener.onRename(oldPath, newPath);
                    }

                    @Override
                    public void onDelete(String path) {
                        listener.onDelete(path);
                    }
                });
    }

    private void init() {
        binding = CodeEditorExplorerBinding.inflate(LayoutInflater.from(C));
        LEA = new LoadTreeViewAndroid(C, DataRefManager.getInstance().P.getAbsolutePath());
        LEP = new LoadTreeViewProject(C, DataRefManager.getInstance().P.getAbsolutePath());
    }

    public View getView() {
        return (View) binding.getRoot();
    }

    private void loadDataAndroid() {
        Toast.makeText(C, "soon", Toast.LENGTH_SHORT).show();
        loadDataExplorer();
        /*binding.toggleButton.check(binding.btnAndroid.getId());
        LEA.loadRoot();*/
    }

    private void loadDataExplorer() {
        binding.toggleButton.check(binding.btnProject.getId());
        binding.linData.removeAllViews();
        binding.linData.addView(LEP.getView());
    }

    public void setOnAnyEventListener(OnAnyEventListener listener) {
        this.listener = listener;
    }

    public interface OnAnyEventListener {
        public void onClick(String path);

        public void onRename(String oldPath, String newPath);

        public void onDelete(String path);
    }
}
