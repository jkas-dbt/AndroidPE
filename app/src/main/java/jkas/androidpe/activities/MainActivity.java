package jkas.androidpe.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import com.google.android.material.elevation.SurfaceColors;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.databinding.ActivityMainBinding;
import jkas.androidpe.projectAnalyzer.NewProject;
import jkas.androidpe.projectAnalyzer.ProjectView;
import jkas.androidpe.resources.R;
import jkas.androidpe.explorer.SelectFF;
import jkas.androidpe.project.Project;
import jkas.androidpe.projectAnalyzer.SearchingProjects;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class MainActivity extends AppCompatActivity {
    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;

    private NewProject newProject;
    private AppCompatActivity C = this;
    private ActivityMainBinding binding;
    private SearchingProjects SP;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) loadProjectView();
                    });

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED})
    public @interface PermissionStatus {}

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface attr {
        String name();
    }

    @PermissionStatus
    public static int getPermissionStatus(
            AppCompatActivity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName)
                        != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, androidPermissionName)
                        != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, androidPermissionName)) {
                return BLOCKED_OR_NEVER_ASKED;
            } else return DENIED;
        } else return GRANTED;
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadProjectView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.initFromZero();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 || requestCode == 4343) loadProjectView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initInstances();
        initTheme();
        loadListeners();
        allOnClick();
    }

    private void loadProjectView() {
        if (!checkIfPermissionGranted()) {
            binding.tvStatusPermission.setText("DENIED");
            if (getPermissionStatus(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == BLOCKED_OR_NEVER_ASKED) {
                binding.tvStatusPermission.setText("DENIED & NEVER ASKED");
                new MaterialAlertDialogBuilder(C)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.permission_denied_blocked)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(
                                R.string.yes,
                                (v1, v2) -> {
                                    CodeUtil.showApplicationSettingsDetails(C);
                                })
                        .show();
            }
            binding.viewSwitcher.setDisplayedChild(0);
            return;
        }
        binding.tvStatusPermission.setText("GRANTED");
        binding.gridLayoutListProjects.removeAllViews();
        SP.load();
    }

    private boolean checkIfPermissionGranted() {
        if (!CodeUtil.checkIfPermissionAccessStorageGranted(C)) {
            showPermissionError();
            return false;
        } else if (!CodeUtil.checkIfPermissionAccessStorageManagerGranted()) {
            showPermissionManagerError();
            return false;
        }
        final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATION_PERMISSION_REQUEST_CODE);
        }
        return true;
    }

    private void showPermissionError() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            loadProjectView();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void showPermissionManagerError() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new MaterialAlertDialogBuilder(C)
                    .setTitle(getString(R.string.permission))
                    .setMessage(getString(R.string.request_permission_manager))
                    .setNegativeButton(getString(android.R.string.cancel), null)
                    .setPositiveButton(
                            getString(R.string.open),
                            (v1, v2) -> CodeUtil.requestPermissionManager(this, 4343))
                    .show();
        }
    }

    private void allOnClick() {
        binding.icSettings.setOnClickListener(
                (bs) -> CodeUtil.startActivity(C, PreferencesActivity.class));

        binding.icOpenProject.setOnClickListener(
                (bop) -> {
                    new MaterialAlertDialogBuilder(C)
                            .setTitle(getString(R.string.warning))
                            .setMessage(getString(R.string.opening_project_description_1))
                            .setNegativeButton(getString(android.R.string.cancel), null)
                            .setPositiveButton(
                                    getString(R.string.open),
                                    (v1, v2) -> {
                                        final SelectFF selector = new SelectFF(C);
                                        selector.setSelectorType(SelectFF.FOLDER_SELECTOR);
                                        selector.setSelectMode(SelectFF.SELECT_SINGLE);
                                        selector.lookRoot(Files.getExternalStorageDir());
                                        selector.setOnSaveListener((l) -> openProject(l.get(0)));
                                        selector.showView();
                                        selector.loadData();
                                    })
                            .show();
                });

        binding.fab.setOnClickListener(
                (bf) -> {
                    if (checkIfPermissionGranted()) newProject.show();
                });
    }

    private void openProject(String data) {
        String p2p = data.replace(Files.getExternalStorageDir() + File.separator, "");
        String folderName = null;
        if (p2p.contains(File.separator)) {
            folderName = p2p.substring(p2p.lastIndexOf(File.separator) + 1);
            p2p = p2p.substring(0, p2p.lastIndexOf(File.separator));
        } else {
            folderName = p2p;
            p2p = null;
        }

        final Project p = new Project(p2p, folderName);
        p.setPackageName("...");
        p.setPackageName(SearchingProjects.tryFindPkg(p));
        new ProjectView(C, p);
        DataRefManager.getInstance().P = p;
        CodeUtil.startActivity(C, ProjectEditorActivity.class);
    }

    private void loadListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> loadProjectView());
        newProject.setOnCreateProjectListener(
                (path) -> {
                    newProject.dismiss();
                    openProject(path);
                    loadProjectView();
                });
        SP.setOnProjectFound(
                new SearchingProjects.OnProjectFoundListener() {
                    private boolean found = false;

                    @Override
                    public void onFound(View view) {
                        binding.gridLayoutListProjects.addView(view);
                        if (!found) {
                            binding.viewSwitcher.setDisplayedChild(1);
                            found = true;
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (!found) binding.viewSwitcher.setDisplayedChild(0);
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void initTheme() {
        int color =
                CodeUtil.isTheSystemThemeDark(C)
                        ? getColor(R.color.roseDark)
                        : getColor(R.color.roseLight);
        color = MaterialColors.harmonizeWithPrimary(C, color);
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        binding.fab.setBackgroundTintList(colorStateList);

        viewGradientTop();
        viewGradientBottom();
    }

    private void viewGradientTop() {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColors(
                new int[] {ResourcesValuesFixer.getColor(C, "?colorSurface"), Color.TRANSPARENT});
        gradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        binding.viewGradientTop.setBackground(gradient);
    }

    private void viewGradientBottom() {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColors(
                new int[] {Color.TRANSPARENT, ResourcesValuesFixer.getColor(C, "?colorSurface")});
        gradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        binding.viewGradientBottom.setBackground(gradient);
    }

    private void initInstances() {
        SP = new SearchingProjects(C);
        newProject = new NewProject(C);

        if (CodeUtil.isScreenLandscape(this)) {
            GradientDrawable gradient = new GradientDrawable();
            gradient.setColor(SurfaceColors.SURFACE_3.getColor(this));
            gradient.setCornerRadii(new float[] {0, 0, 43, 43, 43, 43, 0, 0});
            binding.navRailView.setBackground(gradient);
            binding.navRailView.setOnItemSelectedListener(
                    item -> {
                        return false;
                    });
        } else {
            CodeUtil.setNavigationBarColor(this, SurfaceColors.SURFACE_3.getColor(this));
            binding.bottomAppBar.setBackgroundColor(SurfaceColors.SURFACE_3.getColor(this));
            binding.bottomAppBar
                    .getMenu()
                    .getItem(0)
                    .setIconTintList(
                            ColorStateList.valueOf(
                                    ResourcesValuesFixer.getColor(C, "?colorPrimary")));
        }
    }
}
