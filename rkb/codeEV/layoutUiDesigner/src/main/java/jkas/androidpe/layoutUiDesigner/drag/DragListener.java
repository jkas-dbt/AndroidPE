package jkas.androidpe.layoutUiDesigner.drag;

import android.content.ClipData;
import android.content.Context;
import android.view.View;
import android.view.DragEvent;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import jkas.androidpe.layoutUiDesigner.tools.AndroidXmlParser;
import jkas.androidpe.layoutUiDesigner.tools.RefViewElement;
import jkas.androidpe.layoutUiDesigner.utils.Utils;
import jkas.androidpe.resourcesUtils.utils.ViewUtils;
import jkas.codeUtil.CodeUtil;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DragListener implements View.OnDragListener {
    private OnEventListener listener;
    private Context C;
    private View newViewParent;
    private ViewGroup newContainer;
    private View shadow;
    private View draggedView;
    private ClipData data;

    private int orientation = -1;
    private int dropPosition = -1;

    private Element currentElement;
    private Element newParentElement;

    @Override
    public synchronized boolean onDrag(View vCurrentParent, DragEvent event) {
        try {
            initView(vCurrentParent);

            orientation = -1;
            dropPosition = -1;
            draggedView = (View) event.getLocalState();
            data = event.getClipData();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (event.getClipData() == null) removeViewFromCurrentParent(draggedView);
                    listener.onDragStarted();
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    removeViewFromCurrentParent(draggedView);
                    Utils.drawDashPathStrokeSelected(newViewParent);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    dropPosition = getDropPosition(event);
                    addToContainer(shadow, false);
                    break;
                case DragEvent.ACTION_DROP:
                    dropPosition = getDropPosition(event);
                    addToContainer(draggedView, true);
                    if (listener != null) listener.onDragFinish();
                    break;
                case DragEvent.ACTION_DRAG_ENDED, DragEvent.ACTION_DRAG_EXITED:
                    removeViewFromCurrentParent(shadow);
                    Utils.drawDashPathStroke(newViewParent);
                    if (listener != null) listener.info(null);
                    break;
            }
        } catch (Exception err) {
            if (listener != null) {
                listener.onDragError();
                listener.info("drop error : " + err.getMessage());
            }
        }
        return true;
    }

    private void update() {
        if (listener == null) return;
        if (dropPosition == -1) return;

        if (isTheSameDropPosition()) return;
        else if (data == null) updateXmlRef();
        else updateNewXmlCode();
    }

    private boolean isTheSameDropPosition() {
        for (var te :
                listener.onAndroidXmlParserNeeded().getRefViewElement().getListTreeElement()) {
            if (newParentElement == te.getCurrentElement()) {
                if (dropPosition < te.getChildren().size()) {
                    if (currentElement == te.getChildren().get(dropPosition)) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    private void updateXmlRef() {
        Element childSubling = null;
        if (orientation != -1) {
            if (newContainer.getChildCount() > 0) {
                for (var te :
                        listener.onAndroidXmlParserNeeded()
                                .getRefViewElement()
                                .getListTreeElement()) {
                    if (newParentElement == te.getCurrentElement()) {
                        if (dropPosition < te.getChildren().size()) {
                            childSubling = te.getChildren().get(dropPosition);
                        }
                        break;
                    }
                }
            }
        }
        var teCurrent =
                listener.onAndroidXmlParserNeeded()
                        .getRefViewElement()
                        .getTreeElement(currentElement);
        if (teCurrent != null) teCurrent.setParent(newParentElement);
        for (var te :
                listener.onAndroidXmlParserNeeded().getRefViewElement().getListTreeElement()) {
            if (te.getCurrentElement() == newParentElement) {
                te.getChildren().remove(currentElement);
                if (dropPosition >= te.getChildren().size()) te.addChild(currentElement);
                else te.getChildren().add(dropPosition, currentElement);
                break;
            }
        }
        if (childSubling == null) newParentElement.appendChild(currentElement);
        else newParentElement.insertBefore(currentElement, childSubling);
        listener.onAndroidXmlParserNeeded().setAttr(draggedView, currentElement);
    }

    private void updateNewXmlCode() {
        Element newElement = createElement(data.getItemAt(0).getText().toString());
        Element newParent =
                listener.onAndroidXmlParserNeeded()
                        .getRefViewElement()
                        .getListRef()
                        .get(this.newViewParent);
        Element childSubling = null;
        if (orientation != -1) {
            if (newContainer.getChildCount() > 0) {
                for (var te :
                        listener.onAndroidXmlParserNeeded()
                                .getRefViewElement()
                                .getListTreeElement()) {
                    if (newParentElement == te.getCurrentElement()) {
                        if (dropPosition < te.getChildren().size()) {
                            childSubling = te.getChildren().get(dropPosition);
                        }
                        break;
                    }
                }
            }
        }
        for (var te :
                listener.onAndroidXmlParserNeeded().getRefViewElement().getListTreeElement()) {
            if (te.getCurrentElement() == newParent) {
                if (dropPosition >= te.getChildren().size()) te.addChild(newElement);
                else te.getChildren().add(dropPosition, newElement);
                break;
            }
        }
        RefViewElement.TreeElement te = new RefViewElement.TreeElement(newElement, false);
        te.setParent(newParent);
        if (childSubling != null) newParent.insertBefore(newElement, childSubling);
        else newParent.appendChild(newElement);
        listener.onAndroidXmlParserNeeded().setAttr(draggedView, newElement);
        listener.onAndroidXmlParserNeeded().getRefViewElement().addRef(draggedView, newElement);
        listener.onAndroidXmlParserNeeded().getRefViewElement().addTreeRef(te);
        listener.onAddView(draggedView);
    }

    private Element createElement(String pkg) {
        Element element =
                listener.onAndroidXmlParserNeeded()
                        .getXmlManager()
                        .getDocument()
                        .createElement(pkg);
        element.setAttribute("android:layout_width", "wrap_content");
        element.setAttribute("android:layout_height", "wrap_content");
        element.setAttribute("android:padding", "8dp");
        return element;
    }

    private void removeViewFromCurrentParent(View view) {
        if (newContainer != null) newContainer.removeView(view);
    }

    private void initView(View v) {
        if (C == null) {
            C = v.getContext();
            shadow = new View(v.getContext());
            shadow.setLayoutParams(new LinearLayout.LayoutParams(100, 70));
            ViewUtils.setBgCornerRadius(shadow, Color.GRAY, 12f);
        }

        newViewParent = v;
        if (newViewParent instanceof ViewGroup) newContainer = (ViewGroup) newViewParent;

        currentElement =
                listener.onAndroidXmlParserNeeded()
                        .getRefViewElement()
                        .getListRef()
                        .get(this.draggedView);

        newParentElement =
                listener.onAndroidXmlParserNeeded()
                        .getRefViewElement()
                        .getListRef()
                        .get(this.newViewParent);
    }

    private int getDropPosition(DragEvent event) {
        if (newContainer == null) return -1;

        final int x = (int) event.getX();
        final int y = (int) event.getY();

        orientation = -1;
        try {
            Object obj =
                    CodeUtil.invoke(
                            newViewParent, "getOrientation", new Class[] {}, new Object[] {});
            orientation = Integer.parseInt(obj.toString());
        } catch (Exception err) {
            orientation = -1;
        }

        int index = -1;
        if (orientation == LinearLayout.VERTICAL) {
            index = 0;
            for (int i = 0; i < newContainer.getChildCount(); i++) {
                View child = newContainer.getChildAt(i);
                if (child == shadow) continue;
                if (child.getBottom() < y) index++;
            }
        } else if (orientation == LinearLayout.HORIZONTAL) {
            index = 0;
            for (int i = 0; i < newContainer.getChildCount(); i++) {
                View child = newContainer.getChildAt(i);
                if (child == shadow) continue;
                if (child.getRight() < x) index++;
            }
        }
        if (index == -1 || index >= newContainer.getChildCount())
            index = newContainer.getChildCount() - 1;
        return index;
    }

    private void addToContainer(View view, boolean drop) {
        if (newContainer == null) return;
        removeViewFromCurrentParent(shadow);
        removeViewFromCurrentParent(view);

        try {
            add(view, drop);
        } catch (Exception err) {
            ViewParent vp = view.getParent();
            if (vp != null && err.getMessage().contains("remove")) {
                try {
                    ViewGroup vg = (ViewGroup) vp;
                    vg.removeView(view);
                    add(view, drop);
                } catch (Exception subErr) {
                    if (listener != null) listener.info(subErr.getMessage());
                }
                if (listener != null) listener.info(err.getMessage());
            } else if (drop && listener != null) {
                listener.reloadViews();
            } else if (listener != null) {
                if (err.getMessage().contains("Scroll")) listener.info(err.getMessage());
                else
                    listener.info(
                            "A " + getParentClassName() + " can not have a child by this way");
            }
        }
    }

    private String getParentClassName() {
        String pkg = newViewParent.getClass().getName();
        if (pkg.contains(".")) pkg = pkg.substring(pkg.lastIndexOf(".") + 1);
        return pkg;
    }

    private void add(View view, boolean drop) {
        if (newContainer == null) return;
        removeViewFromCurrentParent(shadow);
        removeViewFromCurrentParent(view);

        if (dropPosition < newContainer.getChildCount() && dropPosition >= 0)
            newContainer.addView(view, dropPosition);
        else newContainer.addView(view);
        if (listener != null) listener.info(null);
        if (drop) update();
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        public void onDragStarted();

        public void onDragFinish();

        public void onDragError();

        public void onAddView(View view);

        public void reloadViews();

        public void info(String infoToPrint);

        public AndroidXmlParser onAndroidXmlParserNeeded();
    }
}
