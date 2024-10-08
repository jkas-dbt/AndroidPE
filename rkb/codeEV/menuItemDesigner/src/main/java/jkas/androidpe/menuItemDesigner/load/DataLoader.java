package jkas.androidpe.menuItemDesigner.load;

import android.content.Context;
import android.view.View;
import jkas.androidpe.menuItemDesigner.data.NodeViewRoot;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class DataLoader {
    private Context C;
    private XmlManager xml;

    public DataLoader(Context c, XmlManager xml) {
        this.C = c;
        this.xml = xml;
    }

    public View getRootView() {
        Element e = xml.getFirstElement();
        if (e == null) return new View(C);
        NodeViewRoot nvr = new NodeViewRoot(C, e);
        return nvr.getView();
    }
}
