package jkas.codeUtil;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.content.Context;

/**
 * @auther JKas
 */
public class XmlManager {
    private Context C;
    private String pathTMP, path;
    public File file;

    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document document;
    public boolean isInitialized = false;
    public String debug = "", code;

    public XmlManager(Context c) {
        C = c;
        isInitialized = false;
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
    }

    public String getPath() {
        return path;
    }

    public boolean initializeFromPath(@NonNull String path) {
        this.path = path;
        isInitialized = false;
        pathTMP =
                C.getFilesDir()
                        + File.separator
                        + "home"
                        + File.separator
                        + ".androidpe"
                        + File.separator
                        + ".tmp"
                        + File.separator
                        + "xml"
                        + File.separator
                        + Files.getNameFromAbsolutePath(this.path);

        String chaine = Files.readFile(this.path);
        Files.writeFile(pathTMP, chaine);
        file = new File(pathTMP);

        try {
            db = dbf.newDocumentBuilder();
            document = db.parse(file);
            document.getDocumentElement().normalize();
            isInitialized = true;
        } catch (Exception e) {
            isInitialized = false;
            debug = e.getMessage();
        }

        if (!isInitialized) {
            chaine =
                    chaine.replace("< ", "<")
                            .replace(" >", ">")
                            .replace(": ", ":")
                            .replace(" :", ":")
                            .replace("= ", "=")
                            .replace(" =", "=");
            Files.writeFile(pathTMP, chaine);
            file = new File(pathTMP);
            try {
                document = db.parse(file);
                document.getDocumentElement().normalize();
                isInitialized = true;
            } catch (Exception e) {
                isInitialized = false;
                debug = e.getMessage();
            }
        }

        code = chaine;
        if (isInitialized) debug = "";
        return isInitialized;
    }

    public boolean saveAllModif() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);
            Files.writeFile(path, result.getWriter().toString());
            code = Files.readFile(path);
            return true;
        } catch (TransformerException e) {
            debug = e.getMessage();
            e.printStackTrace();
        }
        return false;
    }

    public Element getFirstElement() {
        Element element = null;
        Node node = document.getFirstChild();
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            do {
                node = node.getNextSibling();
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    break;
                }
            } while (node.getNextSibling() != null);
        } else element = (Element) node;
        return element;
    }

    public Document getDocument() {
        return document;
    }

    public void removeNode(Node n) {
        n.getParentNode().removeChild(n);
    }

    public void addChild(Node nodeParent, Node nodeChild) {
        nodeParent.appendChild(nodeChild);
    }

    public void setAttribute(Node n, String attr, String value) {
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) n;
            e.setAttribute(attr, value);
        }
    }

    public void setAttribute(Element e, String attr, String value) {
        e.setAttribute(attr, value);
    }

    public void removeAttribute(Node n, String attr) {
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) n;
            e.removeAttribute(attr);
        }
    }

    public void removeAttribute(Element e, String attr) {
        e.removeAttribute(attr);
    }

    public Element getElement(int nodeIndex) {
        ArrayList<Element> e = new ArrayList<>();
        NodeList nl = document.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                e.add((Element) nl.item(i));
            }
        }
        if (e.size() == 0) return null;
        return e.get(nodeIndex);
    }

    public Element getElement(String nodeName, int nodeIndex) {
        ArrayList<Element> e = new ArrayList<>();
        NodeList nl = document.getElementsByTagName(nodeName);
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                e.add((Element) nl.item(i));
            }
        }
        if (e.size() == 0) return null;
        return e.get(nodeIndex);
    }

    public Node getNode(int nodeIndex) {
        Node n = document.getChildNodes().item(nodeIndex);
        return n;
    }

    public Node getNode(String nodeName, int nodeIndex) {
        Node n = document.getElementsByTagName(nodeName).item(nodeIndex);
        return n;
    }

    public int countNodesByTagName(String nodeName) {
        return document.getElementsByTagName(nodeName).getLength();
    }

    public int countAllNodes() {
        return document.getChildNodes().getLength();
    }

    public ArrayList<Node> getAllNodes() {
        ArrayList<Node> list = new ArrayList<>();
        NodeList nl = document.getFirstChild().getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            list.add(nl.item(i));
        }
        return list;
    }

    public ArrayList<Element> getAllElements() {
        ArrayList<Element> list = new ArrayList<>();
        NodeList nl = document.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                list.add((Element) nl.item(i));
            }
        }
        return list;
    }

    public ArrayList<String> getTextContentOfNodesNamed(String nodeName) {
        ArrayList<String> list = new ArrayList<>();
        NodeList nl = document.getElementsByTagName(nodeName);
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                list.add(e.getTextContent());
            }
        }
        return list;
    }

    public ArrayList<String> getAttributeValueOfNodesNamed(String nodeName, String attr) {
        ArrayList<String> list = new ArrayList<>();
        NodeList nl = document.getElementsByTagName(nodeName);
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                list.add(e.getAttribute(attr));
            }
        }
        return list;
    }

    public ArrayList<Node> getNodesByTagName(String nodeName) {
        ArrayList<Node> list = new ArrayList<>();
        list.clear();
        NodeList nl = document.getElementsByTagName(nodeName);
        for (int i = 0; i < nl.getLength(); i++) {
            list.add(nl.item(i));
        }
        return list;
    }

    public ArrayList<Element> getElementsByTagName(String elementName) {
        final ArrayList<Element> list = new ArrayList<>();
        NodeList nl = document.getElementsByTagName(elementName);
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                list.add((Element) nl.item(i));
            }
        }
        return list;
    }

    public boolean containsElementNamed(String elementName) {
        return getElementsByTagName(elementName).size() != 0;
    }

    public boolean containsNodeNamed(String nodeName) {
        return getNodesByTagName(nodeName).size() != 0;
    }

    // STATIC METHODS
    public static ArrayList<Pair<String, String>> getAllAttrNValuesFromElement(Element element) {
        final ArrayList<Pair<String, String>> list = new ArrayList<>();
        NamedNodeMap nnmList = element.getAttributes();
        for (int i = 0; i < nnmList.getLength(); i++) {
            Node n = nnmList.item(i);
            list.add(new Pair<>(n.getNodeName(), n.getNodeValue()));
        }
        return list;
    }

    public static ArrayList<Element> getAllFirstChildFromElement(Element element) {
        final ArrayList<Element> list = new ArrayList<>();
        Node node = element.getFirstChild();
        do {
            if (node == null) return list;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                list.add(child);
            }
            node = node.getNextSibling();
        } while (true);
    }

    public static Element getNextElement(Node element) {
        Node n = element;
        while ((n = n.getNextSibling()) != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) n;
            }
        }
        return null;
    }

    public static Element getPreviousElement(Node element) {
        Node n = element;
        while ((n = n.getPreviousSibling()) != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) n;
            }
        }
        return null;
    }

    public static ArrayList<Element> getAllChildFromElement(Element element) {
        ArrayList<Element> list = new ArrayList<>();
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) list.add((Element) n);
        }
        return list;
    }
}
