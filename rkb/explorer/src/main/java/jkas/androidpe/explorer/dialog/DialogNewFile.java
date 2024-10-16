package jkas.androidpe.explorer.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.projectUtils.dataCreator.FilesRef;
import jkas.androidpe.projectUtils.utils.ValuesTools;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import jkas.androidpe.explorer.databinding.DialogNewFileBinding;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class DialogNewFile {
    private String SRC = "New File";
    private Performer perform = new Performer();
    private OnCreateListener listener;
    private DialogNewFileBinding binding;
    private MaterialAlertDialogBuilder builder;
    private Context C;
    private String path;
    private int idDataType, idFileType;

    public DialogNewFile(Context c, String path) {
        C = c;
        this.path = path;

        init();
    }

    private void init() {
        binding = DialogNewFileBinding.inflate(LayoutInflater.from(C));
        builder = DialogBuilder.getDialogBuilder(C, C.getString(R.string.new_file), null);

        events();
        loadDataDefault();

        builder.setView(binding.getRoot());
        builder.show();
    }

    private void loadDataDefault() {
        if (ValuesTools.PathController.isCodeFile(path)) {
            binding.btnRes.setEnabled(false);
            binding.btnOther.setEnabled(false);
            binding.toggleBtnTypeData.check(binding.btnClass.getId());
        } else if (ValuesTools.PathController.isResFile(path)) {
            binding.btnClass.setEnabled(false);
            binding.btnInterface.setEnabled(false);
            binding.btnEnum.setEnabled(false);
            binding.btnOther.setEnabled(false);
            binding.toggleBtnTypeData.check(binding.btnRes.getId());
        } else {
            binding.toggleBtnTypeData.check(binding.btnOther.getId());
        }
        loadDefaultPathType();
    }

    private void enabledAllFileType(boolean enabled) {
        binding.btnJava.setEnabled(enabled);
        binding.btnKotlin.setEnabled(enabled);

        binding.btnDrawable.setEnabled(enabled);
        binding.btnValues.setEnabled(enabled);
        binding.btnLayout.setEnabled(enabled);
        binding.btnMenu.setEnabled(enabled);
    }

    private void loadDefaultPathType() {
        enabledAllFileType(false);
        if (ValuesTools.PathController.isCodeFile(path)) {
            binding.btnJava.setEnabled(true);
            binding.btnKotlin.setEnabled(true);
            binding.toggleBtnFileType.check(binding.btnJava.getId());
        } else if (ValuesTools.PathController.isDrawableFile(path)) {
            binding.btnDrawable.setEnabled(true);
            binding.toggleBtnFileType.check(binding.btnDrawable.getId());
        } else if (ValuesTools.PathController.isLayoutFile(path)) {
            binding.btnLayout.setEnabled(true);
            binding.toggleBtnFileType.check(binding.btnLayout.getId());
        } else if (ValuesTools.PathController.isMenuFile(path)) {
            binding.btnMenu.setEnabled(true);
            binding.toggleBtnFileType.check(binding.btnMenu.getId());
        } else if (ValuesTools.PathController.isValuesFile(path)) {
            binding.btnValues.setEnabled(true);
            binding.toggleBtnFileType.check(binding.btnValues.getId());
        } else {
            enabledAllFileType(true);
        }
    }

    public void updateEvent() {
        binding.TILName.setError(null);
        binding.textInputName.removeTextChangedListener(null);

        if (idDataType == binding.btnClass.getId()
                || idDataType == binding.btnInterface.getId()
                || idDataType == binding.btnEnum.getId())
            ResCodeUtils.ResAndCodeFilesFixer.fixCodeFileNameAndAssign(
                    binding.TILName, binding.textInputName);
        else if (idDataType == binding.btnRes.getId())
            ResCodeUtils.ResAndCodeFilesFixer.fixXmlFileNameAndAssign(
                    binding.TILName, binding.textInputName);
        else if (idDataType == binding.btnOther.getId())
            ResCodeUtils.ResAndCodeFilesFixer.fixPathNameAndAssign(
                    binding.TILName, binding.textInputName);
    }

    private void events() {
        binding.toggleBtnTypeData.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (!checked) return;
                    if (idBtn == binding.btnOther.getId())
                        binding.toggleBtnFileType.setVisibility(View.GONE);
                    else binding.toggleBtnFileType.setVisibility(View.VISIBLE);

                    idDataType = idBtn;
                    perform.onToggleDataTypeChange();
                    updateEvent();
                });

        binding.toggleBtnFileType.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (!checked) return;
                    idFileType = idBtn;
                });

        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.create, (d, v) -> save());
    }

    private void save() {
        String txt = binding.textInputName.getText().toString();
        String txtCode = "";
        String pkg = "";

        if (txt == null || txt.length() == 0) {
            Logger.error(
                    SRC,
                    C.getString(R.string.cant_create),
                    txt,
                    C.getString(R.string.invalide_name));
            Toast.makeText(C, R.string.cant_add_files, Toast.LENGTH_SHORT).show();
            return;
        } else if (idDataType == binding.btnOther.getId()) {
            if (ResCodeUtils.isAValideNameForSimplePath(txt)) {
                Files.createNewFile(path + "/" + txt);
            } else {
                Logger.error(
                        SRC,
                        C.getString(R.string.cant_create),
                        txt,
                        C.getString(R.string.invalide_name));
                Toast.makeText(C, R.string.cant_add_files, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            boolean verif = false;
            if (idDataType == binding.btnRes.getId()) if (!txt.contains(".")) txt += ".xml";
            if (idFileType == binding.btnDrawable.getId()) {
                if (txt.endsWith(".xml")) {
                    txtCode = FilesRef.Resources.Drawable.CODEViewXml;
                    verif = true;
                }
            } else if (idFileType == binding.btnValues.getId()) {
                if (txt.contains("colors")) {
                    txtCode = FilesRef.Resources.Values.CODEcolorsXml;
                    verif = true;
                } else if (txt.contains("themes") || txt.contains("styles")) {
                    txtCode = FilesRef.Resources.Values.CODEthemesXml;
                    verif = true;
                } else if (txt.endsWith(".xml")) {
                    txtCode = FilesRef.Resources.Values.CODEstringsXml;
                    verif = true;
                }
            } else if (idFileType == binding.btnLayout.getId()) {
                if (txt.endsWith(".xml")) {
                    txtCode = FilesRef.Resources.Layout.layoutXml;
                    verif = true;
                }
            } else if (idFileType == binding.btnMenu.getId()) {
                if (txt.endsWith(".xml")) {
                    txtCode = FilesRef.Resources.Menu.menuXml;
                    verif = true;
                }
            }

            if (verif == true) binding.TILName.setError(ResCodeUtils.getInfoXmlFileName(C, txt));
            if (binding.TILName.getError() != null) {
                Logger.error(
                        SRC,
                        C.getString(R.string.cant_create),
                        txt,
                        C.getString(R.string.invalide_name));
                Toast.makeText(C, R.string.cant_add_files, Toast.LENGTH_SHORT).show();
                return;
            }

            verif = false;

            if (idFileType == binding.btnJava.getId()) {
                if (!txt.contains(".")) txt += ".java";
                if (txt.endsWith(".java")) {
                    txtCode = FilesRef.SourceCode.Java.CODEclass;
                    verif = true;
                }
            } else if (idFileType == binding.btnKotlin.getId()) {
                if (!txt.contains(".")) txt += ".kt";
                if (txt.endsWith(".kt")) {
                    txtCode = FilesRef.SourceCode.Kotlin.CODEclass;
                    verif = true;
                }
            }
            if (verif == true) binding.TILName.setError(ResCodeUtils.getInfoCodeFileName(C, txt));
            if (binding.TILName.getError() != null) {
                Logger.error(
                        SRC,
                        C.getString(R.string.cant_create),
                        txt,
                        C.getString(R.string.invalide_name));
                Toast.makeText(C, R.string.cant_add_files, Toast.LENGTH_SHORT).show();
                return;
            }

            if (idDataType == binding.btnEnum.getId()) txtCode = txtCode.replace("class", "enum");
            else if (idDataType == binding.btnInterface.getId())
                txtCode = txtCode.replace("class", "interface");

            txtCode = txtCode.replace("$CLASS_NAME$", txt.substring(0, txt.indexOf(".")));
            { // searching for package name
                String stringPattern =
                        (DataRefManager.getInstance().P.getAbsolutePath()
                                        + "/.*"
                                        + "/src/main/java|kotlin")
                                .replace("/", "\\/");
                Pattern pattern = Pattern.compile(stringPattern);
                Matcher matcher = pattern.matcher(path);
                pkg = matcher.replaceFirst("").replace("/", ".").substring(1);
                txtCode = txtCode.replace("$PACKAGE_NAME$", pkg);
            }
        }

        Files.writeFile(path + "/" + txt, txtCode);
        Logger.success(
                SRC,
                C.getString(R.string.new_file) + " (" + C.getString(R.string.created) + ")",
                path + "/" + txt);
        listener.onCreate(path + "/" + txt);
    }

    public void setOnCreateListener(OnCreateListener listener) {
        this.listener = listener;
    }

    public interface OnCreateListener {
        public void onCreate(String fileName);
    }

    private class Performer {
        private static final int OTHER = 0;
        private static final int SRC_CODE = 1;
        private static final int RES = 2;

        private int typeFile = OTHER;

        public void onToggleDataTypeChange() {
            if (binding.btnClass.getId() == idDataType) loadClass();
            else if (binding.btnInterface.getId() == idDataType
                    || binding.btnEnum.getId() == idDataType) loadInterfaceEnum();
            else if (binding.btnRes.getId() == idDataType) loadRes();
            else if (binding.btnOther.getId() == idDataType) enableAll();
        }

        private void enableAll() {
            binding.toggleBtnFileType.check(binding.btnJava.getId());
            binding.toggleBtnFileType.uncheck(binding.btnJava.getId());

            binding.btnJava.setVisibility(View.VISIBLE);
            binding.btnKotlin.setVisibility(View.VISIBLE);

            binding.btnDrawable.setVisibility(View.VISIBLE);
            binding.btnValues.setVisibility(View.VISIBLE);
            binding.btnLayout.setVisibility(View.VISIBLE);
            binding.btnMenu.setVisibility(View.VISIBLE);
        }

        public void loadClass() {
            binding.toggleBtnFileType.check(binding.btnJava.getId());

            binding.btnJava.setVisibility(View.VISIBLE);
            binding.btnKotlin.setVisibility(View.VISIBLE);

            binding.btnDrawable.setVisibility(View.GONE);
            binding.btnValues.setVisibility(View.GONE);
            binding.btnLayout.setVisibility(View.GONE);
            binding.btnMenu.setVisibility(View.GONE);
            typeFile = SRC_CODE;
        }

        public void loadInterfaceEnum() {
            loadClass();
            binding.btnKotlin.setVisibility(View.GONE);
            typeFile = SRC_CODE;
        }

        public void loadRes() {
            binding.toggleBtnFileType.check(binding.btnValues.getId()); // default
            if (ValuesTools.PathController.isDrawableFile(path)) {
                binding.toggleBtnFileType.check(binding.btnDrawable.getId());
            } else if (ValuesTools.PathController.isLayoutFile(path)) {
                binding.toggleBtnFileType.check(binding.btnLayout.getId());
            } else if (ValuesTools.PathController.isMenuFile(path)) {
                binding.toggleBtnFileType.check(binding.btnMenu.getId());
            } else if (ValuesTools.PathController.isValuesFile(path)) {
                binding.toggleBtnFileType.check(binding.btnValues.getId());
            }
            binding.btnJava.setVisibility(View.GONE);
            binding.btnKotlin.setVisibility(View.GONE);

            binding.btnDrawable.setVisibility(View.VISIBLE);
            binding.btnValues.setVisibility(View.VISIBLE);
            binding.btnLayout.setVisibility(View.VISIBLE);
            binding.btnMenu.setVisibility(View.VISIBLE);
            typeFile = RES;
        }
    }
}
