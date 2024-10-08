package jkas.androidpe.layoutUiDesigner.adapter.dataAllViews;

import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.adapter.ViewPalettesAdapter;
import jkas.androidpe.resources.R;

/**
 * @author JKas
 */
public class DataItemsMaterial3 {
    private static ArrayList<ViewPalettesAdapter.DataItem> list = new ArrayList<>();

    static {
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.bottomappbar.BottomAppBar",
                        R.drawable.ic_widget_horizontal_scrollview));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.appbar.MaterialToolbar",
                        R.drawable.ic_widget_frame_layout));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.appbar.AppBarLayout",
                        R.drawable.ic_widget_view));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.bottomnavigation.BottomNavigationView",
                        R.drawable.ic_widget_horizontal_scrollview));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.divider.MaterialDivider",
                        R.drawable.ic_widget_material_diviser));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.button.MaterialButton",
                        R.drawable.ic_widget_button));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.floatingactionbutton.FloatingActionButton",
                        R.drawable.ic_widget_floating_action_button));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.card.MaterialCardView",
                        R.drawable.ic_widget_button));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.checkbox.MaterialCheckBox",
                        R.drawable.ic_widget_checkbox));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.navigationrail.NavigationRailView",
                        R.drawable.ic_widget_scrollview));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.navigation.NavigationView",
                        R.drawable.ic_widget_scrollview));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.radiobutton.MaterialRadioButton",
                        R.drawable.ic_widget_radio_button));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.textview.MaterialTextView",
                        R.drawable.ic_widget_text_view));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.textfield.MaterialAutoCompleteTextView",
                        R.drawable.ic_widget_edit_text));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.imageview.ShapeableImageView",
                        R.drawable.ic_widget_image_view));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.slider.Slider",
                        R.drawable.ic_widget_seek_bar));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.slider.RangeSlider",
                        R.drawable.ic_widget_seek_bar));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.button.MaterialButtonToggleGroup",
                        R.drawable.ic_widget_frame_layout));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.progressindicator.LinearProgressIndicator",
                        R.drawable.ic_widget_linear_progress));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.progressindicator.CircularProgressIndicator",
                        R.drawable.ic_widget_progress_bar));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.textfield.TextInputLayout",
                        R.drawable.ic_widget_frame_layout));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.textfield.TextInputEditText",
                        R.drawable.ic_widget_edit_text));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.materialswitch.MaterialSwitch",
                        R.drawable.ic_widget_switch));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.switchmaterial.SwitchMaterial",
                        R.drawable.ic_widget_switch));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "com.google.android.material.tabs.TabLayout",
                        R.drawable.ic_widget_tab_layout));
    }

    public static ArrayList<ViewPalettesAdapter.DataItem> getList() {
        return list;
    }
}
