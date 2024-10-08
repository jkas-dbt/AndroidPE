package jkas.androidpe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * @author JKas
 */
public class DrawerLayoutCloseLocked extends DrawerLayout {

    public DrawerLayoutCloseLocked(Context context) {
        super(context);
    }

    public DrawerLayoutCloseLocked(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isDrawerOpen(GravityCompat.START)) return false;
        else return super.onInterceptTouchEvent(ev);
    }
}
