package jkas.androidpe.resourcesUtils.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rarepebble.colorpicker.ColorPickerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jkas.androidpe.logger.LoggerRes;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.adapters.CustomAutoCompleteAdapter;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.databinding.DialogAttrValueParserAssistBinding;
import jkas.androidpe.resourcesUtils.modules.ModuleProject;
import jkas.androidpe.resourcesUtils.modules.ModuleRes;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DialogAttrValueParserAssist {
    private OnSaveListener listener;
    private Context C;
    private DialogAttrValueParserAssistBinding binding;
    private MaterialAlertDialogBuilder builder;
    private String attr;
    private String typeValue = "@Values";
    private String currentValue;
    private Element element;
    private ArrayList<String> listAdapter = new ArrayList<>();
    private ArrayList<String> listAdapterAssist = new ArrayList<>();
    private ArrayList<String> listResForAdd = new ArrayList<>();

    public DialogAttrValueParserAssist(Context c) {
        this.C = c;
        binding = DialogAttrValueParserAssistBinding.inflate(LayoutInflater.from(C));
    }

    public void parseValues(
            Element element, String attr, String typeValue, ArrayList<String> listAdapter) {
        this.element = element;
        this.attr = attr;
        this.typeValue = typeValue;
        this.currentValue = element.getAttribute(attr);
        this.listAdapter.clear();
        this.listAdapter.addAll(listAdapter);
        this.listAdapterAssist.clear();
        this.listAdapterAssist.addAll(listAdapter);
        searchAllFilesRefForAdd();
        init();
        events();
        loadDefault();
        loadDefaultSetting();
    }

    private void searchAllFilesRefForAdd() {
        ModuleProject MP = DataRefManager.getInstance().currentModuleProject;

        { // add for current module
            String path = DataRefManager.getProjectAbsolutePath();
            path += DataRefManager.getInstance().currentModuleRes.getPath().replace(":", "/");
            path += ProjectsPathUtils.VALUES_PATH;
            for (String p : Files.listFile(path)) if (p.endsWith(".xml")) listResForAdd.add(p);
        }

        for (String pathModuleRes : MP.getRefToOtherModule()) {
            for (ModuleRes mr : DataRefManager.getInstance().listModuleRes) {
                if (!mr.getPath().equals(pathModuleRes)) continue;
                String path = DataRefManager.getProjectAbsolutePath();
                path += mr.getPath().replace(":", "/");
                path += ProjectsPathUtils.VALUES_PATH;
                for (String p : Files.listFile(path)) if (p.endsWith(".xml")) listResForAdd.add(p);
            }
        }
    }

    private void events() {
        binding.imgSwitch.setOnClickListener(v -> binding.editRef.setText(currentValue));
        binding.editRef.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        binding.tilRef.setError(null);
                        if (editable.toString().trim().isEmpty()) {
                            new Handler(Looper.getMainLooper())
                                    .postDelayed(() -> binding.editRef.showDropDown(), 43);
                        } else if (!ResourcesValuesFixer.matchesToDefaultRefRes(
                                editable.toString())) {
                            binding.tilRef.setError(
                                    C.getString(R.string.warning_only_ref_are_supported_here));
                        }
                        if (editable.toString().equals(currentValue))
                            binding.imgSwitch.setVisibility(View.GONE);
                        else binding.imgSwitch.setVisibility(View.VISIBLE);
                        showValue(editable.toString());
                    }
                });

        binding.editValue.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        int color = ResourcesValuesFixer.getColor(C, editable.toString());
                        binding.btnPickColor.setBackgroundColor(color);
                    }
                });

        binding.btnPrevious.setOnClickListener(
                v -> {
                    float p = binding.discreteRangeSlider.getValues().get(0);
                    if (p > 1) {
                        p--;
                        binding.discreteRangeSlider.setValues(new Float[] {p});
                    }
                });

        binding.btnNext.setOnClickListener(
                v -> {
                    float p = binding.discreteRangeSlider.getValues().get(0);
                    if (p < binding.discreteRangeSlider.getValueTo()) {
                        p++;
                        binding.discreteRangeSlider.setValues(new Float[] {p});
                    }
                });

        binding.discreteRangeSlider.addOnChangeListener(
                (slider, value, fromUser) -> {
                    int position = (int) value;
                    binding.editRef.setText(listAdapter.get(position - 1));
                });

        binding.btnPickColor.setOnClickListener(
                v1 -> {
                    final ColorPickerView picker = new ColorPickerView(C);
                    picker.setColor(
                            ResourcesValuesFixer.getColor(
                                    C, binding.editValue.getText().toString()));
                    picker.showAlpha(true);
                    picker.showHex(true);
                    picker.showPreview(true);

                    DialogBuilder.getDialogBuilder(C, null, null)
                            .setView(picker)
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(
                                    "OK",
                                    (dialog, which) -> {
                                        if (binding.editValue.isEnabled())
                                            if (binding.editRef
                                                    .getText()
                                                    .toString()
                                                    .trim()
                                                    .isEmpty())
                                                binding.editRef.setText(
                                                        "#"
                                                                + Integer.toHexString(
                                                                        picker.getColor()));
                                            else
                                                binding.editValue.setText(
                                                        "#"
                                                                + Integer.toHexString(
                                                                        picker.getColor()));
                                        else
                                            binding.editRef.setText(
                                                    "#" + Integer.toHexString(picker.getColor()));
                                    })
                            .show();
                });

        binding.btnImports.setOnClickListener(
                v2 -> {
                    Toast.makeText(C, "soon", Toast.LENGTH_SHORT).show();
                    /* final PopupMenu popupMenu = new PopupMenu(C, v2);
                    popupMenu.setForceShowIcon(true);
                    popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Internal Memory");
                    popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Icon Pack");
                    popupMenu.setOnMenuItemClickListener(
                            item -> {
                                switch (item.getItemId()) {
                                    case 1:
                                        //
                                        break;
                                    case 2:
                                        //
                                        break;
                                }
                                return true;
                            });
                    show();*/
                });
    }

    private void showValue(String text) {
        try {
            binding.tvInfo.setText("...");
            binding.viewFlipper.setDisplayedChild(0);
            binding.editValue.setText(ResourcesValuesFixer.getValuesAsString(C, text));

            boolean refToRes = ResourcesValuesFixer.matchesToDefaultRefRes(text);
            binding.editValue.setEnabled(refToRes);
            if (text.trim().isEmpty()) binding.editValue.setEnabled(true);
            else if (refToRes
                    && (text.startsWith("@string/")
                            || text.startsWith("@bool/")
                            || text.startsWith("@color/")
                            || text.startsWith("@dimen/")
                            || text.startsWith("@integer/"))) {
                if (!ResourcesValuesFixer.exists(text)) {
                    binding.tvInfo.setText(R.string.warning_ref_not_exist_and_will_be_added);
                }
            }

            if (ResourcesValuesFixer.existsInAndroidRes(text)) {
                binding.editValue.setEnabled(false);
            }

            if (text.trim().startsWith("@drawable")
                    || text.trim().startsWith("@mipmap")
                    || text.trim().startsWith("@android:drawable")) {
                binding.imgValue.setImageDrawable(ResourcesValuesFixer.getDrawable(C, text));
                binding.viewFlipper.setDisplayedChild(1);
                if (typeValue.contains("drawable")) {
                    binding.viewFlipper.setDisplayedChild(1);
                    binding.btnPickColor.setVisibility(View.GONE);
                    binding.btnImports.setVisibility(View.VISIBLE);
                }
            } else {
                binding.viewFlipper.setDisplayedChild(0);
                if (typeValue.contains("color")) {
                    binding.btnPickColor.setVisibility(View.VISIBLE);
                    binding.btnImports.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultSetting() {
        binding.imgSwitch.setVisibility(View.GONE);
        if (typeValue.contains("color")) binding.btnPickColor.setVisibility(View.VISIBLE);
        else binding.btnPickColor.setVisibility(View.GONE);
        if (typeValue.contains("drawable")) binding.btnImports.setVisibility(View.VISIBLE);
        else binding.btnImports.setVisibility(View.GONE);

        binding.discreteRangeSlider.setValueTo((float) listAdapter.size());
        if (typeValue.contains("drawable")) binding.viewFlipper.setDisplayedChild(1);
        else binding.viewFlipper.setDisplayedChild(0);

        final CustomAutoCompleteAdapter adapter =
                new CustomAutoCompleteAdapter(
                        C, android.R.layout.simple_dropdown_item_1line, listAdapterAssist);
        binding.editRef.setAdapter(adapter);
    }

    private void loadDefault() {
        builder.setTitle(typeValue.substring(1).toUpperCase());
        binding.tilRef.setHint(attr);

        if (ResourcesValuesFixer.matchesToDefaultRefRes(currentValue))
            binding.editRef.setText(currentValue);
        else binding.editValue.setText(currentValue);
    }

    private void init() {
        builder = DialogBuilder.getDialogBuilder(C, null, null);
        builder.setView((View) binding.getRoot());
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(
                R.string.save,
                (d, v) -> {
                    String val = save();
                    if (val == null) {
                        val = "";
                        Toast.makeText(
                                        C,
                                        C.getString(R.string.attribute)
                                                + " : "
                                                + attr
                                                + " "
                                                + C.getString(R.string.deleted),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                    listener.onSave(val);
                });
    }

    private String save() {
        String ref = binding.editRef.getText().toString();
        String value = binding.editValue.getText().toString();

        try {
            if (ref.trim().isEmpty()) {
                if (!value.trim().isEmpty()) return value;
                return null;
            }

            if (typeValue.equals("@anim")
                    || typeValue.equals("@layout")
                    || typeValue.equals("@style")
                    || typeValue.equals("@attr")
                    || typeValue.equals("@menu")
                    || typeValue.equals("@raw")
                    || typeValue.contains("drawable")) return ref;

            if (!ResourcesValuesFixer.existsInAndroidRes(ref)) {
                if (typeValue.equals("@string")
                        || typeValue.equals("@bool")
                        || typeValue.equals("@dimen")
                        || typeValue.equals("@integer")
                        || typeValue.contains("@color")) {
                    if (ResourcesValuesFixer.existsInProjectRes(ref)) {
                        if (ref.contains("/")) ref = ref.split("\\/")[1];
                        for (String path : listResForAdd) {
                            final XmlManager xmlFile = new XmlManager(C);
                            xmlFile.initializeFromPath(path);
                            if (!xmlFile.isInitialized) continue;

                            ArrayList<Element> listE = xmlFile.getElementsByTagName("resources");
                            if (listE.size() == 0) continue;
                            Element e = listE.get(0);

                            for (Element child : XmlManager.getAllFirstChildFromElement(e)) {
                                if (child.getAttribute("name").equals(ref)) {
                                    if (!child.getTextContent().equals(value)) {
                                        child.setTextContent(value);
                                        xmlFile.saveAllModif();
                                        LoggerRes.reloadResRef();
                                    }
                                    return binding.editRef.getText().toString();
                                }
                            }
                        }
                        addToResNewRef();
                    } else addToResNewRef();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return binding.editRef.getText().toString();
    }

    private void addToResNewRef() {
        String ref = binding.editRef.getText().toString();
        String value = binding.editValue.getText().toString();

        String resType = ref.substring(1).split("\\/")[0];
        String resName = ref.split("\\/")[1];
        String pathToRes = getPathToRes(resType);

        try {
            XmlManager xmlFile = new XmlManager(C);
            if (!xmlFile.initializeFromPath(pathToRes)) return;
            LoggerRes.onSaveRequested();

            Element newRes = xmlFile.getDocument().createElement(resType);
            newRes.setAttribute("name", resName);
            newRes.setTextContent(value);

            xmlFile.getElement("resources", 0).appendChild(newRes);
            xmlFile.saveAllModif();

            LoggerRes.reloadResRef();
        } catch (Exception err) {
            // ignore can not add
        }
    }

    private String getPathToRes(String resType) {
        String path = DataRefManager.getInstance().currentModuleProject.getAbsolutePath();
        path += ProjectsPathUtils.VALUES_PATH + "/" + resType + "s.xml";
        if (Files.isFile(path)) return path;

        for (var pathToOtherModule :
                DataRefManager.getInstance().currentModuleProject.getRefToOtherModule()) {
            for (var module : DataRefManager.getInstance().listModuleProject) {
                if (pathToOtherModule.equals(module.getPath())) {
                    path =
                            module.getAbsolutePath()
                                    + ProjectsPathUtils.VALUES_PATH
                                    + "/"
                                    + resType
                                    + "s.xml";
                    if (Files.isFile(path)) return path;
                    break;
                }
            }
        }

        String newRes = """
<?xml version="1.0" encoding="UTF-8"?>
<resources/>
        """;
        Files.writeFile(path, newRes);
        return path;
    }

    public void show() {
        builder.show();
    }

    public void setOnSaveListener(OnSaveListener listener) {
        this.listener = listener;
    }

    public interface OnSaveListener {
        public void onSave(String finalValue);
    }
}
