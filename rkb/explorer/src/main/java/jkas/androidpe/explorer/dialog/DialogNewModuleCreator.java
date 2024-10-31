package jkas.androidpe.explorer.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.concurrent.Executors;
import jkas.androidpe.explorer.databinding.DialogNewModuleCreatorBinding;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.projectUtils.dataCreator.ModulesCreator;
import jkas.androidpe.projectUtils.dataCreator.ProjectsCreator;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resources.R;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class DialogNewModuleCreator {
    private String SRC = "Module Creator";
    private OnSaveListener listener;
    private Context C;
    private DialogNewModuleCreatorBinding binding;
    private String pathParent;
    private MaterialAlertDialogBuilder builder;
    private int moduleType = ModulesCreator.LIB_TYPE, language = ProjectsCreator.JAVA_LANGUAGE;

    public DialogNewModuleCreator(Context c, String path) {
        C = c;
        pathParent = path;

        init();
    }

    private void init() {
        binding = DialogNewModuleCreatorBinding.inflate(LayoutInflater.from(C));
        builder = DialogBuilder.getDialogBuilder(C, C.getString(R.string.new_module), null);
        builder.setView(binding.getRoot());

        events();
        loadDefaultData();

        builder.show();
    }

    private void events() {
        builder.setPositiveButton(
                C.getString(R.string.create),
                (v, d) -> {
                    save();
                });
        builder.setNegativeButton(C.getString(R.string.cancel), null);
        binding.textInputModuleName.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        binding.TILModuleName.setError(null);
                        if (!ResCodeUtils.isAValideFolderName(arg0.toString()))
                            binding.TILModuleName.setError(C.getString(R.string.no_valide));

                        if (Files.isDirectory(
                                binding.textInputFinalPath.getText().toString()
                                        + "/"
                                        + binding.textInputModuleName.getText().toString()))
                            binding.TILModuleName.setError(C.getString(R.string.already_exists));
                    }
                });
        binding.textInputPkg.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        binding.TILModulePackage.setError(null);
                        if (!ResCodeUtils.isAValidePackageName(arg0.toString()))
                            binding.TILModulePackage.setError(C.getString(R.string.no_valide));
                    }
                });

        binding.toggleBtnModuleType.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (checked)
                        if (idBtn == binding.btnApplication.getId())
                            moduleType = ModulesCreator.APP_TYPE;
                        else moduleType = ModulesCreator.LIB_TYPE;
                    else moduleType = 0;
                });

        binding.toggleBtnLanguage.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (checked)
                        if (idBtn == binding.btnJava.getId())
                            language = ProjectsCreator.JAVA_LANGUAGE;
                        else language = ProjectsCreator.KOTLIN_LANGUAGE;
                    else language = 0;
                });
    }

    private void save() {
        if (!ResCodeUtils.isAValideFolderName(binding.textInputModuleName.getText().toString())) {
            DialogBuilder.getDialogBuilder(
                            C, C.getString(R.string.warning), C.getString(R.string.invalide_entry))
                    .show();
            return;
        }

        if (!ResCodeUtils.isAValidePackageName(binding.textInputPkg.getText().toString())) {
            DialogBuilder.getDialogBuilder(
                            C, C.getString(R.string.warning), C.getString(R.string.invalide_entry))
                    .show();
            return;
        }

        if (Files.isDirectory(
                binding.textInputFinalPath.getText().toString()
                        + "/"
                        + binding.textInputModuleName.getText().toString())) {
            DialogBuilder.getDialogBuilder(
                            C, C.getString(R.string.folder), C.getString(R.string.already_exists))
                    .show();
            return;
        }

        ModulesCreator mc = new ModulesCreator(C);
        mc.setFolderName(binding.textInputModuleName.getText().toString());
        mc.setPackageName(binding.textInputPkg.getText().toString());
        mc.setRootProjectPath(binding.textInputFinalPath.getText().toString());
        mc.setSdkMin(21);
        mc.setSdkTarget(33);
        mc.setModuleGradleScript(
                binding.cbKotlinScript.isChecked()
                        ? ProjectsCreator.SCRIPT_KOTLIN
                        : ProjectsCreator.SCRIPT_GROOVY);
        mc.setRootProjectType(ProjectsCreator.PROJECT_TYPE_ANDROIDX);
        mc.setType(moduleType);
        mc.setLanguage(language);
        mc.writeModule();

        Logger.success(
                SRC,
                C.getString(R.string.module)
                        + " : "
                        + binding.textInputModuleName.getText().toString(),
                C.getString(R.string.created)
                        + " : "
                        + binding.textInputFinalPath.getText().toString()
                        + "/"
                        + binding.textInputModuleName.getText().toString(),
                "Gradle file (settings.gradle ...) " + C.getString(R.string.modified));
        listener.saved();
        Toast.makeText(C, R.string.created, Toast.LENGTH_SHORT).show();
    }

    private void loadDefaultData() {
        binding.textInputPkg.setText("com." + binding.textInputModuleName.getText());
        binding.textInputFinalPath.setText(pathParent);
        binding.toggleBtnModuleType.check(binding.btnAndroidLib.getId());
        binding.toggleBtnLanguage.check(binding.btnJava.getId());

        String path = DataRefManager.getInstance().P.getAbsolutePath() + "/build.gradle.kts";
        if (Files.isFile(path)) {
            binding.cbKotlinScript.setChecked(true);
            binding.toggleBtnLanguage.check(binding.btnKotlin.getId());
        }
    }

    public void setOnSaveListener(OnSaveListener listener) {
        this.listener = listener;
    }

    public interface OnSaveListener {
        public void saved();
    }
}
