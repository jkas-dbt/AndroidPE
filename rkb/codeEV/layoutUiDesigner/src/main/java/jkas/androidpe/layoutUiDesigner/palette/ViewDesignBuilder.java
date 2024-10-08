package jkas.androidpe.layoutUiDesigner.palette;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import jkas.codeUtil.CodeUtil;

/**
 * @author JKas
 */
public class ViewDesignBuilder {
    private OnViewNotFound listener;
    private String pkg;
    private int style;
    private Context C;
    private View view;
    private boolean isParent;

    public ViewDesignBuilder(Context context, String pkg, int style, boolean isParent) {
        this.C = context;
        this.pkg = pkg;
        this.style = style;
        this.isParent = isParent;
    }

    private void initDefault() {
        if (pkg.contains("Linear")) initView("LinearLayout");
        else if (pkg.contains("Drawer") || pkg.contains("DrawerLayout")) {
            if (style == -1) view = new DrawerLayout(C);
            else view = new DrawerLayout(C, null, style);
            view.setId(View.generateViewId());
        } else if (pkg.endsWith("SwipeRefreshLayout")) initView("LinearLayout");
        else view = new DefaultView(C, pkg);
    }

    private void initView(String pkg) {
        if (!pkg.contains(".")) {
            if (pkg.equals("View")) {
                view = new View(C);
                view.setId(View.generateViewId());
                return;
            } else if (pkg.equals("WebView")) {
                view = new WebView(C);
                view.setId(View.generateViewId());
                return;
            } else pkg = "android.widget." + pkg;
        } else if (pkg.equals("androidx.swiperefreshlayout.widget.SwipeRefreshLayout")) {
            initDefault();
            return;
        }

        try {
            if (!CodeUtil.isClass(pkg)) {
                initDefault();
                return;
            }
            Class<?> clazz = Class.forName(pkg);
            Constructor<?> constructor;
            if (style == -1) constructor = clazz.getDeclaredConstructor(Context.class);
            else
                constructor =
                        clazz.getDeclaredConstructor(Context.class, AttributeSet.class, int.class);
            Object object;
            if (style == -1) object = constructor.newInstance(C);
            else object = constructor.newInstance(C, null, style);
            view = (View) object;
            view.setId(View.generateViewId());
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | IllegalArgumentException e) {
            listener.onNotFound(e.getMessage());
        }
    }

    public View getView() {
        if (view == null) {
            style = -1;
            initView(pkg);
            if (view == null) initDefault();
        }
        return view;
    }

    public void setOnViewNotFound(OnViewNotFound listener) {
        this.listener = listener;
        initView(pkg);
    }

    public interface OnViewNotFound {
        public void onNotFound(String msg);
    }
}
