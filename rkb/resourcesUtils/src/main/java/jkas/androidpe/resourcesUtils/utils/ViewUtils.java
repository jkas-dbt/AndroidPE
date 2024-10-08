package jkas.androidpe.resourcesUtils.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.graphics.Bitmap;
import java.io.File;
import java.util.Random;
import java.io.FileOutputStream;
import android.graphics.drawable.GradientDrawable;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;

/**
 * @author JKas
 */
public class ViewUtils {
    
    public static void setBackground(
            View view, int bgColor, int stroke, int strokeColor, float cornerRadius) {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(bgColor);
        gradient.setCornerRadius(cornerRadius);
        gradient.setStroke(stroke, strokeColor);
        view.setBackground(gradient);
    }

    public static void setBgCornerRadius(View v, int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(43f);
        ColorStateList csl = ColorStateList.valueOf(Color.GRAY);
        RippleDrawable rd = new RippleDrawable(csl, gd, null);
        v.setBackground(rd);
    }

    public static void setBgCornerRadius(View v, int color, float cornerRadius) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(cornerRadius);
        ColorStateList csl = ColorStateList.valueOf(Color.GRAY);
        RippleDrawable rd = new RippleDrawable(csl, gd, null);
        v.setBackground(rd);
    }

    public static void setBackgroundRipple(View v, int colorGradient, int colorRipple) {
        setBackgroundRipple(v, colorGradient, colorRipple, 43);
    }

    public static void setBackgroundRipple(View v, int colorG, int colorR, float cornerR) {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(colorG);
        gradient.setCornerRadius(cornerR);
        ColorStateList colorState = ColorStateList.valueOf(colorR);
        RippleDrawable ripple = new RippleDrawable(colorState, gradient, null);
        v.setBackground(ripple);
    }
}
