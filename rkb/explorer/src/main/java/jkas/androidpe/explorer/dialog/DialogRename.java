package jkas.androidpe.explorer.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import jkas.androidpe.explorer.databinding.DialogRenameFileFolderBinding;
import jkas.androidpe.projectUtils.utils.ValuesTools;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resources.R;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class DialogRename {
    private OnChangeDetected listener;
    private DialogRenameFileFolderBinding binding;
    private MaterialAlertDialogBuilder builder;
    private Context C;
    private String path;

    public DialogRename(Context c, String path) {
        C = c;
        this.path = path;

        init();
    }

    private void init() {
        binding = DialogRenameFileFolderBinding.inflate(LayoutInflater.from(C));
        builder = DialogBuilder.getDialogBuilder(C, C.getString(R.string.rename), null);

        defaultValues();
        events();

        builder.setView(binding.getRoot());
        builder.show();
    }

    private void events() {
        binding.textInputName.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable edit) {
                        if (edit.toString().isEmpty()) return;
                        if (Files.isFile(Files.getPrefixPath(path) + "/" + edit.toString())) {
                            binding.TILName.setError(C.getString(R.string.already_exists));
                            return;
                        }
                        if (edit.toString().startsWith("/"))
                            binding.TILName.setError(C.getString(R.string.invalide_name));
                        if (ValuesTools.PathController.isCodeFile(path))
                            initEventsCODE(edit.toString());
                        else if (ValuesTools.PathController.isResFile(path))
                            initEventsXML(edit.toString());
                        else initEventsSIMPLE(edit.toString());
                    }
                });

        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(
                R.string.save,
                (v, d) -> {
                    save();
                });
    }

    private void save() {
        String txt = binding.textInputName.getText().toString();

        if (ValuesTools.PathController.isCodeFile(path)) {
            if (Files.isDirectory(path)) {
                if (ResCodeUtils.getInfoCodeFolderName(C, txt) != null) {
                    Toast.makeText(C, R.string.invalide_name, Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                if (ResCodeUtils.getInfoCodeFileName(C, txt) != null) {
                    Toast.makeText(C, R.string.invalide_name, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (ValuesTools.PathController.isResFile(path)) {
            if (Files.isDirectory(path)) {
                if (ResCodeUtils.getInfoXmlFolderName(C, txt) != null) {
                    Toast.makeText(C, R.string.invalide_name, Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                if (ResCodeUtils.getInfoXmlFileName(C, txt) != null) {
                    Toast.makeText(C, R.string.invalide_name, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (binding.TILName.getError() != null) return;

        if (Files.rename(path, txt))
            if (listener != null) listener.onSave(Files.getPrefixPath(path) + "/" + txt, txt);
    }

    private void initEventsXML(String txt) {
        binding.tvInfo.setText(C.getString(R.string.msg_error_rename_code));
        String msg =
                Files.isFile(path)
                        ? ResCodeUtils.getInfoXmlFileName(C, txt)
                        : ResCodeUtils.getInfoXmlFolderName(C, txt);
        binding.TILName.setError(msg);
    }

    private void initEventsCODE(String txt) {
        binding.tvInfo.setText(C.getString(R.string.msg_error_rename_code));
        String msg =
                Files.isFile(path)
                        ? ResCodeUtils.getInfoCodeFileName(C, txt)
                        : ResCodeUtils.getInfoCodeFolderName(C, txt);

        binding.TILName.setError(msg);
    }

    private void initEventsSIMPLE(String txt) {
        binding.TILName.setError(null);
        if (!ResCodeUtils.isAValideNameForSimplePath(txt))
            binding.TILName.setError(C.getString(R.string.error));
    }

    private void defaultValues() {
        binding.textInputName.setText(Files.getNameFromAbsolutePath(path));
    }

    public void setOnChangeDetected(OnChangeDetected listener) {
        this.listener = listener;
    }

    public interface OnChangeDetected {
        public void onSave(String path, String name);
    }
}
