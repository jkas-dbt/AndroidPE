package jkas.androidpe.resValuesModifier;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rarepebble.colorpicker.ColorPickerView;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import jkas.androidpe.projectUtils.current.Environment;
import jkas.androidpe.resValuesModifier.databinding.LayoutResValuesModifierBinding;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dialog.DialogValuesResAddItem;
import jkas.androidpe.resourcesUtils.dialog.DialogDeleteElement;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * @author JKas
 */
public class ResValuesModifier {
    private OnEventListener listener;
    private String TAG;
    private XmlManager xmlFile;
    private Context C;
    private LayoutResValuesModifierBinding binding;
    private ArrayList<Element> listElement = new ArrayList<>();
    private Element element;
    private boolean viewEnable = false; // will be true when path is res(values) file
    public String CODE, tmpPath;
    private float position = 0f;

    public ResValuesModifier(Context c) {
        this.C = c;
        this.xmlFile = new XmlManager(C);
        TAG = "resValue" + View.generateViewId();
        tmpPath = Environment.DEFAULT_ANDROIDPE_TMP_DATA + "/" + TAG + "/xmlFile.xml";
        init();
    }

    public void parseCode(String code) {
        CODE = code;
        Files.writeFile(tmpPath, CODE);
        if (!viewEnable) return;
        reloadData();
    }

    public String getCode() {
        return CODE;
    }

    private void init() {
        binding = LayoutResValuesModifierBinding.inflate(LayoutInflater.from(C));
    }

    public View getView() {
        viewEnable = true;
        return (View) binding.getRoot();
    }

    private void reloadData() {
        if (!isXmlFileInitialized()) return;
        loadData();
        initDefaultData();
        events();
        if (listElement.size() == 0) {
            binding.icDelete.setEnabled(false);
            binding.TILname.setEnabled(false);
            binding.TILvalue.setEnabled(false);
            binding.textInputName.setText("");
            binding.textInputValue.setText("");
            binding.tvTemplate.setVisibility(View.GONE);
            return;
        }
        binding.icDelete.setEnabled(true);
        binding.TILname.setEnabled(true);
        binding.TILvalue.setEnabled(true);
        binding.tvTemplate.setVisibility(View.VISIBLE);
        // in case we switch again to this View and in the CodeEditor
        // some tag has been added and/or removed
        if (position >= listElement.size()) position = (float) listElement.size() - 1;
        binding.discreteRangeSlider.setValues(new Float[] {position});
        binding.tvCurrentPosition.setText("0");
        updatePosition();
    }

    private boolean containsError() {
        XmlPullParser parser = null;
        String info = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            Reader reader = (Reader) new StringReader(Files.readFile(tmpPath));
            parser.setInput(reader);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        if (!parser.getText().trim().isEmpty()) {
                            if (parser.getText().matches(".* name=\\\".*\\\".*")
                                    && (parser.getText().contains(">")
                                            || parser.getText().contains("<"))) {
                                info =
                                        (" Row "
                                                + parser.getLineNumber()
                                                + ", Column "
                                                + parser.getColumnNumber()
                                                + " : The text should not appear inside the label = "
                                                + parser.getText());
                                throw new Exception(info);
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
            reader.close();
            return false;
        } catch (Exception e) {
            info = "**";
            info +=
                    "XML code analyzer"
                            + " Row "
                            + parser.getLineNumber()
                            + ", Column "
                            + parser.getColumnNumber()
                            + " : An error occurred while parsing \n\n"
                            + e;
        }

        binding.tvMoreInfo.setText(info == null || info.trim().equals("") ? "***" : info);
        return true;
    }

    private boolean isXmlFileInitialized() {
        xmlFile.initializeFromPath(tmpPath);
        if (containsError()) binding.viewFlipper.setDisplayedChild(1);
        else binding.viewFlipper.setDisplayedChild(0);
        return xmlFile.isInitialized;
    }

    private void initDefaultData() {
        // progressBar
        binding.discreteRangeSlider.setValueFrom(0);
        if (listElement.size() < 2) {
            binding.btnPrevious.setEnabled(false);
            binding.btnNext.setEnabled(false);
            binding.discreteRangeSlider.setEnabled(false);
            binding.discreteRangeSlider.setValueTo(100);
            binding.discreteRangeSlider.setValues(new Float[] {0f});
        } else {
            binding.btnPrevious.setEnabled(true);
            binding.btnNext.setEnabled(true);
            binding.discreteRangeSlider.setValueTo(listElement.size() - 1);
            binding.discreteRangeSlider.setEnabled(true);
        }
    }

    private void updatePosition() {
        if (listElement.size() == 0) return;
        binding.textInputName.removeTextChangedListener(nameTW);
        binding.textInputValue.removeTextChangedListener(valueTW);

        element = listElement.get((int) position);
        binding.textInputName.setText(element.getAttribute("name"));
        binding.textInputValue.setText(element.getTextContent());

        // text info
        binding.tvCurrentPosition.setText(
                element.getTagName() + " : " + (((int) position) + 1) + "/" + listElement.size());
        if (element.getTagName().equals("color")) {
            binding.tvTemplate.setVisibility(View.VISIBLE);
            initTemplate(element.getTextContent());
        } else binding.tvTemplate.setVisibility(View.GONE);

        binding.textInputName.addTextChangedListener(nameTW);
        binding.textInputValue.addTextChangedListener(valueTW);
    }

    private void loadData() {
        listElement.clear();
        listElement.addAll(xmlFile.getElementsByTagName("string"));
        listElement.addAll(xmlFile.getElementsByTagName("color"));
        listElement.addAll(xmlFile.getElementsByTagName("dimen"));
    }

    private void events() {
        ResCodeUtils.ResAndCodeFilesFixer.fixXmlIdNameAndAssign(
                binding.TILname, binding.textInputName);

        binding.btnAdd.setOnClickListener(
                v -> {
                    new DialogValuesResAddItem(
                                    C,
                                    xmlFile.getDocument(),
                                    xmlFile.getElement("resources", 0),
                                    tmpPath)
                            .setOnChildAddedListener(
                                    added -> {
                                        if (added) {
                                            xmlFile.saveAllModif();
                                            saveAll();
                                            reloadData();
                                        } else {
                                            Toast.makeText(
                                                            C,
                                                            R.string.cant_create,
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    })
                            .show();
                });

        binding.discreteRangeSlider.addOnChangeListener(
                (slider, value, fromUser) -> {
                    position = (int) value;
                    if ((int) position == listElement.size()) position--;
                    else updatePosition();
                });
        binding.btnNext.setOnClickListener(
                v -> {
                    if ((position + 1) < listElement.size()) {
                        position++;
                        binding.discreteRangeSlider.setValues(new Float[] {position});
                    }
                });
        binding.btnPrevious.setOnClickListener(
                v -> {
                    if (position > 0f) {
                        position--;
                        binding.discreteRangeSlider.setValues(new Float[] {position});
                    }
                });

        binding.icDelete.setOnClickListener(
                v -> {
                    new DialogDeleteElement(C, element)
                            .setOnDeleteListener(
                                    deleted -> {
                                        if (!deleted) {
                                            Toast.makeText(
                                                            C,
                                                            R.string.cant_delete,
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                            return;
                                        }
                                        listElement.remove(position);
                                        if (position > 0) position--;
                                        if ((int) position == listElement.size()) position--;
                                        saveAll();
                                        Toast.makeText(C, R.string.deleted, Toast.LENGTH_SHORT)
                                                .show();
                                        reloadData();
                                    })
                            .show();
                });

        binding.tvTemplate.setOnClickListener(
                v -> prepareDialogBuilderColor(new MaterialAlertDialogBuilder(C)));
    }

    private void save() {
        if (binding.viewFlipper.getDisplayedChild() == 1) return;
        if (binding.TILname.getError() == null)
            element.setAttribute("name", binding.textInputName.getText().toString());
        element.setTextContent(binding.textInputValue.getText().toString());
        saveAll();
    }

    private void saveAll() {
        xmlFile.saveAllModif();
        if (listener != null) listener.onTextChanged(xmlFile.code);
    }

    public void saveRequested() {
        if (!viewEnable) return;
        save();
    }

    private void prepareDialogBuilderColor(MaterialAlertDialogBuilder builder) {
        final ColorPickerView picker = new ColorPickerView(C);
        picker.setColor(ResourcesValuesFixer.getColor(C, element.getTextContent()));
        picker.showAlpha(true);
        picker.showHex(true);
        picker.showPreview(true);

        builder.setTitle(null);
        builder.setView(picker);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(
                "OK",
                (dialog, which) ->
                        binding.textInputValue.setText(
                                "#" + Integer.toHexString(picker.getColor())));
        builder.show();
    }

    // Name & Value TextWatcher
    private TextWatcher nameTW =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    saveRequested();
                }
            };

    private TextWatcher valueTW =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (element.getTagName().equals("color")) initTemplate(editable.toString());
                    saveRequested();
                }
            };

    private void initTemplate(String txt) {
        if (!element.getTagName().equals("color")) return;
        binding.tvTemplate.setBackgroundColor(Color.TRANSPARENT);
        try {
            binding.tvTemplate.setBackgroundColor(ResourcesValuesFixer.getColor(C, txt));
            if (txt.startsWith("#")) {
                binding.tvTemplate.setText("");
            } else if (txt.startsWith("@") && txt.startsWith("?")) {
                binding.tvTemplate.setText(R.string.reference);
            }
            saveRequested();
        } catch (Exception e) {
            binding.tvTemplate.setText(R.string.error);
            binding.tvTemplate.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        public void onTextChanged(String code);
    }
}
