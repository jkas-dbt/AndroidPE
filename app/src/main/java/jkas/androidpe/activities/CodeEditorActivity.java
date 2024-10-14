package jkas.androidpe.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import java.util.ArrayList;
import jkas.androidpe.codeEditor.viewModels.ContentViewModel;
import jkas.androidpe.logger.LoggerRes;
import jkas.androidpe.resources.R;
import jkas.androidpe.mainEditorView.FilesManager;
import jkas.androidpe.databinding.ActivityCodeEditorBinding;
import jkas.androidpe.explorer.CodeEditorExplorer;
import jkas.androidpe.resourcesUtils.dataInitializer.*;
import jkas.androidpe.resourcesUtils.dialog.DialogBottomSheetAttrModifier;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.dialog.DialogProgressIndeterminate;
import jkas.androidpe.utils.ResReferences;
import jkas.codeUtil.Files;
import rkb.datasaver.AMLProjectsData;
import rkb.datasaver.RKBDataAppSettings;

/**
 * @author JKas
 */
public class CodeEditorActivity extends AppCompatActivity {
    private Context C = this;
    private ActivityCodeEditorBinding binding;
    private CodeEditorExplorer CEE; // explorer for code editor
    private FilesManager filesManager;
    private Bundle savedInstanceState;
    private AlertDialog alert;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoSaveRunnable =
            () -> {
                if (filesManager != null) filesManager.save();
                setAutoCodeSave();
            };

    @Override
    @CallSuper
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        filesManager.saveContent(bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(autoSaveRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(autoSaveRunnable);
    }

    @Override
    @MainThread
    public void onBackPressed() {
        if (binding.getRoot().isOpen()) {
            binding.getRoot().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
            binding.getRoot().close();
        } else if (filesManager.isThereAnyUnsavedContent()) {
            DialogBuilder.getDialogBuilder(
                            this,
                            getString(R.string.warning),
                            getString(R.string.warning_unsaved_content))
                    .setPositiveButton(R.string.save, (d, v) -> filesManager.save())
                    .setNegativeButton(R.string.exit, (d, v) -> super.onBackPressed())
                    .show();
        } else {
            DialogBuilder.getDialogBuilder(
                            this,
                            getString(R.string.warning),
                            getString(R.string.warning_close_code_editor))
                    .setPositiveButton(R.string.close, (d, v) -> super.onBackPressed())
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        binding = ActivityCodeEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        alert = DialogProgressIndeterminate.getAlertDialog(C);
        DialogBottomSheetAttrModifier.instance = null;
        instances();
        loadListener();
        loadDefaultValues();
        events();
        showProgressDialog();
        setAutoCodeSave();
        new Handler(Looper.getMainLooper())
                .postDelayed(
                        () -> {
                            ResReferences.init(this);
                            LoggerRes.initFromZero();
                            LoggerRes.addLogListener(() -> DataResInitializer.init(C));
                            loadData();
                        },
                        43);
    }

    public void setAutoCodeSave() {
        String value = RKBDataAppSettings.getCodeAutoSavedTime();
        if (!value.equals(RKBDataAppSettings.AUTO_SAVE_NONE)) {
            handler.postDelayed(autoSaveRunnable, Integer.parseInt(value));
        }
    }

    private void showProgressDialog() {
        if (savedInstanceState == null) alert.show();
    }

    private void events() {
        binding.toolBar.setOnMenuItemClickListener(
                item -> {
                    int id = item.getItemId();
                    if (id == jkas.androidpe.R.id.undo) filesManager.undo();
                    else if (id == jkas.androidpe.R.id.redo) filesManager.redo();
                    else if (id == jkas.androidpe.R.id.design) filesManager.switchEditor();
                    else if (id == jkas.androidpe.R.id.show_in_explorer)
                        filesManager.showInExplorer();
                    else if (id == jkas.androidpe.R.id.show_explorer) binding.getRoot().open();
                    else if (id == jkas.androidpe.R.id.save) {
                        filesManager.save();
                        Toast.makeText(C, R.string.saved, Toast.LENGTH_SHORT).show();
                    }
                    return false;
                });

        binding.getRoot()
                .setOnDragListener(
                        (v, e) -> {
                            switch (e.getAction()) {
                                case DragEvent.ACTION_DROP:
                                    filesManager.refreshCurrentDesign();
                            }
                            return true;
                        });
    }

    private void loadData() {
        final ArrayList<String> listPath = new ArrayList<>();
        AMLProjectsData.addProjectIfNotAvialable(DataRefManager.getInstance().P.getAbsolutePath());
        AMLProjectsData.appendNewOpenedFile(DataRefManager.getInstance().filePathSelected);
        listPath.addAll(AMLProjectsData.getOpenedFiles());

        boolean verif = false;
        int position = 0;
        for (String path : listPath) {
            if (!Files.isFile(path)) {
                AMLProjectsData.fileClosed(path);
                continue;
            }
            if (path.equals(DataRefManager.getInstance().filePathSelected)) {
                verif = true;
                break;
            }
            position++;
        }
        if (verif) listPath.remove(position);
        if (DataRefManager.getInstance().filePathSelected != null) {
            listPath.add(0, DataRefManager.getInstance().filePathSelected);
        }

        DataRefManager.getInstance().filePathSelected = null;
        filesManager.parseListOpenFile(listPath);
        alert.dismiss();
    }

    private void loadDefaultValues() {
        binding.leftDrawer.addView(CEE.getView());
        binding.toolBar.setSubtitle(DataRefManager.getInstance().P.getFolderName());

        binding.toolBar.getMenu().findItem(jkas.androidpe.R.id.undo).setVisible(false);
        binding.toolBar.getMenu().findItem(jkas.androidpe.R.id.redo).setVisible(false);
        binding.toolBar.getMenu().findItem(jkas.androidpe.R.id.save).setVisible(false);
        binding.toolBar.getMenu().findItem(jkas.androidpe.R.id.design).setVisible(false);
    }

    private void loadListener() {
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this, binding.getRoot(), binding.toolBar, R.string.open, R.string.close);
        binding.getRoot().addDrawerListener(toggle);
        toggle.syncState();

        binding.viewFlipper.setAlpha(1.0f);
        binding.getRoot()
                .addDrawerListener(
                        new DrawerLayout.SimpleDrawerListener() {
                            @Override
                            public void onDrawerSlide(View drawerView, float slideOffset) {
                                super.onDrawerSlide(drawerView, slideOffset);
                                float moveFactor = (drawerView.getWidth() * slideOffset) * 0.5f;
                                if (drawerView.getId() == jkas.androidpe.R.id.left_drawer) {
                                    binding.linContainer.setTranslationX(moveFactor);
                                    float maxDrawerWidth = drawerView.getWidth();
                                    float width = binding.viewFlipper.getWidth();
                                    float alphaValue = 1 - (slideOffset * (width / maxDrawerWidth));
                                    binding.viewFlipper.setAlpha(alphaValue);
                                    if (alphaValue > 0.3)
                                        binding.viewFlipper.setVisibility(View.VISIBLE);
                                    else {
                                        alphaValue = 0;
                                        binding.viewFlipper.setVisibility(View.GONE);
                                    }
                                    if (alphaValue > 0.7) binding.viewFlipper.setEnabled(true);
                                }
                            }

                            @Override
                            public void onDrawerOpened(View arg0) {
                                super.onDrawerOpened(arg0);
                                binding.viewFlipper.setEnabled(false);
                                binding.viewFlipper.setVisibility(View.GONE);
                            }

                            @Override
                            public void onDrawerClosed(View arg0) {
                                super.onDrawerClosed(arg0);
                                binding.viewFlipper.setAlpha(1.0f);
                            }
                        });

        CEE.setOnAnyEventListener(
                new CodeEditorExplorer.OnAnyEventListener() {
                    @Override
                    public void onClick(String path) {
                        AMLProjectsData.appendNewOpenedFile(path);
                        filesManager.setNewFileClicked(path);
                    }

                    @Override
                    public void onRename(String oldPath, String newPath) {
                        filesManager.setRenamePath(oldPath, newPath);
                    }

                    @Override
                    public void onDelete(String path) {
                        filesManager.setPathDeleted(path);
                    }
                });

        filesManager.setOnRequest(
                new FilesManager.OnRequest() {

                    @Override
                    public void onCloseDrawer() {
                        binding.getRoot().close();
                    }

                    @Override
                    public void onCloseFileFromDateAML(String path) {
                        AMLProjectsData.fileClosed(path);
                    }

                    @Override
                    public void onSavedEnabled(boolean enabled) {
                        MenuItem item =
                                binding.toolBar.getMenu().findItem(jkas.androidpe.R.id.save);
                        if (item != null) item.setVisible(enabled);
                    }

                    @Override
                    public void onSwitchEditorEnabled(boolean enabled) {
                        MenuItem item =
                                binding.toolBar.getMenu().findItem(jkas.androidpe.R.id.design);
                        if (item != null) item.setVisible(enabled);
                    }

                    @Override
                    public void onSwitchEditor(boolean assistView) {
                        MenuItem item =
                                binding.toolBar.getMenu().findItem(jkas.androidpe.R.id.design);
                        if (assistView) item.setIcon(R.drawable.ic_pencil);
                        else item.setIcon(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onUndoEnabled(boolean enabled) {
                        MenuItem item =
                                binding.toolBar.getMenu().findItem(jkas.androidpe.R.id.undo);
                        if (item != null) item.setVisible(enabled);
                    }

                    @Override
                    public void onRedoEnabled(boolean enabled) {
                        MenuItem item =
                                binding.toolBar.getMenu().findItem(jkas.androidpe.R.id.redo);
                        if (item != null) item.setVisible(enabled);
                    }

                    @Override
                    public void onFullScreenNeeded(boolean setFullScreen) {
                        binding.linForFullScreen.setVisibility(
                                setFullScreen ? View.GONE : View.VISIBLE);
                        binding.getRoot()
                                .setDrawerLockMode(
                                        setFullScreen
                                                ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                                                : DrawerLayout.LOCK_MODE_UNDEFINED);
                    }
                });
    }

    private void instances() {
        CEE = new CodeEditorExplorer(C);
        filesManager = new FilesManager(this, savedInstanceState);
        filesManager.bindInterViews(binding.tabLayout, binding.viewFlipper, binding.tvInfo);
    }
}
