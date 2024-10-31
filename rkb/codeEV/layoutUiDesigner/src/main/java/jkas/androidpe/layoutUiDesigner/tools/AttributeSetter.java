package jkas.androidpe.layoutUiDesigner.tools;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import androidx.core.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButtonToggleGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.HashMap;
import android.view.Gravity;
import jkas.androidpe.layoutUiDesigner.utils.Utils;
import jkas.androidpe.logger.LoggerLayoutUI;
import jkas.androidpe.resourcesUtils.dataBuilder.MenuItemCreator;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author JKas
 */
public class AttributeSetter {
    private static String TAG = "AttributeSetter";
    private OnDataRefNeeded listener;
    private Context C;
    private LoggerLayoutUI debug;
    private Element element;
    private View view;

    private static Map<String, Map<String, String>> enumMap = new HashMap<>();
    private static Map<String, Map<String, Integer>> flagMap = new HashMap<>();
    private static Map<String, Map<String, Object>> otherTypeValueMap = new HashMap<>();

    static {
        Map<String, String> orientationMap = new HashMap<>();
        enumMap.put("orientation", orientationMap);
        orientationMap.put("vertical", "1");
        orientationMap.put("horizontal", "0");

        Map<String, String> layoutMap = new HashMap<>();
        enumMap.put("layout_width", layoutMap);
        enumMap.put("layout_height", layoutMap);
        layoutMap.put("fill_parent", String.valueOf(ViewGroup.LayoutParams.MATCH_PARENT));
        layoutMap.put("match_parent", String.valueOf(ViewGroup.LayoutParams.MATCH_PARENT));
        layoutMap.put("wrap_content", String.valueOf(ViewGroup.LayoutParams.WRAP_CONTENT));

        Map<String, String> importantForAccessibilityMap = new HashMap<>();
        enumMap.put("importantForAccessibility", importantForAccessibilityMap);
        importantForAccessibilityMap.put(
                "auto", String.valueOf(View.IMPORTANT_FOR_ACCESSIBILITY_AUTO));
        importantForAccessibilityMap.put(
                "yes", String.valueOf(View.IMPORTANT_FOR_ACCESSIBILITY_YES));
        importantForAccessibilityMap.put("no", String.valueOf(View.IMPORTANT_FOR_ACCESSIBILITY_NO));
        importantForAccessibilityMap.put(
                "noHideDescendants",
                String.valueOf(View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS));

        Map<String, String> verticalScrollbarPositionMap = new HashMap<>();
        enumMap.put("verticalScrollbarPosition", verticalScrollbarPositionMap);
        verticalScrollbarPositionMap.put(
                "defaultPosition", String.valueOf(View.SCROLLBAR_POSITION_DEFAULT));
        verticalScrollbarPositionMap.put("left", String.valueOf(View.SCROLLBAR_POSITION_LEFT));
        verticalScrollbarPositionMap.put("right", String.valueOf(View.SCROLLBAR_POSITION_RIGHT));

        Map<String, String> typefaceMap = new HashMap<>();
        enumMap.put("typeface", typefaceMap);
        typefaceMap.put("sans", "SANS_SERIF");
        typefaceMap.put("serif", "SERIF");
        typefaceMap.put("monospace", "MONOSPACE");
        typefaceMap.put("normal", "DEFAULT");

        Map<String, String> ellipsizeMap = new HashMap<>();
        enumMap.put("ellipsize", ellipsizeMap);
        ellipsizeMap.put("none", "END_SMALL");
        ellipsizeMap.put("start", "START");
        ellipsizeMap.put("middle", "MIDDLE");
        ellipsizeMap.put("end", "END");
        ellipsizeMap.put("marquee", "MARQUEE");

        Map<String, String> textDirectionMap = new HashMap<>();
        enumMap.put("textDirection", textDirectionMap);
        textDirectionMap.put("inherit", String.valueOf(View.TEXT_DIRECTION_INHERIT));
        textDirectionMap.put("firstStrong", String.valueOf(View.TEXT_DIRECTION_FIRST_STRONG));
        textDirectionMap.put("anyRtl", String.valueOf(View.TEXT_DIRECTION_ANY_RTL));
        textDirectionMap.put("ltr", String.valueOf(View.TEXT_DIRECTION_LTR));
        textDirectionMap.put("rtl", String.valueOf(View.TEXT_DIRECTION_RTL));
        textDirectionMap.put("locale", String.valueOf(View.TEXT_DIRECTION_LOCALE));
        textDirectionMap.put(
                "firstStrongLtr", String.valueOf(View.TEXT_DIRECTION_FIRST_STRONG_LTR));
        textDirectionMap.put(
                "firstStrongRtl", String.valueOf(View.TEXT_DIRECTION_FIRST_STRONG_RTL));

        Map<String, String> alignmentModeMap = new HashMap<>();
        enumMap.put("alignmentMode", alignmentModeMap);
        alignmentModeMap.put("alignBounds", "0");
        alignmentModeMap.put("alignMargins", "1");

        Map<String, String> scaleTypeMap = new HashMap<>();
        enumMap.put("scaleType", scaleTypeMap);
        scaleTypeMap.put("matrix", "MATRIX");
        scaleTypeMap.put("fitXY", "FIT_XY");
        scaleTypeMap.put("fitStart", "FIT_START");
        scaleTypeMap.put("fitCenter", "FIT_CENTER");
        scaleTypeMap.put("fitEnd", "FIT_END");
        scaleTypeMap.put("center", "CENTER");
        scaleTypeMap.put("centerCrop", "CENTER_CROP");
        scaleTypeMap.put("centerInside", "CENTER_INSIDE");

        Map<String, String> scrollbarStyleMap = new HashMap<>();
        enumMap.put("scrollbarStyle", scrollbarStyleMap);
        scrollbarStyleMap.put("insideOverlay", String.valueOf(View.SCROLLBARS_INSIDE_OVERLAY));
        scrollbarStyleMap.put("insideInset", String.valueOf(View.SCROLLBARS_INSIDE_INSET));
        scrollbarStyleMap.put("outsideOverlay", String.valueOf(View.SCROLLBARS_OUTSIDE_OVERLAY));
        scrollbarStyleMap.put("outsideInset", String.valueOf(View.SCROLLBARS_OUTSIDE_INSET));

        Map<String, String> overScrollModeMap = new HashMap<>();
        enumMap.put("overScrollMode", overScrollModeMap);
        overScrollModeMap.put("always", String.valueOf(View.OVER_SCROLL_ALWAYS));
        overScrollModeMap.put(
                "ifContentScrolls", String.valueOf(View.OVER_SCROLL_IF_CONTENT_SCROLLS));
        overScrollModeMap.put("never", String.valueOf(View.OVER_SCROLL_NEVER));

        Map<String, String> layoutDirectionMap = new HashMap<>();
        enumMap.put("layoutDirection", layoutDirectionMap);
        layoutDirectionMap.put("ltr", String.valueOf(View.LAYOUT_DIRECTION_LTR));
        layoutDirectionMap.put("rtl", String.valueOf(View.LAYOUT_DIRECTION_RTL));
        layoutDirectionMap.put("inherit", String.valueOf(View.LAYOUT_DIRECTION_INHERIT));
        layoutDirectionMap.put("locale", String.valueOf(View.LAYOUT_DIRECTION_LOCALE));

        Map<String, String> layerTypeMap = new HashMap<>();
        enumMap.put("layerType", layerTypeMap);
        layerTypeMap.put("none", String.valueOf(View.LAYER_TYPE_NONE));
        layerTypeMap.put("software", String.valueOf(View.LAYER_TYPE_SOFTWARE));
        layerTypeMap.put("hardware", String.valueOf(View.LAYER_TYPE_HARDWARE));

        Map<String, String> drawingCacheQualityMap = new HashMap<>();
        enumMap.put("drawingCacheQuality", drawingCacheQualityMap);
        drawingCacheQualityMap.put("auto", String.valueOf(View.DRAWING_CACHE_QUALITY_AUTO));
        drawingCacheQualityMap.put("low", String.valueOf(View.DRAWING_CACHE_QUALITY_LOW));
        drawingCacheQualityMap.put("high", String.valueOf(View.DRAWING_CACHE_QUALITY_HIGH));

        Map<String, String> visibilityMap = new HashMap<>();
        enumMap.put("visibility", visibilityMap);
        visibilityMap.put("visible", String.valueOf(View.VISIBLE));
        visibilityMap.put("invisible", String.valueOf(View.INVISIBLE));
        visibilityMap.put("gone", String.valueOf(View.GONE));
    }

    static {
        Map<String, Integer> textStyleMap = new HashMap<>();
        flagMap.put("textStyle", textStyleMap);
        textStyleMap.put("normal", 0);
        textStyleMap.put("bold", 1);
        textStyleMap.put("italic", 2);

        Map<String, Integer> inputTypeMap = new HashMap<>();
        flagMap.put("inputType", inputTypeMap);
        inputTypeMap.put("none", 0x00000000);
        inputTypeMap.put("text", 0x00000001);
        inputTypeMap.put("textCapCharacters", 0x00001001);
        inputTypeMap.put("textCapWords", 0x00002001);
        inputTypeMap.put("textCapSentences", 0x00004001);
        inputTypeMap.put("textAutoCorrect", 0x00008001);
        inputTypeMap.put("textAutoComplete", 0x00010001);
        inputTypeMap.put("textMultiLine", 0x00020001);
        inputTypeMap.put("textImeMultiLine", 0x00040001);
        inputTypeMap.put("textNoSuggestions", 0x00080001);
        inputTypeMap.put("textUri", 0x00000011);
        inputTypeMap.put("textEmailAddress", 0x00000021);
        inputTypeMap.put("textEmailSubject", 0x00000031);
        inputTypeMap.put("textShortMessage", 0x00000041);
        inputTypeMap.put("textLongMessage", 0x00000051);
        inputTypeMap.put("textPersonName", 0x00000061);
        inputTypeMap.put("textPostalAddress", 0x00000071);
        inputTypeMap.put("textPassword", 0x00000081);
        inputTypeMap.put("textVisiblePassword", 0x00000091);
        inputTypeMap.put("textWebEditText", 0x000000a1);
        inputTypeMap.put("textFilter", 0x000000b1);
        inputTypeMap.put("textPhonetic", 0x000000c1);
        inputTypeMap.put("textWebEmailAddress", 0x000000d1);
        inputTypeMap.put("textWebPassword", 0x000000e1);
        inputTypeMap.put("number", 0x00000002);
        inputTypeMap.put("numberSigned", 0x00001002);
        inputTypeMap.put("numberDecimal", 0x00002002);
        inputTypeMap.put("numberPassword", 0x00000012);
        inputTypeMap.put("phone", 0x00000003);
        inputTypeMap.put("datetime", 0x00000004);
        inputTypeMap.put("date", 0x00000014);
        inputTypeMap.put("time", 0x00000024);

        Map<String, Integer> scrollbarsMap = new HashMap<>();
        flagMap.put("scrollbars", scrollbarsMap);
        scrollbarsMap.put("none", 0x00000000);
        scrollbarsMap.put("horizontal", 0x00001000);
        scrollbarsMap.put("vertical", 0x00002000);

        Map<String, Integer> fadingEdgeMap = new HashMap<>();
        flagMap.put("fadingEdge", fadingEdgeMap);
        flagMap.put("requiresFadingEdge", fadingEdgeMap);
        fadingEdgeMap.put("none", 0x00000000);
        fadingEdgeMap.put("horizontal", 0x00001000);
        fadingEdgeMap.put("vertical", 0x00002000);

        Map<String, Integer> gravityMap = new HashMap<>();
        flagMap.put("gravity", gravityMap);
        flagMap.put("layout_gravity", gravityMap);
        flagMap.put("foregroundGravity", gravityMap);
        flagMap.put("layout_anchorGravity", gravityMap);
        gravityMap.put("top", Gravity.TOP);
        gravityMap.put("bottom", Gravity.BOTTOM);
        gravityMap.put("left", Gravity.LEFT);
        gravityMap.put("right", Gravity.RIGHT);
        gravityMap.put("center_vertical", Gravity.CENTER_VERTICAL);
        gravityMap.put("fill_vertical", Gravity.FILL_VERTICAL);
        gravityMap.put("center_horizontal", Gravity.CENTER_HORIZONTAL);
        gravityMap.put("fill_horizontal", Gravity.FILL_HORIZONTAL);
        gravityMap.put("center", Gravity.CENTER);
        gravityMap.put("fill", Gravity.FILL);
        gravityMap.put("clip_vertical", Gravity.CLIP_VERTICAL);
        gravityMap.put("clip_horizontal", Gravity.CLIP_HORIZONTAL);
        gravityMap.put("start", Gravity.START);
        gravityMap.put("end", Gravity.END);
    }

    static {
        Map<String, Object> backgroundTM = new HashMap<>();
        otherTypeValueMap.put("backgroundTintMode", backgroundTM);
        backgroundTM.put("add", PorterDuff.Mode.ADD);
        backgroundTM.put("multiply", PorterDuff.Mode.MULTIPLY);
        backgroundTM.put("screen", PorterDuff.Mode.SCREEN);
        backgroundTM.put("src_atop", PorterDuff.Mode.SRC_ATOP);
        backgroundTM.put("src_in", PorterDuff.Mode.SRC_IN);
        backgroundTM.put("src_over", PorterDuff.Mode.SRC_OVER);
        backgroundTM.put("darken", PorterDuff.Mode.DARKEN);
        backgroundTM.put("lighten", PorterDuff.Mode.LIGHTEN);
    }

    public AttributeSetter(Context c) {
        this.C = c;
    }

    private void setLayoutParams() {
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        String widthS = element.getAttribute("android:layout_width");
        String heightS = element.getAttribute("android:layout_height");

        if (widthS.equals("match_parent") || widthS.equals("fill_parent"))
            width = ViewGroup.LayoutParams.MATCH_PARENT;
        else if (widthS.equals("wrap_content")) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT;
            if (element.getNodeName().equals("View")) width = 30;
        } else if (widthS.startsWith("@") | widthS.startsWith("?"))
            width = (int) ResourcesValuesFixer.getDimen(C, widthS);
        else width = (int) CodeUtil.getDimenValue(C, widthS);

        if (heightS.equals("match_parent") || heightS.equals("fill_parent"))
            height = ViewGroup.LayoutParams.MATCH_PARENT;
        else if (heightS.equals("wrap_content")) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
            if (element.getNodeName().equals("View")) width = 30;
        } else if (heightS.startsWith("@") || heightS.startsWith("?"))
            height = (int) ResourcesValuesFixer.getDimen(C, heightS);
        else height = (int) CodeUtil.getDimenValue(C, heightS);

        if (view.getLayoutParams() == null) view.setLayoutParams(CodeUtil.getLayoutParamsWW(0));
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = height;

        try {
            String value = element.getAttribute("android:layout_weight");
            if (!value.trim().equals("")) {
                CodeUtil.setField(view.getLayoutParams(), "weight", Float.valueOf(value));
            }
        } catch (Exception e) {
            if (debug != null)
                debug.e(
                        "AttributeSetter",
                        element.getNodeName()
                                + "\nandroid:layout_weight error : "
                                + e.getMessage());
        }
    }

    private void setPadding() {
        try {
            int paddingValue = 0;
            String value = element.getAttribute("android:padding");
            if (!value.trim().equals("")) {
                paddingValue = (int) ResourcesValuesFixer.getDimen(C, value);
                view.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            } else {
                int left = view.getPaddingLeft(),
                        top = view.getPaddingTop(),
                        right = view.getPaddingRight(),
                        bottom = view.getPaddingBottom();

                boolean parentHorizontal = true;
                try {
                    Element parent = (Element) element.getParentNode();
                    String val = parent.getAttribute("android:orientation");
                    parentHorizontal = val.equals("horizontal");
                } catch (Exception err) {
                    parentHorizontal = false;
                }

                value = element.getAttribute("android:paddingLeft");
                if (!value.trim().equals("")) {
                    left = (int) ResourcesValuesFixer.getDimen(C, value);
                }
                value = element.getAttribute("android:paddingTop");
                if (!value.trim().equals("")) {
                    top = (int) ResourcesValuesFixer.getDimen(C, value);
                }
                value = element.getAttribute("android:paddingRight");
                if (!value.trim().equals("")) {
                    right = (int) ResourcesValuesFixer.getDimen(C, value);
                }
                value = element.getAttribute("android:paddingBottom");
                if (!value.trim().equals("")) {
                    bottom = (int) ResourcesValuesFixer.getDimen(C, value);
                }

                value = element.getAttribute("android:paddingStart");
                if (!value.trim().equals("")) {
                    if (parentHorizontal) left = (int) ResourcesValuesFixer.getDimen(C, value);
                    else top = (int) ResourcesValuesFixer.getDimen(C, value);
                }

                value = element.getAttribute("android:paddingEnd");
                if (!value.trim().equals("")) {
                    if (parentHorizontal) right = (int) ResourcesValuesFixer.getDimen(C, value);
                    else bottom = (int) ResourcesValuesFixer.getDimen(C, value);
                }

                view.setPadding(left, top, right, bottom);

                if (view instanceof ViewGroup) {
                    if (left == 0 && top == 0 && right == 0 && bottom == 0) {
                        if (((ViewGroup) view).getChildCount() == 0) {
                            int dimen = (int) ResourcesValuesFixer.getDimen(C, "8dp");
                            view.setPadding(dimen, dimen, dimen, dimen);
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (debug != null)
                debug.e(
                        TAG,
                        getElementNameAndRow(),
                        "A problem occurred while assigning value related to Paddings.");
        }
    }

    private void setMargings() {
        try {
            int left = 0, top = 0, right = 0, bottom = 0;

            String value = element.getAttribute("android:layout_margin");
            if (!value.trim().equals("")) {
                CodeUtil.invoke(
                        view.getLayoutParams(),
                        "setMargins",
                        new Class[] {int.class, int.class, int.class, int.class},
                        (int) ResourcesValuesFixer.getDimen(C, value),
                        (int) ResourcesValuesFixer.getDimen(C, value),
                        (int) ResourcesValuesFixer.getDimen(C, value),
                        (int) ResourcesValuesFixer.getDimen(C, value));
            } else {
                boolean parentHorizontal = true;
                try {
                    Element parent = (Element) element.getParentNode();
                    String val = parent.getAttribute("android:orientation");
                    parentHorizontal = val.equals("horizontal");
                } catch (Exception err) {
                    parentHorizontal = false;
                }

                value = element.getAttribute("android:layout_marginLeft");
                if (!value.trim().equals("")) {
                    left = (int) ResourcesValuesFixer.getDimen(C, value);
                }

                value = element.getAttribute("android:layout_marginTop");
                if (!value.trim().equals("")) {
                    top = (int) ResourcesValuesFixer.getDimen(C, value);
                }
                value = element.getAttribute("android:layout_marginRight");
                if (!value.trim().equals("")) {
                    right = (int) ResourcesValuesFixer.getDimen(C, value);
                }

                value = element.getAttribute("android:layout_marginBottom");
                if (!value.trim().equals("")) {
                    bottom = (int) ResourcesValuesFixer.getDimen(C, value);
                }

                value = element.getAttribute("android:layout_marginStart");
                if (!value.trim().equals("")) {
                    if (parentHorizontal) left = (int) ResourcesValuesFixer.getDimen(C, value);
                    else top = (int) ResourcesValuesFixer.getDimen(C, value);
                }

                value = element.getAttribute("android:layout_marginEnd");
                if (!value.trim().equals("")) {
                    if (parentHorizontal) right = (int) ResourcesValuesFixer.getDimen(C, value);
                    else bottom = (int) ResourcesValuesFixer.getDimen(C, value);
                }

                CodeUtil.invoke(
                        view.getLayoutParams(),
                        "setMargins",
                        new Class[] {int.class, int.class, int.class, int.class},
                        left,
                        top,
                        right,
                        bottom);
            }
        } catch (Exception e) {
            if (debug != null) debug.e("AttributeSetter", "Margins didn't apply", e.getMessage());
        }
    }

    private void setDefault() {
        CodeUtil.invoke(
                view,
                "setImageResource",
                new Class[] {int.class},
                jkas.androidpe.resources.R.drawable.ic_android);

        if (element.getNodeName().contains("TextView"))
            CodeUtil.invoke(
                    view, "setText", new Class[] {CharSequence.class}, element.getNodeName());
    }

    public void set(View v, Element e) {
        this.element = e;
        this.view = v;
        if (debug == null && listener != null) debug = LoggerLayoutUI.get(listener.tagNeeded());
        setLayoutParams();
        setMargings();
        setPadding();
        setDefault();
        setAttr(view, element);
    }

    private void setAttr(View v, Element e) {
        for (Pair<String, String> pair : XmlManager.getAllAttrNValuesFromElement(element)) {
            String name = pair.first;
            if (name.contains(":")) name = name.split("\\:")[1];
            final String value = pair.second;

            ConstraintLayout constraintLayout = getParentAsConstraintLayout();
            CoordinatorLayout coordinatorLayout = getParentAsCoordinatorLayout();
            int targetedId = -1;

            if (value.equals("parent")) {
                if (constraintLayout != null) targetedId = constraintLayout.getId();
                else if (coordinatorLayout != null) targetedId = coordinatorLayout.getId();
                else targetedId = getParentId();
            } else if (value.startsWith("@id/") || value.startsWith("@+id/"))
                targetedId = getIdValueAsIntFromListRef(value);

            try {
                switch (name) {
                    case "background":
                        if (!value.trim().equals("")) {
                            if (value.startsWith("?") || value.startsWith("@android:attr/")) {
                                view.setBackgroundColor(ResourcesValuesFixer.getColor(C, value));
                            } else if (value.startsWith("@drawable")
                                    || value.startsWith("@android:drawable/")) {
                                view.setBackground(ResourcesValuesFixer.getDrawable(C, value));
                            } else {
                                int color = ResourcesValuesFixer.getColor(C, value);
                                view.setBackgroundColor(color);

                                if (color == Color.TRANSPARENT) {
                                    if (debug != null)
                                        debug.w(
                                                "ValueParser",
                                                element.getNodeName()
                                                        + " Row "
                                                        + element.getUserData("lineNumber"),
                                                "android:backgroud=\"" + value + "\"",
                                                "Value does not exist in color resources");
                                }
                            }
                        }
                        continue;
                    case "tint":
                        CodeUtil.invoke(
                                view,
                                "setBackgroundTintList",
                                new Class[] {ColorStateList.class},
                                ColorStateList.valueOf(ResourcesValuesFixer.getColor(C, value)));
                        continue;
                    case "backgroundTint":
                        CodeUtil.invoke(
                                view,
                                "setBackgroundTintList",
                                new Class[] {ColorStateList.class},
                                ColorStateList.valueOf(ResourcesValuesFixer.getColor(C, value)));
                        continue;
                    case "backgroundTintMode":
                        CodeUtil.invoke(
                                view,
                                "setBackgroundTintMode",
                                new Class[] {PorterDuff.Mode.class},
                                otherTypeValueMap.get(name).get(value));
                        continue;
                    case "elevation":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    (element.getNodeName().endsWith(".MaterialCardView"))
                                            ? "setCardElevation"
                                            : "setElevation",
                                    new Class[] {float.class},
                                    ResourcesValuesFixer.getDimen(C, value));
                        }
                        continue;
                    case "cardElevation":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    "setCardElevation",
                                    new Class[] {float.class},
                                    ResourcesValuesFixer.getDimen(C, value));
                        }
                        continue;
                    case "rotation":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    "setRotation",
                                    new Class[] {float.class},
                                    Float.parseFloat(value));
                        }
                        continue;
                    case "rotationX":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    "setRotationX",
                                    new Class[] {float.class},
                                    ResourcesValuesFixer.getDimen(C, value));
                        }
                        continue;

                    case "rotationY":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    "setRotationY",
                                    new Class[] {float.class},
                                    ResourcesValuesFixer.getDimen(C, value));
                        }
                        continue;
                    case "translationY":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    "setTranslationX",
                                    new Class[] {float.class},
                                    ResourcesValuesFixer.getDimen(C, value));
                        }
                        continue;
                    case "translationX":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    "setTranslationY",
                                    new Class[] {float.class},
                                    ResourcesValuesFixer.getDimen(C, value));
                        }
                        continue;
                    case "translationZ":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    "setTranslationZ",
                                    new Class[] {float.class},
                                    ResourcesValuesFixer.getDimen(C, value));
                        }
                        continue;
                    case "scaleX":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    "setScaleX",
                                    new Class[] {float.class},
                                    ResourcesValuesFixer.getDimen(C, value));
                        }
                        continue;
                    case "scaleZ":
                        if (!value.trim().equals("")) {
                            CodeUtil.invoke(
                                    view,
                                    "setScaleY",
                                    new Class[] {float.class},
                                    ResourcesValuesFixer.getDimen(C, value));
                        }
                        continue;
                    case "visibility":
                        if (!value.trim().equals(""))
                            CodeUtil.invoke(
                                    view,
                                    "setVisibility",
                                    new Class[] {int.class},
                                    Integer.parseInt(enumMap.get("visibility").get(value)));
                        continue;

                        // Start of modifying attributes related to RelativeLayout
                    case "layout_centerInParent":
                        if (ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    v.getLayoutParams(),
                                    "addRule",
                                    new Class[] {int.class},
                                    RelativeLayout.CENTER_IN_PARENT);
                        continue;
                    case "layout_centerVertical":
                        if (ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    v.getLayoutParams(),
                                    "addRule",
                                    new Class[] {int.class},
                                    RelativeLayout.CENTER_VERTICAL);
                        continue;
                    case "layout_centerHorizontal":
                        if (ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    v.getLayoutParams(),
                                    "addRule",
                                    new Class[] {int.class},
                                    RelativeLayout.CENTER_HORIZONTAL);
                        continue;
                    case "layout_alignEnd":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.ALIGN_END,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_alignStart":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.ALIGN_START,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_alignLeft":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.ALIGN_LEFT,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_alignRight":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.ALIGN_RIGHT,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_alignTop":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.ALIGN_TOP,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_alignBottom":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.ALIGN_BOTTOM,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_alignBaseline":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.ALIGN_BASELINE,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_toRightOf":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.RIGHT_OF,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_toLeftOf":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.LEFT_OF,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_toStartOf":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.START_OF,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_toEndOf":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.END_OF,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_above":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.ABOVE,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_below":
                        CodeUtil.invoke(
                                v.getLayoutParams(),
                                "addRule",
                                new Class[] {int.class, int.class},
                                RelativeLayout.BELOW,
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layout_alignParentRight":
                        if (ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    v.getLayoutParams(),
                                    "addRule",
                                    new Class[] {int.class},
                                    RelativeLayout.ALIGN_PARENT_RIGHT);
                        continue;
                    case "layout_alignParentBottom":
                        if (ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    v.getLayoutParams(),
                                    "addRule",
                                    new Class[] {int.class},
                                    RelativeLayout.ALIGN_PARENT_BOTTOM);
                        continue;
                    case "layout_alignParentEnd":
                        if (ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    v.getLayoutParams(),
                                    "addRule",
                                    new Class[] {int.class},
                                    RelativeLayout.ALIGN_PARENT_END);
                        continue;
                    case "layout_alignParentLeft":
                        if (ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    v.getLayoutParams(),
                                    "addRule",
                                    new Class[] {int.class},
                                    RelativeLayout.ALIGN_PARENT_LEFT);
                        continue;
                    case "layout_alignParentStart":
                        if (ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    v.getLayoutParams(),
                                    "addRule",
                                    new Class[] {int.class},
                                    RelativeLayout.ALIGN_PARENT_START);
                        continue;
                    case "layout_alignParentTop":
                        if (ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    v.getLayoutParams(),
                                    "addRule",
                                    new Class[] {int.class},
                                    RelativeLayout.ALIGN_PARENT_TOP);
                        continue;
                        // End of attributes related to RelativeLayout

                        // Start of modifying attributes related to ConstraintLayout

                    case "layout_constraintLeft_toLeftOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintLeft_toLeftOf=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintLeft_toLeftOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.LEFT, targetedId, ConstraintSet.LEFT);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintLeft_toRightOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintLeft_toRightOf=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintLeft_toRightOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.LEFT, targetedId, ConstraintSet.RIGHT);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintRight_toLeftOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintRight_toLeftOf=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintRight_toLeftOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.RIGHT, targetedId, ConstraintSet.LEFT);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintRight_toRightOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintRight_toRightOf=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintRight_toRightOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.RIGHT, targetedId, ConstraintSet.RIGHT);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintTop_toTopOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintTop_toTopOf=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintTop_toTopOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.TOP, targetedId, ConstraintSet.TOP);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintTop_toBottomOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintTop_toBottomOf=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintTop_toBottomOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.TOP, targetedId, ConstraintSet.BOTTOM);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintBottom_toBottomOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintBottom_toBottomOf=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintBottom_toBottomOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(),
                                ConstraintSet.BOTTOM,
                                targetedId,
                                ConstraintSet.BOTTOM);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintBottom_toTopOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintBottom_toTopOf=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintBottom_toTopOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.BOTTOM, targetedId, ConstraintSet.TOP);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintBaseline_toBaselineOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintBaseline_toBaselineOf=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintBaseline_toBaselineOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(),
                                ConstraintSet.BASELINE,
                                targetedId,
                                ConstraintSet.BASELINE);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintStart_toEndOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintStart_toEndOf=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintStart_toEndOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.START, targetedId, ConstraintSet.END);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintStart_toStartOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintStart_toStartOf=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintStart_toStartOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.START, targetedId, ConstraintSet.START);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintEnd_toStartOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintEnd_toStartOf=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintEnd_toStartOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.END, targetedId, ConstraintSet.START);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintEnd_toEndOf":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintEnd_toEndOf=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }

                        if (targetedId == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintEnd_toEndOf=\""
                                                + value
                                                + "\""
                                                + " does not exist.",
                                        "if you think otherwise, then, check if the view with this id is loaded(declared) before this View");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(
                                view.getId(), ConstraintSet.END, targetedId, ConstraintSet.END);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_marginBaseline":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_goneMarginBaseline=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setMargin(
                                view.getId(),
                                ConstraintSet.BASELINE,
                                (int) ResourcesValuesFixer.getDimen(C, value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_goneMarginStart":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_goneMarginStart=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setGoneMargin(
                                view.getId(),
                                ConstraintSet.START,
                                (int) ResourcesValuesFixer.getDimen(C, value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_goneMarginEnd":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_goneMarginEnd=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setGoneMargin(
                                view.getId(),
                                ConstraintSet.END,
                                (int) ResourcesValuesFixer.getDimen(C, value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_goneMarginLeft":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_goneMarginLeft=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setGoneMargin(
                                view.getId(),
                                ConstraintSet.LEFT,
                                (int) ResourcesValuesFixer.getDimen(C, value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_goneMarginTop":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_goneMarginTop=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setGoneMargin(
                                view.getId(),
                                ConstraintSet.TOP,
                                (int) ResourcesValuesFixer.getDimen(C, value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_goneMarginRight":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_goneMarginRight=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setGoneMargin(
                                view.getId(),
                                ConstraintSet.RIGHT,
                                (int) ResourcesValuesFixer.getDimen(C, value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_goneMarginBottom":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_goneMarginBottom=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setGoneMargin(
                                view.getId(),
                                ConstraintSet.BOTTOM,
                                (int) ResourcesValuesFixer.getDimen(C, value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_goneMarginBaseline":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_goneMarginBaseline=\""
                                                + value
                                                + "\"",
                                        "Because it''s parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setGoneMargin(
                                view.getId(),
                                ConstraintSet.BASELINE,
                                (int) ResourcesValuesFixer.getDimen(C, value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintHorizontal_bias":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintHorizontal_bias=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        ConstraintLayout.LayoutParams layoutParams =
                                (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.horizontalBias = Float.parseFloat(value);
                        constraintLayout.updateViewLayout(view, layoutParams);
                        continue;

                    case "layout_constraintVertical_bias":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintVertical_bias=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.verticalBias = Float.parseFloat(value);
                        constraintLayout.updateViewLayout(view, layoutParams);
                        continue;

                    case "layout_constraintCircle":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintCircle=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        int id = getIdValueAsIntFromListRef(value);
                        if (id == -1) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "the targeted id("
                                                + value
                                                + ") in the method app:layout_constraintCircle=\""
                                                + value
                                                + "\""
                                                + " does not exist.");
                            continue;
                        }
                        String valRadius =
                                element.getAttribute("app:layout_constraintCircleRadius");
                        String valAngle = element.getAttribute("app:layout_constraintCircleAngle");
                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.circleConstraint = id;
                        layoutParams.circleRadius =
                                (int) ResourcesValuesFixer.getDimen(C, valRadius);
                        layoutParams.circleAngle = Float.parseFloat(valAngle);
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constrainedWidth":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constrainedWidth=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.constrainedWidth = ResourcesValuesFixer.getBoolean(C, value);
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constrainedHeight":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constrainedHeight=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.constrainedHeight = ResourcesValuesFixer.getBoolean(C, value);
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constraintWidth_min":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintWidth_min=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.matchConstraintMinWidth =
                                (int) ResourcesValuesFixer.getDimen(C, value);
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constraintHeight_min":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintHeight_min=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.matchConstraintMinHeight =
                                (int) ResourcesValuesFixer.getDimen(C, value);
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constraintWidth_max":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintWidth_max=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.matchConstraintMaxWidth =
                                (int) ResourcesValuesFixer.getDimen(C, value);
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constraintHeight_max":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintHeight_max=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.matchConstraintMaxHeight =
                                (int) ResourcesValuesFixer.getDimen(C, value);
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constraintWidth_percent":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintWidth_percent=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.matchConstraintPercentWidth = Float.parseFloat(value);
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constraintHeight_percent":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintHeight_percent=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.matchConstraintPercentHeight = Float.parseFloat(value);
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constraintWidth_default":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintWidth_default=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        int val = -1;
                        if (value.equals("wrap")) val = ConstraintSet.WRAP_CONTENT;
                        else if (value.equals("percent")) val = ConstraintSet.MATCH_CONSTRAINT;
                        else if (value.equals("none")) val = ConstraintSet.WRAP_CONTENT;
                        else continue;

                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.constrainDefaultWidth(view.getId(), val);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintHeight_default":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintHeight_default=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        val = -1;
                        if (value.equals("wrap")) val = ConstraintSet.WRAP_CONTENT;
                        else if (value.equals("percent")) val = ConstraintSet.MATCH_CONSTRAINT;
                        else if (value.equals("none")) val = ConstraintSet.WRAP_CONTENT;
                        else continue;

                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.constrainDefaultHeight(view.getId(), val);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintDimensionRatio":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintDimensionRatio=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }

                        layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.dimensionRatio = value;
                        view.setLayoutParams(layoutParams);
                        continue;

                    case "layout_constraintHorizontal_chainStyle":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintHorizontal_chainStyle=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        val = -1;
                        if (value.equals("spread")) val = ConstraintSet.CHAIN_SPREAD;
                        else if (value.equals("packed")) val = ConstraintSet.CHAIN_PACKED;
                        else if (value.equals("spread_inside"))
                            val = ConstraintSet.CHAIN_SPREAD_INSIDE;
                        else continue;
                        if (!(view instanceof ViewGroup)) continue;
                        var treeE = listener.onRefViewElementNeeded().getTreeElement(element);
                        if (treeE == null) continue;
                        for (var eChild : treeE.getChildren()) {
                            String startOrLeft =
                                    eChild.getAttribute("app:layout_constraintStart_toStartOf")
                                            .trim();
                            if (startOrLeft.isEmpty())
                                startOrLeft =
                                        eChild.getAttribute("app:layout_constraintLeft_toLeftOf")
                                                .trim();
                            if (!startOrLeft.equals("parent")) continue;
                            final var tmpView = listener.onRefViewElementNeeded().getView(eChild);
                            if (tmpView == null) continue;
                            constraintSet.setHorizontalChainStyle(tmpView.getId(), val);
                        }
                        continue;

                    case "layout_constraintVertical_chainStyle":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintVertical_chainStyle=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        val = -1;
                        if (value.equals("spread")) val = ConstraintSet.CHAIN_SPREAD;
                        else if (value.equals("packed")) val = ConstraintSet.CHAIN_PACKED;
                        else if (value.equals("spread_inside"))
                            val = ConstraintSet.CHAIN_SPREAD_INSIDE;
                        else continue;
                        if (!(view instanceof ViewGroup)) continue;
                        treeE = listener.onRefViewElementNeeded().getTreeElement(element);
                        if (treeE == null) continue;
                        for (var eChild : treeE.getChildren()) {
                            String top =
                                    eChild.getAttribute("app:layout_constraintTop_toTopOf").trim();
                            if (!top.equals("parent")) continue;
                            final var tmpView = listener.onRefViewElementNeeded().getView(eChild);
                            if (tmpView == null) continue;
                            constraintSet.setHorizontalChainStyle(tmpView.getId(), val);
                        }
                        continue;

                    case "layout_constraintHorizontal_weight":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintHorizontal_weight=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setHorizontalWeight(view.getId(), Float.parseFloat(value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_constraintVertical_weight":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_constraintVertical_weight=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.setVerticalWeight(view.getId(), Float.parseFloat(value));
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_restrictedWidth":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_restrictedWidth=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.constrainWidth(
                                view.getId(), ConstraintSet.MATCH_CONSTRAINT_WRAP);
                        constraintSet.connect(
                                view.getId(),
                                ConstraintSet.LEFT,
                                constraintLayout.getId(),
                                ConstraintSet.LEFT);
                        constraintSet.connect(
                                view.getId(),
                                ConstraintSet.RIGHT,
                                constraintLayout.getId(),
                                ConstraintSet.RIGHT);
                        constraintSet.applyTo(constraintLayout);
                        continue;

                    case "layout_restrictedHeight":
                        if (constraintLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_restrictedHeight=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a ConstraintLayout.");
                            continue;
                        }
                        constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.constrainHeight(
                                view.getId(), ConstraintSet.MATCH_CONSTRAINT_WRAP);
                        constraintSet.connect(
                                view.getId(),
                                ConstraintSet.LEFT,
                                constraintLayout.getId(),
                                ConstraintSet.LEFT);
                        constraintSet.connect(
                                view.getId(),
                                ConstraintSet.RIGHT,
                                constraintLayout.getId(),
                                ConstraintSet.RIGHT);
                        constraintSet.applyTo(constraintLayout);
                        continue;
                        // End of attributes related to ConstraintLayout

                        // start of attributes related to the CoordinatorLayout
                    case "layout_anchor":
                        if (coordinatorLayout == null) {
                            debug.w(
                                    TAG,
                                    getElementNameAndRow(),
                                    "Cannot apply this attribute "
                                            + "app:layout_anchor=\""
                                            + value
                                            + "\"",
                                    "Because it's parent is not a CoordinatorLayout.");
                            continue;
                        }

                        {
                            CoordinatorLayout.LayoutParams params =
                                    (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                            params.setAnchorId(targetedId);
                        }
                        continue;

                    case "behavior_draggable":
                        if (coordinatorLayout == null) {
                            debug.w(
                                    TAG,
                                    getElementNameAndRow(),
                                    "Cannot apply this attribute "
                                            + "app:layout_anchor=\""
                                            + value
                                            + "\"",
                                    "Because it's parent is not a CoordinatorLayout.");
                            continue;
                        }

                        {
                            CoordinatorLayout.LayoutParams params =
                                    (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                            if (params.getBehavior() == null
                                    || !(params.getBehavior() instanceof BottomSheetBehavior)) {
                                continue;
                            }
                            BottomSheetBehavior behavior =
                                    (BottomSheetBehavior) params.getBehavior();
                            behavior.setDraggable(ResourcesValuesFixer.getBoolean(C, value));
                        }
                        continue;

                    case "behavior_hideable":
                        if (coordinatorLayout == null) {
                            debug.w(
                                    TAG,
                                    getElementNameAndRow(),
                                    "Cannot apply this attribute "
                                            + "app:behavior_hideable=\""
                                            + value
                                            + "\"",
                                    "Because it's parent is not a CoordinatorLayout.");
                            continue;
                        }

                        {
                            CoordinatorLayout.LayoutParams params =
                                    (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                            if (params.getBehavior() == null
                                    || !(params.getBehavior() instanceof BottomSheetBehavior)) {
                                continue;
                            }
                            BottomSheetBehavior behavior =
                                    (BottomSheetBehavior) params.getBehavior();
                            behavior.setHideable(ResourcesValuesFixer.getBoolean(C, value));
                        }
                        continue;

                    case "behavior_peekHeight":
                        if (coordinatorLayout == null) {
                            debug.w(
                                    TAG,
                                    getElementNameAndRow(),
                                    "Cannot apply this attribute "
                                            + "app:behavior_peekHeight=\""
                                            + value
                                            + "\"",
                                    "Because it's parent is not a CoordinatorLayout.");
                            continue;
                        }

                        {
                            CoordinatorLayout.LayoutParams params =
                                    (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                            if (params.getBehavior() == null
                                    || !(params.getBehavior() instanceof BottomSheetBehavior)) {
                                continue;
                            }
                            BottomSheetBehavior behavior =
                                    (BottomSheetBehavior) params.getBehavior();
                            behavior.setPeekHeight((int) ResourcesValuesFixer.getDimen(C, value));
                            view.setLayoutParams(params);
                        }
                        continue;

                    case "layout_behavior":
                        if (coordinatorLayout == null) {
                            debug.w(
                                    TAG,
                                    getElementNameAndRow(),
                                    "Cannot apply this attribute "
                                            + "app:layout_behavior=\""
                                            + value
                                            + "\"",
                                    "Because it's parent is not a CoordinatorLayout.");
                            continue;
                        }

                        {
                            CoordinatorLayout.LayoutParams params =
                                    (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                            if (CodeUtil.isClass(value)) {
                                try {
                                    Class<?> clazz = Class.forName(value);
                                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                                    CoordinatorLayout.Behavior behavior =
                                            (CoordinatorLayout.Behavior) constructor.newInstance();
                                    params.setBehavior(behavior);
                                } catch (ClassNotFoundException
                                        | NoSuchMethodException
                                        | SecurityException
                                        | InstantiationException
                                        | IllegalAccessException
                                        | InvocationTargetException
                                        | IllegalArgumentException err) {
                                }
                                Element newE = (Element) e.cloneNode(false);
                                newE.removeAttribute("app:layout_behavior");
                                set(view, newE);
                            }
                        }
                        continue;

                    case "layout_anchorGravity":
                        if (coordinatorLayout == null) {
                            if (debug != null)
                                debug.w(
                                        TAG,
                                        getElementNameAndRow(),
                                        "Cannot apply this attribute "
                                                + "app:layout_anchorGravity=\""
                                                + value
                                                + "\"",
                                        "Because it's parent is not a CoordinatorLayout.");
                            continue;
                        }
                        {
                            CoordinatorLayout.LayoutParams params =
                                    (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                            String anchorGrav = value;
                            if (anchorGrav == null || anchorGrav.trim().length() == 0)
                                anchorGrav = "center";
                            anchorGrav = parseFlag("gravity", anchorGrav);
                            params.anchorGravity = Integer.parseInt(anchorGrav);
                        }
                        continue;
                        // end of attributes related to CoordinatorLayout

                    case "navigationIcon":
                        CodeUtil.invoke(
                                view,
                                "setNavigationIcon",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;

                    case "fabCradleRoundedCornerRadius":
                        CodeUtil.invoke(
                                view,
                                "setFabCradleRoundedCornerRadius",
                                new Class[] {Float.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;

                    case "fabAlignmentMode":
                        val = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER;
                        if (value.equals("end")) val = BottomAppBar.FAB_ALIGNMENT_MODE_END;
                        CodeUtil.invoke(view, "setFabAlignmentMode", new Class[] {int.class}, val);
                        continue;

                    case "fabAnimationMode":
                        val = BottomAppBar.FAB_ANIMATION_MODE_SCALE;
                        if (value.equals("slide")) val = BottomAppBar.FAB_ANIMATION_MODE_SLIDE;
                        CodeUtil.invoke(view, "setFabCradleMargin", new Class[] {float.class}, val);
                        continue;

                    case "fabCradleMargin":
                        CodeUtil.invoke(
                                view,
                                "setFabCradleMargin",
                                new Class[] {float.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;
                    case "fabAnchorMode":
                        CodeUtil.invoke(
                                view,
                                "setFabAnchorMode",
                                new Class[] {int.class},
                                value.equals("cradle")
                                        ? BottomAppBar.FAB_ANCHOR_MODE_CRADLE
                                        : BottomAppBar.FAB_ANCHOR_MODE_EMBED);
                        continue;
                        /*case "valueFrom":
                            {
                                String sv = element.getAttribute("android:values");
                                String svt = element.getAttribute("android:valueTo");
                                if (sv.isEmpty() || svt.isEmpty() || value.isEmpty()) continue;
                                if ((Float.parseFloat(value) >= Float.parseFloat(svt))
                                        && (Float.parseFloat(value) >= Float.parseFloat(sv))) {
                                    if (debug != null)
                                        debug.w(
                                                TAG,
                                                getElementNameAndRow(),
                                                "attr \"valueFrom\"",
                                                "cannot be greater than \"valueTo/values\"");
                                    continue;
                                }

                                CodeUtil.invoke(
                                        view,
                                        "setValueFrom",
                                        new Class[] {float.class},
                                        Float.parseFloat(value));
                            }
                            continue;
                        case "valueTo":
                            {
                                String sv = element.getAttribute("android:values");
                                String svf = element.getAttribute("android:valueFrom");
                                if (sv.isEmpty() || svf.isEmpty() || value.isEmpty()) continue;
                                if ((Float.parseFloat(value) <= Float.parseFloat(svf))
                                        && (Float.parseFloat(value) <= Float.parseFloat(sv))) {
                                    if (debug != null)
                                        debug.w(
                                                TAG,
                                                getElementNameAndRow(),
                                                "attr \"valueTo\"",
                                                "must not be less than \"valueFrom\"");
                                    continue;
                                }

                                CodeUtil.invoke(
                                        view,
                                        "setValueTo",
                                        new Class[] {float.class},
                                        Float.parseFloat(value));
                            }
                            continue;
                        case "values":
                            {
                                String svf = element.getAttribute("android:valueFrom");
                                String svt = element.getAttribute("android:valueTo");
                                if (svf.isEmpty() || svt.isEmpty() || value.isEmpty()) continue;
                                float fVal = Float.parseFloat(value);
                                float valFrom = Float.parseFloat(svf);
                                float valTo = Float.parseFloat(svt);

                                if (fVal >= valFrom && fVal <= valTo)
                                    CodeUtil.invoke(
                                            view, "setValues", new Class[] {Float[].class}, fVal);
                            }
                            continue;*/
                    case "cardCornerRadius":
                        CodeUtil.invoke(
                                view,
                                "setRadius",
                                new Class[] {float.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;
                    case "singleSelection":
                        Boolean bool = ResourcesValuesFixer.getBoolean(C, value);
                        if (bool == null) {
                            debug.e(
                                    "ResManager",
                                    "The value or reference does not not to a boolean value. Please fix this.");
                            continue;
                        }
                        CodeUtil.invoke(
                                view, "setSingleSelection", new Class[] {boolean.class}, bool);
                        continue;
                    case "selectionRequired":
                        bool = ResourcesValuesFixer.getBoolean(C, value);
                        if (bool == null) {
                            debug.e(
                                    "ResManager",
                                    "The value or reference does not not to a boolean value. Please fix this.");
                            continue;
                        }
                        CodeUtil.invoke(
                                view, "setSelectionRequired", new Class[] {boolean.class}, bool);
                        continue;
                    case "checkedButton":
                        CodeUtil.invoke(
                                view,
                                "check",
                                new Class[] {int.class},
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "menu":
                        String path = null;
                        String sVal = value.matches(".*\\/.*") ? value.split("/")[1] : value;
                        if (DataRefManager.getInstance()
                                .currentModuleRes
                                .menus
                                .containsKey(sVal.intern())) {
                            path =
                                    DataRefManager.getInstance()
                                            .currentModuleRes
                                            .menus
                                            .get(sVal.intern());
                            Menu menu = (Menu) CodeUtil.invoke(v, "getMenu", null, new Object[] {});
                            MenuItemCreator.buildMenu(C, menu, path);
                        } else if (debug != null)
                            debug.w(
                                    TAG,
                                    getElementNameAndRow(),
                                    "the \"menu\" attribute refers to a resource that does not exist or has not been detected in the project.",
                                    value);
                        continue;
                    case "title":
                        CodeUtil.invoke(
                                view,
                                "setTitle",
                                new Class[] {CharSequence.class},
                                ResourcesValuesFixer.getString(C, value));
                        continue;

                    case "subtitle":
                        CodeUtil.invoke(
                                view,
                                "setSubtitle",
                                new Class[] {CharSequence.class},
                                ResourcesValuesFixer.getString(C, value));
                        continue;
                    case "src":
                        CodeUtil.invoke(
                                view,
                                "setImageDrawable",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "srcCompat":
                        CodeUtil.invoke(
                                view,
                                "setImageDrawable",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "text":
                        CodeUtil.invoke(
                                view,
                                "setText",
                                new Class[] {CharSequence.class},
                                ResourcesValuesFixer.getString(C, value));
                        continue;
                    case "gravity":
                        CodeUtil.invoke(
                                view,
                                "setGravity",
                                new Class[] {int.class},
                                Integer.parseInt(parseFlag(name, value)));
                        continue;

                    case "orientation":
                        CodeUtil.invoke(
                                view,
                                "setOrientation",
                                new Class[] {int.class},
                                Integer.parseInt(enumMap.get("orientation").get(value)));
                        continue;

                    case "width":
                        CodeUtil.invoke(
                                view,
                                "setWidth",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;
                    case "height":
                        CodeUtil.invoke(
                                view,
                                "setHeight",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;
                    case "maxEms":
                        CodeUtil.invoke(
                                view,
                                "setMaxEms",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "ems":
                        CodeUtil.invoke(
                                view, "setEms", new Class[] {int.class}, Integer.parseInt(value));
                        continue;
                    case "hint":
                        CodeUtil.invoke(
                                view,
                                "setHint",
                                new Class[] {CharSequence.class},
                                ResourcesValuesFixer.getString(C, value));
                        continue;
                    case "lines":
                        CodeUtil.invoke(
                                view, "setLines", new Class[] {int.class}, Integer.parseInt(value));
                        continue;
                    case "maxLines":
                        CodeUtil.invoke(
                                view,
                                "setMaxLines",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "maxWidth":
                        CodeUtil.invoke(
                                view,
                                "setMaxWidth",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;
                    case "minEms":
                        CodeUtil.invoke(
                                view,
                                "setMinEms",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "minHeight":
                        CodeUtil.invoke(
                                view,
                                "setMinHeight",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;
                    case "minLines":
                        CodeUtil.invoke(
                                view,
                                "setMinLines",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "minWidth":
                        CodeUtil.invoke(
                                view,
                                "setMinWidth",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;
                    case "maxHeight":
                        CodeUtil.invoke(
                                view,
                                "setMaxHeight",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;
                    case "singleLine":
                        CodeUtil.invoke(
                                view,
                                "setSingleLine",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "textColor":
                        CodeUtil.invoke(
                                view,
                                "setTextColor",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getColor(C, value));
                        continue;
                    case "textColorHighlight":
                        CodeUtil.invoke(
                                view,
                                "setHighlightColor",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getColor(C, value));
                        continue;
                    case "textColorHint":
                        CodeUtil.invoke(
                                view,
                                "setHintTextColor",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getColor(C, value));
                        continue;
                    case "textColorLink":
                        CodeUtil.invoke(
                                view,
                                "setLinkTextColor",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getColor(C, value));
                        continue;
                    case "textScaleX":
                        CodeUtil.invoke(
                                view,
                                "setTextScaleX",
                                new Class[] {float.class},
                                Float.parseFloat(value));
                        continue;
                    case "textIsSelectable":
                        CodeUtil.invoke(
                                view,
                                "setTextIsSelectable",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "textSize":
                        CodeUtil.invoke(
                                view,
                                "setTextSize",
                                new Class[] {float.class},
                                ResourcesValuesFixer.getDimen(C, value) / 2);
                        continue;

                    case "shadowColor":
                        {
                            int color = ResourcesValuesFixer.getColor(C, value);
                            float dx = (float) CodeUtil.invoke(v, "getShadowDx", null);
                            float dy = (float) CodeUtil.invoke(v, "getShadowDy", null);
                            float r = (float) CodeUtil.invoke(v, "getShadowRadius", null);
                            CodeUtil.invoke(
                                    view,
                                    "setShadowLayer",
                                    new Class[] {float.class, float.class, float.class, int.class},
                                    r,
                                    dx,
                                    dy,
                                    color);
                            continue;
                        }

                    case "shadowDx":
                        {
                            int color = (int) CodeUtil.invoke(v, "getShadowColor", null);
                            float dx = Float.parseFloat(value);
                            float dy = (float) CodeUtil.invoke(v, "getShadowDy", null);
                            float r = (float) CodeUtil.invoke(v, "getShadowRadius", null);
                            CodeUtil.invoke(
                                    view,
                                    "setShadowLayer",
                                    new Class[] {float.class, float.class, float.class, int.class},
                                    r,
                                    dx,
                                    dy,
                                    color);
                            continue;
                        }

                    case "shadowDy":
                        {
                            int color = (int) CodeUtil.invoke(v, "getShadowColor", null);
                            float dx = (float) CodeUtil.invoke(v, "getShadowDx", null);
                            float dy = Float.parseFloat(value);
                            float r = (float) CodeUtil.invoke(v, "getShadowRadius", null);
                            CodeUtil.invoke(
                                    view,
                                    "setShadowLayer",
                                    new Class[] {float.class, float.class, float.class, int.class},
                                    r,
                                    dx,
                                    dy,
                                    color);
                            continue;
                        }
                    case "shadowRadius":
                        {
                            int color = (int) CodeUtil.invoke(v, "getShadowColor", null);
                            float dx = (float) CodeUtil.invoke(v, "getShadowDx", null);
                            float dy = (float) CodeUtil.invoke(v, "getShadowDy", null);
                            float r = Float.parseFloat(value);
                            CodeUtil.invoke(
                                    view,
                                    "setShadowLayer",
                                    new Class[] {float.class, float.class, float.class, int.class},
                                    r,
                                    dx,
                                    dy,
                                    color);
                            continue;
                        }

                    case "typeface":
                        {
                            Typeface tf =
                                    (Typeface)
                                            CodeUtil.getFiledValueFromClass(
                                                    Typeface.class, value.toUpperCase());
                            CodeUtil.invoke(v, "setTypeface", new Class[] {Typeface.class}, tf);
                        }
                        continue;
                    case "ellipsize":
                        CodeUtil.invoke(
                                view,
                                "setEllipsize",
                                new Class[] {TextUtils.TruncateAt.class},
                                TextUtils.TruncateAt.valueOf(value));
                        continue;
                    case "inputType":
                        CodeUtil.invoke(
                                view,
                                "setInputType",
                                new Class[] {int.class},
                                Integer.parseInt(parseFlag(name, value)));
                        continue;
                    case "textAlignment":
                        CodeUtil.invoke(
                                view,
                                "setTextAlignment",
                                new Class[] {int.class},
                                Integer.valueOf(value));
                        continue;
                    case "adjustViewBounds":
                        CodeUtil.invoke(
                                view,
                                "setAdjustViewBounds",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "baseLine":
                        CodeUtil.invoke(
                                view,
                                "setBaseline",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "scaleType":
                        CodeUtil.invoke(
                                view,
                                "setScaleType",
                                new Class[] {ImageView.ScaleType.class},
                                ImageView.ScaleType.valueOf(value));
                        continue;
                    case "checked":
                        CodeUtil.invoke(
                                view,
                                "setChecked",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;

                    case "textOff":
                        {
                            CodeUtil.invoke(
                                    view,
                                    "setTextOff",
                                    new Class[] {CharSequence.class},
                                    ResourcesValuesFixer.getString(C, value));
                            boolean b = (boolean) CodeUtil.invoke(v, "isChecked", null);
                            CodeUtil.invoke(v, "setChecked", new Class[] {boolean.class}, b);
                            continue;
                        }
                    case "textOn":
                        {
                            CodeUtil.invoke(
                                    view,
                                    "setTextOn",
                                    new Class[] {CharSequence.class},
                                    ResourcesValuesFixer.getString(C, value));
                            boolean b = (boolean) CodeUtil.invoke(v, "isChecked", null);
                            CodeUtil.invoke(v, "setChecked", new Class[] {boolean.class}, b);
                            continue;
                        }
                    case "enabled":
                        if (!ResourcesValuesFixer.getBoolean(C, value))
                            CodeUtil.invoke(
                                    view,
                                    "setEnabled",
                                    new Class[] {boolean.class},
                                    ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "switchMinWidth":
                        CodeUtil.invoke(
                                view,
                                "setSwitchMinWidth",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "switchPadding":
                        CodeUtil.invoke(
                                view,
                                "setSwitchPadding",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "thumbTextPadding":
                        CodeUtil.invoke(
                                view,
                                "setThumbTextPadding",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "clipChildren":
                        CodeUtil.invoke(
                                view,
                                "setClipChildren",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "calendarViewShown":
                        CodeUtil.invoke(
                                view,
                                "setCalendarViewShown",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "clipToPadding":
                        CodeUtil.invoke(
                                view,
                                "setClipToPadding",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "measureAllChildren":
                        CodeUtil.invoke(
                                view,
                                "setMeasureAllChildren",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "spinnersShown":
                        CodeUtil.invoke(
                                view,
                                "setSpinnersShown",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;

                    case "setClipChildren":
                        CodeUtil.invoke(
                                view,
                                "setClipChildren",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "baselineAlignBottom":
                        CodeUtil.invoke(
                                view,
                                "setBaselineAlignBottom",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "cropToPadding":
                        CodeUtil.invoke(
                                view,
                                "setCropToPadding",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "baselineAligned":
                        CodeUtil.invoke(
                                view,
                                "setBaselineAligned",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "baselineAlignedChildIndex":
                        CodeUtil.invoke(
                                view,
                                "setBaselineAlignedChildIndex",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "weightSum":
                        CodeUtil.invoke(
                                view,
                                "setWeightSum",
                                new Class[] {float.class},
                                Float.parseFloat(value));
                        continue;
                    case "indeterminate":
                        CodeUtil.invoke(
                                view,
                                "setIndeterminate",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "indeterminateDrawable":
                        CodeUtil.invoke(
                                view,
                                "setIndeterminateDrawable",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "indeterminateOnly":
                        CodeUtil.setField(v, "mIndeterminateOnly", Boolean.valueOf(value));
                        continue;

                    case "progressDrawable":
                        CodeUtil.invoke(
                                view,
                                "setProgressDrawable",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "numStars":
                        CodeUtil.invoke(
                                view,
                                "setNumStars",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "rating":
                        CodeUtil.invoke(
                                view,
                                "setRating",
                                new Class[] {float.class},
                                Float.parseFloat(value));
                        continue;
                    case "isIndicator":
                        CodeUtil.invoke(
                                view,
                                "setIsIndicator",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "stepSize":
                        CodeUtil.invoke(
                                view,
                                "setStepSize",
                                new Class[] {float.class},
                                Float.parseFloat(value));
                        continue;
                    case "progress":
                        CodeUtil.invoke(
                                view,
                                "setProgress",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "min":
                        CodeUtil.invoke(
                                view, "setMin", new Class[] {int.class}, Integer.parseInt(value));
                        continue;
                    case "max":
                        CodeUtil.invoke(
                                view, "setMax", new Class[] {int.class}, Integer.parseInt(value));
                        continue;
                    case "fillViewport":
                        CodeUtil.invoke(
                                view,
                                "setFillViewport",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "alignmentMode":
                        CodeUtil.invoke(
                                view,
                                "setAlignmentMode",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "columnCount":
                        CodeUtil.invoke(
                                view,
                                "setColumnCount",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "columnOrderPreserved":
                        CodeUtil.invoke(
                                view,
                                "setColumnOrderPreserved",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "rowCount":
                        CodeUtil.invoke(
                                view,
                                "setRowCount",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "rowOrderPreserved":
                        CodeUtil.invoke(
                                view,
                                "setRowOrderPreserved",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "setUseDefaultMargins":
                        CodeUtil.invoke(
                                view,
                                "setFillViewport",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "measureWithLargestChild":
                        CodeUtil.invoke(
                                view,
                                "setMeasureWithLargestChildEnabled",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "textStyle":
                        {
                            int style = Integer.parseInt(parseFlag(name, value));
                            Typeface tf = (Typeface) CodeUtil.invoke(v, "getTypeface", null);
                            CodeUtil.invoke(
                                    view,
                                    "setTypeface",
                                    new Class[] {Typeface.class, int.class},
                                    tf,
                                    style);
                            continue;
                        }
                    case "textAppearance":
                        {
                            int attrid = ResourcesValuesFixer.getStyle(value);
                            CodeUtil.invoke(
                                    view,
                                    "setTextAppearance",
                                    new Class[] {Context.class, int.class},
                                    C,
                                    attrid);
                            continue;
                        }

                    case "alpha":
                        CodeUtil.invoke(
                                view,
                                "setAlpha",
                                new Class[] {float.class},
                                Float.parseFloat(value));
                        continue;
                    case "clickable":
                        CodeUtil.invoke(
                                view,
                                "setClickable",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "contentDescription":
                        CodeUtil.invoke(
                                view,
                                "setContentDescription",
                                new Class[] {CharSequence.class},
                                ResourcesValuesFixer.getString(C, value));
                        continue;
                    case "drawingCacheQuality":
                        CodeUtil.invoke(
                                view,
                                "setDrawingCacheQuality",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "duplicateParentState":
                        CodeUtil.invoke(
                                view,
                                "setDuplicateParentStateEnabled",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "fadeScrollbars":
                        CodeUtil.invoke(
                                view,
                                "setScrollbarFadingEnabled",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "fadingEdge":
                    case "requiresFadingEdge":
                        CodeUtil.invoke(
                                view,
                                "setHorizontalFadingEdgeEnabled",
                                new Class[] {boolean.class},
                                false);
                        CodeUtil.invoke(
                                view,
                                "setVerticalFadingEdgeEnabled",
                                new Class[] {boolean.class},
                                false);
                        switch (Integer.parseInt(value)) {
                            case 0x00000000: // none
                                continue;
                            case 0x00001000: // h
                                CodeUtil.invoke(
                                        view,
                                        "setHorizontalFadingEdgeEnabled",
                                        new Class[] {boolean.class},
                                        true);
                                continue;
                            case 0x00002000: // v
                                CodeUtil.invoke(
                                        view,
                                        "setVerticalFadingEdgeEnabled",
                                        new Class[] {boolean.class},
                                        true);
                                continue;
                            case 0x00003000: // hv
                                CodeUtil.invoke(
                                        view,
                                        "setHorizontalFadingEdgeEnabled",
                                        new Class[] {boolean.class},
                                        true);
                                CodeUtil.invoke(
                                        view,
                                        "setVerticalFadingEdgeEnabled",
                                        new Class[] {boolean.class},
                                        true);
                                continue;
                        }
                        continue;
                    case "fadingEdgeLength":
                        CodeUtil.invoke(
                                view,
                                "setFadingEdgeLength",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "filterTouchesWhenObscured":
                        CodeUtil.invoke(
                                view,
                                "setFilterTouchesWhenObscured",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "fitsSystemWindows":
                        CodeUtil.invoke(
                                view,
                                "setFitsSystemWindows",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "focusable":
                        CodeUtil.invoke(
                                view,
                                "setFocusable",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "focusableInTouchMode":
                        CodeUtil.invoke(
                                view,
                                "setFocusableInTouchMode",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "foreground":
                        CodeUtil.invoke(
                                view,
                                "setForeground",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "foregroundTint":
                        CodeUtil.invoke(
                                view,
                                "setForegroundTintList",
                                new Class[] {ColorStateList.class},
                                ColorStateList.valueOf(ResourcesValuesFixer.getColor(C, value)));
                        continue;
                    case "foregroundTintMode":
                        CodeUtil.invoke(
                                view,
                                "setForegroundTintMode",
                                new Class[] {PorterDuff.Mode.class},
                                otherTypeValueMap.get(name).get(value));
                        continue;
                    case "foregroundGravity":
                        CodeUtil.invoke(
                                view,
                                "setForegroundGravity",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "hapticFeedbackEnabled":
                        CodeUtil.invoke(
                                view,
                                "setHapticFeedbackEnabled",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "importantForAccessibility":
                        CodeUtil.invoke(
                                view,
                                "setImportantForAccessibility",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "isScrollContainer":
                        CodeUtil.invoke(
                                view,
                                "setScrollContainer",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "keepScreenOn":
                        CodeUtil.invoke(
                                view,
                                "setKeepScreenOn",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "labelFor":
                        CodeUtil.invoke(
                                view,
                                "setLabelFor",
                                new Class[] {int.class},
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "layerType":
                        CodeUtil.invoke(
                                view,
                                "setLayerType",
                                new Class[] {int.class, Paint.class},
                                getIdValueAsIntFromListRef(value),
                                null);
                        continue;
                    case "layoutDirection":
                        CodeUtil.invoke(
                                view,
                                "setLayoutDirection",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "layout_gravity":
                        CodeUtil.setField(
                                v.getLayoutParams(),
                                "gravity",
                                Integer.parseInt(parseFlag(name, value)));
                        continue;
                    case "longClickable":
                        CodeUtil.invoke(
                                view,
                                "setLongClickable",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "nextFocusDown":
                        CodeUtil.invoke(
                                view,
                                "setNextFocusDownId",
                                new Class[] {int.class},
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "nextFocusForward":
                        CodeUtil.invoke(
                                view,
                                "setNextFocusForwardId",
                                new Class[] {int.class},
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "nextFocusLeft":
                        CodeUtil.invoke(
                                view,
                                "setNextFocusLeftId",
                                new Class[] {int.class},
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "nextFocusRight":
                        CodeUtil.invoke(
                                view,
                                "setNextFocusRightId",
                                new Class[] {int.class},
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "nextFocusUp":
                        CodeUtil.invoke(
                                view,
                                "setNextFocusUpId",
                                new Class[] {int.class},
                                getIdValueAsIntFromListRef(value));
                        continue;
                    case "onClick":
                        // do nothing
                        continue;
                    case "overScrollMode":
                        CodeUtil.invoke(
                                view,
                                "setOverScrollMode",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "saveEnabled":
                        CodeUtil.invoke(
                                view,
                                "setSaveEnabled",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "scrollX":
                        CodeUtil.invoke(
                                view,
                                "setScrollX",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "scrollY":
                        CodeUtil.invoke(
                                view,
                                "setScrollY",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "scrollbarAlwaysDrawHorizontalTrack":
                        CodeUtil.invoke(
                                view,
                                "setHorizontalScrollBarEnabled",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "scrollbarAlwaysDrawVerticalTrack":
                        CodeUtil.invoke(
                                view,
                                "setVerticalScrollBarEnabled",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "scrollbarDefaultDelayBeforeFade":
                        CodeUtil.invoke(
                                view,
                                "setScrollBarDefaultDelayBeforeFade",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "scrollbarFadeDuration":
                        CodeUtil.invoke(
                                view,
                                "setScrollBarFadeDuration",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "scrollbarSize":
                        CodeUtil.invoke(
                                view,
                                "setScrollBarSize",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "scrollbarStyle":
                        CodeUtil.invoke(
                                view,
                                "setScrollBarStyle",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "scrollbarThumbHorizontal":
                        CodeUtil.invoke(
                                view,
                                "setHorizontalScrollbarThumbDrawable",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "scrollbarThumbVertical":
                        CodeUtil.invoke(
                                view,
                                "setVerticalScrollbarThumbDrawable",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "scrollbarTrackHorizontal":
                        CodeUtil.invoke(
                                view,
                                "setHorizontalScrollbarTrackDrawable",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "scrollbarTrackVertical":
                        CodeUtil.invoke(
                                view,
                                "setVerticalScrollbarTrackDrawable",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "scrollbars":
                        CodeUtil.invoke(
                                view,
                                "setHorizontalScrollBarEnabled",
                                new Class[] {boolean.class},
                                false);
                        CodeUtil.invoke(
                                view,
                                "setVerticalScrollBarEnabled",
                                new Class[] {boolean.class},
                                false);
                        switch (Integer.parseInt(value)) {
                            case 0x00000000: // none
                                continue;
                            case 0x00001000: // h
                                CodeUtil.invoke(
                                        view,
                                        "setHorizontalScrollBarEnabled",
                                        new Class[] {boolean.class},
                                        true);
                                continue;
                            case 0x00002000: // v
                                CodeUtil.invoke(
                                        view,
                                        "setVerticalScrollBarEnabled",
                                        new Class[] {boolean.class},
                                        true);
                                continue;
                            case 0x00003000: // hv
                                CodeUtil.invoke(
                                        view,
                                        "setHorizontalScrollBarEnabled",
                                        new Class[] {boolean.class},
                                        true);
                                CodeUtil.invoke(
                                        view,
                                        "setVerticalScrollBarEnabled",
                                        new Class[] {boolean.class},
                                        true);
                                continue;
                        }
                        continue;
                    case "soundEffectsEnabled":
                        CodeUtil.invoke(
                                view,
                                "setSoundEffectsEnabled",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "textDirection":
                        CodeUtil.invoke(
                                view,
                                "setTextDirection",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "transformPivotX":
                        CodeUtil.invoke(
                                view,
                                "setPivotX",
                                new Class[] {float.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "transformPivotY":
                        CodeUtil.invoke(
                                view,
                                "setPivotY",
                                new Class[] {float.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;
                    case "verticalScrollbarPosition":
                        CodeUtil.invoke(
                                view,
                                "setVerticalScrollbarPosition",
                                new Class[] {int.class},
                                Integer.parseInt(value));
                        continue;
                    case "showText":
                        CodeUtil.invoke(
                                view,
                                "setShowText",
                                new Class[] {boolean.class},
                                ResourcesValuesFixer.getBoolean(C, value));
                        continue;
                    case "icon":
                        CodeUtil.invoke(
                                view,
                                "setIconDrawable",
                                new Class[] {Drawable.class},
                                ResourcesValuesFixer.getDrawable(C, value));
                        continue;

                    case "iconGravity":
                        CodeUtil.invoke(
                                view,
                                "setIconGravity",
                                new Class[] {int.class},
                                Integer.parseInt(parseFlag(name, value)));
                        continue;

                    case "iconPadding":
                        CodeUtil.invoke(
                                view,
                                "setIconPadding",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;

                    case "iconSize":
                        CodeUtil.invoke(
                                view,
                                "setIconSize",
                                new Class[] {int.class},
                                ResourcesValuesFixer.getDimen(C, value));
                        continue;

                    case "iconTint":
                        CodeUtil.invoke(
                                view,
                                "setIconTint",
                                new Class[] {ColorStateList.class},
                                ColorStateList.valueOf(ResourcesValuesFixer.getColor(C, value)));
                        continue;
                }
            } catch (Exception err) {
                if (debug != null)
                    debug.e(
                            "AttributeSetter",
                            getElementNameAndRow(),
                            "Cannot apply attribute \"" + name + "\"",
                            "Exception error : " + err.getMessage());
            }
        }
    }

    private int getParentId() {
        try {
            View parent = (View) view.getParent();
            if (parent != null) return view.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private ConstraintLayout getParentAsConstraintLayout() {
        try {
            View parent = (View) view.getParent();
            if (parent == null) return null;
            if (parent instanceof ConstraintLayout) {
                ConstraintLayout constraint = (ConstraintLayout) parent;
                if (constraint != null) return constraint;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private CoordinatorLayout getParentAsCoordinatorLayout() {
        try {
            View parent = (View) view.getParent();
            if (parent == null) return null;
            if (parent instanceof CoordinatorLayout) {
                CoordinatorLayout coordinator = (CoordinatorLayout) parent;
                if (coordinator != null) return coordinator;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getIdValueAsIntFromListRef(String id) {
        if (!id.trim().startsWith("@id/") && !id.trim().startsWith("@+id/") && !id.equals("parent"))
            return -1;

        if (id.equals("parent")) {
            Node nd = element.getParentNode();
            if (nd != null && nd.getNodeType() == Node.ELEMENT_NODE)
                getIdValueAsIntFromListRef(((Element) nd).getAttribute("android:id"));
        }
        if (id.endsWith("/")) return -1;
        id = id.contains("/") ? id.split("/")[1] : id;
        for (Pair<?, ?> pair :
                Utils.convertMapToPairViewElement(listener.onRefViewElementNeeded().getListRef())) {
            View v = (View) pair.first;
            Element e = (Element) pair.second;
            if (e.getAttribute("android:id").endsWith("/" + id)) return v.getId();
        }
        return -1;
    }

    private String parseFlag(String name, String value) {
        Map<String, Integer> map = flagMap.get(name);
        assert map != null;
        if (!value.contains("|")) {
            return String.valueOf(map.get(value));
        }
        int flag = -1;
        String[] flags = value.split("\\|");
        for (String f : flags) {
            if (!f.trim().isEmpty()) {
                int v = map.get(f);
                if (flag == -1) {
                    flag = v;
                } else {
                    flag |= v;
                }
            } else {
                if (debug != null)
                    debug.w(TAG, getElementNameAndRow(), "attr : " + name, f + ": wrong format");
            }
        }
        return String.valueOf(flag);
    }

    private String getElementNameAndRow() {
        return element.getNodeName() + ", Row : " + ((Node) element).getUserData("lineNumber");
    }

    public void setOnDataRefNeeded(OnDataRefNeeded listener) {
        this.listener = listener;
    }

    public interface OnDataRefNeeded {
        public RefViewElement onRefViewElementNeeded();

        public String tagNeeded();
    }
}
