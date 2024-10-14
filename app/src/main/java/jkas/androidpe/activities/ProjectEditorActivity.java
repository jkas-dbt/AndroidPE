package jkas.androidpe.activities;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import jkas.androidpe.adapter.ViewPagersAdapter;
import jkas.androidpe.codeEditor.viewModels.ContentViewModel;
import jkas.androidpe.initializer.ProjectInitializer;
import jkas.androidpe.databinding.LayoutInfosViewBinding;
import jkas.androidpe.explorer.dialog.DialogNewModuleCreator;
import jkas.androidpe.explorer.dialog.DialogSelectModule;
import jkas.androidpe.logger.LogMsg;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.logger.LoggerLayoutUI;
import jkas.androidpe.fragments.projectEditor.ExplorerFragment;
import jkas.androidpe.fragments.projectEditor.MainFragment;
import jkas.androidpe.fragments.projectEditor.ManagerFragment;
import jkas.androidpe.project.Project;
import jkas.androidpe.projectUtils.utils.ProjectsUtils;
import jkas.androidpe.resources.R;
import jkas.androidpe.databinding.ActivityProjectEditorBinding;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.Images;

/**
 * @author JKas
 */
public class ProjectEditorActivity extends AppCompatActivity {
    private AppCompatActivity C = this;
    private ActivityProjectEditorBinding binding;
    private ArrayList<Fragment> listFrag = new ArrayList<>();
    private MainFragment MF;
    private ExplorerFragment EF;
    private ManagerFragment MGF;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int statusBottomAppBar = BottomSheetBehavior.STATE_HIDDEN;
    private long firstBackTime;
    private Logger.LogListener logListener = logMsg -> runOnUiThread(() -> appendLog(logMsg));

    @Override
    protected void onResume() {
        super.onResume();
        Logger.addLogListener(logListener);
        LoggerLayoutUI.clear();
        ContentViewModel.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.removeLogListener(logListener);
    }

    @Override
    @CallSuper
    protected void onSaveInstanceState(Bundle savedIS) {
        super.onSaveInstanceState(savedIS);
        savedIS.putString("projectDir", DataRefManager.getInstance().P.getProjectDir());
        savedIS.putString("folderName", DataRefManager.getInstance().P.getFolderName());
        savedIS.putString("packageName", DataRefManager.getInstance().P.getPackageName());
        savedIS.putString("iconPath", DataRefManager.getInstance().P.getIconPath());
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else if (binding.tabLayout.getSelectedTabPosition() != 1) {
            if (System.currentTimeMillis() - firstBackTime > 2000) {
                Toast.makeText(C, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT)
                        .show();
                firstBackTime = System.currentTimeMillis();
                return;
            }
            cleanAll();
            super.onBackPressed();
        } else if (binding.tabLayout.getSelectedTabPosition() == 1)
            if (!EF.onBackPressed()) binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0));
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        loadListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProjectEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Logger.addLogListener(logListener);

        theme();
        iniProject(savedInstanceState);
        if (!checkIfProjectValide()) return;
        loadBottomSheet();
        loadData();
        events();
    }

    private void cleanAll() {
        DataRefManager.getInstance().P = null;
        DataRefManager.getInstance().currentAndroidModule = null;
        DataRefManager.getInstance().listAndroidModule.clear();
    }

    private void events() {
        binding.viewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int i) {
                        super.onPageSelected(i);
                    }
                });

        binding.btnAllModule.setOnClickListener(
                (v) -> {
                    new DialogSelectModule(C, DialogSelectModule.SINGLE_SELECT_MODE)
                            .setOnModuleSelected(
                                    (modulePosition) -> {
                                        binding.btnAllModule.setText("('~') : Modules");
                                        DataRefManager.getInstance().currentAndroidModule =
                                                DataRefManager.getInstance()
                                                        .listAndroidModule
                                                        .get(modulePosition);
                                        initDataModules();
                                        refreshFrag();
                                    });
                });
        binding.btnSync.setOnClickListener((v) -> refreshAllModules());
        binding.btnNewModule.setOnClickListener(
                (v) -> {
                    new DialogNewModuleCreator(C, DataRefManager.getInstance().P.getAbsolutePath())
                            .setOnSaveListener(() -> refreshAllModules());
                });
    }

    private void initDataModules() {
        if (DataRefManager.getInstance().currentAndroidModule == null)
            if (DataRefManager.getInstance().listAndroidModule.size() > 0)
                DataRefManager.getInstance().currentAndroidModule =
                        DataRefManager.getInstance().listAndroidModule.get(0);
            else return;

        String path = DataRefManager.getInstance().currentAndroidModule.getPath();
        binding.btnAllModule.setText(path);
        DataRefManager.getInstance().setCurrentModuleRes(path);
    }

    private void refreshFrag() {
        MF.refresh();
        EF.refresh();
        MGF.refresh();
    }

    private void refreshAllModules() {
        Logger.addLogListener(logListener);
        loadModulesFirst();
        initDataModules();
        refreshFrag();
    }

    private boolean checkIfProjectValide() {
        if (ProjectsUtils.getProjectStatus(DataRefManager.getInstance().P) == ProjectsUtils.NAP) {
            new MaterialAlertDialogBuilder(C)
                    .setCancelable(false)
                    .setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.opening_project_error_nap))
                    .setPositiveButton(
                            getString(android.R.string.ok),
                            (v1, v2) -> {
                                finish();
                            })
                    .show();
            return false;
        }
        binding.tvFolderName.setText(DataRefManager.getInstance().P.getFolderName());
        binding.tvPackageName.setText(DataRefManager.getInstance().P.getPackageName());
        return true;
    }

    private void loadBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
    }

    private void loadData() {
        binding.tvFolderName.setText(DataRefManager.getInstance().P.getFolderName());
        binding.tvPackageName.setText(DataRefManager.getInstance().P.getPackageName());
        Images.setImageFromDir(DataRefManager.getInstance().P.getIconPath(), binding.icIcon);

        MF = new MainFragment();
        EF = new ExplorerFragment();
        MGF = new ManagerFragment();

        loadModulesFirst();
        loadListener();
        initDataModules();

        final ViewPagersAdapter adapter =
                new ViewPagersAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(MF);
        adapter.addFragment(EF);
        adapter.addFragment(MGF);
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {}).attach();

        iniTab();
    }

    private void loadListener() {
        EF.setOnRequestListener(
                new ExplorerFragment.OnRequestListener() {

                    @Override
                    public void onRefresh() {
                        if (!Files.isDirectory(DataRefManager.getInstance().P.getAbsolutePath())) {
                            DialogBuilder.getDialogBuilder(
                                            C,
                                            C.getString(R.string.warning),
                                            C.getString(R.string.msg_error_projects_not_found))
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            android.R.string.ok,
                                            (d, v) -> {
                                                finish();
                                            })
                                    .show();
                            return;
                        }
                        refreshAllModules();
                    }

                    @Override
                    public void onPathChanged(String path) {
                        for (var module : DataRefManager.getInstance().listAndroidModule) {
                            if (module.getProjectAbsolutePath().equals(path)) {
                                DataRefManager.getInstance().currentAndroidModule = module;
                                binding.btnAllModule.setText(module.getPath());
                                return;
                            }
                        }
                    }
                });
    }

    private void loadModulesFirst() {
        new ProjectInitializer(C).iniProject();
    }

    private void appendLog(LogMsg logMsg) {
        LayoutInfosViewBinding bind = LayoutInfosViewBinding.inflate(getLayoutInflater());
        final View v = bind.getRoot();
        bind.tvSrc.setText(logMsg.src);
        bind.tvLevel.setText(logMsg.level);
        bind.tvMsg.setText(logMsg.message);
        themeInfosView(bind.tvLevel, logMsg.resColor);
        v.setLayoutParams(CodeUtil.getLayoutParamsMW(8));
        binding.linBottomSheet.addView(v, 0);
        binding.tvInfoBottomSheet.setTypeface(Typeface.DEFAULT_BOLD);
        binding.tvInfoBottomSheet.setText(
                "info : "
                        + Logger.info
                        + "  |  "
                        + "Warn : "
                        + Logger.warn
                        + "  |  "
                        + "Error : "
                        + Logger.error
                        + "  |  "
                        + "Success : "
                        + Logger.success);
    }

    private void iniProject(Bundle savedIS) {
        if (savedIS != null) {
            binding.linBottomSheet.removeAllViews();
            DataRefManager.getInstance().P =
                    new Project(savedIS.getString("projectDir"), savedIS.getString("folderName"));
            DataRefManager.getInstance().P.setPackageName(savedIS.getString("packageName"));
            DataRefManager.getInstance().P.setIconPath(savedIS.getString("iconPath"));
        }
        Logger.info = 0;
        Logger.warn = 0;
        Logger.error = 0;
        Logger.success = 0;
    }

    private void iniTab() {
        binding.tabLayout.getTabAt(0).setIcon(getDrawable(R.drawable.ic_developer_board));
        binding.tabLayout.getTabAt(1).setIcon(getDrawable(R.drawable.ic_folder));
        binding.tabLayout.getTabAt(2).setIcon(getDrawable(R.drawable.ic_package));
    }

    private void theme() {
        binding.bottomSheet.setBackgroundColor(SurfaceColors.SURFACE_0.getColor(C));
        CodeUtil.setNavigationBarColor(this, SurfaceColors.SURFACE_0.getColor(C));

        setLinBar();
        setLeftBar();
    }

    private void setLinBar() {
        if (CodeUtil.isScreenLandscape(C)) return;
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(SurfaceColors.SURFACE_2.getColor(C));
        binding.linBar.setBackground(gradient);
        binding.linBar.setElevation(12f);
        CodeUtil.setNotificationBarColor(this, SurfaceColors.SURFACE_2.getColor(C));
    }

    private void setLeftBar() {
        if (!CodeUtil.isScreenLandscape(C)) return;
        float radius = 76.43f;
        float[] corners = new float[] {0, 0, radius, radius, radius, radius, 0, 0};
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(SurfaceColors.SURFACE_2.getColor(C));
        gradient.setCornerRadii(corners);
        binding.linLeftBar.setBackground(gradient);
    }

    private void themeInfosView(View v, int resColor) {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(getColor(resColor));
        gradient.setCornerRadius(43f);
        v.setBackground(gradient);
    }
}
