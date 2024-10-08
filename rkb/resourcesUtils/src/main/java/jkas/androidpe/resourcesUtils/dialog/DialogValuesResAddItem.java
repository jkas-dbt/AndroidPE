package jkas.androidpe.resourcesUtils.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.databinding.DialogValuesResAddItemBinding;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.codeUtil.Files;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DialogValuesResAddItem {
    private OnChildAddedListener listener;
    private Context C;
    private Element element;
    private Document doc;
    private DialogValuesResAddItemBinding binding;
    private String tagName = "string";
    private String path;

    public DialogValuesResAddItem(Context C, Document doc, Element e, String path) {
        this.C = C;
        this.element = e;
        this.doc = doc;
        this.path = path;

        ini();
        events();
        defaultValues();
    }

    private void defaultValues() {
        String fileName = Files.getNameFromAbsolutePath(path);
        if (fileName.startsWith("color"))
            binding.radioGroup.check(jkas.androidpe.resourcesUtils.R.id.btnColor);
        else if (fileName.startsWith("dimen"))
            binding.radioGroup.check(jkas.androidpe.resourcesUtils.R.id.btnDimen);
    }

    private void events() {
        ResCodeUtils.ResAndCodeFilesFixer.fixXmlIdNameAndAssign(
                binding.TILName, binding.textInputName);
        binding.radioGroup.setOnCheckedChangeListener(
                (rg, id) -> tagName = ((RadioButton) rg.findViewById(id)).getText().toString());
    }

    private void ini() {
        binding = DialogValuesResAddItemBinding.inflate(LayoutInflater.from(C));
        binding.tvInfo.setText(element.getTagName());
        binding.textInputName.setText("name");
    }

    public void show() {
        DialogBuilder.getDialogBuilder(C, C.getString(R.string.add), null)
                .setView((View) binding.getRoot())
                .setPositiveButton(R.string.add, (d, v) -> save())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void save() {
        if (binding.textInputName.getText().toString().trim().length() == 0) {
            listener.onAdded(false);
            return;
        }

        Element newItem = doc.createElement(tagName);
        newItem.setAttribute("name", binding.textInputName.getText().toString());
        newItem.setTextContent(binding.textInputValue.getText().toString().replace("'", "\\'"));
        element.appendChild(newItem);

        listener.onAdded(true);
    }

    public DialogValuesResAddItem setOnChildAddedListener(OnChildAddedListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnChildAddedListener {
        public void onAdded(boolean added);
    }
}
