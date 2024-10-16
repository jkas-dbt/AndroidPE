package jkas.androidpe.fragments.projectEditor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import jkas.androidpe.activities.CodeEditorActivity;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.resources.R;
import jkas.androidpe.databinding.FragmentPeExplorerBinding;
import jkas.androidpe.explorer.ExplorerView;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class ExplorerFragment extends Fragment {
    private final String SRC = "Explorer";
    private FragmentActivity C;
    private FragmentPeExplorerBinding binding;
    private ExplorerView EV;
    private OnRequestListener listenerRequest;
    private String finalPath;

    @Override
    public View onCreateView(
            LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        C = getActivity();
        EV = new ExplorerView(C);
        loadListener();
        if (_savedInstanceState != null) finalPath = _savedInstanceState.getString("finalPath");
        binding = FragmentPeExplorerBinding.inflate(_inflater);
        return (View) binding.getRoot();
    }

    public boolean onBackPressed() {
        return EV.goBack();
    }

    @Override
    @MainThread
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("finalPath", finalPath);
    }

    @Override
    @MainThread
    @CallSuper
    public void onResume() {
        super.onResume();
        preReload();
        event();
        init();
    }

    private void preReload() {
        binding.linExplorer.removeAllViews();
        binding.linExplorer.addView(EV.getViewExplorer());
    }

    private void init() {
        if (isVisible() && binding != null && finalPath != null) EV.initPath(finalPath);
        else refresh();
    }

    public void refresh() {
        if (isVisible() && binding != null) {
            try {
                if (DataRefManager.getInstance().currentAndroidModule == null)
                    EV.initPath(DataRefManager.getInstance().P.getAbsolutePath());
                else
                    EV.initPath(
                            DataRefManager.getInstance()
                                    .currentAndroidModule
                                    .getProjectAbsolutePath());
            } catch (Exception e) {
                Logger.error(
                        SRC,
                        getString(R.string.cant_load_data),
                        getString(R.string.please_report_it_to_developer),
                        e.getMessage());
            }
        }
    }

    private void loadListener() {
        EV.setOnAnyChangeDetected(
                new ExplorerView.OnAnyChangeDetected() {

                    public void onFileClick(View view, String path) {
                        DataRefManager.getInstance().filePathSelected = path;
                        CodeUtil.startActivity(C, CodeEditorActivity.class);
                    }

                    @Override
                    public void onPathChanged(String path) {
                        C.runOnUiThread(
                                () -> {
                                    finalPath = path;
                                    binding.tvPath.setText(path.replace("/", " ⟩ "));
                                    listenerRequest.onPathChanged(path);
                                });
                    }
                });
        EV.setOnRequestListener(() -> listenerRequest.onRefresh());
    }

    private void event() {
        binding.btnRootModule.setOnClickListener(
                (v) -> {
                    if (DataRefManager.getInstance().currentAndroidModule == null) {
                        currentAndroidMmoduleNull();
                        return;
                    }
                    loadData(
                            DataRefManager.getInstance()
                                    .currentAndroidModule
                                    .getProjectAbsolutePath());
                });

        binding.btnJavaKotlin.setOnClickListener(
                (v) -> {
                    if (DataRefManager.getInstance().currentAndroidModule == null) {
                        currentAndroidMmoduleNull();
                        return;
                    }
                    final String path =
                            DataRefManager.getInstance()
                                            .currentAndroidModule
                                            .getProjectAbsolutePath()
                                    + "/src/main/";
                    if (Files.isDirectory(path + "java")) loadData(path + "java");
                    else if (Files.isDirectory(path + "kotlin")) loadData(path + "kotlin");
                    else if (Files.isDirectory(path + "cpp")) loadData(path + "cpp");
                    else loadData(path + "java");
                });

        binding.btnRes.setOnClickListener(
                (v) -> {
                    if (DataRefManager.getInstance().currentAndroidModule == null) {
                        currentAndroidMmoduleNull();
                        return;
                    }
                    loadData(
                            DataRefManager.getInstance()
                                            .currentAndroidModule
                                            .getProjectAbsolutePath()
                                    + "/src/main/res");
                });

        binding.btnLayout.setOnClickListener(
                (v) -> {
                    if (DataRefManager.getInstance().currentAndroidModule == null) {
                        currentAndroidMmoduleNull();
                        return;
                    }
                    loadData(
                            DataRefManager.getInstance()
                                            .currentAndroidModule
                                            .getProjectAbsolutePath()
                                    + "/src/main/res/layout");
                });

        binding.btnValues.setOnClickListener(
                (v) -> {
                    if (DataRefManager.getInstance().currentAndroidModule == null) {
                        currentAndroidMmoduleNull();
                        return;
                    }
                    loadData(
                            DataRefManager.getInstance()
                                            .currentAndroidModule
                                            .getProjectAbsolutePath()
                                    + "/src/main/res/values");
                });

        binding.btnAssets.setOnClickListener(
                (v) -> {
                    if (DataRefManager.getInstance().currentAndroidModule == null) {
                        currentAndroidMmoduleNull();
                        return;
                    }
                    loadData(
                            DataRefManager.getInstance()
                                            .currentAndroidModule
                                            .getProjectAbsolutePath()
                                    + "/src/main/assets");
                });

        binding.btnLibs.setOnClickListener(
                (v) -> {
                    if (DataRefManager.getInstance().currentAndroidModule == null) {
                        currentAndroidMmoduleNull();
                        return;
                    }
                    loadData(
                            DataRefManager.getInstance()
                                            .currentAndroidModule
                                            .getProjectAbsolutePath()
                                    + "/libs");
                });
    }

    private void currentAndroidMmoduleNull() {
        Logger.warn(
                SRC,
                getString(R.string.cant_load_data),
                "Internal error : curruntAndroidModule == null Or No any module has been detected",
                getString(R.string.if_you_think_gradle_file_not_contains_any_error)
                        + ", "
                        + getString(R.string.please_report_it_to_developer));
    }

    private void loadData(String path) {
        if (Files.isDirectory(path)) EV.initPath(path);
        else
            DialogBuilder.getDialogBuilder(
                            C,
                            getString(R.string.warning),
                            getString(R.string.folder_not_found_error))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(
                            R.string.create,
                            (d, i) -> {
                                Files.makeDir(path);
                                Logger.success(SRC, getString(R.string.folder_created), path);
                                EV.initPath(path);
                            })
                    .show();
    }

    public void setOnRequestListener(OnRequestListener listener) {
        this.listenerRequest = listener;
    }

    public interface OnRequestListener {
        public void onRefresh();

        public void onPathChanged(String path);
    }
}
