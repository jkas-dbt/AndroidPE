package jkas.androidpe.layoutUiDesigner.palette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;

/**
 * @author JKas
 */
public class MainView extends LinearLayout {

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(ResourcesValuesFixer.getColor(getContext(), "?colorSurface"));
        gradient.setCornerRadius(ResourcesValuesFixer.getDimen(getContext(), "12dp"));
        gradient.setStroke(2, ResourcesValuesFixer.getColor(getContext(), "?colorOnSurface"));
        this.setBackground(gradient);
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0)
            throw new IllegalStateException("RootView can host only one direct child");
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0)
            throw new IllegalStateException("RootView can host only one direct child");
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0)
            throw new IllegalStateException("RootView can host only one direct child");
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0)
            throw new IllegalStateException("RootView can host only one direct child");
        super.addView(child, index, params);
    }
}
