package jkas.androidpe.layoutUiDesigner.tools;

import android.view.View;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class RefViewElement {
    private static final int PREVIOUS_SIBLING = 0;
    private static final int NEXT_SIBLING = 1;

    private LinkedHashMap<View, Element> listRef;
    private LinkedHashMap<Element, View> listRefInverse;
    private ArrayList<TreeElement> listTreeElement;

    public RefViewElement() {
        listRef = new LinkedHashMap<>();
        listRefInverse = new LinkedHashMap<>();
        listTreeElement = new ArrayList<>();
    }

    public boolean addRef(View v, Element e) {
        if (!listRef.containsKey(v)) {
            listRef.put(v, e);
            return true;
        }

        if (!listRefInverse.containsKey(e)) {
            listRefInverse.put(e, v);
            return true;
        }

        return false;
    }

    public void addTreeRef(TreeElement tE) {
        listTreeElement.add(tE);
    }

    public void clear() {
        listRef.clear();
        listRefInverse.clear();
        listTreeElement.clear();
    }

    public boolean containsView(View v) {
        return listRef.containsKey(v);
    }

    public boolean containsElement(Element e) {
        return listRef.containsValue(e);
    }

    public Element getElement(View v) {
        return listRef.get(v);
    }

    public View getView(Element e) {
        return listRefInverse.get(e);
    }

    public LinkedHashMap<View, Element> getListRef() {
        return this.listRef;
    }

    public LinkedHashMap<Element, View> getListRefInverse() {
        return this.listRefInverse;
    }

    public ArrayList<TreeElement> getListTreeElement() {
        return this.listTreeElement;
    }

    public TreeElement getTreeElement(Element element) {
        for (var te : listTreeElement) {
            if (te.getCurrentElement() == element) return te;
        }
        return null;
    }

    public Element getFirstChild(Element e) {
        for (TreeElement te : listTreeElement) {
            if (e == te.currentElement) {
                for (var pair : te.getChildren()) {
                    return pair;
                }
            }
        }
        return null;
    }

    public Element getParent(Element e) {
        for (var te : listTreeElement) {
            if (e == te.currentElement) {
                if (te.isRoot()) return null;
                return te.getParent();
            }
        }
        return null;
    }

    public Element getPreviousSibling(Element e) {
        return getSubling(e, PREVIOUS_SIBLING);
    }

    public Element getNextSibling(Element e) {
        return getSubling(e, NEXT_SIBLING);
    }

    private Element getSubling(Element e, int type) {
        var pair = getParent(e);
        if (pair == null) return null;
        int i = 0;
        for (var te : listTreeElement) {
            if (pair == te.currentElement) {
                for (var child : te.getChildren()) {
                    if (e == child) {
                        if (type == PREVIOUS_SIBLING) i--;
                        else if (type == NEXT_SIBLING) i++;
                        if (i >= 0 && i < te.getChildren().size()) return te.getChildren().get(i);
                    }
                    i++;
                }
            }
        }
        return null;
    }

    public static class TreeElement {
        private boolean root = false;
        private Element eParent;
        private Element currentElement;
        private ArrayList<Element> children = new ArrayList<>();

        public TreeElement(Element e, boolean root) {
            this.currentElement = e;
            this.root = root;
        }

        public void setParent(Element eP) {
            this.eParent = eP;
        }

        public void addChild(Element e) {
            this.children.add(e);
        }

        public boolean isRoot() {
            return this.root;
        }

        public Element getParent() {
            return eParent;
        }

        public Element getCurrentElement() {
            return this.currentElement;
        }

        public ArrayList<Element> getChildren() {
            return this.children;
        }
    }
}
