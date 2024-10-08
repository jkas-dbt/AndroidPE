package jkas.androidpe.resourcesUtils.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.databinding.LayoutAttrModifierBinding;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author JKas
 */
public class DialogBottomSheetAttrModifier {
    public LayoutAttrModifierBinding binding;
    private Node CLONE = null;
    public OnTaskRequested listener;
    private Context C;
    public Element element;
    public BottomSheetDialog BSD;
    private boolean showLayout = true, showDeclared = true, showAll = true;
    public static DialogBottomSheetAttrModifier instance;

    private DialogBottomSheetAttrModifier(Context context) {
        C = context;
        binding = LayoutAttrModifierBinding.inflate(LayoutInflater.from(C));
        BSD = new BottomSheetDialog(C);
        BSD.setContentView(binding.getRoot());

        init();
        setEvents();
    }

    public static DialogBottomSheetAttrModifier getInstance(Context C) {
        if (instance == null) instance = new DialogBottomSheetAttrModifier(C);
        return instance;
    }

    private void setEvents() {
        BSD.setOnCancelListener(dialog -> listener.onRefresh());
        binding.icAdd.setOnClickListener(v -> listener.onAdd(v, element));
        binding.icDelete.setOnClickListener(v2 -> listener.onDelete(element));

        binding.tvCopy.setOnClickListener(
                v -> {
                    CLONE = element.cloneNode(true);
                    copyPast();
                    Toast.makeText(C, R.string.copied, Toast.LENGTH_SHORT).show();
                });

        binding.tvPast.setOnClickListener(
                v -> {
                    element.appendChild(CLONE.cloneNode(true));
                    listener.onRefresh();
                    Toast.makeText(C, R.string.pasted, Toast.LENGTH_SHORT).show();
                });

        ResCodeUtils.ResAndCodeFilesFixer.fixXmlIdNameAndAssign(binding.tilId, binding.editId);
        binding.editId.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.toString().trim().isEmpty())
                            element.removeAttribute("android:id");
                        else element.setAttribute("android:id", "@+id/" + editable.toString());
                    }
                });

        binding.icLayout.setOnClickListener(
                v -> {
                    showLayout = !showLayout;
                    if (showLayout) {
                        binding.linLayout.setVisibility(View.VISIBLE);
                        binding.icLayout.setRotation(180);
                    } else {
                        binding.linLayout.setVisibility(View.GONE);
                        binding.icLayout.setRotation(0);
                    }
                });

        binding.icLayout.setOnClickListener(
                v -> {
                    if (binding.linLayout.getVisibility() == View.VISIBLE) {
                        binding.linLayout.setVisibility(View.GONE);
                        binding.icLayout.setRotation(0);
                    } else {
                        binding.linLayout.setVisibility(View.VISIBLE);
                        binding.icLayout.setRotation(180);
                    }
                });

        binding.icDeclared.setOnClickListener(
                v -> {
                    if (binding.linDeclaredAttributes.getVisibility() == View.VISIBLE) {
                        binding.linDeclaredAttributes.setVisibility(View.GONE);
                        binding.icDeclared.setRotation(0);
                    } else {
                        binding.linDeclaredAttributes.setVisibility(View.VISIBLE);
                        binding.icDeclared.setRotation(180);
                    }
                });

        binding.icAll.setOnClickListener(
                v -> {
                    if (binding.linAllAttributes.getVisibility() == View.VISIBLE) {
                        binding.linAllAttributes.setVisibility(View.GONE);
                        binding.icAll.setRotation(0);
                    } else {
                        binding.linAllAttributes.setVisibility(View.VISIBLE);
                        binding.icAll.setRotation(180);
                    }
                });
    }

    public DialogBottomSheetAttrModifier show() {
        BSD.show();
        copyPast();
        binding.tilId.clearFocus();
        return this;
    }

    private void copyPast() {
        if (CLONE != null) {
            binding.tvPast.setEnabled(true);
            binding.tvPast.setAlpha(1.0f);
        } else {
            binding.tvPast.setEnabled(false);
            binding.tvPast.setAlpha(0.5f);
        }
    }

    public DialogBottomSheetAttrModifier initElement(Element e) {
        element = e;
        initData();
        return this;
    }

    private void initData() {
        defaultValues();
    }

    private void defaultValues() {
        binding.tvTitle.setText(
                Files.getNameFromAbsolutePath(element.getTagName().replace(".", "/")));
        if (element.getTagName().contains("."))
            binding.tvSubTitle.setText(
                    "Pkg : " + Files.getPrefixPath(element.getTagName().replace(".", "/")));
        binding.linAllAttributes.removeAllViews();
        binding.linLayout.removeAllViews();

        try {
            String id = element.getAttribute("android:id");
            if (id == null) return;
            if (id.contains("/")) id = id.split("\\/")[1];
            binding.editId.setText(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        setBg(binding.tvCopy);
        setBg(binding.tvPast);

        binding.icLayout.setRotation(180);
        binding.icDeclared.setRotation(180);
        binding.icAll.setRotation(180);
    }

    /** background theme for buttons */
    private void setBg(View view) {
        Class classAttr = ResourcesValuesFixer.MaterialR.getAttrClass();
        int id = CodeUtil.getSystemResourceId(classAttr, "colorOnSurface");
        int color = Color.WHITE;
        if (id != -1) color = ResCodeUtils.getColorFromResolveAttribute(C, id);

        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(Color.TRANSPARENT);
        gradient.setStroke(2, color);
        gradient.setCornerRadius(16f);

        ColorStateList colorState = ColorStateList.valueOf(Color.parseColor("#777777"));
        RippleDrawable ripple = new RippleDrawable(colorState, gradient, null);
        view.setBackground(ripple);
        view.setElevation(10);
    }

    public DialogBottomSheetAttrModifier setOnTaskRequested(OnTaskRequested listener) {
        this.listener = listener;
        return this;
    }

    public interface OnTaskRequested {
        public void onAdd(View v, Element e);

        public void onDelete(Element e);

        public void onRefresh();
    }
}
