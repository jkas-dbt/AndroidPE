package jkas.androidpe.resourcesUtils.utils;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParser;
import java.io.StringReader;

/**
 * @author JKas
 */
public class ResFormatter {
    private static int tabSize = 0;
    public static Exception err;

    public static synchronized String formatXmlCode(String xmlCode) {
        err = null;
        tabSize = 0;
        String CODE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlCode));

            int eventType = parser.getEventType();
            boolean newTagEntered = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        newTagEntered = true;
                        String tagName = parser.getName();
                        CODE += "\n" + getTab() + "<" + tagName;
                        tabSize++;
                        boolean multiAttr = parser.getAttributeCount() > 1;
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attrName = parser.getAttributeName(i);
                            String attrValue = parser.getAttributeValue(i);
                            attrValue = attrValue.replace("<", "&lt;");
                            attrValue = attrValue.replace(">", "&gt;");
                            attrValue = attrValue.replace("&", "&amp;");
                            attrValue = attrValue.replace("&amp;lt;", "&lt;");
                            attrValue = attrValue.replace("&amp;gt;", "&gt;");
                            attrValue = attrValue.replace("\"", "\\\"");
                            attrValue = attrValue.replace("\'", "\\\'");
                            attrValue = attrValue.replace("\\\\", "\\");
                            if (multiAttr) {
                                CODE += "\n" + getTab() + attrName + "=\"" + attrValue + "\"";
                            } else {
                                CODE += " " + attrName + "=\"" + attrValue + "\"";
                            }
                        }
                        CODE += ">\n";
                        break;

                    case XmlPullParser.END_TAG:
                        tabSize--;
                        if (newTagEntered) {
                            newTagEntered = false;
                            if (CODE.length() > 3) {
                                CODE = CODE.substring(0, CODE.length() - 2);
                                CODE += "/>\n";
                                break;
                            }
                        }
                        String endTagName = parser.getName();
                        CODE += "\n" + getTab() + "</" + endTagName + ">\n";
                        newTagEntered = false;
                        break;

                    case XmlPullParser.TEXT:
                        if (!parser.getText().replace("\n", "").trim().isEmpty()) return xmlCode;
                }
                eventType = parser.next();
            }
            return CODE;
        } catch (Exception e) {
            // code contains error. it will not be formatted
            err = e;
        }
        return xmlCode;
    }

    private static String getTab() {
        String tab = "";
        if (tabSize > 0) {
            for (int i = 1; i <= tabSize; i++) {
                tab += "\t";
            }
        }
        return tab;
    }
}
