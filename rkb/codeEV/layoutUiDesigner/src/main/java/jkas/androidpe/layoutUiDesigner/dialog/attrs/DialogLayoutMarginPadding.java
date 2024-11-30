package jkas.androidpe.layoutUiDesigner.dialog.attrs;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.databinding.DialogLayoutMarginPaddingBinding;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.adapters.AttrViewDataAdapter;
import jkas.androidpe.resourcesUtils.adapters.CustomAutoCompleteAdapter;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.CodeUtil;
import android.view.LayoutInflater;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DialogLayoutMarginPadding {
    private static final String maring = "layout_margin";
    private static final String padding = "padding";

    private OnChangedListener listener;
    private Context C;
    private Element element;
    private DialogLayoutMarginPaddingBinding binding;
    private MaterialAlertDialogBuilder dialog;
    private ArrayList<String> listId = new ArrayList<>();
    private String attr = maring;
    private ArrayList<String> listItem = new ArrayList<>();

    public DialogLayoutMarginPadding(Context c) {
        this.C = c;
        listItem.clear();
        listItem.addAll(AttrViewDataAdapter.getAllData("@dimen"));
    }

    private void events() {
        dialog.setPositiveButton(android.R.string.ok, (d, v) -> listener.onChange());
        binding.toggleType.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (checked) {
                        if (idBtn == binding.btnMargin.getId()) attr = maring;
                        else if (idBtn == binding.btnPadding.getId()) attr = padding;
                        binding.tilMP.setHint(attr);
                        sets();
                    }
                });
    }

    private void sets() {
        setEdit(binding.tilStart, binding.editStart, false);
        setEdit(binding.tilEnd, binding.editEnd, false);
        setEdit(binding.tilLeft, binding.editLeft, false);
        setEdit(binding.tilRight, binding.editRight, false);
        setEdit(binding.tilTop, binding.editTop, false);
        setEdit(binding.tilBottom, binding.editBottom, false);
        setEdit(binding.tilMP, binding.editMP, true);
    }

    private void setEdit(
            final TextInputLayout til, final AutoCompleteTextView edit, final boolean global) {
        edit.setText(
                element.getAttribute("android:" + attr + (global ? "" : til.getHint().toString())));
        if (edit.getAdapter() != null) return;

        final ArrayList<String> list = new ArrayList<>();
        list.addAll(listItem);
        CustomAutoCompleteAdapter adapter =
                new CustomAutoCompleteAdapter(C, android.R.layout.simple_dropdown_item_1line, list);
        edit.setAdapter(adapter);
        edit.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        til.setError(null);
                        if (!editable.toString().trim().isEmpty()) {
                            if (editable.toString().startsWith("@")) {
                                if (ResourcesValuesFixer.getDimen(C, editable.toString()) == -1) {
                                    til.setError(C.getString(R.string.not_found));
                                    return;
                                }
                            } else {
                                if (!CodeUtil.isDimenValue(editable.toString())) {
                                    til.setError(C.getString(R.string.invalide_entry));
                                    return;
                                }
                            }
                        }
                        if (editable.toString().trim().isEmpty()) {
                            element.removeAttribute(
                                    "android:" + attr + (global ? "" : til.getHint().toString()));
                        } else {
                            element.setAttribute(
                                    "android:" + attr + (global ? "" : til.getHint().toString()),
                                    editable.toString());
                        }
                    }
                });
    }

    private void init() {
        binding = DialogLayoutMarginPaddingBinding.inflate(LayoutInflater.from(C));
        binding.editStart.setText("");
        binding.editEnd.setText("");
        binding.editLeft.setText("");
        binding.editRight.setText("");
        binding.editTop.setText("");
        binding.editBottom.setText("");

        dialog = new MaterialAlertDialogBuilder(C);
        dialog.setTitle("Margin - Padding");
        dialog.setView(binding.getRoot());
        dialog.setCancelable(false);
    }

    public void show(Element element) {
        this.element = element;
        init();
        events();
        dialog.show();
        binding.toggleType.check(binding.btnMargin.getId());
    }

    public void setOnChangeListener(OnChangedListener listener) {
        this.listener = listener;
    }

    public interface OnChangedListener {
        public void onChange();
    }
}
