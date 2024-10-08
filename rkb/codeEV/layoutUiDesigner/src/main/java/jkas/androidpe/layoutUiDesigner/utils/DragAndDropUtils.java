package jkas.androidpe.layoutUiDesigner.utils;

import android.view.View;
import android.content.ClipData;

/**
 * @author JKas
 */
public class DragAndDropUtils {
    public static boolean startDragAndDrop(View v, ClipData data, Object localState) {
        return v.startDragAndDrop(data, new View.DragShadowBuilder(v), localState, 1);
    }

    public static void cancelDragAndDrop(View v) {
        v.cancelDragAndDrop();
    }

    public static void updateDragShadow(View v, View.DragShadowBuilder shadowBuilder) {
        v.updateDragShadow(shadowBuilder);
    }
}
