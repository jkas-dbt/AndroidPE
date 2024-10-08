package jkas.androidpe.resourcesUtils.dataBuilder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.PopupMenu;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class MenuItemCreator {
    private static Context C;

    public static void buildMenu(Context context, Menu menu, String path) {
        C = context;
        menu.clear();
        XmlManager xmlFile = new XmlManager(C);
        if (!xmlFile.initializeFromPath(path)) return;
        Element root = xmlFile.getFirstElement();
        if (root != null) attachAllChild(menu, root);
    }

    private static void attachAllChild(final Menu menu, final Element element) {
        for (final Element e : XmlManager.getAllFirstChildFromElement(element)) {
            if (e.getTagName().equals("item")) {
                if (XmlManager.getAllFirstChildFromElement(e).size() == 1) {
                    final Element eMenu = XmlManager.getAllFirstChildFromElement(e).get(0);
                    if (eMenu.getTagName().equals("menu")) {
                        final SubMenu subMenu =
                                menu.addSubMenu(
                                        ResourcesValuesFixer.getString(
                                                C, e.getAttribute("android:title")));
                        attachAllChild(subMenu, eMenu);
                    }
                } else {
                    final MenuItem item =
                            menu.add(
                                    ResourcesValuesFixer.getString(
                                            C, e.getAttribute("android:title")));
                    applyAttrToItem(item, e);
                }
            } else if (e.getTagName().equals("group")) {
                final SubMenu groupMenu =
                        menu.addSubMenu(
                                ResourcesValuesFixer.getString(C, e.getAttribute("android:title")));
                attachAllChild(groupMenu, e);
                applyAttrToGroup(groupMenu, e);
            }
        }
    }

    private static void applyAttrToGroup(SubMenu group, Element e) {
        String value = null;
        try {
            {
                value = ResourcesValuesFixer.getString(C, e.getAttribute("android:title"));
                if (value != null) group.setHeaderTitle(value);
            }
            {
                value = e.getAttribute("android:checkableBehavior");
                if (value == null) value = e.getAttribute("app:checkableBehavior");
                if (value == null) return;
                if (value.equals("none")) group.setGroupCheckable(0, false, false);
                if (value.equals("all")) group.setGroupCheckable(0, true, false);
                if (value.equals("single")) group.setGroupCheckable(0, true, true);
            } /*
              {
                  value = e.getAttribute("android:checkable");
                  if (value == null) value = e.getAttribute("app:checkable");
                  if (value != null) group.setGroupEnabled(Boolean.parseBoolean(value));
              }

              {
                  value = e.getAttribute("android:visible");
                  if (value == null) value = e.getAttribute("app:visible");
                  if (value != null) group.setGroupVisible(Boolean.parseBoolean(value));
              }*/
        } catch (Exception err) {
            err.printStackTrace();
            // CodeUtil.copyTextToClipBoard(C, err.getMessage());
        }
    }

    private static void applyAttrToItem(MenuItem item, Element e) {
        String value = null;

        /*value = e.getAttribute("android:titleCondensed");
        if (value == null) value = e.getAttribute("app:titleCondensed");
        if (value != null) item.setTitleCondensed(value);*/

        Integer type = -1;
        value = e.getAttribute("android:icon");
        if (value == null || value.length() == 0) value = e.getAttribute("app:icon");
        if (value != null && value.length() != 0) {
            Drawable d = ResourcesValuesFixer.getDrawable(C, value);
            if (d != null) item.setIcon(d);
        }

        value = e.getAttribute("android:showAsAction");
        if (value == null || value.length() == 0) value = e.getAttribute("app:showAsAction");
        if (value != null && value.length() != 0) {
            if (value.contains("ifRoom"))
                if (type == -1) type = MenuItem.SHOW_AS_ACTION_IF_ROOM;
                else type |= MenuItem.SHOW_AS_ACTION_IF_ROOM;

            if (value.contains("never"))
                if (type == -1) type = MenuItem.SHOW_AS_ACTION_NEVER;
                else type |= MenuItem.SHOW_AS_ACTION_NEVER;
            if (value.contains("withText"))
                if (type == -1) type = MenuItem.SHOW_AS_ACTION_WITH_TEXT;
                else type |= MenuItem.SHOW_AS_ACTION_WITH_TEXT;
            if (value.contains("always"))
                if (type == -1) type = MenuItem.SHOW_AS_ACTION_ALWAYS;
                else type |= MenuItem.SHOW_AS_ACTION_ALWAYS;
            if (value.contains("collapseActionView"))
                if (type == -1) type = MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW;
                else type |= MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW;
            if (type != null) item.setShowAsAction(type);
        }

        /*try {
            value = e.getAttribute("android:checkable");
            if (value == null) value = e.getAttribute("app:checkable");
            if (value != null) item.setCheckable(Boolean.parseBoolean(value));
        } catch (Exception err) {
            err.printStackTrace();
        }

        try {
            value = e.getAttribute("android:visible");
            if (value == null) value = e.getAttribute("app:visible");
            if (value != null) item.setVisible(Boolean.parseBoolean(value));
        } catch (Exception err) {
            err.printStackTrace();
        }

        try {
            value = e.getAttribute("android:enabled");
            if (value == null) value = e.getAttribute("app:enabled");
            if (value != null) item.setEnabled(Boolean.parseBoolean(value));
        } catch (Exception err) {
            err.printStackTrace();
        }*/
    }

    /*









    */

    public static PopupMenu createPopupMenu(Context context, View view, String path) {
        final Menu menu = new MenuBuilder(context);
        buildMenu(context, menu, path);
        final PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setForceShowIcon(true);
        attachMenuItemsToPopupMenu(popupMenu, menu);
        popupMenu.setOnMenuItemClickListener(
                item -> {
                    SubMenu m = item.getSubMenu();
                    if (m != null) createSubPopupMenu(context, view, m).show();
                    return false;
                });
        return popupMenu;
    }

    public static PopupMenu createSubPopupMenu(Context context, View view, Menu menu) {
        final PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setForceShowIcon(true);
        attachMenuItemsToPopupMenu(popupMenu, menu);
        popupMenu.setOnMenuItemClickListener(
                item -> {
                    SubMenu m = item.getSubMenu();
                    if (m != null) createSubPopupMenu(context, view, m).show();
                    return false;
                });
        return popupMenu;
    }

    private static void attachMenuItemsToPopupMenu(final PopupMenu popupMenu, final Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            final MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                final SubMenu subMenu =
                        popupMenu
                                .getMenu()
                                .addSubMenu(
                                        Menu.NONE, item.getItemId(), Menu.NONE, item.getTitle());
                attachMenuItemsToPopupMenu(popupMenu, subMenu);
            } else {
                popupMenu.getMenu().add(Menu.NONE, item.getItemId(), Menu.NONE, item.getTitle());
            }
        }
    }
}
