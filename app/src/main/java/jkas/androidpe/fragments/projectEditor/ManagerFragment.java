package jkas.androidpe.fragments.projectEditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentViewHolder;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import jkas.androidpe.databinding.FragmentPeManagerBinding;
import jkas.androidpe.dialog.StringTranslaterDialog;
import jkas.androidpe.explorer.dialog.DialogSelectModule;
import jkas.androidpe.fragments.projectEditor.manager.ActivitiesPerformer;
import jkas.androidpe.fragments.projectEditor.manager.PermissionsPerformer;
import jkas.androidpe.fragments.projectEditor.manager.ServicesReceiversProvidersPerformer;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class ManagerFragment extends Fragment {
    private final String SRC = "Project Manager";
    private FragmentActivity C;
    private FragmentPeManagerBinding binding;

    private ActivitiesPerformer activityPerformer;
    private PermissionsPerformer permissionPerformer;
    private ServicesReceiversProvidersPerformer servicesformer;
    private ServicesReceiversProvidersPerformer receiversformer;
    private ServicesReceiversProvidersPerformer providersformer;
    private StringTranslaterDialog dialogStringTranslater;
    private FragmentViewHolder viewModel;
    private Logger.LogListener logListener = logMsg -> C.runOnUiThread(() -> onListener());

    @Override
    @MainThread
    @CallSuper
    public void onPause() {
        super.onPause();
        Logger.removeLogListener(logListener);
    }

    @Override
    @MainThread
    @CallSuper
    public void onResume() {
        super.onResume();
        Logger.addLogListener(logListener);
        loadData();
    }

    @Override
    public View onCreateView(
            LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        C = getActivity();
        binding = FragmentPeManagerBinding.inflate(_inflater);
        return (View) binding.getRoot();
    }

    private void loadFirst() {
        activityPerformer = new ActivitiesPerformer(C);
        permissionPerformer = new PermissionsPerformer(C);
        servicesformer =
                new ServicesReceiversProvidersPerformer(
                        C, ServicesReceiversProvidersPerformer.SERVICES_TAG_TYPE);
        receiversformer =
                new ServicesReceiversProvidersPerformer(
                        C, ServicesReceiversProvidersPerformer.RECEIVERS_TAG_TYPE);
        providersformer =
                new ServicesReceiversProvidersPerformer(
                        C, ServicesReceiversProvidersPerformer.PROVIDERS_TAG_TYPE);
        dialogStringTranslater = new StringTranslaterDialog(C);
    }

    private void onListener() {
        loadANOData();
    }

    public void refresh() {
        if (isVisible() && binding != null) loadData();
    }

    private void loadData() {
        binding.btnActivitiesAdded.setText("0");
        binding.btnPermissionsAdded.setText("0");
        binding.btnServicesAdded.setText("0");
        binding.btnReceiversAdded.setText("0");
        binding.btnProvidersAdded.setText("0");
        binding.btnShowAllRefModules.setText("0");

        if (DataRefManager.getInstance().currentAndroidModule == null) {
            setAllNullForCurrentModulesView();
            return;
        }

        String manifPath =
                DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath()
                        + ProjectsPathUtils.ANDROID_MANIFEST_PATH;
        if (!Files.isFile(manifPath)) return;
        loadFirst();
        loadCurrentModules();
        loadProjectManager();
    }

    private void setAllNullForCurrentModulesView() {
        binding.tvCurrentModules.setText(getString(R.string.no_module_is_selected));
        binding.btnShowAllRefModules.setText("('~') !!!");
        binding.btnAddProjectsDependencies.setEnabled(false);
        binding.btnAddProjectsDependencies.setAlpha(0.5f);
        binding.btnDependencies.setEnabled(false);
        binding.btnDependencies.setAlpha(0.5f);
    }

    private void setEnabledForCurrentModulesView() {
        binding.btnAddProjectsDependencies.setEnabled(true);
        binding.btnAddProjectsDependencies.setAlpha(1.0f);
        binding.btnDependencies.setEnabled(true);
        binding.btnDependencies.setAlpha(1.0f);
    }

    private void loadCurrentModules() {
        reference();
        activityAndOthersOnClick();
        loadANOData();
    }

    private void loadANOData() {
        binding.btnActivitiesAdded.setText("" + activityPerformer.getCount());
        binding.btnPermissionsAdded.setText("" + permissionPerformer.getCount());
        binding.btnServicesAdded.setText("" + servicesformer.getCount());
        binding.btnReceiversAdded.setText("" + receiversformer.getCount());
        binding.btnProvidersAdded.setText("" + providersformer.getCount());
    }

    private void activityAndOthersOnClick() {
        binding.cardActivities.setOnClickListener((r) -> activityPerformer.show());
        binding.cardPermissions.setOnClickListener((o) -> permissionPerformer.show());
        binding.cardServices.setOnClickListener((s) -> servicesformer.show());
        binding.cardReceivers.setOnClickListener((e) -> receiversformer.show());
        binding.cardProviders.setOnClickListener((t) -> providersformer.show());
        binding.cardIcones.setOnClickListener(
                (t) -> {
                    Toast.makeText(C, "soon", Toast.LENGTH_SHORT).show();
                });
        binding.cardStrings.setOnClickListener((str) -> dialogStringTranslater.show());
    }

    private void reference() {
        if (DataRefManager.getInstance().currentAndroidModule == null) {
            setAllNullForCurrentModulesView();
            return;
        }
        setEnabledForCurrentModulesView();
        if (DataRefManager.getInstance().currentAndroidModule.getRefToOthersModules().size() > 0)
            binding.tvCurrentModules.setText(getString(R.string.the_current_module_depend_others));
        else
            binding.tvCurrentModules.setText(
                    getString(R.string.the_current_module_does_not_depend_others));

        binding.btnShowAllRefModules.setText(
                getString(R.string.modules)
                        + " : "
                        + DataRefManager.getInstance()
                                .currentAndroidModule
                                .getRefToOthersModules()
                                .size());
    }

    private void showAllRefModules() {
        if (!(DataRefManager.getInstance().currentAndroidModule.getRefToOthersModules().size()
                > 0)) {
            Toast.makeText(
                            C,
                            getString(R.string.cant_load)
                                    + "\n"
                                    + getString(R.string.modules)
                                    + " : 0",
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String m =
                "["
                        + DataRefManager.getInstance()
                                .currentAndroidModule
                                .getRefToOthersModules()
                                .size()
                        + "]";
        for (String s : DataRefManager.getInstance().currentAndroidModule.getRefToOthersModules())
            if (!s.equals(DataRefManager.getInstance().currentAndroidModule.getPath()))
                m += "\n" + s;

        DialogBuilder.getDialogBuilder(C, C.getString(R.string.modules), m).show();
    }

    private void loadProjectManager() {
        allOnClick();
    }

    private void allOnClick() {
        onClickReference();
    }

    private void onClickReference() {
        binding.btnShowAllRefModules.setOnClickListener(
                (v) -> {
                    showAllRefModules();
                });
        binding.btnAddProjectsDependencies.setOnClickListener(
                (v) -> {
                    if (!(DataRefManager.getInstance().currentAndroidModule != null)) {
                        Toast.makeText(C, getString(R.string.cant_load), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DialogSelectModule(C, DialogSelectModule.MODULES_REF_SELECT_MODE)
                            .setOnModulesRefSelected(
                                    (list) -> {
                                        Executors.newSingleThreadExecutor()
                                                .execute(
                                                        () -> {
                                                            String msg =
                                                                    getString(R.string.module)
                                                                            + " : "
                                                                            + DataRefManager
                                                                                    .getInstance()
                                                                                    .currentAndroidModule
                                                                                    .getName()
                                                                            + "\n\n"
                                                                            + getString(
                                                                                    R.string
                                                                                            .dependencies)
                                                                            + " : "
                                                                            + getString(
                                                                                    R.string.added)
                                                                            + " ";
                                                            for (String s : list) {
                                                                if (!updateGradleDependencies(s)) {
                                                                    C.runOnUiThread(
                                                                            () -> {
                                                                                Toast.makeText(
                                                                                                C,
                                                                                                R
                                                                                                        .string
                                                                                                        .error,
                                                                                                Toast
                                                                                                        .LENGTH_SHORT)
                                                                                        .show();
                                                                            });
                                                                    return;
                                                                }
                                                                msg += "\n" + s;
                                                                DataRefManager.getInstance()
                                                                        .currentAndroidModule
                                                                        .getRefToOthersModules()
                                                                        .add(s);
                                                            }
                                                            msg += "";

                                                            if (list.size() != 0)
                                                                Logger.success(SRC, msg);
                                                            C.runOnUiThread(
                                                                    () -> {
                                                                        loadData();
                                                                    });
                                                        });
                                    });
                });
        binding.btnDependencies.setOnClickListener(
                (v) -> {
                    DialogBuilder.getDialogBuilder(
                                    C,
                                    getString(R.string.information),
                                    getString(
                                            R.string.info_a_batter_dependency_will_be_implemented))
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                });
    }

    String pathGradleFile;

    public boolean updateGradleDependencies(String dependency) {
        pathGradleFile =
                DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath()
                        + ProjectsPathUtils.gradlePath;

        if (!Files.isFile(pathGradleFile)) pathGradleFile += ".kts";
        String gradleCode = Files.readFile(pathGradleFile);
        if (gradleCode == null || gradleCode.isEmpty()) return false;
        if (pathGradleFile.endsWith(".kts"))
            dependency = "\n    implementation(project(\"" + dependency + "\"))";
        else dependency = "\n    implementation project('" + dependency + "')";
        if (pathGradleFile.endsWith(".kts")) dependency = dependency.replace("'", "\"");

        String[] GCS = gradleCode.split("dependencies");
        if (GCS.length > 1) {
            String finalGradleCode = GCS[0];
            finalGradleCode +=
                    "dependencies {" + dependency + GCS[1].substring(GCS[1].indexOf("{") + 1);
            if (GCS.length > 2)
                for (int i = 2; i < GCS.length; i++) finalGradleCode += "dependencies" + GCS[i];

            Files.writeFile(pathGradleFile, finalGradleCode);
            return true;
        }
        return false;
    }

    private void performList(final ArrayList<String> list) {
        String path =
                DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath()
                        + "/build.gradle";
        if (!Files.isFile(path)) path += ".kts";
        if (!Files.isFile(path)) {
            Toast.makeText(
                            C,
                            "build.gradle : " + C.getString(R.string.not_found),
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String gradleCode = Files.readFile(path);
    }
}
