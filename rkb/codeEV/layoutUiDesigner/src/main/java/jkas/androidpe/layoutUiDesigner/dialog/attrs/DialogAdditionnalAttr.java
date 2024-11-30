package jkas.androidpe.layoutUiDesigner.dialog.attrs;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.util.Pair;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.elevation.SurfaceColors;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import jkas.androidpe.resourcesUtils.adapters.CustomAutoCompleteAdapter;
import jkas.androidpe.layoutUiDesigner.databinding.DialogAdditionnalAttrBinding;
import jkas.androidpe.resourcesUtils.utils.ViewUtils;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.adapters.AttrViewAdapter;
import jkas.androidpe.resourcesUtils.adapters.AttrViewDataAdapter;
import jkas.androidpe.resourcesUtils.attrs.layout.AttrViews;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DialogAdditionnalAttr {
    public static final String pkgLayouts = "jkas.androidpe.resourcesUtils.attrs.layout.layouts.";
    public static final String pkgWidgets = "jkas.androidpe.resourcesUtils.attrs.layout.widgets.";
    public static final String pkgMaterial3 =
            "jkas.androidpe.resourcesUtils.attrs.layout.material3.";

    private Context C;
    private OnChangedListener listener;
    private Element element;
    private DialogAdditionnalAttrBinding binding;
    private MaterialAlertDialogBuilder dialog;
    private String viewName;

    private ArrayList<String> listCommon = new ArrayList<>();
    private ArrayList<String> listAdditionnal = new ArrayList<>();
    private ArrayList<String> attrUsed = new ArrayList<>();

    private String currentDirectionValue = "";
    private boolean TCFAS = false; // Text changed from another source.
    private String[] autoCompAssist;
    private ArrayList<String> listValueAdapter = new ArrayList<>();

    public DialogAdditionnalAttr(Context c) {
        this.C = c;
    }

    private void events() {
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.setPositiveButton(R.string.save, (d, v) -> save());

        binding.toggleType.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (checked) {
                        if (idBtn == binding.btnCommon.getId()) loadAttr(listCommon);
                        else if (idBtn == binding.btnAdditional.getId()) loadAttr(listAdditionnal);
                        else if (idBtn == binding.btnAdd.getId()) binding.vf.setDisplayedChild(1);
                    }
                });

        binding.autoCompAttr.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        binding.tilAttr.setError(null);
                        if (editable.toString().isEmpty())
                            new Handler(Looper.getMainLooper())
                                    .postDelayed(() -> binding.autoCompAttr.showDropDown(), 50);
                        else onAttrTextChanged(editable.toString());
                    }
                });

        binding.autoCompValue.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        binding.tilValue.setError(null);
                        if (editable.toString().isEmpty())
                            new Handler(Looper.getMainLooper())
                                    .postDelayed(() -> binding.autoCompValue.showDropDown(), 50);
                        onValueTextChanged(editable.toString());
                    }
                });
    }

    private void onAttrTextChanged(String attr) {
        for (String attrUsed : attrUsed) {
            if (attr.equals(attrUsed)) {
                binding.tilAttr.setError("The attribute is already in use.");
                return;
            }
        }

        boolean found = false;
        for (Pair<?, ?> pair : CodeUtil.convertLinkedHashMapToPair(AttrViews.baseAttrs)) {
            String viewAttr = (String) pair.first;
            if (attr.equals(viewAttr) || attr.endsWith(":" + viewAttr)) {
                autoCompAssist = (String[]) pair.second;
                found = true;
                break;
            }
        }
        if (!found)
            for (Pair<?, ?> pair : CodeUtil.convertLinkedHashMapToPair(AttrViews.baseAttrsApp)) {
                String viewAttr = (String) pair.first;
                if (attr.equals(viewAttr) || attr.endsWith(":" + viewAttr)) {
                    autoCompAssist = (String[]) pair.second;
                    found = true;
                    break;
                }
            }

        if (!found)
            for (Pair<?, ?> pair : CodeUtil.convertLinkedHashMapToPair(AttrViews.allAttrs)) {
                String viewAttr = (String) pair.first;
                if (attr.equals(viewAttr) || attr.endsWith(":" + viewAttr)) {
                    autoCompAssist = (String[]) pair.second;
                    found = true;
                    break;
                }
            }

        if (!found) autoCompAssist = null;

        CustomAutoCompleteAdapter adapter = null;
        if (autoCompAssist != null) {
            listValueAdapter.clear();
            if (autoCompAssist[0].startsWith("@"))
                listValueAdapter = AttrViewDataAdapter.getAllData(autoCompAssist[0]);
            else for (String v : autoCompAssist) listValueAdapter.add(v);
            adapter =
                    new CustomAutoCompleteAdapter(
                            C, android.R.layout.simple_dropdown_item_1line, listValueAdapter);
        }
        binding.autoCompValue.setTokenizer(new AttrViewAdapter.SpaceTokenizer());
        binding.autoCompValue.setAdapter(adapter);
        binding.autoCompValue.setThreshold(1);
        binding.autoCompValue.setText(binding.autoCompValue.getText());
    }

    private void onValueTextChanged(String txt) {
        if (listValueAdapter.size() == 0) return;

        boolean exist = false;
        for (String value : listValueAdapter) {
            if (txt.equals(value)) {
                exist = true;
                return;
            } else if (txt.contains(value) && txt.contains("|")) {
                exist = true;
                break;
            }
        }

        if (autoCompAssist != null) {
            if (!autoCompAssist[0].startsWith("@")) {
                if (!exist) binding.tilValue.setError(C.getString(R.string.invalide_entry));
            }
        }
    }

    private void save() {
        String attr = binding.autoCompAttr.getText().toString();
        String value = binding.autoCompValue.getText().toString();

        if (binding.tilValue.getError() != null) {
            Toast.makeText(C, R.string.cant_add, Toast.LENGTH_SHORT).show();
        } else {
            try {
                element.setAttribute(attr, value);
                listener.onChange();
            } catch (Exception err) {
                Toast.makeText(C, R.string.cant_add, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadAttr(ArrayList<String> listAttr) {
        binding.vf.setDisplayedChild(0);
        binding.lin.removeAllViews();
        for (final String attr : listAttr) {
            final TextView txt = new TextView(C);
            txt.setLayoutParams(CodeUtil.getLayoutParamsMW(16));
            txt.setPadding(43, 43, 43, 43);
            txt.setText(attr);
            txt.setTypeface(Typeface.MONOSPACE);
            txt.setTextSize(14f);
            txt.setOnClickListener(
                    v -> {
                        binding.autoCompAttr.setText(txt.getText().toString());
                        binding.toggleType.check(binding.btnAdd.getId());
                    });
            ViewUtils.setBgCornerRadius(txt, SurfaceColors.SURFACE_3.getColor(C));
            binding.lin.addView(txt);
        }
    }

    private void loadCommon() {
        listCommon.clear();
        attrUsed.clear();
        for (Pair<?, ?> pair : CodeUtil.convertLinkedHashMapToPair(AttrViews.baseAttrs)) {
            String attr = (String) pair.first;
            boolean exist = false;
            for (Pair<String, String> p : XmlManager.getAllAttrNValuesFromElement(element)) {
                String at = p.first;
                if (attr.equals(at)) {
                    exist = true;
                    break;
                }
            }
            if (!exist)
                if (attr.equals("style")) listCommon.add(attr);
                else listCommon.add("android:" + attr);
            else attrUsed.add("android:" + attr);
        }

        if (element.getNodeName().contains(".")) {
            for (Pair<?, ?> pair : CodeUtil.convertLinkedHashMapToPair(AttrViews.baseAttrsApp)) {
                String attr = (String) pair.first;
                boolean exist = false;
                for (Pair<String, String> p : XmlManager.getAllAttrNValuesFromElement(element)) {
                    String at = p.first;
                    if (attr.equals(at)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) listCommon.add("app:" + attr);
                else attrUsed.add("app:" + attr);
            }
        }

        Collections.sort(listCommon);
        Collections.sort(attrUsed);
    }

    private void loadDefaultData() {
        loadCommon();
        listAdditionnal.clear();
        Map<String, String[]> listAttr = getList();
        if (listAttr == null) {
            binding.btnAdditional.setEnabled(false);
            binding.toggleType.check(binding.btnCommon.getId());
            return;
        }
        binding.btnAdditional.setEnabled(true);
        for (Pair<?, ?> pair : CodeUtil.convertMapToPair(listAttr)) {
            String attr = (String) pair.first;
            boolean exist = false;
            for (Pair<?, ?> p : XmlManager.getAllAttrNValuesFromElement(element)) {
                String at = (String) p.first;
                String val = (String) p.second;
                if (attr.equals(at)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) listAdditionnal.add(attr);
            else attrUsed.add(attr);
        }
        Collections.sort(listAdditionnal);
        binding.toggleType.check(binding.btnAdditional.getId());
        ArrayList<String> list = new ArrayList<>();
        list.addAll(listAdditionnal);
        list.addAll(listCommon);
        Collections.sort(list);
        CustomAutoCompleteAdapter adapter =
                new CustomAutoCompleteAdapter(C, android.R.layout.simple_dropdown_item_1line, list);
        binding.autoCompAttr.setTokenizer(new AttrViewAdapter.SpaceTokenizer());
        binding.autoCompAttr.setAdapter(adapter);
        binding.autoCompAttr.setThreshold(1);
    }

    private Map<String, String[]> getList() {
        String pkg = pkgLayouts + "Attr" + viewName;
        try {
            Class<?> clazz = Class.forName(pkg);
            Field attrs = clazz.getDeclaredField("attrs");
            attrs.setAccessible(true);
            Object obj = attrs.get(null);
            if (obj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String[]> list = (Map<String, String[]>) obj;
                return list;
            }
        } catch (ClassNotFoundException
                | ClassCastException
                | SecurityException
                | NoSuchFieldException
                | IllegalAccessException
                | IllegalArgumentException e) {
            e.printStackTrace();
        }

        pkg = pkgWidgets + "Attr" + viewName;
        try {
            Class<?> clazz = Class.forName(pkg);
            Field attrs = clazz.getDeclaredField("attrs");
            attrs.setAccessible(true);
            Object obj = attrs.get(null);
            if (obj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String[]> list = (Map<String, String[]>) obj;
                return list;
            }
        } catch (ClassNotFoundException
                | ClassCastException
                | SecurityException
                | NoSuchFieldException
                | IllegalAccessException
                | IllegalArgumentException e) {
            e.printStackTrace();
        }

        pkg = pkgMaterial3 + "Attr" + viewName;
        try {
            Class<?> clazz = Class.forName(pkg);
            Field attrs = clazz.getDeclaredField("attrs");
            attrs.setAccessible(true);
            Object obj = attrs.get(null);
            if (obj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String[]> list = (Map<String, String[]>) obj;
                return list;
            }
        } catch (ClassNotFoundException
                | ClassCastException
                | SecurityException
                | NoSuchFieldException
                | IllegalAccessException
                | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void init() {
        binding = DialogAdditionnalAttrBinding.inflate(LayoutInflater.from(C));
        String name = element.getNodeName().replace(".", "/");
        viewName = Files.getNameFromAbsolutePath(name);
        dialog = DialogBuilder.getDialogBuilder(C, viewName, null);
        dialog.setView(binding.getRoot());

        events();
        loadDefaultData();
    }

    public void show(Element element) {
        this.element = element;
        init();
        dialog.show();
    }

    public void setOnChangeListener(OnChangedListener listener) {
        this.listener = listener;
    }

    public interface OnChangedListener {
        public void onChange();
    }
}
