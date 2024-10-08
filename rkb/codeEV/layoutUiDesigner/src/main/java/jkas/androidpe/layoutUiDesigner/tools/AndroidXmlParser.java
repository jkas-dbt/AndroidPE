package jkas.androidpe.layoutUiDesigner.tools;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.core.util.Pair;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.dialog.DialogBottomSheetAttrSetter;
import jkas.androidpe.layoutUiDesigner.palette.MainView;
import jkas.androidpe.layoutUiDesigner.treeView.TreeNodeView;
import jkas.androidpe.layoutUiDesigner.utils.Utils;
import jkas.androidpe.logger.LoggerLayoutUI;
import jkas.androidpe.projectUtils.current.Environment;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * The construction method of all views will be based on the XmlManager class from the CodeUtil
 * package that said, the creation will not be like in many projects but it will be based on a
 * recursion which will load the views from the sub-packages of the palettes package of the module.
 *
 * <p>@IMPORTANT : The XmlPullParser class will only be responsibl00e for detecting possible errors.
 * It is the XmlManager class which will be used to load the views.
 */

/**
 * @author JKas
 */
public class AndroidXmlParser {
    private LinearLayout linTreeView;
    private OnTaskListener listener;
    private Context C;
    private MainView mvRoot;
    private XmlPullParser parser;
    private LoggerLayoutUI debug;
    private XmlManager xmlFile;
    private String tmpPath;
    private AttributeSetter attrSetter;
    private RefViewElement refViewElement;
    private ArrayList<Pair<View, Element>> attrViewNeedToBeReloaded = new ArrayList<>();
    private DialogBottomSheetAttrSetter dialogAttrSetter;

    public boolean dontTryToLoadView = false;

    public AndroidXmlParser(Context c, DialogBottomSheetAttrSetter dialog) {
        this.C = c;
        this.xmlFile = new XmlManager(c);
        this.refViewElement = new RefViewElement();
        this.attrSetter = new AttributeSetter(C);
        this.linTreeView = new LinearLayout(C);
        this.linTreeView.setOrientation(LinearLayout.VERTICAL);
        this.dialogAttrSetter = dialog;
        init();
    }

    public void with(MainView root) {
        this.mvRoot = root;
        debug = LoggerLayoutUI.get(listener.tagNeeded());
    }

    public void parseXmlCode(String code) {
        try {
            tmpPath =
                    Environment.DEFAULT_ANDROIDPE_TMP_DATA
                            + "/LayoutEditor/"
                            + listener.tagNeeded()
                            + "/xmlFile.xml";
            Files.writeFile(tmpPath, code);
            xmlFile.initializeFromPath(tmpPath);
            Reader reader = (Reader) new StringReader(code);
            parser.setInput(reader);
            parse();
            reader.close();
        } catch (Exception e) {
            debug.e("LayoutParser", "Parsing xml code failed：" + e);
        }
    }

    public void setAttr(View v, Element e) {
        attrSetter.set(v, e);
        Utils.drawDashPathStroke(v);
    }

    private void parse() {
        attrViewNeedToBeReloaded.clear();
        mvRoot.removeAllViews();
        linTreeView.removeAllViews();
        refViewElement.clear();

        listener.onStart();
        debug.i("Parser", "Analysis of XML code.");

        searchForError();
        parseView();

        for (var pair : attrViewNeedToBeReloaded) attrSetter.set(pair.first, pair.second);
        for (var pair : attrViewNeedToBeReloaded) Utils.drawDashPathStroke(pair.first);

        updateTreeView();
        listener.onFinish();
    }

    public void updateTreeView() {
        linTreeView.removeAllViews();
        if (mvRoot.getChildCount() == 0) return;

        try {
            if (refViewElement.getListTreeElement().size() == 0) return;
            var teRoot = refViewElement.getListTreeElement().get(0);
            Element eRoot = teRoot.getCurrentElement();

            TreeNodeView tnvRoot = new TreeNodeView(C, eRoot, true);
            tnvRoot.binding.linView.setOnClickListener(
                    lin -> {
                        if (tnvRoot.element.getNodeName().equals("include"))
                            Toast.makeText(C, "Not yet supported ", Toast.LENGTH_SHORT).show();
                        else dialogAttrSetter.show(tnvRoot.element);
                    });
            linTreeView.addView((View) tnvRoot.binding.getRoot());
            loadTreeView(teRoot, tnvRoot);
        } catch (Exception err) {
            debug.e("updateTreeView", err.getMessage());
        }
    }

    private void loadTreeView(
            final RefViewElement.TreeElement teParent, final TreeNodeView tnvParent) {
        for (var eChild : teParent.getChildren()) {
            boolean isChildParent = false;

            final RefViewElement.TreeElement teChild = refViewElement.getTreeElement(eChild);
            if (teChild.getChildren().size() > 0) isChildParent = true;
            else isChildParent = Utils.isParentView(C, eChild.getNodeName());

            final TreeNodeView tnvChild = new TreeNodeView(C, eChild, isChildParent);
            tnvChild.binding.linView.setOnClickListener(
                    lin -> {
                        if (tnvChild.element.getNodeName().equals("include"))
                            Toast.makeText(C, "Not yet supported ", Toast.LENGTH_SHORT).show();
                        else dialogAttrSetter.show(tnvChild.element);
                    });
            tnvParent.binding.linChild.addView((View) tnvChild.binding.getRoot());
            if (isChildParent) loadTreeView(teChild, tnvChild);
        }
    }

    private void parseView() {
        if (dontTryToLoadView) {
            debug.e("Xml Code", "Cannot display Views, please fix the problem detected.");
            return;
        }
        try {
            final Element root = xmlFile.getFirstElement();
            if (root == null)
                throw new Exception("No elements were found in the code. please check that.");
            int attr = -1;
            if (!root.getNodeName().contains("Switch")) {
                String style = root.getAttribute("style");
                if (style == null || style.length() == 0)
                    style = root.getAttribute("android:style");
                if (style != null && style.length() != 0) {
                    if (style.startsWith("@style/") || style.startsWith("@android:style/"))
                        attr = ResourcesValuesFixer.getStyle(style);
                    else attr = ResourcesValuesFixer.getAttr(style);
                }
            }
            boolean isParent = (root.getFirstChild() != null);
            final View view;
            if (attr != -1)
                view =
                        ViewCreator.create(
                                listener.tagNeeded(), C, root.getNodeName(), attr, isParent);
            else view = ViewCreator.create(listener.tagNeeded(), C, root.getNodeName(), isParent);
            final RefViewElement.TreeElement tRoot = new RefViewElement.TreeElement(root, true);
            tRoot.setParent(null);
            refViewElement.addTreeRef(tRoot);
            refViewElement.addRef(view, root);
            mvRoot.addView(view);
            listener.onViewAdded(view, true);
            attrViewNeedToBeReloaded.add(new Pair<>(view, root));
            if (view instanceof ViewGroup) appendView((ViewGroup) view, root, tRoot);
        } catch (Exception e) {
            debug.e(
                    "androidXmlParser",
                    "No views were found declared. Please check your code.",
                    e.getMessage());
        }
    }

    private void appendView(
            final ViewGroup viewParent,
            final Element elementRoot,
            final RefViewElement.TreeElement teParent) {
        for (final Element eChild : XmlManager.getAllFirstChildFromElement(elementRoot)) {
            try {
                int attr = -1;
                if (!eChild.getNodeName().contains("Switch")) {
                    String style = eChild.getAttribute("style");
                    if (style == null || style.length() == 0)
                        style = eChild.getAttribute("android:style");
                    if (style != null && style.length() != 0) {
                        if (style.startsWith("@style/") || style.startsWith("@android:style/"))
                            attr = ResourcesValuesFixer.getStyle(style);
                        else attr = ResourcesValuesFixer.getAttr(style);
                    }
                }
                boolean isParent = XmlManager.getAllFirstChildFromElement(eChild).size() > 0;
                final View vChild;
                if (attr != -1) {
                    vChild =
                            ViewCreator.create(
                                    listener.tagNeeded(), C, eChild.getNodeName(), attr, isParent);
                } else {
                    vChild =
                            ViewCreator.create(
                                    listener.tagNeeded(), C, eChild.getNodeName(), isParent);
                }
                final RefViewElement.TreeElement tChild =
                        new RefViewElement.TreeElement(eChild, false);
                tChild.setParent(elementRoot);
                teParent.addChild(eChild);
                refViewElement.addTreeRef(tChild);
                refViewElement.addRef(vChild, eChild);
                viewParent.addView(vChild);
                listener.onViewAdded(vChild, false);
                attrViewNeedToBeReloaded.add(new Pair<>(vChild, eChild));
                if (vChild instanceof ViewGroup) appendView((ViewGroup) vChild, eChild, tChild);
            } catch (Exception e) {
                debug.e("AndroidXmlParser", e.getMessage() + " :\n" + eChild.getNodeName());
            }
        }
    }

    private void searchForError() {
        dontTryToLoadView = false;
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        if (!parser.getText().trim().isEmpty()) {
                            debug.e(
                                    " Row "
                                            + parser.getLineNumber()
                                            + ", Column "
                                            + parser.getColumnNumber()
                                            + " : The text should not appear inside the label = "
                                            + parser.getText());
                            dontTryToLoadView = true;
                            return;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            debug.e(
                    "XML code analyzer",
                    " Row "
                            + parser.getLineNumber()
                            + ", Column "
                            + +parser.getColumnNumber()
                            + " : An error occurred while parsing "
                            + e);
            listener.onFinish();
        }
    }

    private void init() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            attrSetter.setOnDataRefNeeded(
                    new AttributeSetter.OnDataRefNeeded() {
                        @Override
                        public RefViewElement onRefViewElementNeeded() {
                            return refViewElement;
                        }

                        @Override
                        public java.lang.String tagNeeded() {
                            return listener.tagNeeded();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XmlManager getXmlManager() {
        return this.xmlFile;
    }

    public RefViewElement getRefViewElement() {
        return this.refViewElement;
    }

    public View getTreeView() {
        return linTreeView;
    }

    public void setTaskListener(OnTaskListener listener) {
        this.listener = listener;
    }

    public interface OnTaskListener {
        public String tagNeeded();

        public void onStart();

        public void onFinish();

        public void onViewAdded(View v, boolean root);
    }
}
