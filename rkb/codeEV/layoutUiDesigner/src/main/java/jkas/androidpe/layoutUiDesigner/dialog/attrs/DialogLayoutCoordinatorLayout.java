package jkas.androidpe.layoutUiDesigner.dialog.attrs;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.tools.ViewCreator;
import jkas.androidpe.layoutUiDesigner.databinding.DialogLayoutCoordinatorLayoutBinding;
import jkas.androidpe.resourcesUtils.adapters.AttrViewAdapter;
import jkas.androidpe.resourcesUtils.adapters.CustomAutoCompleteAdapter;
import jkas.androidpe.resourcesUtils.attrs.AllAttrBase;
import jkas.codeUtil.CodeUtil;
import org.w3c.dom.Element;

/**
 * @ author JKas
 */
public class DialogLayoutCoordinatorLayout {
    private ArrayList<String> listAdapterClass = new ArrayList<>();

    private OnChangedListener listener;
    private Context C;
    private Element element;
    private DialogLayoutCoordinatorLayoutBinding binding;
    private MaterialAlertDialogBuilder dialog;

    public DialogLayoutCoordinatorLayout(Context c) {
        C = c;
        initDefault();
    }

    private void loadData() {
        binding.linAttrBottomSheetBehavior.removeAllViews();
        binding.linAttrBottomSheetBehavior.addView(
                getEditor("android:maxHeight", new String[] {"@dimen"}));
        binding.linAttrBottomSheetBehavior.addView(
                getEditor("android:maxWidth", new String[] {"@dimen"}));
        binding.linAttrBottomSheetBehavior.addView(
                getEditor("app:behavior_draggable", AllAttrBase.VALUES_BOOLEAN));
        binding.linAttrBottomSheetBehavior.addView(
                getEditor("app:behavior_expandedOffset", new String[] {"@dimen"}));
        binding.linAttrBottomSheetBehavior.addView(
                getEditor("app:behavior_halfExpandedRatio", null));
        binding.linAttrBottomSheetBehavior.addView(
                getEditor("app:behavior_hideable", AllAttrBase.VALUES_BOOLEAN));
        binding.linAttrBottomSheetBehavior.addView(
                getEditor("app:behavior_peekHeight", new String[] {"@dimen"}));
        binding.linAttrBottomSheetBehavior.addView(getEditor("app:behavior_saveFlags", null));
        binding.linAttrBottomSheetBehavior.addView(
                getEditor("app:behavior_skipCollapsed", AllAttrBase.VALUES_BOOLEAN));
        binding.autoCompLayoutBehavior.setText(element.getAttribute("app:layout_behavior"));
    }

    private View getEditor(String attrName, String[] list) {
        AttrViewAdapter atv = new AttrViewAdapter(C, element, attrName, list);
        atv.setDeleteBtnVisible(false);
        atv.setAutoRemoveAttrIfEmpty(true);
        atv.setOnAttrChangedListener(
                new AttrViewAdapter.OnAttrChangedListener() {

                    @Override
                    public void onDeleted() {
                        if (listener != null) listener.onChange();
                    }

                    @Override
                    public void onChanged() {
                        if (listener != null) listener.onChange();
                    }
                });
        return atv.getView();
    }

    private void events() {
        dialog.setCancelable(false);
        dialog.setPositiveButton(
                android.R.string.ok,
                (d, v) -> {
                    if (listener != null) listener.onChange();
                });

        final CustomAutoCompleteAdapter adapter =
                new CustomAutoCompleteAdapter(
                        C, android.R.layout.simple_dropdown_item_1line, listAdapterClass);
        binding.autoCompLayoutBehavior.setAdapter(adapter);
        binding.autoCompLayoutBehavior.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        binding.tilLayoutBehavior.setError(null);
                        if (editable.toString().trim().isEmpty()) {
                            element.removeAttribute("app:layout_behavior");
                        } else {
                            element.setAttribute("app:layout_behavior", editable.toString());
                            searchForError(editable.toString());
                        }
                    }
                });
    }

    private void searchForError(String clazz) {
        if (CodeUtil.isClass(clazz)) {
            if (!clazz.contains("Behavior")) {
                if (ViewCreator.create("null", C, clazz, false) instanceof View) {
                    binding.tilLayoutBehavior.setError("View class");
                } else {
                    binding.tilLayoutBehavior.setError(
                            "The class you provided was found but we cannot determine if it implements the CoordinatorLayout.Behavior");
                }
            } else {
                if (clazz.equals("com.google.android.material.bottomsheet.BottomSheetBehavior")) {
                    binding.checkBoxAutoDeleteAttr.setVisibility(View.VISIBLE);
                }
            }
        } else {
            binding.checkBoxAutoDeleteAttr.setVisibility(View.GONE);
            binding.tilLayoutBehavior.setError(
                    "The class you provided was not found. This may be a class you implemented yourself."
                            + "If not, then a semantic error is imminent for the activity that will contain this file.");
        }
    }

    private void init() {
        binding = DialogLayoutCoordinatorLayoutBinding.inflate(LayoutInflater.from(C));
        dialog = new MaterialAlertDialogBuilder(C);
        dialog.setTitle("CoordinatorLayout");
        dialog.setView(binding.getRoot());
    }

    public void show(Element element) {
        this.element = element;
        init();
        events();
        loadData();
        dialog.show();
    }

    private void initDefault() {
        // full classes for some Behavior impl
        listAdapterClass.clear();
        listAdapterClass.add("com.google.android.material.bottomsheet.BottomSheetBehavior");
        listAdapterClass.add(
                "com.google.android.material.floatingactionbutton.FloatingActionButton$Behavior");
        listAdapterClass.add(
                "com.google.android.material.appbar.AppBarLayout.ScrollingViewBehavior");
        listAdapterClass.add("com.google.android.material.appbar.HeaderScrollingViewBehavior");
        listAdapterClass.add("com.google.android.material.appbar.AppBarLayout$Behavior");
        listAdapterClass.add(
                "com.google.android.material.appbar.AppBarLayout$ScrollingViewBehavior");
    }

    public void setOnChangeListener(OnChangedListener listener) {
        this.listener = listener;
    }

    public interface OnChangedListener {
        public void onChange();
    }
}
