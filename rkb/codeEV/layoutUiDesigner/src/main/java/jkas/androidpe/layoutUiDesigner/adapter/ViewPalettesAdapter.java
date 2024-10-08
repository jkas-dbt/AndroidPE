package jkas.androidpe.layoutUiDesigner.adapter;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.elevation.SurfaceColors;
import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.databinding.LayoutListItemBinding;
import jkas.androidpe.layoutUiDesigner.tools.ViewCreator;
import jkas.androidpe.layoutUiDesigner.utils.DragAndDropUtils;
import jkas.androidpe.layoutUiDesigner.utils.Utils;
import jkas.androidpe.resourcesUtils.utils.ViewUtils;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class ViewPalettesAdapter extends ArrayAdapter<ViewPalettesAdapter.DataItem> {
    private OnEventListener listener;
    private Context C;
    private boolean addByDragDrop;

    public ViewPalettesAdapter(
            @NonNull Context context, ArrayList<ViewPalettesAdapter.DataItem> arrayList) {
        super(context, 0, arrayList);
        this.C = context;
        this.addByDragDrop = addByDragDrop;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataItem dataItem = getItem(position);
        LayoutListItemBinding binding = LayoutListItemBinding.inflate(LayoutInflater.from(C));
        binding.img.setImageResource(dataItem.imgRes);
        String name = dataItem.name;
        if (name.contains(".")) name = Files.getNameFromAbsolutePath(name.replace(".", "/"));
        binding.tv.setText(name);
        ViewUtils.setBgCornerRadius(binding.getRoot(), SurfaceColors.SURFACE_1.getColor(C));
        event(binding.getRoot(), dataItem);
        return (View) binding.getRoot();
    }

    private void event(final View view, DataItem item) {
        view.setOnLongClickListener(
                v -> {
                    if (!Utils.CurrentSettings.isDrawStrokeEnabled) return true;
                    Utils.CurrentSettings.addByDrag = true;
                    if (listener.onDragAccepted())
                        DragAndDropUtils.startDragAndDrop(
                                v,
                                new ClipData(
                                        item.name,
                                        new String[] {"text/plain"},
                                        new ClipData.Item(item.name)),
                                ViewCreator.create("null", C, item.name, false));
                    return true;
                });

        view.setOnClickListener(v -> listener.onClick(item));
    }

    public static class DataItem {
        public int imgRes;
        public String name;

        public DataItem(String name, int imgRes) {
            this.name = name;
            this.imgRes = imgRes;
        }
    }

    public ViewPalettesAdapter setOnEventListener(OnEventListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnEventListener {
        public boolean onDragAccepted();

        public void onClick(DataItem item);
    }
}
