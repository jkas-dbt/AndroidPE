package jkas.androidpe.layoutUiDesigner.dialog.attrs;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputLayout;
import io.github.rosemoe.sora.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.databinding.DialogLayoutRelativeLayoutBinding;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.adapters.CustomAutoCompleteAdapter;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DialogLayoutRelativeLayout {
    private OnChangedListener listener;
    private Context C;
    private Element element;
    private DialogLayoutRelativeLayoutBinding binding;
    private MaterialAlertDialogBuilder dialog;
    private ArrayList<String> listId = new ArrayList<>();
    private ArrayList<String> listIdAdapter = new ArrayList<>();

    public DialogLayoutRelativeLayout(Context c) {
        C = c;
    }

    private void events() {
        dialog.setCancelable(false);
        dialog.setPositiveButton(android.R.string.ok, (d, v) -> listener.onChange());

        binding.toggleType.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (checked) {
                        if (idBtn == binding.btnParent.getId()) {
                            binding.viewFlipper.setDisplayedChild(0);
                        } else if (idBtn == binding.btnCenter.getId()) {
                            binding.viewFlipper.setDisplayedChild(1);
                        } else if (idBtn == binding.btnAlign.getId()) {
                            binding.viewFlipper.setDisplayedChild(2);
                        } else if (idBtn == binding.btnOthers.getId()) {
                            binding.viewFlipper.setDisplayedChild(3);
                        }
                    }
                });

        binding.checkBoxRec.setOnCheckedChangeListener(
                (c, b) -> {
                    if (b) {
                        eventSwitch();
                        eventEdit();
                    }
                });

        eventSwitch();
        eventEdit();
    }

    private void eventSwitch() {
        setSwitch(binding.switchLAPL);
        setSwitch(binding.switchLAPT);
        setSwitch(binding.switchLAPR);
        setSwitch(binding.switchLAPB);
        setSwitch(binding.switchLAPS);
        setSwitch(binding.switchLAPE);
        setSwitch(binding.switchLCH);
        setSwitch(binding.switchLCV);
        setSwitch(binding.switchLCIP);
    }

    private void eventEdit() {
        setEdit(binding.tilAL, binding.editAL);
        setEdit(binding.tilAT, binding.editAT);
        setEdit(binding.tilAR, binding.editAR);
        setEdit(binding.tilAB, binding.editAB);
        setEdit(binding.tilAS, binding.editAS);
        setEdit(binding.tilAE, binding.editAE);
        setEdit(binding.tilABL, binding.editABL);
        setEdit(binding.tilTL, binding.editTL);
        setEdit(binding.tilTR, binding.editTR);
        setEdit(binding.tilTS, binding.editTS);
        setEdit(binding.tilTE, binding.editTE);
        setEdit(binding.tilABV, binding.editABV);
        setEdit(binding.tilBL, binding.editBL);
    }

    private void setSwitch(final MaterialSwitch ms) {
        ms.setChecked(element.getAttribute(ms.getText().toString()).equals("true"));
        ms.setOnCheckedChangeListener(
                (c, b) -> {
                    if (b) element.setAttribute(ms.getText().toString(), "" + b);
                    else element.removeAttribute(ms.getText().toString());
                });
    }

    private void setEdit(final TextInputLayout til, final AutoCompleteTextView edit) {
        final CustomAutoCompleteAdapter adapter =
                new CustomAutoCompleteAdapter(
                        C, android.R.layout.simple_dropdown_item_1line, listIdAdapter);
        edit.setText(element.getAttribute(til.getHint().toString()));
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
                        element.setAttribute(til.getHint().toString(), editable.toString());
                        if (editable.toString().trim().isEmpty()) {
                            element.removeAttribute(til.getHint().toString());
                        } else if (!editable.toString().trim().startsWith("@id/")
                                && !editable.toString().trim().startsWith("@+id/")) {
                            til.setError("Fill in a correct ID");
                            return;
                        } else if (editable.toString().trim().startsWith("@id/")
                                || editable.toString().trim().startsWith("@+id/")) {
                            boolean verif = false;
                            for (String s : listId) {
                                verif = s.equals(editable.toString());
                                if (verif) return;
                            }
                            if (!verif) til.setError("ID : " + C.getString(R.string.not_found));
                        }
                    }
                });
    }

    private void initId() {
        listId.clear();
        listIdAdapter.clear();
        for (Element e :
                XmlManager.getAllFirstChildFromElement((Element) element.getParentNode())) {
            String id = e.getAttribute("android:id");
            if (id.startsWith("@+id/")) {
                listId.add(id);
                listId.add("@id/" + id.split("\\/")[1]);
                listIdAdapter.add("@id/" + id.split("\\/")[1]);
            }
        }

        if (listIdAdapter.size() == 0) {
            binding.tv.setText("No other IDs were found.");
            binding.tv.setTextColor(ResourcesValuesFixer.getColor(C, "?colorPrimary"));
        } else {
            binding.tv.setText("...");
            binding.tv.setTextColor(ResourcesValuesFixer.getColor(C, "?colorOnSurface"));
        }
    }

    private void init() {
        binding = DialogLayoutRelativeLayoutBinding.inflate(LayoutInflater.from(C));
        dialog = new MaterialAlertDialogBuilder(C);
        dialog.setTitle("RelativeLayout");
        dialog.setView(binding.getRoot());
        binding.viewFlipper.dispatchDisplayHint(0);
        initId();
        events();
    }

    public void show(Element element) {
        this.element = element;

        init();
        dialog.show();
        binding.toggleType.check(binding.btnParent.getId());
    }

    public void setOnChangeListener(OnChangedListener listener) {
        this.listener = listener;
    }

    public interface OnChangedListener {
        public void onChange();
    }
}
