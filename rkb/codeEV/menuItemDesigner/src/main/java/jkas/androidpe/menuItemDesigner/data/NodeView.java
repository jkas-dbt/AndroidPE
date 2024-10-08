package jkas.androidpe.menuItemDesigner.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.text.HtmlCompat;
import jkas.androidpe.logger.LoggerRes;
import jkas.androidpe.menuItemDesigner.MenuItemDesigner;
import jkas.androidpe.menuItemDesigner.databinding.LayoutItemEditorAdapterBinding;
import jkas.androidpe.menuItemDesigner.load.AttrDataLoader;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dialog.DialogBottomSheetAttrModifier;
import jkas.androidpe.resourcesUtils.dialog.DialogDeleteElement;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class NodeView {
    private static final int TYPE_MENU = 0;
    private static final int TYPE_GROUP = 1;
    private static final int TYPE_ITEM = 3;

    private Context C;
    private Element eItem;
    private int type;
    public LayoutItemEditorAdapterBinding binding;

    public NodeView(Context c, Element e) {
        this.C = c;
        this.eItem = e;

        init();
        appendMoreInfo();
        events();
    }

    private void events() {
        binding.lin.setOnClickListener(v -> performClick(v));
    }

    private void performClick(View v) {
        DialogBottomSheetAttrModifier.getInstance(C)
                .setOnTaskRequested(
                        new DialogBottomSheetAttrModifier.OnTaskRequested() {
                            @Override
                            public void onAdd(View v, Element e) {
                                performClickAdd(v);
                            }

                            @Override
                            public void onDelete(Element e) {
                                deleteItem(e);
                            }

                            @Override
                            public void onRefresh() {
                                MenuItemDesigner.MainEditor.getInstance().save();
                                MenuItemDesigner.MainEditor.getInstance().refresh();
                            }
                        });

        if (type != TYPE_MENU) {
            DialogBottomSheetAttrModifier.getInstance(C).initElement(eItem).show();
            DialogBottomSheetAttrModifier.getInstance(C).binding.tvSubTitle.setText("res/menu");
            AttrDataLoader.loadAttrData(C);
        } else {
            final PopupMenu popupMenu = new PopupMenu(C, v);
            popupMenu.setForceShowIcon(true);
            popupMenu
                    .getMenu()
                    .add(Menu.NONE, 1, Menu.NONE, C.getString(R.string.add) + " : (item)")
                    .setIcon(R.drawable.ic_add);
            if (type != TYPE_GROUP)
                popupMenu
                        .getMenu()
                        .add(Menu.NONE, 2, Menu.NONE, C.getString(R.string.add) + " : (group)")
                        .setIcon(R.drawable.ic_add);
            popupMenu
                    .getMenu()
                    .add(Menu.NONE, 3, Menu.NONE, C.getString(R.string.delete))
                    .setIcon(R.drawable.ic_delete);
            popupMenu.setOnMenuItemClickListener(
                    item -> {
                        switch (item.getItemId()) {
                            case 1:
                                addItem(eItem, TYPE_ITEM);
                                MenuItemDesigner.MainEditor.getInstance().refresh();
                                break;
                            case 2:
                                addItem(eItem, TYPE_GROUP);
                                MenuItemDesigner.MainEditor.getInstance().refresh();
                                break;
                            case 3:
                                deleteItem(eItem);
                                break;
                        }
                        return true;
                    });
            popupMenu.show();
        }
    }

    private void performClickAdd(View v) {
        // checks if a submenu already exists
        for (Element e : XmlManager.getAllFirstChildFromElement(eItem)) {
            if (e.getNodeName().equals("menu")) {
                Toast.makeText(
                                C,
                                C.getString(R.string.already_exists) + " : select SubMenu",
                                Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }

        final PopupMenu popupMenu = new PopupMenu(C, v);
        popupMenu.setForceShowIcon(true);
        popupMenu
                .getMenu()
                .add(Menu.NONE, 1, Menu.NONE, C.getString(R.string.add) + " : (item)")
                .setIcon(R.drawable.ic_add);
        if (type != TYPE_GROUP)
            popupMenu
                    .getMenu()
                    .add(Menu.NONE, 2, Menu.NONE, C.getString(R.string.add) + " : (group)")
                    .setIcon(R.drawable.ic_add);
        popupMenu.setOnMenuItemClickListener(
                item -> {
                    switch (item.getItemId()) {
                        case 1:
                            addItem(eItem, TYPE_ITEM);
                            break;
                        case 2:
                            addItem(eItem, TYPE_GROUP);
                            break;
                    }
                    return true;
                });

        popupMenu.show();
    }

    private void addItem(final Element el, int addType) {
        Element e =
                MenuItemDesigner.MainEditor.getInstance()
                        .getXmlManager()
                        .getDocument()
                        .createElement(addType == TYPE_GROUP ? "group" : "item");
        e.setAttribute("android:title", "title");
        if (el.getTagName().equals("item")) {
            Element subMenu =
                    MenuItemDesigner.MainEditor.getInstance()
                            .getXmlManager()
                            .getDocument()
                            .createElement("menu");
            subMenu.appendChild(e);
            el.appendChild(subMenu);
        } else el.appendChild(e);
        MenuItemDesigner.MainEditor.getInstance().save();
        DialogBottomSheetAttrModifier.getInstance(C).BSD.cancel();
    }

    private void deleteItem(Element e) {
        new DialogDeleteElement(C, e)
                .setOnDeleteListener(
                        deleted -> {
                            if (deleted) {
                                MenuItemDesigner.MainEditor.getInstance().save();
                                DialogBottomSheetAttrModifier.getInstance(C).BSD.cancel();
                                MenuItemDesigner.MainEditor.getInstance().refresh();
                            } else
                                Toast.makeText(C, R.string.cant_delete, Toast.LENGTH_SHORT).show();
                        })
                .show();
    }

    private void init() {
        binding = LayoutItemEditorAdapterBinding.inflate(LayoutInflater.from(C));
        NodeViewRoot.setBg(C, binding.lin);
        { // title
            String ref = eItem.getAttribute("android:title");
            if (ref != null) {
                binding.tvTitle.setText(
                        HtmlCompat.fromHtml(
                                "<b>[item] : </b>" + ResourcesValuesFixer.getString(C, ref),
                                HtmlCompat.FROM_HTML_MODE_COMPACT));
            }
        }

        { // icon
            String ref = eItem.getAttribute("android:icon");
            if (ref == null) ref = eItem.getAttribute("app:icon");
            if (ref == null) {
                binding.icon.setVisibility(View.GONE);
                return;
            }
            Drawable d = ResourcesValuesFixer.getDrawable(C, ref);
            if (d == null) binding.icon.setVisibility(View.GONE);
            else {
                binding.icon.setVisibility(View.VISIBLE);
                binding.icon.setImageDrawable(d);
            }
        }
    }

    private void appendMoreInfo() {
        if (eItem.getTagName().equals("menu")) type = TYPE_MENU;
        if (eItem.getTagName().equals("group")) type = TYPE_GROUP;
        if (eItem.getTagName().equals("item")) type = TYPE_ITEM;

        if (type == TYPE_GROUP) {
            String ref = eItem.getAttribute("android:title");
            if (ref != null) {
                binding.tvTitle.setText(
                        HtmlCompat.fromHtml(
                                "<b>[group] : </b>" + ResourcesValuesFixer.getString(C, ref),
                                HtmlCompat.FROM_HTML_MODE_COMPACT));
            } else
                binding.tvTitle.setText(
                        HtmlCompat.fromHtml("<b>[group]</b>", HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else if (type == TYPE_MENU)
            binding.tvTitle.setText(
                    HtmlCompat.fromHtml("<b>SUB[menu]</b>", HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

    public LinearLayout getViewLinSub() {
        return binding.linSub;
    }

    public View getView() {
        return (View) binding.getRoot();
    }
}
