package jkas.androidpe.layoutUiDesigner.dialog.attrs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.text.HtmlCompat;
import androidx.core.util.Pair;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.RangeSlider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jkas.androidpe.layoutUiDesigner.databinding.DialogLayoutConstraintLayoutBinding;
import jkas.androidpe.layoutUiDesigner.tools.AndroidXmlParser;
import jkas.androidpe.layoutUiDesigner.tools.AttributeSetter;
import jkas.androidpe.layoutUiDesigner.tools.RefViewElement;
import jkas.androidpe.layoutUiDesigner.tools.ViewCreator;
import jkas.androidpe.layoutUiDesigner.utils.Utils;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.adapters.AttrViewAdapter;
import jkas.androidpe.resourcesUtils.adapters.AttrViewDataAdapter;
import jkas.androidpe.resourcesUtils.attrs.AllAttrBase;
import jkas.androidpe.resourcesUtils.dialog.DialogAutoComplete;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.androidpe.resourcesUtils.utils.ViewUtils;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DialogLayoutConstraintLayout {
    private OnChangedListener listener;
    private Context C;
    private Element element;
    private DialogLayoutConstraintLayoutBinding binding;
    private BottomSheetDialog BSD;
    private String[] listId, listIdAdapter;
    private ArrayList<Pair<String, String[]>> listRelativePosition = new ArrayList<>();
    private ArrayList<Pair<String, String[]>> listMargins = new ArrayList<>();
    private ArrayList<Pair<String, String[]>> listCircular = new ArrayList<>();
    private ArrayList<Pair<String, String[]>> listDimensionMinMax = new ArrayList<>();
    private ArrayList<Pair<String, String[]>> listWeighted = new ArrayList<>();

    public DialogLayoutConstraintLayout(Context c) {
        C = c;
    }

    private void loadData() {
        loadConstraints();
        loadMargins();
        loadRatio();
    }

    private void loadRatio() {
        String value = element.getAttribute("app:layout_constraintDimensionRatio");
        Pattern pattern = Pattern.compile("\\b(\\d+):(\\d+)\\b");
        Matcher matcher = pattern.matcher(value);
        if (!value.trim().isEmpty()) {
            if (matcher.find()) {
                String width = matcher.group(1);
                String height = matcher.group(2);
                binding.tvRatioWidth.setText(width);
                binding.tvRatioHeight.setText(height);
            }
        } else {
            binding.tvRatioWidth.setText("1");
            binding.tvRatioHeight.setText("1");
        }
    }

    private void loadBias() {
        if (element.getAttribute("app:layout_constraintStart_toStartOf").trim().equals("parent")
                && element.getAttribute("app:layout_constraintEnd_toEndOf")
                        .trim()
                        .equals("parent")) {
            binding.rsBiasH.setEnabled(true);
        } else binding.rsBiasH.setEnabled(false);
        if (!binding.rsBiasH.isEnabled()) {
            if (element.getAttribute("app:layout_constraintLeft_toLeftOf").trim().equals("parent")
                    && element.getAttribute("app:layout_constraintRight_toRightOf")
                            .trim()
                            .equals("parent")) {
                binding.rsBiasH.setEnabled(true);
            } else {
                binding.rsBiasH.setEnabled(false);
                element.removeAttribute("app:layout_constraintHorizontal_bias");
            }
        }

        if (element.getAttribute("app:layout_constraintTop_toTopOf").trim().equals("parent")
                && element.getAttribute("app:layout_constraintBottom_toBottomOf")
                        .trim()
                        .equals("parent")) {
            binding.rsBiasV.setEnabled(true);
        } else {
            binding.rsBiasV.setEnabled(false);
            element.removeAttribute("app:layout_constraintVertical_bias");
        }

        String valueV = element.getAttribute("app:layout_constraintVertical_bias");
        if (valueV.trim().isEmpty()) binding.rsBiasV.setValues(new Float[] {50.0f});
        else {
            try {
                binding.rsBiasV.setValues(new Float[] {Float.parseFloat(valueV) * 100});
            } catch (Exception err) {
                binding.rsBiasV.setValues(new Float[] {50.0f});
            }
        }
        String valueH = element.getAttribute("app:layout_constraintHorizontal_bias");
        if (valueH.trim().isEmpty()) binding.rsBiasH.setValues(new Float[] {50.0f});
        else {
            try {
                binding.rsBiasH.setValues(new Float[] {Float.parseFloat(valueH) * 100});
            } catch (Exception err) {
                binding.rsBiasH.setValues(new Float[] {50.0f});
            }
        }
    }

    private void loadMargins() {
        loadMargins(binding.btnLeft, "Left");
        loadMargins(binding.btnTop, "Top");
        loadMargins(binding.btnRight, "Right");
        loadMargins(binding.btnBottom, "Bottom");
    }

    private void loadMargins(final MaterialButton btn, String val) {
        String value = element.getAttribute("android:layout_margin" + val).trim();
        if (!value.isEmpty()) {
            if (value.startsWith("@") || value.startsWith("?")) btn.setText("ref");
            else btn.setText(value);
        } else btn.setText("16dp");
    }

    private void loadConstraints() {
        binding.linCurrentConstraints.removeAllViews();
        if (!loadConstraints(
                binding.tvAddConstraintLeft, binding.btnLeft, "Left_toLeftOf", "Left_toRightOf"))
            loadConstraints(
                    binding.tvAddConstraintLeft,
                    binding.btnLeft,
                    "Start_toStartOf",
                    "Start_toEndOf");
        loadConstraints(
                binding.tvAddConstraintTop, binding.btnTop, "Top_toTopOf", "Top_toBottomOf");
        if (!loadConstraints(
                binding.tvAddConstraintRight,
                binding.btnRight,
                "Right_toRightOf",
                "Right_toLeftOf"))
            loadConstraints(
                    binding.tvAddConstraintRight, binding.btnRight, "End_toEndOf", "End_toStartOf");
        loadConstraints(
                binding.tvAddConstraintBottom,
                binding.btnBottom,
                "Bottom_toBottomOf",
                "Bottom_toTopOf");
        loadBias();
    }

    private boolean loadConstraints(
            final TextView tvConstraint,
            final MaterialButton btn,
            final String v1,
            final String v2) {
        String vF = "";
        String value = element.getAttribute("app:layout_constraint" + v1).trim();
        vF = v1;
        if (value.isEmpty()) {
            value = element.getAttribute("app:layout_constraint" + v2).trim();
            vF = v2;
        }
        if (!value.isEmpty()) {
            ViewUtils.setBackground(
                    tvConstraint,
                    ResourcesValuesFixer.getColor(C, "?colorOnSurface"),
                    0,
                    Color.TRANSPARENT,
                    43f);
            tvConstraint.setText("");
            btn.setVisibility(View.VISIBLE);
            tvConstraint.setTag(vF);

            String s = vF;
            s = s.replace("_", "  >  ");
            s += "  <b>" + value + "</b>";
            TextView tv = new TextView(C);
            tv.setTypeface(Typeface.MONOSPACE);
            tv.setLayoutParams(CodeUtil.getLayoutParamsMW(8));
            tv.setText(HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_COMPACT));
            tv.setTextSize(12f);
            tv.setPadding(8, 8, 8, 8);
            binding.linCurrentConstraints.addView(tv);
            return true;
        } else {
            ViewUtils.setBackground(
                    tvConstraint,
                    ResourcesValuesFixer.getColor(C, "?colorOnTertiary"),
                    0,
                    Color.TRANSPARENT,
                    43f);
            tvConstraint.setText("+");
            btn.setVisibility(View.INVISIBLE);
        }
        return false;
    }

    private void events() {
        eventAddConstraint(binding.tvAddConstraintLeft, "Left_toLeftOf", "Left_toRightOf");
        eventAddConstraint(binding.tvAddConstraintTop, "Top_toTopOf", "Top_toBottomOf");
        eventAddConstraint(binding.tvAddConstraintRight, "Right_toRightOf", "Right_toLeftOf");
        eventAddConstraint(binding.tvAddConstraintBottom, "Bottom_toBottomOf", "Bottom_toTopOf");

        eventSetMargin(binding.btnLeft, "Left");
        eventSetMargin(binding.btnTop, "Top");
        eventSetMargin(binding.btnRight, "Right");
        eventSetMargin(binding.btnBottom, "Bottom");

        eventRatio();

        eventBias(binding.rsBiasV, "Vertical");
        eventBias(binding.rsBiasH, "Horizontal");

        binding.toggleType.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (checked) {
                        if (idBtn == binding.btnHideAttr.getId()) {
                            binding.linAttrs.removeAllViews();
                        } else if (idBtn == binding.btnRelatvePositioning.getId()) {
                            loadAttrs(1, listRelativePosition);
                        } else if (idBtn == binding.btnMargins.getId()) {
                            loadAttrs(2, listMargins);
                        } else if (idBtn == binding.btnBiasCircular.getId()) {
                            loadAttrs(0, listCircular);
                        } else if (idBtn == binding.btnDimensionsMinMax.getId()) {
                            loadAttrs(0, listDimensionMinMax);
                        } else if (idBtn == binding.btnWeighted.getId()) {
                            loadAttrs(0, listWeighted);
                        }
                    }
                });

        binding.icMoreInfo.setOnClickListener(
                icInfoView ->
                        DialogBuilder.showDialog(
                                C, null, C.getString(R.string.info_coordinator_layout_attr_test)));
    }

    private void loadAttrs(int p, ArrayList<Pair<String, String[]>> list) {
        binding.linAttrs.removeAllViews();
        for (var pair : list) {
            final AttrViewAdapter attr = new AttrViewAdapter(C, element, pair.first, pair.second);
            attr.setDeleteBtnVisible(false);
            attr.setAutoRemoveAttrIfEmpty(true);
            attr.setOnAttrChangedListener(
                    new AttrViewAdapter.OnAttrChangedListener() {
                        @Override
                        public void onChanged() {
                            loadAttrs((int) attr.getView().getTag());
                            listener.onChange();
                        }
                    });
            binding.linAttrs.addView(attr.getView());
            attr.getView().setTag(p);
        }
    }

    private void loadAttrs(int p) {
        switch (p) {
            case 1:
                loadConstraints();
                break;
            case 2:
                loadMargins();
                break;
        }
    }

    public void eventBias(final RangeSlider rs, final String val) {
        rs.setTag(val);
        rs.addOnChangeListener(
                (rangeSlider, f, fromUser) -> {
                    final String value = (String) rangeSlider.getTag();
                    if (fromUser) {
                        if (f == 50.0 || f == 50) {
                            element.removeAttribute("app:layout_constraint" + value + "_bias");
                        } else {
                            element.setAttribute(
                                    "app:layout_constraint" + value + "_bias", "" + (f / 100));
                        }
                        if (listener != null) listener.onChange();
                    }
                });
    }

    public void eventRatio() {
        String value = element.getAttribute("app:layout_constraintDimensionRatio");
        binding.tieRatio.setText(value.trim().isEmpty() ? "1:1" : value);
        binding.tieRatio.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable edit) {
                        if (edit.toString().trim().isEmpty() || edit.toString().contains("1:1"))
                            element.removeAttribute("app:layout_constraintDimensionRatio");
                        else
                            element.setAttribute(
                                    "app:layout_constraintDimensionRatio", edit.toString());
                        if (listener != null) listener.onChange();
                        loadRatio();
                    }
                });
    }

    private void eventSetMargin(final MaterialButton btn, final String val) {
        btn.setTag(val);
        btn.setOnClickListener(
                v -> {
                    final String value = (String) v.getTag();
                    final DialogAutoComplete dialog = new DialogAutoComplete(C, true);
                    dialog.setTitle(C.getString(R.string.value));
                    dialog.setHint("android:layout_margin" + value);
                    dialog.setInfo(". . .");
                    dialog.setMsgError(C.getString(R.string.invalide_entry));
                    dialog.setDefaultValue(
                            element.getAttribute("android:layout_margin" + value).trim().isEmpty()
                                    ? "16dp"
                                    : element.getAttribute("android:layout_margin" + value).trim());
                    dialog.setListForAutoCompletion(AttrViewDataAdapter.getAllData("@dimen"));
                    dialog.setOnChangeDetected(
                            new DialogAutoComplete.OnChangeDetected() {
                                private String tmpValue = "";

                                @Override
                                public boolean onTextChanged(String text) {
                                    tmpValue = text.trim();
                                    return (ResourcesValuesFixer.getDimen(C, text) != -1);
                                }

                                @Override
                                public void onValueConfirmed(boolean match) {
                                    if (match) {
                                        if (tmpValue.isEmpty() || tmpValue.equals("16dp"))
                                            element.removeAttribute(
                                                    "android:layout_margin" + value);
                                        else
                                            element.setAttribute(
                                                    "android:layout_margin" + value, tmpValue);
                                        loadMargins();
                                        int current = binding.toggleType.getCheckedButtonId();
                                        binding.toggleType.check(binding.btnHideAttr.getId());
                                        binding.toggleType.check(current);
                                        if (listener != null) listener.onChange();
                                    }
                                }
                            });
                    dialog.show();
                });
    }

    private void addConstraint(View v, String v1, String v2) {
        final PopupMenu popupMenu = new PopupMenu(C, v);
        popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, " ... " + v1);
        popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, " ... " + v2);
        popupMenu.setOnMenuItemClickListener(
                item -> {
                    final String attr = (item.getItemId() == 1) ? v1 : v2;
                    v.setTag(attr);
                    showDialogAddConstraint(attr);
                    return true;
                });
        popupMenu.show();
    }

    private void eventAddConstraint(final TextView tvConstraint, final String v1, final String v2) {
        tvConstraint.setOnClickListener(
                v -> {
                    if (!((TextView) v).getText().toString().isEmpty()) addConstraint(v, v1, v2);
                    else {
                        final PopupMenu pop = new PopupMenu(C, v);
                        pop.setForceShowIcon(true);
                        pop.getMenu()
                                .add(Menu.NONE, 1, Menu.NONE, R.string.edit)
                                .setIcon(R.drawable.ic_pencil)
                                .setIconTintList(
                                        ColorStateList.valueOf(
                                                ResourcesValuesFixer.getColor(
                                                        C, "?colorOnSurface")));
                        pop.getMenu()
                                .add(Menu.NONE, 2, Menu.NONE, R.string.delete)
                                .setIcon(R.drawable.ic_delete)
                                .setIconTintList(
                                        ColorStateList.valueOf(
                                                ResourcesValuesFixer.getColor(
                                                        C, "?colorOnSurface")));
                        pop.setOnMenuItemClickListener(
                                item -> {
                                    if (item.getItemId() == 1) {
                                        showDialogAddConstraint((String) v.getTag());
                                    } else if (item.getItemId() == 2)
                                        DialogBuilder.getDialogBuilder(
                                                        C,
                                                        C.getString(R.string.warning),
                                                        C.getString(R.string.warning_delete)
                                                                + " Constraint "
                                                                + v.getTag()
                                                                + " ?")
                                                .setNegativeButton(R.string.cancel, null)
                                                .setPositiveButton(
                                                        R.string.delete,
                                                        (d, k) -> {
                                                            element.removeAttribute(
                                                                    "app:layout_constraint"
                                                                            + v.getTag());
                                                            loadConstraints();
                                                            int current =
                                                                    binding.toggleType
                                                                            .getCheckedButtonId();
                                                            binding.toggleType.check(
                                                                    binding.btnHideAttr.getId());
                                                            binding.toggleType.check(current);
                                                            if (listener != null)
                                                                listener.onChange();
                                                        })
                                                .show();
                                    return true;
                                });
                        pop.show();
                    }
                });
    }

    private void showDialogAddConstraint(final String attr) {
        String value = element.getAttribute("app:layout_constraint" + attr);
        if (value.trim().isEmpty()) value = "parent";
        final DialogAutoComplete dialog = new DialogAutoComplete(C, true);
        dialog.setDefaultValue(value);
        dialog.setTitle(C.getString(R.string.value));
        dialog.setHint("app:layout_constraint" + attr);
        dialog.setInfo("A view ID is required (default value : parent)");
        dialog.setMsgError(C.getString(R.string.invalide_entry));
        dialog.setListForAutoCompletion(
                Arrays.asList(Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        dialog.setOnChangeDetected(
                new DialogAutoComplete.OnChangeDetected() {
                    private String tmpValue = "";

                    @Override
                    public boolean onTextChanged(String text) {
                        tmpValue = text.trim();
                        if (Arrays.asList(listId).contains(text)) return true;
                        else if (text.equals("parent")) return true;
                        return false;
                    }

                    @Override
                    public void onValueConfirmed(boolean match) {
                        if (match) {
                            if (tmpValue.isEmpty())
                                element.removeAttribute("app:layout_constraint" + attr);
                            else element.setAttribute("app:layout_constraint" + attr, tmpValue);
                        }
                        loadConstraints();
                        int current = binding.toggleType.getCheckedButtonId();
                        binding.toggleType.check(binding.btnHideAttr.getId());
                        binding.toggleType.check(current);
                        if (listener != null) listener.onChange();
                    }
                });
        addAdditionalView(dialog, attr);
        dialog.show();
    }

    private void init() {
        binding = DialogLayoutConstraintLayoutBinding.inflate(LayoutInflater.from(C));
        this.BSD = new BottomSheetDialog(C);
        BSD.setTitle("ConstraintLayout");
        BottomSheetBehavior behavior = BSD.getBehavior();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setDraggable(false);
        BSD.setContentView((View) binding.getRoot());
        binding.rsBiasH.setValues(new Float[] {50.0f});
        binding.rsBiasV.setValues(new Float[] {50.0f});
        binding.tvViewName.setText(
                element.getNodeName() + " : " + element.getAttribute("android:id"));
        themes();
    }

    public void show(Element e) {
        this.element = e;
        init();
        initId();
        initAdapter();
        loadData();
        events();
        BSD.show();
    }

    private void themes() {
        ViewUtils.setBackground(
                binding.relAddConstraints,
                Color.TRANSPARENT,
                3,
                ResourcesValuesFixer.getColor(C, "?colorOnSurface"),
                0f);
    }

    private void initAdapter() {
        // Relative Position
        listRelativePosition.clear();
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintLeft_toLeftOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintLeft_toRightOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintRight_toLeftOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintRight_toRightOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintTop_toTopOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintTop_toBottomOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintBottom_toTopOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintBottom_toBottomOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintBaseline_toBaselineOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintStart_toEndOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintStart_toStartOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintEnd_toStartOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listRelativePosition.add(
                new Pair<>(
                        "app:layout_constraintEnd_toEndOf",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));

        // Margins
        listMargins.clear();
        listMargins.add(new Pair<>("app:layout_marginBaseline", new String[] {"@dimen"}));
        listMargins.add(new Pair<>("app:layout_goneMarginStart", new String[] {"@dimen"}));
        listMargins.add(new Pair<>("app:layout_goneMarginEnd", new String[] {"@dimen"}));
        listMargins.add(new Pair<>("app:layout_goneMarginLeft", new String[] {"@dimen"}));
        listMargins.add(new Pair<>("app:layout_goneMarginTop", new String[] {"@dimen"}));
        listMargins.add(new Pair<>("app:layout_goneMarginRight", new String[] {"@dimen"}));
        listMargins.add(new Pair<>("app:layout_goneMarginBottom", new String[] {"@dimen"}));
        listMargins.add(new Pair<>("app:layout_goneMarginBaseline", new String[] {"@dimen"}));

        // Circular
        listCircular.clear();
        listCircular.add(
                new Pair<>(
                        "app:layout_constraintCircle",
                        Arrays.copyOf(listIdAdapter, listIdAdapter.length)));
        listCircular.add(new Pair<>("app:layout_constraintCircleRadius", new String[] {"@dimen"}));
        listCircular.add(new Pair<>("app:layout_constraintCircleAngle", null));

        // Dimension - Min & Max
        listDimensionMinMax.clear();
        listDimensionMinMax.add(
                new Pair<>("app:layout_constrainedWidth", AllAttrBase.VALUES_BOOLEAN));
        listDimensionMinMax.add(
                new Pair<>("app:layout_constrainedHeight", AllAttrBase.VALUES_BOOLEAN));
        listDimensionMinMax.add(
                new Pair<>("app:layout_constraintWidth_min", new String[] {"@dimen"}));
        listDimensionMinMax.add(
                new Pair<>("app:layout_constraintHeight_min", new String[] {"@dimen"}));
        listDimensionMinMax.add(
                new Pair<>("app:layout_constraintWidth_max", new String[] {"@dimen"}));
        listDimensionMinMax.add(
                new Pair<>("app:layout_constraintHeight_max", new String[] {"@dimen"}));
        listDimensionMinMax.add(new Pair<>("app:layout_constraintWidth_percent", null));
        listDimensionMinMax.add(new Pair<>("app:layout_constraintHeight_percent", null));
        listDimensionMinMax.add(
                new Pair<>(
                        "app:layout_constraintWidth_default",
                        new String[] {"none", "percent", "wrap"}));
        listDimensionMinMax.add(
                new Pair<>(
                        "app:layout_constraintHeight_default",
                        new String[] {"none", "percent", "wrap"}));

        // Weighted
        listWeighted.clear();
        listWeighted.add(new Pair<>("app:layout_constraintHorizontal_weight", null));
        listWeighted.add(new Pair<>("app:layout_constraintVertical_weight", null));
    }

    private void addAdditionalView(final DialogAutoComplete dialog, final String attr) {
        final LinearLayout lin = new LinearLayout(C);
        lin.setLayoutParams(CodeUtil.getLayoutParamsWW(8));
        lin.setPadding(12, 8, 12, 8);
        lin.setGravity(Gravity.CENTER);
        lin.setOrientation(LinearLayout.HORIZONTAL);
        dialog.getAdditionalViewParent().addView(lin);
        dialog.getAdditionalViewParent().setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams lp = CodeUtil.getLayoutParamsWW(12);
        lp.weight = 1;
        final TextView tv = new TextView(C);
        tv.setLayoutParams(lp);
        tv.setPadding(8, 8, 8, 8);
        tv.setTextSize(12f);
        tv.setText(C.getString(R.string.select_view_to_get_id) + "   :   ");
        lin.addView(tv);

        final MaterialButton btn = new MaterialButton(C);
        btn.setText(R.string.select);
        btn.setOnClickListener(v -> loadViewsForIDcompletion(dialog, attr));
        lin.addView(btn);
    }

    private void loadViewsForIDcompletion(
            final DialogAutoComplete dialogConstraint, final String attr) {
        final AttributeSetter attrSetter = getAttrSetter();
        final Element parent = listener.onRefViewElementNeeded().getParent(element);

        if (parent == null) {
            Toast.makeText(C, R.string.not_found, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean found = false;
        final AlertDialog alert = DialogBuilder.getDialogBuilder(C, "VIEWs", null).create();
        ScrollView sv = new ScrollView(C);
        sv.setLayoutParams(CodeUtil.getLayoutParamsMW(0));
        LinearLayout lin = new LinearLayout(C);
        lin.setLayoutParams(CodeUtil.getLayoutParamsMW(12));
        lin.setOrientation(LinearLayout.VERTICAL);
        lin.setGravity(Gravity.CENTER_HORIZONTAL);
        sv.addView(lin);
        alert.setView(sv);
        for (final var eChild :
                listener.onRefViewElementNeeded().getTreeElement(parent).getChildren()) {
            if (eChild == element) continue;
            final View view = ViewCreator.create("null", C, eChild.getNodeName(), false);
            Utils.drawDashPathStroke(view);
            attrSetter.set(view, (Element) eChild.cloneNode(false));
            view.setOnClickListener(
                    v -> {
                        String id = eChild.getAttribute("android:id");
                        if (id.isEmpty()) {
                            id = generateId(eChild.getNodeName());
                            eChild.setAttribute("android:id", id);
                        }
                        element.setAttribute("app:layout_constraint" + attr, id);
                        initId();
                        initAdapter();
                        loadConstraints();
                        int current = binding.toggleType.getCheckedButtonId();
                        binding.toggleType.check(binding.btnHideAttr.getId());
                        binding.toggleType.check(current);
                        if (listener != null) listener.onChange();
                        alert.cancel();
                        dialogConstraint.cancel();
                    });
            lin.addView(view);
            view.setPadding(20, 20, 20, 20);
            ((LinearLayout.LayoutParams) view.getLayoutParams()).setMargins(20, 20, 20, 20);
            if (!found) found = true;
        }
        if (!found) {
            Toast.makeText(C, R.string.not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        alert.show();
    }

    private String generateId(String viewName) {
        if (viewName.matches(".*\\..*")) {
            viewName = viewName.substring(viewName.lastIndexOf(".") + 1);
        }
        int i = 1;
        while (i < listener.onRefViewElementNeeded().getListRef().size() + 6) {
            String id = "@+id/" + CodeUtil.toLowerCaseFirstLetter(viewName);
            if (i < 10) id += "0" + i;
            else id += i;
            boolean found = false;
            for (var pair :
                    CodeUtil.convertMapToPair(listener.onRefViewElementNeeded().getListRef())) {
                Element e = (Element) pair.second;
                if (e.getAttribute("android:id").equals(id)) found = true;
            }
            if (!found) return id;
            i++;
        }
        return viewName + "ViewId";
    }

    private AttributeSetter getAttrSetter() {
        final AttributeSetter attrSetter = new AttributeSetter(C);
        attrSetter.setOnDataRefNeeded(
                new AttributeSetter.OnDataRefNeeded() {

                    @Override
                    public RefViewElement onRefViewElementNeeded() {
                        return listener.onRefViewElementNeeded();
                    }

                    @Override
                    public String tagNeeded() {
                        return "null";
                    }
                });
        return attrSetter;
    }

    private void initId() {
        final ArrayList<String> list = new ArrayList<>();
        for (Element e :
                XmlManager.getAllFirstChildFromElement((Element) element.getParentNode())) {
            String id = e.getAttribute("android:id");
            if (id.startsWith("@+id/")) {
                list.add(id);
                list.add("@id/" + id.split("\\/")[1]);
            }
        }
        if (list.size() == 0) {
            listId = new String[0];
            listIdAdapter = new String[0];
            return;
        }
        listId = new String[list.size()];
        listIdAdapter = new String[list.size() / 2];
        int r = 0;
        for (int i = 0; i < list.size(); i++) {
            String id = list.get(i);
            listId[i] = id;
            if (id.startsWith("@id/")) {
                listIdAdapter[r] = id;
                r++;
            }
        }
    }

    public void setOnChangeListener(OnChangedListener listener) {
        this.listener = listener;
    }

    public interface OnChangedListener {
        public void onChange();

        public RefViewElement onRefViewElementNeeded();

        public AndroidXmlParser onAndroidXmlParserNeeded();
    }
}
