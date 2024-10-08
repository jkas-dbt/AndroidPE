package jkas.androidpe.layoutUiDesigner.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;
import jkas.codeUtil.CodeUtil;

/**
 * @author JKas
 */
public class DragShadowView {
    public static View getView(Context C) {
        View view = new View(C);
        view.setLayoutParams(new LinearLayout.LayoutParams(CodeUtil.px2dp(70), CodeUtil.px2dp(40)));
        GradientDrawable gradient = new GradientDrawable();
        gradient.setCornerRadius(5f);
        gradient.setColor(Color.GRAY);
        view.setBackground(gradient);
        return view;
    }
}
