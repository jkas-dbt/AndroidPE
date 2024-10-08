package jkas.androidpe.resourcesUtils.adapters;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ author JKas
 */
public class BottomMarginItemDecorationAdapter extends RecyclerView.ItemDecoration {
    private int margin;

    public BottomMarginItemDecorationAdapter(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = margin;
        }
    }
}
