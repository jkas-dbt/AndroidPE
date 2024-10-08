package jkas.androidpe.layoutUiDesigner.utils;

import android.graphics.drawable.Drawable;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import jkas.androidpe.layoutUiDesigner.tools.ViewCreator;
import jkas.androidpe.layoutUiDesigner.palette.DefaultView;
import android.view.View;
import jkas.androidpe.layoutUiDesigner.R;
import jkas.codeUtil.Files;
import org.w3c.dom.Element;
import java.util.Map;
import java.util.ArrayList;
import androidx.core.util.Pair;
import java.util.LinkedHashMap;
import java.util.Set;
import org.w3c.dom.Node;

/**
 * @author JKas
 */
public class Utils {
    public static Node eCopied;

    public static ArrayList<Pair<?, ?>> convertMapToPairViewElement(Map<?, ?> map) {
        final ArrayList<Pair<?, ?>> listPair = new ArrayList<>();

        Set<?> keys = map.keySet();
        for (Object key : keys) {
            View view = (View) key;
            Element element = (Element) map.get(view);
            listPair.add(new Pair<>(view, element));
        }
        return listPair;
    }

    public static ArrayList<Pair<?, ?>> convertLinkedHashMapToPairViewElement(
            LinkedHashMap<?, ?> map) {
        final ArrayList<Pair<?, ?>> listPair = new ArrayList<>();

        Set<?> keys = map.keySet();
        for (Object key : keys) {
            View view = (View) key;
            Element element = (Element) map.get(view);
            listPair.add(new Pair<>(view, element));
        }
        return listPair;
    }

    public static String createTagFromFileName(String path) {
        return Files.getNameFromAbsolutePath(path) + View.generateViewId();
    }

    public static void drawDashPathStroke(View view) {
        if (CurrentSettings.isDrawStrokeEnabled)
            drawDashPath(view, view.getContext().getDrawable(R.drawable.draw_dash));
    }

    public static void drawDashPathStrokeSelected(View view) {
        if (CurrentSettings.isDrawStrokeEnabled)
            drawDashPath(view, view.getContext().getDrawable(R.drawable.draw_dash_selected));
    }

    public static void drawDashPath(View view, Drawable drawable) {
        Drawable foreground = view.getForeground();
        if (foreground != null) {
            if (foreground instanceof LayerDrawable) {
                LayerDrawable oldLayer = (LayerDrawable) foreground;
                if (oldLayer.getNumberOfLayers() == 1) {
                    LayerDrawable layer = new LayerDrawable(new Drawable[] {drawable});
                    view.setForeground(layer);
                } else {
                    LayerDrawable layer =
                            new LayerDrawable(new Drawable[] {oldLayer.getDrawable(0)});
                    layer.addLayer(drawable);
                    view.setForeground(layer);
                }
            } else {
                LayerDrawable layer = new LayerDrawable(new Drawable[] {foreground});
                layer.addLayer(drawable);
                view.setForeground(layer);
            }
        } else {
            LayerDrawable layer = new LayerDrawable(new Drawable[] {drawable});
            view.setForeground(layer);
        }
    }

    public static boolean isParentView(@NonNull View view) {
        return view instanceof ViewGroup;
    }

    public static boolean isParentView(Context context, @NonNull String pkg) {
        View view = ViewCreator.create("", context, pkg, false);
        if (!(view instanceof DefaultView)) return (view instanceof ViewGroup);
        return false;
    }

    public static class CurrentSettings {
        public static boolean isDrawStrokeEnabled = true;
        public static boolean addByDrag = false;
    }
}
