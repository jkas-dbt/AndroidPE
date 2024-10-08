package jkas.androidpe.layoutUiDesigner.treeView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import jkas.androidpe.layoutUiDesigner.adapter.ViewPalettesAdapter;
import jkas.androidpe.layoutUiDesigner.adapter.dataAllViews.DataItemsLayouts;
import jkas.androidpe.layoutUiDesigner.adapter.dataAllViews.DataItemsMaterial3;
import jkas.androidpe.layoutUiDesigner.adapter.dataAllViews.DataItemsWidgets;
import jkas.androidpe.layoutUiDesigner.databinding.LayoutTreeViewItemBinding;
import jkas.androidpe.resources.R;
import jkas.codeUtil.Files;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class TreeNodeView {
    public Context C;
    public Element element;
    public LayoutTreeViewItemBinding binding;
    private boolean enabledArrow;

    public TreeNodeView(Context c, Element e, boolean enabledArrow) {
        C = c;
        this.element = e;
        this.enabledArrow = enabledArrow;

        init();
    }

    private void init() {
        binding = LayoutTreeViewItemBinding.inflate(LayoutInflater.from(C));
        binding.tvPkgName.setText(element.getNodeName());

        String name = Files.getNameFromAbsolutePath(element.getNodeName().replace(".", "/"));

        String id = element.getAttribute("android:id");
        binding.tvIdName.setText(
                id == null ? "id : (not yet)" : id.equals("") ? "id : (not yet)" : id);

        if (enabledArrow) binding.icChevron.setVisibility(View.VISIBLE);
        else binding.icChevron.setVisibility(View.GONE);

        int resDrawable = R.drawable.ic_widget_frame_layout;
        for (ViewPalettesAdapter.DataItem item : DataItemsLayouts.getList()) {
            if (item.name.matches(name + ".*")) {
                resDrawable = item.imgRes;
                break;
            }
        }

        for (ViewPalettesAdapter.DataItem item : DataItemsWidgets.getList()) {
            if (item.name.matches(name + ".*")) {
                resDrawable = item.imgRes;
                break;
            }
        }

        for (ViewPalettesAdapter.DataItem item : DataItemsMaterial3.getList()) {
            if (item.name.matches(name + ".*")) {
                resDrawable = item.imgRes;
                break;
            }
        }

        binding.icIcon.setImageResource(resDrawable);

        if (element.getNodeName().equals("LinearLayout")) {
            if (element.getAttribute("android:orientation").equals("horizontal"))
                binding.icIcon.setImageResource(R.drawable.ic_widget_linear_layout_horizontal);
        } else if (element.getNodeName().equals("HorizontalScrollView")) {
            binding.icIcon.setImageResource(R.drawable.ic_widget_horizontal_scrollview);
        }
    }
}
