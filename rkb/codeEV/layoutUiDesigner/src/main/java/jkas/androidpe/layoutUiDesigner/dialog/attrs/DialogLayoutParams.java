package jkas.androidpe.layoutUiDesigner.dialog.attrs;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.databinding.DialogLayoutParamsBinding;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.adapters.AttrViewDataAdapter;
import jkas.androidpe.resourcesUtils.adapters.CustomAutoCompleteAdapter;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.CodeUtil;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DialogLayoutParams {
    private OnChangedListener listener;
    public static final int WIDTH = 0;
    public static final int HEIGHT = 1;
    public static final int WEIGHT = 2;

    private final String[] attrs = {
        "android:layout_width", "android:layout_height", "android:layout_weight"
    };

    private Context C;
    private Element element;
    private DialogLayoutParamsBinding binding;
    private int type = -1;
    private boolean focus = false;
    private MaterialAlertDialogBuilder dialog;

    public DialogLayoutParams(Context c) {
        C = c;
    }

    private void events() {
        binding.rWrapContent.setOnClickListener(v -> binding.autoComp.setText("wrap_content"));
        binding.rMatchParent.setOnClickListener(v -> binding.autoComp.setText("match_parent"));

        binding.autoComp.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        binding.til.setError(null);
                        if (type == WEIGHT && !editable.toString().isEmpty()) {
                            try {
                                Float.parseFloat(editable.toString());
                            } catch (Exception e) {
                                binding.til.setError(C.getString(R.string.invalide_entry));
                            }
                        } else if (editable.toString().startsWith("@")) {
                            binding.rWrapContent.setChecked(false);
                            binding.rMatchParent.setChecked(false);
                            if (ResourcesValuesFixer.getDimen(C, editable.toString()) == -1) {
                                binding.til.setError(C.getString(R.string.not_found));
                            }
                        } else {
                            if (editable.toString().equals("wrap_content")) {
                                binding.rGroup.check(binding.rWrapContent.getId());
                                return;
                            } else if (editable.toString().equals("match_parent")) {
                                binding.rGroup.check(binding.rMatchParent.getId());
                                return;
                            } else {
                                if (!CodeUtil.isDimenValue(editable.toString())) {
                                    binding.til.setError(C.getString(R.string.invalide_entry));
                                }
                                binding.rWrapContent.setChecked(false);
                                binding.rMatchParent.setChecked(false);
                            }
                        }
                    }
                });

        dialog.setNegativeButton(R.string.cancel, null);
        dialog.setPositiveButton(
                R.string.save,
                (d, v) -> {
                    if (save()) listener.onChange(type);
                    else Toast.makeText(C, R.string.invalide_entry, Toast.LENGTH_SHORT).show();
                });
    }

    private boolean save() {
        String value = binding.autoComp.getText().toString();
        if (type == WEIGHT) {
            if (value.isEmpty()) {
                element.removeAttribute(attrs[type]);
                return true;
            }
            try {
                Float.parseFloat(value);
                element.setAttribute(attrs[type], value);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        if (value.equals("wrap_content") || value.equals("match_parent")) {
            element.setAttribute(attrs[type], value);
            return true;
        }
        if (CodeUtil.isDimenValue(value)) {
            element.setAttribute(attrs[type], value);
            return true;
        }

        if (ResourcesValuesFixer.getDimen(C, value) != -1) {
            element.setAttribute(attrs[type], value);
            return true;
        }

        return false;
    }

    private void init() {
        binding = DialogLayoutParamsBinding.inflate(LayoutInflater.from(C));
        dialog = new MaterialAlertDialogBuilder(C);
        dialog.setTitle(attrs[type].split("_")[1]);
        dialog.setView(binding.getRoot());
        events();

        if (type != WEIGHT) {
            ArrayList<String> suggestions = AttrViewDataAdapter.getAllData("@dimen");
            suggestions.add(0, "match_parent");
            suggestions.add(0, "wrap_content");
            CustomAutoCompleteAdapter adapter =
                    new CustomAutoCompleteAdapter(
                            C, android.R.layout.simple_dropdown_item_1line, suggestions);
            binding.autoComp.setAdapter(adapter);
            binding.rGroup.setVisibility(View.VISIBLE);
        } else {
            binding.autoComp.setAdapter(null);
            binding.rGroup.setVisibility(View.GONE);
            binding.til.setHint("float");
        }

        String value = element.getAttribute(attrs[type]);
        if (value.equals("wrap_content")) binding.rGroup.check(binding.rWrapContent.getId());
        else if (value.equals("match_parent")) binding.rGroup.check(binding.rMatchParent.getId());
        binding.autoComp.setText(value);
    }

    public void show(Element element, int type) {
        this.element = element;
        this.type = type;

        init();
        dialog.show();
    }

    public void setOnChangeListener(OnChangedListener listener) {
        this.listener = listener;
    }

    public interface OnChangedListener {
        public void onChange(int type);
    }
}
