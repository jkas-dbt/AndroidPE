package jkas.androidpe.layoutUiDesigner.attrsSetter;

import android.view.View;
import androidx.core.util.Pair;
import com.google.android.material.slider.RangeSlider;
import jkas.androidpe.layoutUiDesigner.dialog.DialogBottomSheetAttrSetter;
import jkas.androidpe.layoutUiDesigner.dialog.attrs.DialogLayoutGravity;
import jkas.androidpe.layoutUiDesigner.dialog.attrs.DialogLayoutMarginPadding;
import jkas.androidpe.resourcesUtils.adapters.AttrViewDataAdapter;
import jkas.androidpe.resourcesUtils.dialog.DialogAttrValueParserAssist;
import jkas.codeUtil.XmlManager;

/**
 * @author JKas
 */
public class CommonAttrSetter {
    private DialogBottomSheetAttrSetter dialog;
    private DialogLayoutMarginPadding dialogMarginPadding;

    private boolean show = true;

    public CommonAttrSetter(DialogBottomSheetAttrSetter dialog) {
        this.dialog = dialog;
        this.dialogMarginPadding = new DialogLayoutMarginPadding(dialog.C);

        events();
        listeners();

        dialog.binding.linCommonAttributes.setVisibility(View.VISIBLE);
        dialog.binding.icCommonAttr.setRotation(180f);
    }

    private void listeners() {
        // Magin Padding
        dialogMarginPadding.setOnChangeListener(() -> dialog.setValueChanged());
    }

    private void events() {
        dialog.binding.linIcCommonAttr.setOnClickListener(
                vL -> {
                    show = !show;
                    if (show) {
                        dialog.binding.linCommonAttributes.setVisibility(View.VISIBLE);
                        dialog.binding.icCommonAttr.setRotation(180f);
                    } else {
                        dialog.binding.linCommonAttributes.setVisibility(View.GONE);
                        dialog.binding.icCommonAttr.setRotation(0f);
                    }
                });

        // Margin - Padding
        dialog.binding.btnPaddingsMarginsEdit.setOnClickListener(
                vpm -> dialogMarginPadding.show(dialog.element));

        // Tint
        dialog.binding.tvTint.setOnClickListener(
                vt -> {
                    final DialogAttrValueParserAssist AD =
                            new DialogAttrValueParserAssist(dialog.C);
                    AD.parseValues(
                            dialog.element,
                            "android:tint",
                            "@color",
                            AttrViewDataAdapter.getAllData("@color"));
                    AD.setOnSaveListener(
                            finalValue -> {
                                if (finalValue.trim().isEmpty())
                                    dialog.element.removeAttribute("android:tint");
                                else dialog.element.setAttribute("android:tint", finalValue);
                                dialog.binding.tieTint.setText(finalValue);
                                dialog.setValueChanged();
                            });
                    AD.show();
                });

        // Background
        dialog.binding.tvBackground.setOnClickListener(
                vb -> {
                    final DialogAttrValueParserAssist AD =
                            new DialogAttrValueParserAssist(dialog.C);
                    AD.parseValues(
                            dialog.element,
                            "android:background",
                            "@(color|drawable)",
                            AttrViewDataAdapter.getAllData("@(color|drawable)"));
                    AD.setOnSaveListener(
                            finalValue -> {
                                if (finalValue.trim().isEmpty())
                                    dialog.element.removeAttribute("android:background");
                                else dialog.element.setAttribute("android:background", finalValue);
                                dialog.binding.tieBackground.setText(finalValue);
                                dialog.setValueChanged();
                            });
                    AD.show();
                });

        // Tackground Tint
        dialog.binding.tvBackgroundTint.setOnClickListener(
                vbt -> {
                    final DialogAttrValueParserAssist AD =
                            new DialogAttrValueParserAssist(dialog.C);
                    AD.parseValues(
                            dialog.element,
                            "android:backgroundTint",
                            "@color",
                            AttrViewDataAdapter.getAllData("@color"));
                    AD.setOnSaveListener(
                            finalValue -> {
                                if (finalValue.trim().isEmpty())
                                    dialog.element.removeAttribute("android:backgroundTint");
                                else
                                    dialog.element.setAttribute(
                                            "android:backgroundTint", finalValue);
                                dialog.binding.tieBackgroundTint.setText(finalValue);
                                dialog.setValueChanged();
                            });
                    AD.show();
                });

        // Visibility
        dialog.binding.toggleBtnVisibility.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (!checked) dialog.element.removeAttribute("android:visibility");
                    else {
                        if (idBtn == dialog.binding.btnVisible.getId()) {
                            dialog.element.setAttribute("android:visibility", "visible");
                        } else if (idBtn == dialog.binding.btnInvisible.getId()) {
                            dialog.element.setAttribute("android:visibility", "invisible");
                        } else if (idBtn == dialog.binding.btnGone.getId()) {
                            dialog.element.setAttribute("android:visibility", "gone");
                        }
                    }
                    dialog.setValueChanged();
                });
        
        // alpha
        dialog.binding.cbAlpha.setOnCheckedChangeListener(
                (c, b) -> {
                    dialog.binding.rangeSliderAlpha.setEnabled(b);
                    if (b) {
                        float val = dialog.binding.rangeSliderAlpha.getValues().get(0);
                        dialog.element.setAttribute("android:alpha", "" + val);
                    } else dialog.element.removeAttribute("android:alpha");
                    dialog.setValueChanged();
                });
        dialog.binding.rangeSliderAlpha.addOnChangeListener(
                new RangeSlider.OnChangeListener() {
                    @Override
                    public void onValueChange(RangeSlider rs, float f, boolean fromUser) {
                        if (fromUser) dialog.element.setAttribute("android:alpha", "" + f);
                        dialog.setValueChanged();
                    }
                });

        // Gravity
        dialog.binding.btnGravity.setOnClickListener(
                vG -> {
                    final DialogLayoutGravity DLG = new DialogLayoutGravity(dialog.C);
                    DLG.setOnChangeListener(() -> dialog.setValueChanged());
                    DLG.show(dialog.element, DialogLayoutGravity.GRAVITY);
                });

        // Layout Gravity
        dialog.binding.btnLayoutGravity.setOnClickListener(
                vG -> {
                    final DialogLayoutGravity DLG = new DialogLayoutGravity(dialog.C);
                    DLG.setOnChangeListener(() -> dialog.setValueChanged());
                    DLG.show(dialog.element, DialogLayoutGravity.LAYOUT_GRAVITY);
                });
    }

    private void setDefault() {
        { // Margin Padding
            dialog.binding.tieMargins.setText("none");
            dialog.binding.tiePadding.setText("none");
            for (Pair<String, String> pair :
                    XmlManager.getAllAttrNValuesFromElement(dialog.element)) {
                if (pair.first.contains(":layout_margin")) {
                    dialog.binding.tieMargins.setText("value | ref");
                } else if (pair.first.contains(":padding")) {
                    dialog.binding.tiePadding.setText("value | ref");
                }
            }
        }

        { // BG
            dialog.binding.tieTint.setText(dialog.element.getAttribute("android:tint"));
            dialog.binding.tieBackground.setText(dialog.element.getAttribute("android:background"));
            dialog.binding.tieBackgroundTint.setText(
                    dialog.element.getAttribute("android:backgroundTint"));
        }

        { // Visibility
            String visibility = dialog.element.getAttribute("android:visibility");
            if (visibility.equals("visible")) {
                dialog.binding.toggleBtnVisibility.check(dialog.binding.btnVisible.getId());
            } else if (visibility.equals("invisible")) {
                dialog.binding.toggleBtnVisibility.check(dialog.binding.btnInvisible.getId());
            } else if (visibility.equals("gone")) {
                dialog.binding.toggleBtnVisibility.check(dialog.binding.btnGone.getId());
            } else dialog.binding.toggleBtnVisibility.clearChecked();
        }

        { // Alpha
            String alpha = dialog.element.getAttribute("android:alpha");
            try {
                float val = Float.parseFloat(alpha);
                if (val >= 0 && val <= 1.0) {
                    dialog.binding.rangeSliderAlpha.setEnabled(true);
                    dialog.binding.rangeSliderAlpha.setValues(new Float[] {val});
                    dialog.binding.cbAlpha.setChecked(true);
                } else {
                    dialog.binding.rangeSliderAlpha.setEnabled(false);
                    dialog.binding.rangeSliderAlpha.setValues(new Float[] {0.0f});
                    dialog.binding.cbAlpha.setChecked(false);
                }
            } catch (Exception e) {
                dialog.binding.rangeSliderAlpha.setEnabled(false);
                dialog.binding.rangeSliderAlpha.setValues(new Float[] {0.0f});
                dialog.binding.cbAlpha.setChecked(false);
            }
        }
    }

    public void init() {
        setDefault();
    }
}
