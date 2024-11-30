package jkas.androidpe.layoutUiDesigner.attrsSetter;

import android.content.Context;
import androidx.core.util.Pair;
import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.dialog.DialogBottomSheetAttrSetter;
import jkas.androidpe.layoutUiDesigner.dialog.attrs.DialogAdditionnalAttr;
import jkas.androidpe.resourcesUtils.adapters.AttrViewAdapter;
import jkas.androidpe.resourcesUtils.attrs.layout.AttrViews;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.XmlManager;

/**
 * @author JKas
 */
public class AdditionnalAttrSetter {
    private Context C;
    private DialogBottomSheetAttrSetter dialog;
    private DialogAdditionnalAttr dialogAA;

    public AdditionnalAttrSetter(Context C, DialogBottomSheetAttrSetter dialog) {
        this.C = C;
        this.dialog = dialog;

        setDefault();
        events();
        listeners();
    }

    private void events() {
        dialog.binding.icAddAttr.setOnClickListener(v -> dialogAA.show(dialog.element));
    }

    private void listeners() {
        dialogAA.setOnChangeListener(
                () -> {
                    dialog.setValueChanged();
                    init();
                });
    }

    private void loadData() {
        ArrayList<String> list = new ArrayList<>();
        for (Pair<?, ?> pair : XmlManager.getAllAttrNValuesFromElement(dialog.element)) {
            String elementAttr = (String) pair.first;
            boolean used = false;
            for (String usedAttr : AttrViews.usedByAssist) {
                if (elementAttr.equals(usedAttr)
                        || elementAttr.equals("android:" + usedAttr)
                        || elementAttr.equals("app:" + usedAttr)) {
                    used = true;
                    break;
                }
            }
            if (!used) list.add(elementAttr);
        }

        dialog.binding.linAllAttributes.removeAllViews();
        for (String attr : list) {
            final AttrViewAdapter AV = new AttrViewAdapter(C, dialog.element, attr, getList(attr));
            AV.setDeleteBtnVisible(true);
            AV.setAutoRemoveAttrIfEmpty(false);
            AV.setOnAttrChangedListener(
                    new AttrViewAdapter.OnAttrChangedListener() {
                        @Override
                        public void onDeleted() {
                            dialog.binding.linAllAttributes.removeView(AV.getView());
                        }

                        @Override
                        public void onChanged() {
                            dialog.setValueChanged();
                        }
                    });
            dialog.binding.linAllAttributes.addView(AV.getView());
        }
    }

    private String[] getList(String attr) {
        for (Pair<?, ?> pair : CodeUtil.convertLinkedHashMapToPair(AttrViews.baseAttrs)) {
            String viewAttr = (String) pair.first;
            if (attr.equals(viewAttr) || attr.endsWith(":" + viewAttr)) {
                return (String[]) pair.second;
            }
        }

        for (Pair<?, ?> pair : CodeUtil.convertLinkedHashMapToPair(AttrViews.baseAttrsApp)) {
            String viewAttr = (String) pair.first;
            if (attr.equals(viewAttr) || attr.endsWith(":" + viewAttr)) {
                return (String[]) pair.second;
            }
        }

        for (Pair<?, ?> pair : CodeUtil.convertLinkedHashMapToPair(AttrViews.allAttrs)) {
            String viewAttr = (String) pair.first;
            if (attr.equals(viewAttr) || attr.endsWith(":" + viewAttr)) {
                return (String[]) pair.second;
            }
        }
        return null;
    }

    private void setDefault() {
        dialogAA = new DialogAdditionnalAttr(C);
    }

    public void init() {
        loadData();
    }
}
