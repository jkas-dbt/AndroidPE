package jkas.androidpe.resourcesUtils.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import jkas.androidpe.resources.R;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import jkas.androidpe.resourcesUtils.dialog.DialogAttrValueParserAssist;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.databinding.LayoutAttrViewAdapterBinding;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import org.w3c.dom.Element;
import java.util.ArrayList;

/**
 * @author JKas
 */
public class AttrViewAdapter {
    private OnAttrChangedListener listener;
    private LayoutAttrViewAdapterBinding binding;
    private Context C;
    private Element element;
    private String[] listValuesAssist;
    private ArrayList<String> listAdapter = new ArrayList<>();
    private ArrayList<String> listAdapterAssist = new ArrayList<>();
    private String attrName;
    private String valueType;
    private boolean focus = false, autoRemove = false;

    public AttrViewAdapter(Context C, Element e, String attrName, String[] list) {
        this.C = C;
        this.element = e;
        this.attrName = attrName;
        this.listValuesAssist = list;
        init();
        adapter();
        loadDefault();
        events();
    }

    public void setDeleteBtnVisible(boolean visible) {
        if (visible) binding.icDelete.setVisibility(View.VISIBLE);
        else binding.icDelete.setVisibility(View.GONE);
    }

    public void setText(String txt) {
        binding.multiAutoCompTV.setText(txt);
    }

    public void setAutoRemoveAttrIfEmpty(boolean autoRemove) {
        this.autoRemove = autoRemove;
    }

    public String getAttr() {
        return attrName;
    }

    public String getText() {
        return binding.multiAutoCompTV.getText().toString();
    }

    private void events() {
        binding.viewFlipper.setOnClickListener(
                v -> {
                    DialogAttrValueParserAssist dialog = new DialogAttrValueParserAssist(C);
                    dialog.parseValues(element, attrName, valueType, listAdapterAssist);
                    dialog.setOnSaveListener(
                            finalValue -> binding.multiAutoCompTV.setText(finalValue));
                    dialog.show();
                });

        binding.icDelete.setOnClickListener(
                v -> {
                    DialogBuilder.getDialogBuilder(
                                    C,
                                    C.getString(R.string.warning),
                                    C.getString(R.string.warning_delete) + " (item) ?")
                            .setPositiveButton(R.string.delete, (d, vd) -> deleteAttr())
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                });

        binding.multiAutoCompTV.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        binding.TIL.setError(null);
                        if (editable.toString().trim().length() == 0) {
                            if (autoRemove) {
                                element.removeAttribute(attrName.intern());
                                deleteAttr();
                                return;
                            }
                            if (focus)
                                new Handler(Looper.getMainLooper())
                                        .postDelayed(
                                                () -> binding.multiAutoCompTV.showDropDown(), 50);
                        }
                        autoCompTextChanged(editable.toString());
                    }
                });

        binding.multiAutoCompTV.setOnFocusChangeListener((v, hasFocus) -> focus = hasFocus);
    }

    private void adapter() {
        assist(false);
        listAdapter.clear();
        listAdapterAssist.clear();
        valueType = null;
        if (listValuesAssist == null) return;
        if (listValuesAssist.length == 0) return;
        valueType = listValuesAssist[0];
        if (valueType.startsWith("@id")
                || valueType.startsWith("@+id")
                || !valueType.startsWith("@")) {
            for (String s : listValuesAssist) {
                listAdapter.add(s);
                listAdapterAssist.add(s);
            }
            assist(false);
        } else {
            listAdapter.addAll(AttrViewDataAdapter.getAllData(valueType));
            listAdapterAssist.addAll(AttrViewDataAdapter.getAllData(valueType));
            assist(true);
        }
        final CustomAutoCompleteAdapter adapter =
                new CustomAutoCompleteAdapter(
                        C, android.R.layout.simple_dropdown_item_1line, listAdapter);
        binding.multiAutoCompTV.setTokenizer(new SpaceTokenizer());
        binding.multiAutoCompTV.setAdapter(adapter);
        binding.multiAutoCompTV.setThreshold(1);
    }

    private void deleteAttr() {
        element.removeAttribute(attrName);
        if (listener != null) {
            listener.onChanged();
            listener.onDeleted();
        }
    }

    private void autoCompTextChanged(String txt) {
        binding.viewFlipper.setBackground(null);
        element.setAttribute(attrName, txt);
        if (listener != null) listener.onChanged();
        if (valueType != null) {
            if (txt.startsWith("@drawable/")
                    || txt.startsWith("@android:drawable/")
                    || txt.startsWith("@mipmap/")) {
                binding.viewFlipper.setDisplayedChild(1);
                binding.icImg.setImageDrawable(ResourcesValuesFixer.getDrawable(C, txt));
            } else {
                binding.viewFlipper.setDisplayedChild(0);
                binding.viewFlipper.setBackgroundColor(ResourcesValuesFixer.getColor(C, txt));
            }
        }
    }

    private void assist(boolean assist) {
        if (!assist) binding.viewFlipper.setVisibility(View.GONE);
        else binding.viewFlipper.setVisibility(View.VISIBLE);
    }

    private void loadDefault() {
        String value = element.getAttribute(attrName);
        binding.TIL.setHint(attrName);
        if (value.length() > 0) {
            binding.multiAutoCompTV.setText(value);
            if (valueType != null) autoCompTextChanged(value);
        }
    }

    private void init() {
        binding = LayoutAttrViewAdapterBinding.inflate(LayoutInflater.from(C));
        if (listValuesAssist != null && listValuesAssist.length == 0) listValuesAssist = null;
    }

    public View getView() {
        return (View) binding.getRoot();
    }

    public void setOnAttrChangedListener(OnAttrChangedListener listener) {
        this.listener = listener;
    }

    public interface OnAttrChangedListener {
        public default void onDeleted() {}

        public void onChanged();
    }

    public static class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {
        private int i;

        public int findTokenStart(CharSequence inputText, int cursor) {
            int idx = cursor;
            while (idx > 0 && inputText.charAt(idx - 1) != '|') idx--;
            while (idx < cursor && inputText.charAt(idx) == '|') idx++;
            return idx;
        }

        public int findTokenEnd(CharSequence inputText, int cursor) {
            int idx = cursor;
            int length = inputText.length();
            while (idx < length) {
                if (inputText.charAt(i) == '|') return idx;
                else idx++;
            }
            return length;
        }

        public CharSequence terminateToken(CharSequence inputText) {
            int idx = inputText.length();
            while (idx > 0 && inputText.charAt(idx - 1) == '|') idx--;

            if (idx > 0 && inputText.charAt(idx - 1) == '|') return inputText;
            else {
                if (inputText instanceof Spanned) {
                    SpannableString sp = new SpannableString(inputText + "");
                    TextUtils.copySpansFrom(
                            (Spanned) inputText, 0, inputText.length(), Object.class, sp, 0);
                    return sp;
                } else return inputText + "";
            }
        }
    }
}
