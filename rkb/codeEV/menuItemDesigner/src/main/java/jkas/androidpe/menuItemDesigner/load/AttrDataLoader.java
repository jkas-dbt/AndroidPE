package jkas.androidpe.menuItemDesigner.load;

import android.content.Context;
import androidx.core.util.Pair;
import android.view.View;
import java.util.ArrayList;
import jkas.androidpe.menuItemDesigner.MenuItemDesigner;
import jkas.androidpe.resourcesUtils.attrs.menu.AttrGroup;
import jkas.androidpe.resourcesUtils.attrs.menu.AttrItem;
import jkas.androidpe.resourcesUtils.adapters.AttrViewAdapter;
import jkas.androidpe.resourcesUtils.dialog.DialogBottomSheetAttrModifier;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class AttrDataLoader {
    private static Context C;
    private static Element element;
    private static ArrayList<String> listCurrentAttr = new ArrayList<>();

    public static void loadAttrData(Context c) {
        C = c;
        element = DialogBottomSheetAttrModifier.getInstance(C).element;
        setDefaultSetting();
        loadCurrent();
        loadOthersAttrs();
    }

    private static void loadOthersAttrs() {
        DialogBottomSheetAttrModifier.getInstance(C).binding.linAllAttributes.removeAllViews();
        for (String attr :
                element.getTagName().equals("group") ? AttrGroup.getAttrs() : AttrItem.getAttrs())
            if (listCurrentAttr.contains(attr.intern())) continue;
            else loadOthersAttrs(attr);
    }

    private static void loadOthersAttrs(String key) {
        String[] listValueAssist = null;
        if (element.getTagName().equals("item")) listValueAssist = AttrItem.getValuesOfAttr(key);
        else if (element.getTagName().equals("group"))
            listValueAssist = AttrGroup.getValuesOfAttr(key);

        final AttrViewAdapter AV = new AttrViewAdapter(C, element, key, listValueAssist);
        AV.setOnAttrChangedListener(
                new AttrViewAdapter.OnAttrChangedListener() {
                    @Override
                    public void onChanged() {
                        MenuItemDesigner.MainEditor.getInstance().save();
                    }
                });
        AV.setDeleteBtnVisible(false);
        AV.setAutoRemoveAttrIfEmpty(true);
        AV.setText("");
        DialogBottomSheetAttrModifier.getInstance(C).binding.linAllAttributes.addView(AV.getView());
    }

    private static void loadCurrent() {
        listCurrentAttr.clear();
        DialogBottomSheetAttrModifier.getInstance(C).binding.linDeclaredAttributes.removeAllViews();
        for (Pair<String, String> pair :
                XmlManager.getAllAttrNValuesFromElement(
                        DialogBottomSheetAttrModifier.getInstance(null).element))
            loadCurrentEntry(pair);
    }

    private static void loadCurrentEntry(Pair<String, String> pair) {
        String key = pair.first.intern();
        listCurrentAttr.add(key);
        if (key.equals("id") || key.endsWith(":id")) return;

        String[] listValueAssist = null;
        if (element.getTagName().equals("item")) listValueAssist = AttrItem.getValuesOfAttr(key);
        else if (element.getTagName().equals("group"))
            listValueAssist = AttrGroup.getValuesOfAttr(key);

        final AttrViewAdapter AV = new AttrViewAdapter(C, element, key, listValueAssist);
        AV.setOnAttrChangedListener(
                new AttrViewAdapter.OnAttrChangedListener() {

                    @Override
                    public void onDeleted() {
                        DialogBottomSheetAttrModifier.getInstance(C)
                                .binding
                                .linDeclaredAttributes
                                .removeView(AV.getView());
                        DialogBottomSheetAttrModifier.getInstance(C)
                                .binding
                                .linAllAttributes
                                .addView(AV.getView(), 0);
                        AV.setAutoRemoveAttrIfEmpty(true);
                        AV.setDeleteBtnVisible(false);
                        AV.setText("");
                    }

                    @Override
                    public void onChanged() {
                        MenuItemDesigner.MainEditor.getInstance().save();
                    }
                });
        AV.setAutoRemoveAttrIfEmpty(false);
        AV.setDeleteBtnVisible(true);
        DialogBottomSheetAttrModifier.getInstance(C)
                .binding
                .linDeclaredAttributes
                .addView(AV.getView());
    }

    private static void setDefaultSetting() {
        DialogBottomSheetAttrModifier.getInstance(C).binding.cardLayout.setVisibility(View.GONE);
    }
}
