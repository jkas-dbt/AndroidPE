package jkas.androidpe.resourcesUtils.dialog;

import android.content.Context;
import jkas.androidpe.resources.R;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author JKas
 */
public class DialogDeleteElement {
    private OnDeleteListener listener;
    private Context C;
    private Element element;

    public DialogDeleteElement(Context c, Element e) {
        this.C = c;
        this.element = e;
    }

    public void show() {
        DialogBuilder.getDialogBuilder(
                        C,
                        C.getString(R.string.warning),
                        C.getString(R.string.warning_delete)
                                + "\n\n"
                                + "["
                                + element.getTagName()
                                + " : "
                                + element.getAttribute("name")
                                + "]")
                .setPositiveButton(
                        R.string.delete,
                        (d, v) -> {
                            Node parent = element.getParentNode();
                            if (parent == null) {
                                listener.onDeleted(false);
                            }
                            parent.removeChild(element);
                            listener.onDeleted(true);
                        })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public DialogDeleteElement setOnDeleteListener(OnDeleteListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnDeleteListener {
        public void onDeleted(boolean deleted);
    }
}
