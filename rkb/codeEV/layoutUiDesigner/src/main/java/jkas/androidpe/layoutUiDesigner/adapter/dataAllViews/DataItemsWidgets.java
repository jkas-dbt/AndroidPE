package jkas.androidpe.layoutUiDesigner.adapter.dataAllViews;

import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.adapter.ViewPalettesAdapter;
import jkas.androidpe.resources.R;

/**
 * @author JKas
 */
public class DataItemsWidgets {
    private static ArrayList<ViewPalettesAdapter.DataItem> list = new ArrayList<>();

    static {
        list.add(new ViewPalettesAdapter.DataItem("TextView", R.drawable.ic_widget_text_view));
        list.add(new ViewPalettesAdapter.DataItem("Button", R.drawable.ic_widget_button));
        list.add(new ViewPalettesAdapter.DataItem("ImageView", R.drawable.ic_widget_image_view));
        list.add(
                new ViewPalettesAdapter.DataItem("ImageButton", R.drawable.ic_widget_image_button));
        list.add(new ViewPalettesAdapter.DataItem("EditText", R.drawable.ic_widget_edit_text));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "AutoCompleteTextView", R.drawable.ic_widget_auto_complete_text_view));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "MultiAutoCompleteTextView", R.drawable.ic_widget_auto_complete_text_view));
        list.add(
                new ViewPalettesAdapter.DataItem("RadioButton", R.drawable.ic_widget_radio_button));
        list.add(new ViewPalettesAdapter.DataItem("CheckBox", R.drawable.ic_widget_checkbox));
        list.add(new ViewPalettesAdapter.DataItem("Switch", R.drawable.ic_widget_switch));
        list.add(new ViewPalettesAdapter.DataItem("ToggleButton", R.drawable.ic_widget_switch));
        list.add(new ViewPalettesAdapter.DataItem("SeekBar", R.drawable.ic_widget_seek_bar));
        list.add(new ViewPalettesAdapter.DataItem("ProgressBar", R.drawable.ic_widget_seek_bar));
        list.add(new ViewPalettesAdapter.DataItem("Spinner", R.drawable.ic_widget_spinner));
        list.add(new ViewPalettesAdapter.DataItem("RatingBar", R.drawable.ic_widget_rating_bar));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "NumberPicker", R.drawable.ic_widget_number_picker));

        list.add(new ViewPalettesAdapter.DataItem("ListView", R.drawable.ic_widget_list_view));
        list.add(new ViewPalettesAdapter.DataItem("GridView", R.drawable.ic_widget_grid_layout));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "androidx.recyclerview.widget.RecyclerView",
                        R.drawable.ic_widget_list_view));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "androidx.viewpager.widget.ViewPager", R.drawable.ic_widget_view_pager));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "androidx.viewpager2.widget.ViewPager2", R.drawable.ic_widget_view_pager));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "CalendarView", R.drawable.ic_widget_calendar_view));
        list.add(
                new ViewPalettesAdapter.DataItem("AnalogClock", R.drawable.ic_widget_analog_clock));
        list.add(new ViewPalettesAdapter.DataItem("Chronometer", R.drawable.ic_widget_chronometer));
        list.add(new ViewPalettesAdapter.DataItem("TextClock", R.drawable.ic_widget_analog_clock));
        list.add(
                new ViewPalettesAdapter.DataItem("TextureView", R.drawable.ic_widget_texture_view));
        list.add(
                new ViewPalettesAdapter.DataItem("SurfaceView", R.drawable.ic_widget_surface_view));
        list.add(new ViewPalettesAdapter.DataItem("VideoView", R.drawable.ic_widget_video_view));
        list.add(
                new ViewPalettesAdapter.DataItem(
                        "android.webkit.WebView", R.drawable.ic_widget_web_view));
        list.add(new ViewPalettesAdapter.DataItem("View", R.drawable.ic_widget_view));
    }

    public static ArrayList<ViewPalettesAdapter.DataItem> getList() {
        return list;
    }
}
