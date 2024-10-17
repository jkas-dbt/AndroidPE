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
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.databinding.DialogEditTextBinding;

/**
 * @author JKas
 */
public class DialogEditText {
    private OnChangeDetected listener;
    private Context C;
    private DialogEditTextBinding binding;
    private MaterialAlertDialogBuilder builder;
    private AlertDialog dialog;
    private String pattern, msgError, text = "";
    private boolean withConfirmation = false;

    public DialogEditText(Context c, boolean withConfirmation) {
        this.C = c;
        this.withConfirmation = withConfirmation;
        init();
        events();
        dialog = builder.create();
    }

    public LinearLayout getAdditionalViewParent() {
        return binding.linAdditional;
    }

    public void setTitle(@NonNull String title) {
        builder.setTitle(title);
    }

    public void setHint(String hint) {
        binding.til.setHint(hint);
    }

    public void setDefaultValue(@NonNull String value) {
        text = value;
    }

    public void setInfo(String info) {
        binding.tvInfo.setText(info);
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public void setPattern(@Nullable String pattern) {
        this.pattern = pattern;
    }

    public void cancel() {
        dialog.cancel();
    }

    public void show() {
        dialog.show();
        binding.textInput.setText(text);
    }

    private void events() {
        if (withConfirmation) {
            builder.setNegativeButton(R.string.cancel, null);
            builder.setPositiveButton(
                    R.string.save,
                    (d, v) -> {
                        listener.onTextChanged(text);
                        if (pattern == null) listener.onValueConfirmed(true);
                        else if (text.matches(pattern) || (text + "rkb").matches(pattern))
                            listener.onValueConfirmed(true);
                        else listener.onValueConfirmed(false);
                    });
        }

        binding.textInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable edit) {
                        binding.til.setError(null);
                        text = edit.toString();
                        listener.onTextChanged(text);
                        if (pattern == null) return;
                        if (text.matches(pattern)) {
                            if (!withConfirmation) listener.onValueConfirmed(true);
                        } else if ((text + "rkb").matches(pattern)) {
                            if (!withConfirmation) listener.onValueConfirmed(true);
                        } else {
                            if (!withConfirmation) listener.onValueConfirmed(false);
                            binding.til.setError(msgError);
                        }
                    }
                });
    }

    private void init() {
        binding = DialogEditTextBinding.inflate(LayoutInflater.from(C));
        builder = new MaterialAlertDialogBuilder(C);
        builder.setView(binding.getRoot());
    }

    public void setOnChangeDetected(OnChangeDetected listener) {
        this.listener = listener;
    }

    public interface OnChangeDetected {
        public void onTextChanged(String text);

        public void onValueConfirmed(boolean match);
    }
}
