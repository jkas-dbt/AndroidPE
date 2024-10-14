package jkas.androidpe.explorer.codeEditorImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import java.util.ArrayList;
import jkas.androidpe.explorer.CodeEditorExplorer;
import jkas.androidpe.explorer.views.TreeViewFile;
import jkas.androidpe.explorer.views.TreeViewFolder;
import androidx.appcompat.widget.PopupMenu;
import jkas.androidpe.project.AndroidModule;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class LoadTreeViewProject {
    private OnAnyEventListener listener;
    private Context C;
    private PopupMenu menu;
    private String path;
    private View view; // folder root

    public LoadTreeViewProject(Context C, String path) {
        this.C = C;
        this.path = path;
        loadRoot();
    }

    private void loadRoot() {
        final TreeViewFolder root = new TreeViewFolder(C, path, CodeEditorExplorer.TYPE_PROJECT);
        root.setText("<b>" + Files.getNameFromAbsolutePath(path) + "</b>");
        root.setOnAnyEventListener(
                new TreeViewFolder.OnAnyEventListener() {

                    @Override
                    public void onLoad(String path) {
                        loadChildren(root, path, root.opened);
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

        // path -> loadChildren(root, path));
        view = root.getView();
    }

    private void loadChildren(
            final TreeViewFolder rootFolder, final String rootPath, boolean opened) {
        rootFolder.setLoading(opened);
        executeTaskDir(rootFolder, Files.listDir(rootPath), 0);
    }

    private void executeTaskDir(
            final TreeViewFolder rootFolder, final ArrayList<String> listDir, final int index) {

        if (!rootFolder.opened) return;
        if (listDir.size() == 0) {
            rootFolder.setLoading(false);
            executeTaskFiles(rootFolder, Files.listFile(rootFolder.getPath()), 0);
            return;
        }

        final String p = listDir.get(index);
        final TreeViewFolder folder = new TreeViewFolder(C, p, CodeEditorExplorer.TYPE_PROJECT);
        folder.setText(getText(p));
        folder.setOnAnyEventListener(
                new TreeViewFolder.OnAnyEventListener() {

                    @Override
                    public void onLoad(String path) {
                        loadChildren(folder, path, folder.opened);
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
        rootFolder.addChild(folder.getView());

        new Handler(Looper.getMainLooper())
                .postDelayed(
                        () -> {
                            if (index + 1 < listDir.size())
                                executeTaskDir(rootFolder, listDir, index + 1);
                            else {
                                executeTaskFiles(
                                        rootFolder, Files.listFile(rootFolder.getPath()), 0);
                                if (listDir.size() == 1
                                        && Files.listFile(rootFolder.getPath()).size() == 0) {
                                    folder.setLoading(true);
                                    folder.opened = true;
                                    loadChildren(folder, listDir.get(0), folder.opened);
                                }
                            }
                            rootFolder.setLoading(false);
                        },
                        1);
    }

    private void executeTaskFiles(
            final TreeViewFolder rootFolder, final ArrayList<String> listFiles, final int index) {
        if (!rootFolder.opened) return;
        if (listFiles.size() == 0) {
            rootFolder.setLoading(false);
            return;
        }

        final String p = listFiles.get(index);

        final TreeViewFile file = new TreeViewFile(C, p, CodeEditorExplorer.TYPE_PROJECT);
        file.setText(getText(p));
        file.setOnAnyEventListener(
                new TreeViewFile.OnAnyEventListener() {

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
        ResCodeUtils.setImageTreeView(file.getIcon(), p);
        rootFolder.addChild(file.getView());

        new Handler(Looper.getMainLooper())
                .postDelayed(
                        () -> {
                            if (index + 1 < listFiles.size())
                                executeTaskFiles(rootFolder, listFiles, index + 1);
                            else rootFolder.setLoading(false);
                        },
                        1);
    }

    public String getText(String path) {
        String txt = Files.getNameFromAbsolutePath(path);
        for (AndroidModule am : DataRefManager.getInstance().listAndroidModule)
            if (am.getProjectAbsolutePath().equals(path))
                return "<b>" + txt + "</b> <i>(module)</i>";
        if (txt.endsWith(".gradle") || txt.endsWith(".gradle.kts"))
            return "<b>" + txt + "</b> <i>(config)</i>";
        return txt;
    }

    public View getView() {
        return view;
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
