package jkas.androidpe.layoutUiDesigner.adapter.dataAllViews;

import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.adapter.ViewPalettesAdapter;
import jkas.androidpe.resources.R;

/**
 * @author JKas
 */
public class DataItemsLayouts {
    private static ArrayList<ViewPalettesAdapter.DataItem> list = new ArrayList<>();

    static {
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "LinearLayout", R.drawable.ic_widget_linear_layout_vertical));
        list.add(new ViewPalettesAdapter.DataItem("ScrollView", R.drawable.ic_widget_scrollview));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "androidx.core.widget.NestedScrollView", R.drawable.ic_widget_scrollview));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "HorizontalScrollView", R.drawable.ic_widget_horizontal_scrollview));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "androidx.swiperefreshlayout.widget.SwipeRefreshLayout",
                        R.drawable.ic_widget_swipe_refresh_layout));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "RelativeLayout", R.drawable.ic_widget_relative_layout));
        list.add(
                new ViewPalettesAdapter.DataItem("FrameLayout", R.drawable.ic_widget_frame_layout));
        list.add(new ViewPalettesAdapter.DataItem("TableLayout", R.drawable.ic_widget_tab_layout));
        list.add(new ViewPalettesAdapter.DataItem("TableRow", R.drawable.ic_widget_tab_layout));
        list.add(new ViewPalettesAdapter.DataItem("GridLayout", R.drawable.ic_widget_grid_layout));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "androidx.drawerlayout.widget.DrawerLayout",
                        R.drawable.ic_widget_drawer_layout));
        list.add(new ViewPalettesAdapter.DataItem("RadioGroup", R.drawable.ic_widget_radio_button));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "androidx.constraintlayout.widget.ConstraintLayout",
                        R.drawable.ic_widget_constraint_layout));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "androidx.coordinatorlayout.widget.CoordinatorLayout",
                        R.drawable.ic_widget_coordinator_layout));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "androidx.constraintlayout.widget.Constraints", R.drawable.ic_widget_view));
        list.add(
                new ViewPalettesAdapter.DataItem("ViewFlipper", R.drawable.ic_widget_view_flipper));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "ViewSwitcher", R.drawable.ic_widget_view_switcher));
        list.add(new ViewPalettesAdapter.DataItem("Toolbar", R.drawable.ic_widget_view));
    }

    public static ArrayList<ViewPalettesAdapter.DataItem> getList() {
        return list;
    }
}
