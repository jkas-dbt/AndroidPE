package jkas.androidpe.layoutUiDesigner.attrsSetter;

import android.view.View;
import android.widget.Toast;
import jkas.androidpe.layoutUiDesigner.dialog.DialogBottomSheetAttrSetter;
import jkas.androidpe.layoutUiDesigner.dialog.attrs.DialogLayoutConstraintLayout;
import jkas.androidpe.layoutUiDesigner.dialog.attrs.DialogLayoutCoordinatorLayout;
import jkas.androidpe.layoutUiDesigner.dialog.attrs.DialogLayoutParams;
import jkas.androidpe.layoutUiDesigner.dialog.attrs.DialogLayoutRelativeLayout;
import jkas.androidpe.layoutUiDesigner.tools.AndroidXmlParser;
import jkas.androidpe.layoutUiDesigner.tools.RefViewElement;
import jkas.codeUtil.CodeUtil;
import org.w3c.dom.Node;

/**
 * @author JKas
 */
public class LayoutsAttrSetter {
    private DialogBottomSheetAttrSetter dialog;
    private DialogLayoutParams dialogLayoutParams;
    private DialogLayoutRelativeLayout dialogRelativeLayout;
    private DialogLayoutCoordinatorLayout dialogCoordinatorLayout;
    private DialogLayoutConstraintLayout dialogConstraintLayout;

    private boolean show = true;

    public LayoutsAttrSetter(DialogBottomSheetAttrSetter dialog) {
        this.dialog = dialog;
        this.dialogLayoutParams = new DialogLayoutParams(dialog.C);
        this.dialogRelativeLayout = new DialogLayoutRelativeLayout(dialog.C);
        this.dialogCoordinatorLayout = new DialogLayoutCoordinatorLayout(dialog.C);
        this.dialogConstraintLayout = new DialogLayoutConstraintLayout(dialog.C);

        events();
        listeners();

        dialog.binding.linLayout.setVisibility(View.VISIBLE);
        dialog.binding.icLayout.setRotation(180f);
    }

    private void setDefault() {
        { // width
            String val = dialog.element.getAttribute("android:layout_width");
            if (val.startsWith("@")) dialog.binding.btnWidth.setText("width (@res)");
            else if (CodeUtil.isDimenValue(val)) dialog.binding.btnWidth.setText("width (val)");
            else if (val.equals("wrap_content")) dialog.binding.btnWidth.setText("width (w)");
            else if (val.equals("match_parent")) dialog.binding.btnWidth.setText("width (m)");
            else if (val.startsWith("?")) dialog.binding.btnWidth.setText("width (attr)");
        }

        { // height
            String val = dialog.element.getAttribute("android:layout_height");
            if (val.startsWith("@")) dialog.binding.btnHeight.setText("height (@res)");
            else if (CodeUtil.isDimenValue(val)) dialog.binding.btnHeight.setText("height (val)");
            else if (val.equals("wrap_content")) dialog.binding.btnHeight.setText("height (w)");
            else if (val.equals("match_parent")) dialog.binding.btnHeight.setText("height (m)");
            else if (val.startsWith("?")) dialog.binding.btnHeight.setText("height (attr)");
        }

        { // weight
            String val = dialog.element.getAttribute("android:layout_weight");
            if (val.isEmpty()) dialog.binding.btnWeight.setText("weight (none)");
            else dialog.binding.btnWeight.setText("weight (val)");
        }

        { // RelativeLayout
            dialog.binding.btnRelativeLayout.setEnabled(false);
            Node node = dialog.element.getParentNode();
            if (node == null) dialog.binding.btnRelativeLayout.setEnabled(false);
            else {
                if (node.getNodeName().equals("RelativeLayout"))
                    dialog.binding.btnRelativeLayout.setEnabled(true);
            }
        }

        { // ConstraintLayout
            dialog.binding.btnConstraintLayout.setEnabled(false);
            Node node = dialog.element.getParentNode();
            if (node == null) dialog.binding.btnConstraintLayout.setEnabled(false);
            else {
                if (node.getNodeName().endsWith(".ConstraintLayout"))
                    dialog.binding.btnConstraintLayout.setEnabled(true);
            }
        }

        { // CoordinatorLayout
            dialog.binding.btnCoordinatorLayout.setEnabled(false);
            Node node = dialog.element.getParentNode();
            if (node == null) dialog.binding.btnCoordinatorLayout.setEnabled(false);
            else {
                if (node.getNodeName().endsWith(".CoordinatorLayout"))
                    dialog.binding.btnCoordinatorLayout.setEnabled(true);
            }
        }
    }

    public void init() {
        setDefault();
    }

    private void listeners() {
        // LayoutParams
        dialogLayoutParams.setOnChangeListener(
                type -> {
                    setDefault();
                    dialog.setValueChanged();
                });

        // RelativeLayout
        dialogRelativeLayout.setOnChangeListener(
                () -> {
                    dialog.setValueChanged();
                });

        // CoordinatorLayout
        dialogCoordinatorLayout.setOnChangeListener(
                () -> {
                    dialog.setValueChanged();
                });

        // ConstraintLayout
        dialogConstraintLayout.setOnChangeListener(
                new DialogLayoutConstraintLayout.OnChangedListener() {

                    @Override
                    public void onChange() {
                        dialog.setValueChanged();
                    }

                    @Override
                    public RefViewElement onRefViewElementNeeded() {
                        return dialog.getAllRef();
                    }

                    @Override
                    public AndroidXmlParser onAndroidXmlParserNeeded() {
                        return dialog.getAndroidXmlParser();
                    }
                });
    }

    private void events() {
        dialog.binding.linIcLayout.setOnClickListener(
                vL -> {
                    show = !show;
                    if (show) {
                        dialog.binding.linLayout.setVisibility(View.VISIBLE);
                        dialog.binding.icLayout.setRotation(180f);
                    } else {
                        dialog.binding.linLayout.setVisibility(View.GONE);
                        dialog.binding.icLayout.setRotation(0f);
                    }
                });

        dialog.binding.btnWidth.setOnClickListener(
                vw -> dialogLayoutParams.show(dialog.element, DialogLayoutParams.WIDTH));

        dialog.binding.btnHeight.setOnClickListener(
                vh -> dialogLayoutParams.show(dialog.element, DialogLayoutParams.HEIGHT));

        dialog.binding.btnWeight.setOnClickListener(
                vwt -> dialogLayoutParams.show(dialog.element, DialogLayoutParams.WEIGHT));

        dialog.binding.btnRelativeLayout.setOnClickListener(
                vrl -> dialogRelativeLayout.show(dialog.element));

        dialog.binding.btnConstraintLayout.setOnClickListener(
                vconstl -> dialogConstraintLayout.show(dialog.element));

        dialog.binding.btnCoordinatorLayout.setOnClickListener(
                vcoordl -> dialogCoordinatorLayout.show(dialog.element));
    }
}
