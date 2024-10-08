package jkas.androidpe.layoutUiDesigner.dialog.attrs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import jkas.androidpe.layoutUiDesigner.databinding.DialogLayoutGravityBinding;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.adapters.AttrViewAdapter;
import jkas.androidpe.resourcesUtils.adapters.CustomAutoCompleteAdapter;
import jkas.androidpe.resourcesUtils.attrs.AllAttrBase;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DialogLayoutGravity {
    public static final String GRAVITY = "gravity";
    public static final String LAYOUT_GRAVITY = "layout_gravity";

    private Context C;
    private OnChangedListener listener;
    private Element element;
    private DialogLayoutGravityBinding binding;
    private MaterialAlertDialogBuilder dialog;
    private String type;
    private ArrayList<String> listAdapter = new ArrayList<>();
    private ArrayList<ImageView> listDirectionImg = new ArrayList<>();
    private ArrayList<String> listDirectionValue = new ArrayList<>();
    private ArrayList<CheckBox> listCB = new ArrayList<>();

    private String currentDirectionValue = "";
    private boolean TCFAS = false; // Text changed from another source.

    public DialogLayoutGravity(Context c) {
        this.C = c;
        initAdapter();
    }

    private void initAdapter() {
        listAdapter.add("left");
        listAdapter.add("top");
        listAdapter.add("right");
        listAdapter.add("bottom");
        listAdapter.add("start");
        listAdapter.add("end");
        listAdapter.add("center");
        listAdapter.add("center_vertical");
        listAdapter.add("center_horizontal");
        listAdapter.add("fill");
        listAdapter.add("fill_vertical");
        listAdapter.add("fill_horizontal");
        listAdapter.add("clip_vertical");
        listAdapter.add("clip_horizontal");
    }

    private void events() {
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.setPositiveButton(R.string.save, (d, v) -> save());

        CustomAutoCompleteAdapter adapter =
                new CustomAutoCompleteAdapter(
                        C, android.R.layout.simple_dropdown_item_1line, listAdapter);
        binding.autoComp.setTokenizer(new AttrViewAdapter.SpaceTokenizer());
        binding.autoComp.setAdapter(adapter);
        binding.autoComp.setThreshold(1);

        binding.autoComp.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        binding.til.setError(null);
                        if (!TCFAS) {
                            if (editable.toString().trim().length() == 0) {
                                new Handler(Looper.getMainLooper())
                                        .postDelayed(() -> binding.autoComp.showDropDown(), 50);
                            } else {
                                boolean exists = true;
                                for (var item : editable.toString().split("\\|")) {
                                    if (!Arrays.asList(AllAttrBase.VALUES_GRAVITY).contains(item)) {
                                        exists = false;
                                        break;
                                    }
                                }
                                if (!exists) {
                                    binding.til.setError(C.getString(R.string.invalide_entry));
                                }
                            }
                            initValues();
                        }
                        TCFAS = false;
                    }
                });

        cbChackable(binding.cbStart);
        cbChackable(binding.cbEnd);
        cbChackable(binding.cbCenterVertical);
        cbChackable(binding.cbCenterHorizontal);
        cbChackable(binding.cbFill);
        cbChackable(binding.cbFillVertical);
        cbChackable(binding.cbFillHorizontal);
        cbChackable(binding.cbClipVertical);
        cbChackable(binding.cbClipHorizontal);

        direction(binding.linGravityImg1, "left|top");
        direction(binding.linGravityImg2, "top|center_horizontal");
        direction(binding.linGravityImg3, "top|right");
        direction(binding.linGravityImg4, "left|center_vertical");
        direction(binding.linGravityImg5, "center");
        direction(binding.linGravityImg6, "right|center_vertical");
        direction(binding.linGravityImg7, "bottom|left");
        direction(binding.linGravityImg8, "bottom|center_horizontal");
        direction(binding.linGravityImg9, "bottom|right");
    }

    private void initValues() {
        String value = getValue();
        for (var cb : listCB) cb.setChecked(value.contains(cb.getText().toString()));
        setDirectionView(null, false);
        for (int i = 0; i < listDirectionValue.size(); i++) {
            var s = listDirectionValue.get(i);
            if (value.equals(s)) {
                setDirectionView(listDirectionImg.get(i), true);
                break;
            }
        }
    }

    private void cbChackable(final CheckBox cb) {
        listCB.add(cb);
        cb.setOnTouchListener(
                (v, m) -> {
                    TCFAS = true;
                    return false;
                });
        cb.setOnCheckedChangeListener(
                (c, b) -> {
                    if (TCFAS) {
                        if (b) setValue(cb.getText().toString());
                        else removeValue(cb.getText().toString());
                    }
                });
    }

    private void direction(final ImageView img, final String value) {
        listDirectionImg.add(img);
        listDirectionValue.add(value);
        img.setOnTouchListener(
                (v, m) -> {
                    TCFAS = true;
                    return false;
                });
        img.setOnClickListener(
                v -> {
                    binding.autoComp.setText("");
                    if (currentDirectionValue.equals(value)) {
                        currentDirectionValue = "";
                        removeValue(value);
                        setDirectionView(v, false);
                    } else {
                        currentDirectionValue = value;
                        setValue(value);
                        setDirectionView(v, true);
                    }
                });
    }

    private void setDirectionView(View view, boolean enabled) {
        for (ImageView img : listDirectionImg) img.setBackground(null);
        if (!enabled) return;

        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(Color.TRANSPARENT);
        gradient.setStroke(5, ResourcesValuesFixer.getColor(C, "?colorOnSurface"));
        gradient.setCornerRadius(360f);
        view.setBackground(gradient);
    }

    private void removeValue(String value) {
        String val = getValue();
        if (value.equals("center")) {
            if (val.endsWith("|center")) val = val.replace("|center", "");
            val = val.replace("center|", "");
        } else if (value.equals("fill")) {
            if (val.endsWith("|fill")) val = val.replace("|fill", "");
            val = val.replace("fill|", "");
        } else val = val.replace(value, "");
        val = val.replace("||", "|");
        if (val.startsWith("|")) val = val.substring(1);
        if (val.endsWith("|")) val = val.substring(0, val.lastIndexOf("|"));
        binding.autoComp.setText(val);
    }

    private void setValue(String value) {
        binding.autoComp.setText(getValue().isEmpty() ? value : getValue() + "|" + value);

        boolean verif = false;
        for (String val : listDirectionValue) {
            if (val.equals(value)) {
                verif = true;
                break;
            }
        }
        if (!verif) setDirectionView(null, false);
    }

    private String getValue() {
        return binding.autoComp.getText().toString().trim();
    }

    private void save() {
        String text = binding.autoComp.getText().toString();
        if (text.trim().isEmpty()) element.removeAttribute("android:" + type);
        else if (binding.til.getError() == null) {
            element.setAttribute("android:" + type, text);
        }
        listener.onChange();
    }

    private void initDefault() {
        binding.til.setError(null);
        binding.autoComp.setText(element.getAttribute("android:" + type));
        binding.autoComp.clearFocus();
    }

    private void init() {
        binding = DialogLayoutGravityBinding.inflate(LayoutInflater.from(C));
        dialog = new MaterialAlertDialogBuilder(C);
        dialog.setTitle(type.toUpperCase());
        dialog.setView(binding.getRoot());
        binding.til.setHint("android:" + type);

        events();
        initDefault();
    }

    public void show(Element element, String type) {
        this.element = element;
        this.type = type;

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
