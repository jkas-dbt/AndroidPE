package jkas.androidpe.layoutUiDesigner.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import jkas.androidpe.layoutUiDesigner.attrsSetter.AdditionnalAttrSetter;
import jkas.androidpe.layoutUiDesigner.attrsSetter.CommonAttrSetter;
import jkas.androidpe.layoutUiDesigner.attrsSetter.LayoutsAttrSetter;
import jkas.androidpe.layoutUiDesigner.databinding.DialogAttrSetterBinding;
import jkas.androidpe.layoutUiDesigner.tools.AndroidXmlParser;
import jkas.androidpe.layoutUiDesigner.tools.RefViewElement;
import jkas.androidpe.layoutUiDesigner.tools.ViewCreator;
import jkas.androidpe.layoutUiDesigner.utils.Utils;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.dialog.DialogDeleteElement;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author JKas
 */
public class DialogBottomSheetAttrSetter {
    public Context C;

    public static final int ADD_INSIDE = 0;
    public static final int ADD_BEFORE = 1;
    public static final int ADD_SURROUND = 2;

    private OnAnyTaskRequested listener;
    public DialogAttrSetterBinding binding;
    public View viewTemplate;
    public Element element;
    public BottomSheetDialog BSD;

    public String viewName;

    private boolean isParent = false;
    private boolean refreshNeeded = false;
    private int pastCounter = 0;

    private AdditionnalAttrSetter additionalAttrSetter;
    private CommonAttrSetter commonAttrSetter;
    private LayoutsAttrSetter layoutsAttrSetter;

    private boolean visibilityModel = false;

    public DialogBottomSheetAttrSetter(Context c) {
        this.C = c;
        this.BSD = new BottomSheetDialog(C);
        init();
        events();
        listener();
    }

    private void listener() {
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
                        listener.onSave();
                    }
                });
    }

    private void events() {
        binding.icDelete.setOnClickListener(
                v -> {
                    DialogDeleteElement dialog = new DialogDeleteElement(C, element);
                    dialog.setOnDeleteListener(
                            deleted -> {
                                if (deleted) {
                                    listener.onRefresh();
                                    BSD.cancel();
                                }
                            });
                    dialog.show();
                });

        binding.icAdd.setOnClickListener(
                v2 -> {
                    final PopupMenu popupMenu = new PopupMenu(C, v2);
                    popupMenu.setForceShowIcon(true);
                    popupMenu
                            .getMenu()
                            .add(Menu.NONE, 1, Menu.NONE, C.getString(R.string.inside))
                            .setIcon(R.drawable.ic_add);
                    popupMenu
                            .getMenu()
                            .add(Menu.NONE, 2, Menu.NONE, C.getString(R.string.before))
                            .setIcon(R.drawable.ic_add);
                    popupMenu
                            .getMenu()
                            .add(Menu.NONE, 3, Menu.NONE, C.getString(R.string.surround))
                            .setIcon(R.drawable.ic_fullscreen);
                    popupMenu.setOnMenuItemClickListener(
                            item -> {
                                boolean isPossible = false;
                                switch (item.getItemId()) {
                                    case 1:
                                        isPossible = listener.onAdd(element, ADD_INSIDE);
                                        break;
                                    case 2:
                                        isPossible = listener.onAdd(element, ADD_BEFORE);
                                        break;
                                    case 3:
                                        isPossible = listener.onAdd(element, ADD_SURROUND);
                                        break;
                                }
                                if (isPossible) BSD.cancel();
                                return true;
                            });
                    popupMenu.show();
                });

        binding.tvPrivious.setOnClickListener(
                v -> {
                    var pair = listener.onRefViewElementNeeded().getPreviousSibling(element);
                    if (pair == null)
                        Toast.makeText(C, R.string.not_found, Toast.LENGTH_SHORT).show();
                    else prepare(pair);
                });

        binding.tvPrivious.setOnLongClickListener(
                v -> {
                    if (Utils.eCopied == null) {
                        Toast.makeText(C, R.string.cant_load, Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (element.getParentNode() != null) {
                        element.getParentNode()
                                .insertBefore(Utils.eCopied.cloneNode(true), element);
                        pastCounter++;
                        binding.tvPast.setText(C.getString(R.string.pasted) + " " + pastCounter);
                        if (!refreshNeeded) refreshNeeded = true;
                        listener.onSave();
                        Toast.makeText(C, R.string.pasted, Toast.LENGTH_SHORT).show();
                    } else {
                        DialogBuilder.getDialogBuilder(
                                        C,
                                        C.getString(R.string.warning),
                                        C.getString(R.string.warning_view_not_parent))
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                    return true;
                });

        binding.tvNext.setOnClickListener(
                v -> {
                    var pair = listener.onRefViewElementNeeded().getNextSibling(element);
                    if (pair == null)
                        Toast.makeText(C, R.string.not_found, Toast.LENGTH_SHORT).show();
                    else prepare(pair);
                });

        binding.tvFirstChild.setOnClickListener(
                v -> {
                    var pair = listener.onRefViewElementNeeded().getFirstChild(element);
                    if (pair == null) {
                        Toast.makeText(C, R.string.not_found, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    prepare(pair);
                });

        binding.tvParent.setOnClickListener(
                v -> {
                    var pair = listener.onRefViewElementNeeded().getParent(element);
                    if (pair == null) {
                        Toast.makeText(C, R.string.not_found, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    prepare(pair);
                });

        binding.tvCopy.setOnClickListener(
                v -> {
                    Utils.eCopied = element.cloneNode(true);
                    pastCounter = 0;
                    Toast.makeText(C, R.string.copied, Toast.LENGTH_SHORT).show();
                });

        binding.icVisibility.setOnClickListener(
                v -> {
                    visibilityModel = !visibilityModel;
                    binding.relTemplate.setVisibility(visibilityModel ? View.VISIBLE : View.GONE);
                    binding.icVisibility.setImageResource(
                            visibilityModel
                                    ? R.drawable.ic_visibility_off
                                    : R.drawable.ic_visibility);
                });

        binding.tvPast.setOnClickListener(
                v -> {
                    if (Utils.eCopied == null) {
                        Toast.makeText(C, R.string.cant_load, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isParent) {
                        element.appendChild(Utils.eCopied.cloneNode(true));
                        pastCounter++;
                        binding.tvPast.setText(C.getString(R.string.pasted) + " " + pastCounter);
                        if (!refreshNeeded) refreshNeeded = true;
                        listener.onSave();
                    } else {
                        DialogBuilder.getDialogBuilder(
                                        C,
                                        C.getString(R.string.warning),
                                        C.getString(R.string.warning_unsaved_content))
                                .setPositiveButton(
                                        android.R.string.ok,
                                        (i1, i2) -> {
                                            element.appendChild(Utils.eCopied.cloneNode(true));
                                            pastCounter++;
                                            binding.tvPast.setText(
                                                    C.getString(R.string.pasted)
                                                            + " "
                                                            + pastCounter);
                                            if (!refreshNeeded) refreshNeeded = true;
                                            listener.onSave();
                                        })
                                .setNegativeButton(android.R.string.cancel, null)
                                .show();
                    }
                });

        BSD.setOnCancelListener(
                di -> {
                    if (refreshNeeded) listener.onRefresh();
                });
    }

    private void loadAttrData() {
        layoutsAttrSetter.init();
        commonAttrSetter.init();
        new Handler(Looper.getMainLooper()).postDelayed(() -> additionalAttrSetter.init(), 43);
    }

    private void isParent() {
        isParent = false;
        if (viewTemplate != null) isParent = viewTemplate instanceof ViewGroup;
        if (!isParent) {
            View v =
                    ViewCreator.create(
                            "null",
                            C,
                            element.getNodeName(),
                            XmlManager.getAllFirstChildFromElement(element).size() > 0);
            isParent = v instanceof ViewGroup;
        }

        binding.tvPast.setText(R.string.past);
    }

    public void setValueChanged() {
        listener.onSave();
        refreshNeeded = true;
        setVisibilityModel();
    }

    private void setVisibilityModel() {
        binding.relTemplate.removeAllViews();

        Element eP = null;
        Node nP = element.getParentNode();
        if (nP.getNodeType() == Node.ELEMENT_NODE) eP = (Element) nP.cloneNode(false);
        viewTemplate = ViewCreator.create("null", C, element.getNodeName(), false);
        viewTemplate.setPadding(8, 8, 8, 8);
        if (eP == null) {
            binding.relTemplate.addView(viewTemplate);
        } else {
            final View vP = ViewCreator.create("null", C, eP.getNodeName(), true);
            vP.setLayoutParams(CodeUtil.getLayoutParamsMM(0));
            eP.removeAttribute("android:layout_width");
            eP.removeAttribute("android:layout_height");
            eP.setAttribute("android:padding", "0dp");
            eP.setAttribute("android:layout_margin", "0dp");
            listener.onEdit(vP, eP);
            ((ViewGroup) vP).addView(viewTemplate);
            binding.relTemplate.addView(vP);
        }
        listener.onEdit(viewTemplate, element);
    }

    private void prepare(Element e) {
        element = e;

        pastCounter = 0;
        isParent();
        setVisibilityModel();

        String pkg = e.getNodeName();
        viewName = pkg;
        if (pkg.contains(".") && ResCodeUtils.isAValidePackageName(pkg)) {
            viewName = Files.getNameFromAbsolutePath(viewName.replace(".", "/"));
            pkg = Files.getPrefixPath(pkg.replace(".", "/")).replace("/", ".");
        } else {
            pkg = "...";
        }

        binding.tvTitle.setText(viewName);
        binding.tvSubTitle.setText(pkg);

        String id = element.getAttribute("android:id");
        id = id.startsWith("@+id/") ? id.split("\\/")[1] : id;
        binding.editId.setText(id);

        loadAttrData();
        refreshNeeded = false;
    }

    public void show(Element e) {
        BSD.show();
        prepare(e);
        binding.editId.clearFocus();
        updateCopyPastNode();
    }

    private void updateCopyPastNode() {
        if (Utils.eCopied == null) return;

        try {
            final Element newElement =
                    listener.onAndroidXmlParser()
                            .getXmlManager()
                            .getDocument()
                            .createElement(Utils.eCopied.getNodeName());

            newElement.setAttribute("android:layout_width", "wrap_content");
            newElement.setAttribute("android:layout_height", "wrap_content");
            for (var pair : XmlManager.getAllAttrNValuesFromElement((Element) Utils.eCopied)) {
                newElement.setAttribute(pair.first, pair.second);
            }
            Utils.eCopied = newElement;

            for (var e : XmlManager.getAllFirstChildFromElement((Element) Utils.eCopied)) {
                Utils.eCopied.appendChild(e.cloneNode(true));
            }
        } catch (DOMException err) {
            // only the parent will be loaded. If it contains children.
        }
    }

    private void init() {
        binding = DialogAttrSetterBinding.inflate(LayoutInflater.from(C));
        BSD.setContentView((View) binding.getRoot());
        binding.relTemplate.setPadding(0, 0, 0, 0);

        // defaultValue
        setBg(binding.tvPrivious);
        setBg(binding.tvNext);
        setBg(binding.tvParent);
        setBg(binding.tvFirstChild);
        setBg(binding.tvCopy);
        setBg(binding.tvPast);

        layoutsAttrSetter = new LayoutsAttrSetter(this);
        commonAttrSetter = new CommonAttrSetter(this);
        additionalAttrSetter = new AdditionnalAttrSetter(C, this);
    }

    private void setBg(View view) {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(ResourcesValuesFixer.getColor(C, "?colorSurface"));
        gradient.setStroke(2, ResourcesValuesFixer.getColor(C, "?colorOnSurface"));
        gradient.setCornerRadius(16f);

        ColorStateList colorState = ColorStateList.valueOf(Color.GRAY);
        RippleDrawable ripple = new RippleDrawable(colorState, gradient, null);
        view.setBackground(ripple);
        view.setElevation(10);
    }

    public RefViewElement getAllRef() {
        return listener.onRefViewElementNeeded();
    }

    public AndroidXmlParser getAndroidXmlParser() {
        return listener.onAndroidXmlParser();
    }

    public void setOnTaskRequested(OnAnyTaskRequested listener) {
        this.listener = listener;
    }

    public interface OnAnyTaskRequested {
        public boolean onAdd(Element e, int addType);

        public void onEdit(View v, Element e);

        public void onSave();

        public void onRefresh();

        public RefViewElement onRefViewElementNeeded();

        public AndroidXmlParser onAndroidXmlParser();
    }
}
