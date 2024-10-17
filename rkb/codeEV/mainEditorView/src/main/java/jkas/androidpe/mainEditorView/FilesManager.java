package jkas.androidpe.mainEditorView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.util.Pair;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import jkas.androidpe.codeEditor.viewModels.ContentViewModel;
import jkas.androidpe.logger.LoggerRes;
import jkas.androidpe.projectUtils.utils.ValuesTools;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class FilesManager {
    public static FilesManager INSTANCE;
    private OnRequest listenerRequest;
    private AppCompatActivity C;
    private ArrayList<Pair<String, MainEditorView>> tmpListOpenedFiles = new ArrayList<>();
    private MainEditorView currentOpenedFile;
    private Bundle bundle;
    private int POCV = 0; // Position of current view
    private boolean loading = false;
    private TextView tvInfo;
    private TabLayout tabLayout;
    private ViewFlipper viewFlipper;
    private ArrayList<String> listPathFiles = new ArrayList<>();

    public FilesManager(AppCompatActivity C, Bundle bundle) {
        this.C = C;
        this.bundle = bundle;
        tmpListOpenedFiles.clear();
    }

    public void bindInterViews(TabLayout tab, ViewFlipper vf, TextView tv) {
        this.tvInfo = tv;
        this.tabLayout = tab;
        this.viewFlipper = vf;
        tabImpl();
    }

    public void parseListOpenFile(ArrayList<String> list) {
        loading = true;
        listPathFiles = list;
        if (listPathFiles.size() > 0) {
            for (var path : listPathFiles) {
                tabLayout.addTab(tabLayout.newTab().setText(Files.getNameFromAbsolutePath(path)));
            }
            restoreContent();
        }
        loading = false;
        LoggerRes.addLogListener(
                new LoggerRes.LogListener() {

                    @Override
                    public void reloadResRef() {
                        for (var pair : tmpListOpenedFiles) pair.second.reloadCode();
                    }

                    @Override
                    public void onSaveRequested() {
                        save();
                    }
                });
    }

    private MainEditorView addMainEV(final String path) {
        final MainEditorView mainEV = new MainEditorView(C, path);
        mainEV.setOnAnyChangedListener(cause -> updateOnEventListener(cause));
        tmpListOpenedFiles.add(new Pair<>(path, mainEV));
        return mainEV;
    }

    private void setCurrentOpenFile() {
        currentOpenedFile = null;
        String path = listPathFiles.get(POCV);
        for (var pair : tmpListOpenedFiles) {
            if (pair.first.equals(path)) {
                currentOpenedFile = pair.second;
                updateCurrentFile();
                return;
            }
        }
        // if current MainEV not found
        currentOpenedFile = addMainEV(path);
        updateCurrentFile();
    }

    private void updateCurrentFile() {
        viewFlipper.removeAllViews();
        viewFlipper.addView(currentOpenedFile.getView());
        viewFlipper.setDisplayedChild(0);
    }

    private void removeFileFromTmpMainEV(String path) {
        for (var pair : tmpListOpenedFiles) {
            if (pair.first.equals(path)) {
                tmpListOpenedFiles.remove(pair);
                break;
            }
        }
    }

    private void updateOnEventListener(int cause) {
        if (cause == MainEditorView.CAUSE_TEXT_CHANED) {
            tabLayout
                    .getTabAt(POCV)
                    .setText("*" + Files.getNameFromAbsolutePath(currentOpenedFile.getPath()));
            listenerRequest.onSavedEnabled(true);
            undoRedoManager();
        } else if (cause == MainEditorView.CAUSE_FULL_SCREEN_NEEDED) {
            listenerRequest.onFullScreenNeeded(true);
        } else if (cause == MainEditorView.CAUSE_FULL_SCREEN_EXIT_NEEDED) {
            listenerRequest.onFullScreenNeeded(false);
        }
    }

    private void tabImpl() {
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        POCV = tab.getPosition();
                        setCurrentOpenFile();
                        updatePath();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        POCV = tab.getPosition();
                        if (!loading) performFileOption(tab);
                    }
                });
    }

    private void updatePath() {
        String path = currentOpenedFile.getPath();
        int index = DataRefManager.getInstance().P.getAbsolutePath().length();
        tvInfo.setText(path.substring(index).replace("/", " ⟩ "));
        listenerRequest.onSwitchEditorEnabled(
                ValuesTools.PathController.isResFile(currentOpenedFile.getPath()));
        undoRedoManager();
        if (!path.matches(
                DataRefManager.getInstance().P.getAbsolutePath()
                        + "/.*"
                        + ProjectsPathUtils.RES_PATH
                        + "/.*")) return;

        String androidPath = "";
        for (var module : DataRefManager.getInstance().listModuleProject) {
            if (path.startsWith(module.getAbsolutePath())) androidPath = module.getPath();
        }
        DataRefManager.getInstance().setCurrentModuleRes(androidPath);
        listenerRequest.onSwitchEditor(currentOpenedFile.isDesignEnabled());
    }

    private void undoRedoManager() {
        if (currentOpenedFile != null) {
            listenerRequest.onUndoEnabled(currentOpenedFile.canUndo());
            listenerRequest.onRedoEnabled(currentOpenedFile.canRedo());
        }
    }

    public void setNewFileClicked(String path) {
        loading = true;
        int i = 0;
        for (var dataPath : listPathFiles) {
            if (dataPath.equals(path)) {
                tabLayout.selectTab(tabLayout.getTabAt(i));
                listenerRequest.onCloseDrawer();
                return;
            }
            i++;
        }
        addMainEV(path);
        listPathFiles.add(path);
        tabLayout.addTab(tabLayout.newTab().setText(Files.getNameFromAbsolutePath(path)));
        tabLayout.selectTab(tabLayout.getTabAt(listPathFiles.size() - 1));
        listenerRequest.onCloseDrawer();
        loading = false;
    }

    public void setRenamePath(String oldPath, String newPath) {
        int p = 0;
        boolean found = false;
        for (final var path : listPathFiles) {
            if (!path.equals(oldPath)) {
                p++;
                continue;
            } else found = true;
            if (!found) return;
            listPathFiles.set(p, newPath);
            if (oldPath.equals(currentOpenedFile.getPath())) {
                currentOpenedFile.refreshWithNewPath(newPath);
            }

            tabLayout.getTabAt(p).setText(Files.getNameFromAbsolutePath(newPath));

            for (var pair : tmpListOpenedFiles) {
                if (pair.first.equals(oldPath)) {
                    pair.second.refreshWithNewPath(newPath);
                }
            }
            updatePath();
            break;
        }
    }

    public void setPathDeleted(String path) {
        int p = 0;
        boolean found = false;
        for (var tmp : listPathFiles) {
            if (!tmp.equals(path)) {
                p++;
                continue;
            } else found = true;
            if (!found) return;
            removeFileFromTmpMainEV(path);
            tabLayout.removeTabAt(p);
            break;
        }
    }

    public boolean isThereAnyUnsavedContent() {
        if (currentOpenedFile == null) return false;
        for (var pair : tmpListOpenedFiles) if (pair.second.isThereUnsavedContent()) return true;
        return false;
    }

    public synchronized void save() {
        boolean resModified = false;
        for (var pair : tmpListOpenedFiles) {
            if (ValuesTools.PathController.isValuesFile(pair.first)) {
                if (pair.second.isThereUnsavedContent()) {
                    pair.second.save();
                    resModified = true;
                }
            } else pair.second.save();
        }
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setText(tab.getText().toString().replace("*", ""));
        }
        if (resModified) LoggerRes.reloadResRef();
        listenerRequest.onSavedEnabled(false);
    }

    private void performFileOption(TabLayout.Tab tab) {
        PopupMenu menu = new PopupMenu(C, tab.view);
        menu.getMenu().add(Menu.NONE, 1, Menu.NONE, R.string.close);
        menu.getMenu().add(Menu.NONE, 2, Menu.NONE, R.string.closes_others);
        menu.getMenu().add(Menu.NONE, 3, Menu.NONE, R.string.close_all);
        menu.setOnMenuItemClickListener(
                menuItem -> {
                    if (menuItem.getItemId() == 1) requestClose();
                    else if (menuItem.getItemId() == 2) requestCloseOthers();
                    else if (menuItem.getItemId() == 3) {
                        currentOpenedFile = null;
                        requestCloseAll();
                    }
                    return true;
                });
        menu.show();
    }

    private void requestCloseAll() {
        if (isThereAnyUnsavedContent()) {
            DialogBuilder.getDialogBuilder(
                            C,
                            C.getString(R.string.warning),
                            C.getString(R.string.warning_unsaved_content))
                    .setCancelable(false)
                    .setPositiveButton(
                            R.string.save,
                            (d, v) -> {
                                save();
                                tvInfo.setText("...");
                                for (var path : listPathFiles)
                                    listenerRequest.onCloseFileFromDateAML(path);
                                listPathFiles.clear();
                                tmpListOpenedFiles.clear();
                                viewFlipper.removeAllViews();
                                tabLayout.removeAllTabs();
                                listenerRequest.onSwitchEditorEnabled(false);
                            })
                    .setNegativeButton(
                            R.string.close,
                            (d, v) -> {
                                tvInfo.setText("...");
                                for (var path : listPathFiles)
                                    listenerRequest.onCloseFileFromDateAML(path);
                                listPathFiles.clear();
                                tmpListOpenedFiles.clear();
                                viewFlipper.removeAllViews();
                                tabLayout.removeAllTabs();
                                listenerRequest.onSwitchEditorEnabled(false);
                            })
                    .show();
        } else {
            tvInfo.setText("...");
            for (var path : listPathFiles) listenerRequest.onCloseFileFromDateAML(path);
            listPathFiles.clear();
            tmpListOpenedFiles.clear();
            viewFlipper.removeAllViews();
            tabLayout.removeAllTabs();
            listenerRequest.onSwitchEditorEnabled(false);
        }
    }

    private void requestCloseOthers() {
        if (isThereAnyUnsavedContent()) {
            DialogBuilder.getDialogBuilder(
                            C,
                            C.getString(R.string.warning),
                            C.getString(R.string.warning_unsaved_content))
                    .setCancelable(false)
                    .setPositiveButton(
                            R.string.save,
                            (d, v) -> {
                                save();
                                requestCloseAll();
                                listPathFiles.add(currentOpenedFile.getPath());
                                tmpListOpenedFiles.add(
                                        new Pair<>(currentOpenedFile.getPath(), currentOpenedFile));
                                tabLayout.addTab(
                                        tabLayout
                                                .newTab()
                                                .setText(
                                                        Files.getNameFromAbsolutePath(
                                                                currentOpenedFile.getPath())));
                            })
                    .setNegativeButton(
                            R.string.close,
                            (d, v) -> {
                                tvInfo.setText("...");
                                for (var path : listPathFiles)
                                    listenerRequest.onCloseFileFromDateAML(path);
                                listPathFiles.clear();
                                tmpListOpenedFiles.clear();
                                viewFlipper.removeAllViews();
                                tabLayout.removeAllTabs();
                                listPathFiles.add(currentOpenedFile.getPath());
                                tmpListOpenedFiles.add(
                                        new Pair<>(currentOpenedFile.getPath(), currentOpenedFile));
                                tabLayout.addTab(
                                        tabLayout
                                                .newTab()
                                                .setText(
                                                        Files.getNameFromAbsolutePath(
                                                                currentOpenedFile.getPath())));
                            })
                    .show();
        } else {
            tvInfo.setText("...");
            requestCloseAll();
            listPathFiles.add(currentOpenedFile.getPath());
            tmpListOpenedFiles.add(new Pair<>(currentOpenedFile.getPath(), currentOpenedFile));
            tabLayout.addTab(
                    tabLayout
                            .newTab()
                            .setText(Files.getNameFromAbsolutePath(currentOpenedFile.getPath())));
        }
    }

    private void requestClose() {
        tvInfo.setText("...");
        if (currentOpenedFile.isThereUnsavedContent()) {
            DialogBuilder.getDialogBuilder(
                            C,
                            C.getString(R.string.warning),
                            C.getString(R.string.warning_unsaved_content)
                                    + ":\n"
                                    + currentOpenedFile.getPath())
                    .setCancelable(false)
                    .setPositiveButton(
                            R.string.save,
                            (d, v) -> {
                                currentOpenedFile.save();
                                listenerRequest.onCloseFileFromDateAML(currentOpenedFile.getPath());
                                removeFileFromTmpMainEV(listPathFiles.get(POCV));
                                listPathFiles.remove(POCV);
                                tabLayout.removeTabAt(POCV);
                                closeInit();
                            })
                    .setNegativeButton(
                            R.string.close,
                            (d, v) -> {
                                listenerRequest.onCloseFileFromDateAML(currentOpenedFile.getPath());
                                removeFileFromTmpMainEV(listPathFiles.get(POCV));
                                listPathFiles.remove(POCV);
                                tabLayout.removeTabAt(POCV);
                                closeInit();
                            })
                    .show();
        } else {
            listenerRequest.onCloseFileFromDateAML(currentOpenedFile.getPath());
            removeFileFromTmpMainEV(listPathFiles.get(POCV));
            listPathFiles.remove(POCV);
            tabLayout.removeTabAt(POCV);
            closeInit();
        }
    }

    private void closeInit() {
        if (listPathFiles.size() == 0) {
            currentOpenedFile = null;
            listenerRequest.onSwitchEditorEnabled(false);
            viewFlipper.removeAllViews();
        }
    }

    private void restoreContent() {
        if (bundle == null) return;
        if (tmpListOpenedFiles.size() == 0) return;
        for (var pair : tmpListOpenedFiles) pair.second.restoreContent();

        int p = 0;
        for (var path : listPathFiles) {
            String currentPath = bundle.getString("currentPath");
            if (currentPath != null && currentPath.equals(path)) {
                tabLayout.selectTab(tabLayout.getTabAt(p));
                break;
            }
            p++;
        }
        if (bundle.getBoolean("assistView")) {
            currentOpenedFile.flipToAnotherView();
            listenerRequest.onSwitchEditor(currentOpenedFile.isDesignEnabled());
        }
        ContentViewModel.clear();
    }

    public void saveContent(Bundle bundle) {
        if (bundle == null) return;
        if (tmpListOpenedFiles.size() == 0) return;
        for (var pair : tmpListOpenedFiles) pair.second.saveContent();
        bundle.putString("currentPath", currentOpenedFile.getPath());
        bundle.putBoolean("assistView", currentOpenedFile.isDesignEnabled());
    }

    public void refreshCurrentDesign() {
        if (currentOpenedFile != null) currentOpenedFile.refreshDesign();
    }

    public void undo() {
        if (currentOpenedFile != null) currentOpenedFile.undo();
        undoRedoManager();
    }

    public void redo() {
        if (currentOpenedFile != null) currentOpenedFile.redo();
        undoRedoManager();
    }

    public void icMoreSettings() {
        Toast.makeText(C, "soon", Toast.LENGTH_SHORT).show();
    }

    public void switchEditor() {
        save();
        currentOpenedFile.flipToAnotherView();
        listenerRequest.onSwitchEditor(currentOpenedFile.isDesignEnabled());
    }

    public void showInExplorer() {
        Toast.makeText(C, "soon", Toast.LENGTH_SHORT).show();
    }

    public void quickCode(View v) {
        Toast.makeText(C, "soon", Toast.LENGTH_SHORT).show();
    }

    // interface implements
    public void setOnRequest(OnRequest listener) {
        this.listenerRequest = listener;
    }

    public interface OnRequest {
        public void onCloseDrawer();

        public void onCloseFileFromDateAML(String path);

        public void onSavedEnabled(boolean enabled);

        public void onSwitchEditorEnabled(boolean enabled);

        public void onSwitchEditor(boolean assistView);

        public void onUndoEnabled(boolean enabled);

        public void onRedoEnabled(boolean enabled);

        public void onFullScreenNeeded(boolean setFullScreen);
    }
}
