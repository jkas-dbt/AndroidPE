package jkas.androidpe.resourcesUtils.attrs.layout.widgets;

import java.util.Map;
import java.util.HashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.*;

/**
 * @author JKas
 */
public class AttrTextView {
    public static Map<String, String[]> attrs = new HashMap<>();

    static {
        attrs.clear();
        init();
    }

    private static void init() {
        attrs.put("android:ems", null);
        attrs.put("android:minEms", null);
        attrs.put("android:enabled", VALUES_BOOLEAN);
        attrs.put("android:gravity", VALUES_GRAVITY);
        attrs.put("android:width", new String[] {"@dimen"});
        attrs.put("android:height", new String[] {"@dimen"});
        attrs.put("android:maxLength", null);
        attrs.put("android:maxLines", null);
        attrs.put("android:minLines", null);
        attrs.put("android:hint", new String[] {"@string"});
        attrs.put(
                "android:inputType",
                new String[] {
                    "none",
                    "text",
                    "textCapCharacters",
                    "textCapWords",
                    "textCapSentences",
                    "textAutoCorrect",
                    "textAutoComplete",
                    "textMultiLine",
                    "textImeMultiLine",
                    "textNoSuggestions",
                    "textUri",
                    "textEmailAddress",
                    "textEmailSubject",
                    "textShortMessage",
                    "textLongMessage",
                    "textPersonName",
                    "textPostalAddress",
                    "textPassword",
                    "textVisiblePassword",
                    "textWebEditText",
                    "textFilter",
                    "textPhonetic",
                    "textWebEmailAddress",
                    "textWebPassword",
                    "number",
                    "numberSigned",
                    "numberDecimal",
                    "numberPassword",
                    "phone",
                    "datetime",
                    "date",
                    "time"
                });
        attrs.put("android:lines", null);
        attrs.put("android:maxEms", null);
        attrs.put("android:maxHeight", new String[] {"@dimen"});
        attrs.put("android:maxWidth", new String[] {"@dimen"});
        attrs.put("android:minHeight", null);
        attrs.put("android:minWidth", null);
        attrs.put("android:shadowColor", new String[] {"@color"});
        attrs.put("android:shadowDx", null);
        attrs.put("android:shadowDy", null);
        attrs.put("android:shadowRadius", null);
        attrs.put("android:singleLine", VALUES_BOOLEAN);
        attrs.put("android:text", VALUES_STRING);
        attrs.put("android:textAppearance", new String[] {"@style"});
        attrs.put("android:textColor", VALUES_COLOR);
        attrs.put("android:textColorHighlight", new String[] {"@color"});
        attrs.put("android:textColorHint", new String[] {"@color"});
        attrs.put("android:textColorLink", new String[] {"@color"});
        attrs.put("android:textScaleX", null);
        attrs.put("android:textSize", new String[] {"@dimen"});
        attrs.put("android:textStyle", new String[] {"bold", "italic", "normal"});
        attrs.put("android:typeface", new String[] {"monospace", "normal", "sans", "serif"});
    }
}
