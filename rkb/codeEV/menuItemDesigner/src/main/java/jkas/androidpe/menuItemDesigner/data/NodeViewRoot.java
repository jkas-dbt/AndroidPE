package jkas.androidpe.menuItemDesigner.data;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.widget.PopupMenu;
import android.view.Menu;
import com.google.android.material.elevation.SurfaceColors;
import jkas.androidpe.menuItemDesigner.MenuItemDesigner;
import jkas.androidpe.menuItemDesigner.databinding.LayoutItemEditorAdapterBinding;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.utils.ViewUtils;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class NodeViewRoot {
    private Context C;
    private Element eRoot;
    private LayoutItemEditorAdapterBinding binding;

    public NodeViewRoot(Context c, Element e) {
        this.C = c;
        this.eRoot = e;

        init();
        loadData(eRoot, binding.linSub);
        loadEvents();
    }

    private void loadEvents() {
        binding.lin.setOnClickListener(v -> showPopup(v));
    }

    private void showPopup(View v) {
        final PopupMenu popupMenu = new PopupMenu(C, v);
        popupMenu.setForceShowIcon(true);
        popupMenu
                .getMenu()
                .add(Menu.NONE, 1, Menu.NONE, C.getString(R.string.add) + " : (item)")
                .setIcon(R.drawable.ic_add);
        popupMenu
                .getMenu()
                .add(Menu.NONE, 2, Menu.NONE, C.getString(R.string.add) + " : (group)")
                .setIcon(R.drawable.ic_add);
        popupMenu.setOnMenuItemClickListener(
                item -> {
                    addItem(item.getItemId() == 1 ? "item" : "group");
                    return true;
                });
        popupMenu.show();
    }

    private void addItem(String tagName) {
        Element e =
                MenuItemDesigner.MainEditor.getInstance()
                        .getXmlManager()
                        .getDocument()
                        .createElement(tagName);
        e.setAttribute("android:title", "title");
        eRoot.appendChild(e);
        MenuItemDesigner.MainEditor.getInstance().save();
        loadData(eRoot, binding.linSub);
    }

    private void loadData(final Element root, final LinearLayout linSub) {
        linSub.removeAllViews();
        for (final Element e : XmlManager.getAllFirstChildFromElement(root)) {
            final NodeView nv = new NodeView(C, e);
            linSub.addView(nv.getView());
            loadData(e, nv.getViewLinSub());
        }
    }

    private void init() {
        binding = LayoutItemEditorAdapterBinding.inflate(LayoutInflater.from(C));
        binding.icon.setImageResource(R.drawable.ic_xml);
        binding.tvTitle.setText("menu [ROOT]");
        setBg(C, binding.lin);
    }

    public View getView() {
        return (View) binding.getRoot();
    }

    public static View setBg(Context C, View v) {
        ViewUtils.setBackgroundRipple(v, SurfaceColors.SURFACE_2.getColor(C), Color.GRAY);
        v.setLayoutParams(CodeUtil.getLayoutParamsMW(8));
        v.setElevation(12);
        return v;
    }
}
