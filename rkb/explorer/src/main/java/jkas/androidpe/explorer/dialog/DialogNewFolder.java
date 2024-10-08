package jkas.androidpe.explorer.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import jkas.androidpe.explorer.databinding.DialogRenameFileFolderBinding;
import jkas.androidpe.projectUtils.utils.ValuesTools;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resources.R;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class DialogNewFolder {
    private OnChangeDetected listener;
    private DialogRenameFileFolderBinding binding;
    private MaterialAlertDialogBuilder builder;
    private Context C;
    private String path;

    public DialogNewFolder(Context c, String path) {
        C = c;
        this.path = path;

        init();
    }

    private void init() {
        binding = DialogRenameFileFolderBinding.inflate(LayoutInflater.from(C));
        binding.tvDes.setText(C.getString(R.string.enter_new_name_for_folder));
        builder = DialogBuilder.getDialogBuilder(C, C.getString(R.string.new_folder), null);

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
                        if (edit.toString().length() > 0)
                            if (Files.isDirectory(path + "/" + edit.toString())) {
                                binding.TILName.setError(C.getString(R.string.already_exists));
                                return;
                            }
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

        if (ValuesTools.PathController.isCodeFile(path))
            if (ResCodeUtils.getInfoCodeFolderName(C, txt) != null) {
                Toast.makeText(C, R.string.invalide_name, Toast.LENGTH_SHORT).show();
                return;
            }

        if (ValuesTools.PathController.isResFile(path))
            if (ResCodeUtils.getInfoXmlFolderName(C, txt) != null) {
                Toast.makeText(C, R.string.invalide_name, Toast.LENGTH_SHORT).show();
                return;
            }

        if (Files.isDirectory(path + File.separator + txt))
            Toast.makeText(C, R.string.already_exists, Toast.LENGTH_SHORT).show();

        if (binding.TILName.getError() != null) return;
        if (Files.makeDir(path + File.separator + txt))
            if (listener != null) listener.onSave(txt);
            else if (listener != null) listener.onSave(null);
    }

    private void initEventsXML(String txt) {
        String msg = ResCodeUtils.getInfoXmlFolderName(C, txt);
        binding.TILName.setError(msg);
    }

    private void initEventsCODE(String txt) {
        String msg = ResCodeUtils.getInfoCodeFolderName(C, txt);
        binding.TILName.setError(msg);
    }

    private void initEventsSIMPLE(String txt) {
        binding.TILName.setError(null);
        if (!ResCodeUtils.isAValideNameForSimplePath(txt))
            binding.TILName.setError(C.getString(R.string.invalide_name));
    }

    public void setOnChangeDetected(OnChangeDetected listener) {
        this.listener = listener;
    }

    public interface OnChangeDetected {
        public void onSave(String name);
    }
}
