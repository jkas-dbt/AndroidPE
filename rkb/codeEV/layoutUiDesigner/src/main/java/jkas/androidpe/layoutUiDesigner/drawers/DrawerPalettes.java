package jkas.androidpe.layoutUiDesigner.drawers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.navigationrail.NavigationRailView;
import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.adapter.ViewPalettesAdapter;
import jkas.androidpe.layoutUiDesigner.adapter.ViewPalettesAdapter.DataItem;
import jkas.androidpe.layoutUiDesigner.adapter.dataAllViews.DataItemsLayouts;
import jkas.androidpe.layoutUiDesigner.adapter.dataAllViews.DataItemsMaterial3;
import jkas.androidpe.layoutUiDesigner.adapter.dataAllViews.DataItemsWidgets;
import jkas.androidpe.layoutUiDesigner.databinding.LayoutPalettesBinding;
import jkas.androidpe.layoutUiDesigner.dialog.DialogBottomSheetAttrSetter;
import jkas.androidpe.layoutUiDesigner.tools.ViewCreator;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author JKas
 */
public class DrawerPalettes {
    private OnAnyTaskRequested listener;
    private Context C;
    private LayoutPalettesBinding binding;
    private Element element;
    private String addTypeStr = "";
    private int addType = -1;

    public DrawerLayout drawerLayout;
    public LinearLayout mainView;

    private ArrayList<ViewPalettesAdapter.DataItem> list;

    public DrawerPalettes(Context c, LinearLayout mainView, DrawerLayout drawerLayout) {
        this.C = c;
        this.mainView = mainView;
        this.drawerLayout = drawerLayout;

        init();
        events();
    }

    public void showDefault() {
        list = DataItemsLayouts.getList();
        binding.listView.setAdapter(
                new ViewPalettesAdapter(C, list)
                        .setOnEventListener(
                                new ViewPalettesAdapter.OnEventListener() {

                                    @Override
                                    public boolean onDragAccepted() {
                                        boolean accepted = (addType == -1);
                                        if (accepted) drawerLayout.closeDrawers();
                                        else {
                                            DialogBuilder.showDialog(
                                                    C,
                                                    null,
                                                    "Can not add by drag and drop cause your select add "
                                                            + addTypeStr
                                                            + "."
                                                            + "\n Adding view via Drag and Drop will be available when the drawer is reopened again.");
                                        }
                                        return accepted;
                                    }

                                    @Override
                                    public void onClick(DataItem item) {
                                        createView(item);
                                    }
                                }));
    }

    public View getView() {
        return binding.getRoot();
    }

    private void events() {
        binding.navRail.setOnItemSelectedListener(
                item -> {
                    if (item.getTitle().toString().equals("Layouts")) {
                        list = DataItemsLayouts.getList();
                    } else if (item.getTitle().toString().equals("Widgets")) {
                        list = DataItemsWidgets.getList();
                    } else if (item.getTitle().toString().equals("Material 3")) {
                        list = DataItemsMaterial3.getList();
                    }
                    binding.listView.setAdapter(
                            new ViewPalettesAdapter(C, list)
                                    .setOnEventListener(
                                            new ViewPalettesAdapter.OnEventListener() {

                                                @Override
                                                public boolean onDragAccepted() {
                                                    boolean accepted = (addType == -1);
                                                    if (accepted) drawerLayout.closeDrawers();
                                                    else {
                                                        DialogBuilder.showDialog(
                                                                C,
                                                                null,
                                                                "Can not add by drag and drop cause your select add "
                                                                        + addTypeStr
                                                                        + "."
                                                                        + "\n Adding view via Drag and Drop will be available when the drawer is reopened again.");
                                                    }
                                                    return accepted;
                                                }

                                                @Override
                                                public void onClick(DataItem item) {
                                                    createView(item);
                                                }
                                            }));
                    return true;
                });
    }

    private String getTagName(ViewPalettesAdapter.DataItem dataItem) {
        final String name =
                dataItem.name
                        .replace("(H)", "")
                        .replace("(V)", "")
                        .replace(" : MW", "")
                        .replace(" : WM", "");
        return name;
    }

    private void createView(ViewPalettesAdapter.DataItem dataItem) {
        try {
            if (addType == -1) return;
            final String pkg = getTagName(dataItem);
            if (addType == DialogBottomSheetAttrSetter.ADD_SURROUND) {
                View v = ViewCreator.create("null", C, pkg, false);
                if (!(v instanceof ViewGroup)) {
                    Toast.makeText(C, "Select a parent view", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Document doc = listener.onXmlFileNeeded().getDocument();
            final Element eV = doc.createElement(pkg);
            eV.setAttribute("android:layout_width", "wrap_content");
            eV.setAttribute("android:layout_height", "wrap_content");

            if (dataItem.name.contains("(V)")) eV.setAttribute("android:orientation", "vertical");
            else if (dataItem.name.contains("(H)"))
                eV.setAttribute("android:orientation", "horizontal");
            else if (dataItem.name.contains("MW")) {
                eV.setAttribute("android:layout_width", "match_parent");
                eV.setAttribute("android:layout_height", "8dp");
            } else if (dataItem.name.contains("WM")) {
                eV.setAttribute("android:layout_width", "8dp");
                eV.setAttribute("android:layout_height", "match_parent");
            }

            boolean success = false;
            if (addType == DialogBottomSheetAttrSetter.ADD_INSIDE) {
                if (pkg.endsWith("HorizontalScrollview")
                        || pkg.endsWith("NestedScrollView")
                        || pkg.endsWith("ScrollView")) {
                    if (XmlManager.getAllFirstChildFromElement(element).size() > 0) {
                        Toast.makeText(
                                        C,
                                        R.string.the_parent_can_only_have_one_child,
                                        Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        element.appendChild(eV);
                        success = true;
                    }
                } else {
                    element.appendChild(eV);
                    success = true;
                }
            } else if (addType == DialogBottomSheetAttrSetter.ADD_BEFORE) {
                Node parent = element.getParentNode();
                if (parent != null) {
                    parent.insertBefore(eV, element);
                    success = true;
                }
            } else if (addType == DialogBottomSheetAttrSetter.ADD_SURROUND) {
                Node parent = element.getParentNode();
                if (parent != null) {
                    parent.insertBefore(eV, element);
                    eV.appendChild(element);
                    success = true;
                } else {
                    Node tmp = element.cloneNode(true);
                    doc.removeChild(element);
                    doc.appendChild(tmp);
                    success = true;
                }
            }
            addType = -1;
            if (success) {
                listener.onNewViewAppendByAdd(success);
                drawerLayout.closeDrawers();
            } else {
                Toast.makeText(C, R.string.cant_add, Toast.LENGTH_SHORT).show();
            }
        } catch (DOMException err) {
            Toast.makeText(
                            C,
                            "A fatal error occurred while assigning the view. Please report the problem to the developer\n\n"
                                    + err.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void init() {
        binding = LayoutPalettesBinding.inflate(LayoutInflater.from(C));
        binding.navRail.setBackgroundColor(SurfaceColors.SURFACE_2.getColor(C));
        binding.navRail.setLabelVisibilityMode(NavigationRailView.LABEL_VISIBILITY_SELECTED);
        binding.listView.setDivider(null);
        binding.listView.setDividerHeight(0);
    }

    public void setAddRequested(Element element, int addType) {
        this.element = element;
        this.addType = addType;
        if (addType == DialogBottomSheetAttrSetter.ADD_INSIDE) addTypeStr = "inside";
        else if (addType == DialogBottomSheetAttrSetter.ADD_BEFORE) addTypeStr = "before";
        else if (addType == DialogBottomSheetAttrSetter.ADD_SURROUND)
            addTypeStr = "by surrounding the view";
    }

    public void setOnTaskRequested(OnAnyTaskRequested listener) {
        this.listener = listener;
    }

    public interface OnAnyTaskRequested {
        public XmlManager onXmlFileNeeded();

        public void onNewViewAppendByAdd(boolean success);
    }
}
