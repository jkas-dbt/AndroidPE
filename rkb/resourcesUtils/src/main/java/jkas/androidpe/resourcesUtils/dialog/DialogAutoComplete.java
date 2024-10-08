package jkas.androidpe.resourcesUtils.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.List;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.adapters.CustomAutoCompleteAdapter;
import jkas.androidpe.resourcesUtils.databinding.DialogAutoCompleteBinding;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;

/**
 * @author JKas
 */
public class DialogAutoComplete {
    private OnChangeDetected listener;
    private Context C;
    private DialogAutoCompleteBinding binding;
    private MaterialAlertDialogBuilder builder;
    private AlertDialog dialog;
    private String pattern, msgError, text = "";
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> listAdapter = new ArrayList<>();
    private boolean withConfirmation = false;

    public DialogAutoComplete(Context c, boolean withConfirmation) {
        this.C = c;
        this.withConfirmation = withConfirmation;
        init();
        events();
        dialog = builder.create();
    }

    public LinearLayout getAdditionalViewParent() {
        return binding.linAdditional;
    }

    public void setTitle(String title) {
        builder.setTitle(title);
    }

    public void setHint(String hint) {
        binding.til.setHint(hint);
    }

    public void showError(String msgError) {
        binding.til.setError(msgError);
    }

    public void setDefaultValue(@NonNull String value) {
        binding.autoComp.setText(value);
        text = value;
    }

    public void setInfo(String info) {
        binding.tvInfo.setText(info);
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public void setListForAutoCompletion(final List<String> list) {
        this.list.clear();
        this.list.addAll(list);
        this.listAdapter.clear();
        this.listAdapter.addAll(list);
        final CustomAutoCompleteAdapter adapter =
                new CustomAutoCompleteAdapter(
                        C, android.R.layout.simple_dropdown_item_1line, listAdapter);
        binding.autoComp.setAdapter(adapter);
    }

    public void cancel() {
        dialog.cancel();
    }

    public void show() {
        dialog.show();
        binding.autoComp.setText("");
        binding.autoComp.setText(text);
    }

    private void events() {
        if (withConfirmation) {
            builder.setNegativeButton(R.string.cancel, (d, v) -> listener.onValueConfirmed(false));
            builder.setPositiveButton(
                    R.string.save,
                    (d, v) -> {
                        listener.onTextChanged(binding.autoComp.getText().toString());
                        if (binding.til.getError() == null) listener.onValueConfirmed(true);
                        else listener.onValueConfirmed(false);
                    });
        }

        binding.autoComp.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable edit) {
                        binding.til.setError(null);
                        if (listener != null) {
                            if (listener.onTextChanged(edit.toString())) return;
                            binding.til.setError(C.getString(R.string.invalide_entry));
                        }
                    }
                });
    }

    private void init() {
        binding = DialogAutoCompleteBinding.inflate(LayoutInflater.from(C));
        builder = new MaterialAlertDialogBuilder(C);
        builder.setView(binding.getRoot());
    }

    public void setOnChangeDetected(OnChangeDetected listener) {
        this.listener = listener;
    }

    public interface OnChangeDetected {
        public boolean onTextChanged(String text);

        public void onValueConfirmed(boolean match);
    }
}
