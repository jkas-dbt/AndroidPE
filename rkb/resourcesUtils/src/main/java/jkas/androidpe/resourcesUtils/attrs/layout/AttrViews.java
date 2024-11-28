package jkas.androidpe.resourcesUtils.attrs.layout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import static jkas.androidpe.resourcesUtils.attrs.AllAttrBase.*;

/**
 * @author JKas
 */
public class AttrViews {
    public static LinkedHashMap<String, String[]> baseAttrs = new LinkedHashMap<>();
    public static LinkedHashMap<String, String[]> baseAttrsApp = new LinkedHashMap<>();
    public static LinkedHashMap<String, String[]> allAttrs = new LinkedHashMap<>();

    public static ArrayList<String> usedByAssist = new ArrayList<>();

    static {
        initUsed();
        initBaseAttrs();
        initBaseAttrsApp();
        initAllAttrs();
    }

    private static void initUsed() {
        usedByAssist.clear();
        usedByAssist.add("layout_weight");
        usedByAssist.add("layout_width");
        usedByAssist.add("layout_height");
        usedByAssist.add("id");
        usedByAssist.add("padding");
        usedByAssist.add("paddingStart");
        usedByAssist.add("paddingEnd");
        usedByAssist.add("paddingLeft");
        usedByAssist.add("paddingTop");
        usedByAssist.add("paddingRight");
        usedByAssist.add("paddingBottom");
        usedByAssist.add("layout_margin");
        usedByAssist.add("layout_marginStart");
        usedByAssist.add("layout_marginEnd");
        usedByAssist.add("layout_marginLeft");
        usedByAssist.add("layout_marginTop");
        usedByAssist.add("layout_marginRight");
        usedByAssist.add("layout_marginBottom");
        usedByAssist.add("visibility");
        usedByAssist.add("backgroundTint");
        usedByAssist.add("background");
        usedByAssist.add("gravity");
        usedByAssist.add("layout_gravity");
        usedByAssist.add("alpha");
        usedByAssist.add("layout_constraintLeft_toLeftOf");
        usedByAssist.add("layout_constraintLeft_toRightOf");
        usedByAssist.add("layout_constraintRight_toLeftOf");
        usedByAssist.add("layout_constraintRight_toRightOf");
        usedByAssist.add("layout_constraintTop_toTopOf");
        usedByAssist.add("layout_constraintTop_toBottomOf");
        usedByAssist.add("layout_constraintBottom_toTopOf");
        usedByAssist.add("layout_constraintBottom_toBottomOf");
        usedByAssist.add("layout_constraintBaseline_toBaselineOf");
        usedByAssist.add("layout_constraintStart_toEndOf");
        usedByAssist.add("layout_constraintStart_toStartOf");
        usedByAssist.add("layout_constraintEnd_toStartOf");
        usedByAssist.add("layout_constraintEnd_toEndOf");
        usedByAssist.add("layout_marginBaseline");
        usedByAssist.add("layout_goneMarginStart");
        usedByAssist.add("layout_goneMarginEnd");
        usedByAssist.add("layout_goneMarginLeft");
        usedByAssist.add("layout_goneMarginTop");
        usedByAssist.add("layout_goneMarginRight");
        usedByAssist.add("layout_goneMarginBottom");
        usedByAssist.add("layout_goneMarginBaseline");
        usedByAssist.add("layout_constraintCircle");
        usedByAssist.add("layout_constraintCircleRadius");
        usedByAssist.add("layout_constraintCircleAngle");
        usedByAssist.add("layout_constrainedWidth");
        usedByAssist.add("layout_constrainedHeight");
        usedByAssist.add("layout_constraintWidth_min");
        usedByAssist.add("layout_constraintHeight_min");
        usedByAssist.add("layout_constraintWidth_max");
        usedByAssist.add("layout_constraintHeight_max");
        usedByAssist.add("layout_constraintWidth_percent");
        usedByAssist.add("layout_constraintHeight_percent");
        usedByAssist.add("layout_constraintWidth_default");
        usedByAssist.add("layout_constraintHeight_default");
        usedByAssist.add("layout_constraintHorizontal_weight");
        usedByAssist.add("layout_constraintVertical_weight");
    }

    private static void initBaseAttrs() {
        baseAttrs.clear();
        baseAttrs.put("style", new String[] {"@style"});
        baseAttrs.put("foreground", VALUES_DRAWABLE);
        baseAttrs.put("foregroundTint", VALUES_COLOR);
        baseAttrs.put("foregroundTintMode", VALUES_TINT_MODE);
        baseAttrs.put("backgroundTintMode", VALUES_TINT_MODE);
        baseAttrs.put("clipToOutline", VALUES_BOOLEAN);
        baseAttrs.put("contentDescription", VALUES_STRING);
        baseAttrs.put("contextClickable", VALUES_BOOLEAN);
        baseAttrs.put("defaultFocusHightlightEnabled", VALUES_BOOLEAN);
        baseAttrs.put("drawingCacheQuality", new String[] {"auto", "low", "medium", "high"});
        baseAttrs.put("duplicateParentState", VALUES_BOOLEAN);
        baseAttrs.put("elevation", VALUES_DIMEN);
        baseAttrs.put("fadeScrollbars", VALUES_BOOLEAN);
        baseAttrs.put("fadingEdgeLength", VALUES_DIMEN);
        baseAttrs.put("filterTouchesWhenObscured", VALUES_BOOLEAN);
        baseAttrs.put("foreground", VALUES_DRAWABLE);
        baseAttrs.put("foregroundGravity", VALUES_GRAVITY);
        baseAttrs.put("foregroundTint", VALUES_COLOR);
        baseAttrs.put("foregroundTintMode", VALUES_TINT_MODE);
        baseAttrs.put("minHeight", VALUES_DIMEN);
        baseAttrs.put("minWidth", VALUES_DIMEN);
        baseAttrs.put("pivotX", null);
        baseAttrs.put("pivotY", null);
        baseAttrs.put("rotation", null);
        baseAttrs.put("rotationX", null);
        baseAttrs.put("rotationY", null);
        baseAttrs.put("scaleX", null);
        baseAttrs.put("scaleY", null);
        baseAttrs.put("textAlignment", VALUES_BOOLEAN);
        baseAttrs.put("textDirection", VALUES_STRING);
        baseAttrs.put("toolipText", VALUES_STRING);
        baseAttrs.put("transformPivotX", null);
        baseAttrs.put("transformPivotY", null);
        baseAttrs.put("translationX", null);
        baseAttrs.put("translationY", null);
        baseAttrs.put("translationZ", null);
    }

    private static void initBaseAttrsApp() {
        baseAttrsApp.clear();
    }

    private static void initAllAttrs() {
        allAttrs.put("icon", VALUES_DRAWABLE);
        allAttrs.put("layout_anchor", new String[] {"@id"});
        allAttrs.put("layout_anchorGravity", VALUES_GRAVITY);
        allAttrs.put("foregroundGravity", VALUES_GRAVITY);
        allAttrs.put("measureAllChildren", VALUES_BOOLEAN);
        allAttrs.put("alignmentMode", new String[] {"alignBounds", "alignMargins"});
        allAttrs.put("columnCount", null);
        allAttrs.put("columnOrderPreserved", VALUES_BOOLEAN);
        allAttrs.put("orientation", new String[] {"vertical", "horizontal"});
        allAttrs.put("rowCount", null);
        allAttrs.put("rowOrderPreserved", VALUES_BOOLEAN);
        allAttrs.put("useDefaultMargins", VALUES_BOOLEAN);
        allAttrs.put("fillViewport", VALUES_BOOLEAN);
        allAttrs.put("baselineAligned", VALUES_BOOLEAN);
        allAttrs.put("baselineAlignedChildIndex", new String[] {"@id"});
        allAttrs.put("divider", VALUES_DRAWABLE);
        allAttrs.put("gravity", VALUES_GRAVITY);
        allAttrs.put("measureWithLargestChild", VALUES_BOOLEAN);
        allAttrs.put("weightSum", null);
        allAttrs.put("checkedButton", new String[] {"@id"});
        allAttrs.put(
                "ignoreGravity",
                new String[] {
                    "above",
                    "align_baseline",
                    "align_bottom",
                    "align_end",
                    "align_left",
                    "align_parent_bottom",
                    "align_parent_end",
                    "align_parent_left",
                    "align_parent_right",
                    "align_parent_start",
                    "align_parent_top",
                    "align_right",
                    "align_start",
                    "align_top",
                    "align_below",
                    "center_horizontal",
                    "center_vertical",
                    "center_in_parent",
                    "end_of",
                    "left_of",
                    "right_of",
                    "start_of",
                    "true"
                });
        allAttrs.put("autoStart", VALUES_BOOLEAN);
        allAttrs.put("flipInterval", null); // not yet
        allAttrs.put("fabAlignmentMode", new String[] {"center", "end"});
        allAttrs.put("fabAnchorMode", new String[] {"cradle", "embed"});
        allAttrs.put("fabAnimationMode", new String[] {"scale", "slide"});
        allAttrs.put("fabCradleMargin", VALUES_DIMEN);
        allAttrs.put("fabCradleRoundedCornerRadius", VALUES_DIMEN);
        allAttrs.put("fabCradleVerticalOffset", VALUES_DIMEN);
        allAttrs.put("hideOnScroll", null); // not yet
        allAttrs.put("paddingBottomSystemWindowInsets", VALUES_BOOLEAN);
        allAttrs.put("menu", new String[] {"@menu"});
        allAttrs.put("menuAlignmentMode", new String[] {"auto", "start"});
        allAttrs.put("navigationIconTint", VALUES_COLOR);
        allAttrs.put("fabAlignmentModeEndMargin", VALUES_DIMEN); // not yet
        allAttrs.put("itemHorizontalTranslationEnabled", VALUES_BOOLEAN);
        allAttrs.put(
                "layout_insetEdge",
                new String[] {"left", "top", "right", "bottom", "start", "end", "none"});
        allAttrs.put("layout_behavior", VALUES_STRING);
        allAttrs.put("indicatorInset", VALUES_DIMEN);
        allAttrs.put("indicatorSize", VALUES_DIMEN);
        allAttrs.put("elevation", VALUES_DIMEN);
        allAttrs.put("ensureMinTouchTargetSize", null);
        allAttrs.put("fabCustomSize", VALUES_DIMEN);
        allAttrs.put("fabSize", new String[] {"auto", "mini", "normal"});
        allAttrs.put("hideMotionSpec", null);
        allAttrs.put("hoveredFocusedTranslationZ", null);
        allAttrs.put("maxImageSize", VALUES_DIMEN);
        allAttrs.put("pressedTranslationZ", null);
        allAttrs.put("rippleColor", VALUES_COLOR);
        allAttrs.put("showMotionSpec", null);
        allAttrs.put("useCompatPadding", VALUES_BOOLEAN);
        allAttrs.put("srcCompat", VALUES_DRAWABLE);
        allAttrs.put("borderWidth", VALUES_DIMEN);
        allAttrs.put("dropDownBackgroundTint", VALUES_COLOR);
        allAttrs.put("simpleItemSelectedColor", VALUES_COLOR);
        allAttrs.put("simpleItemSelectedRippleColor", VALUES_COLOR);
        allAttrs.put("popupElevation", VALUES_DIMEN);
        allAttrs.put("popupBackground", VALUES_COLOR);
        allAttrs.put("selectionRequired", VALUES_BOOLEAN);
        allAttrs.put("singleSelection", VALUES_BOOLEAN);
        allAttrs.put("android_checkable", VALUES_BOOLEAN);
        allAttrs.put("cardForegroundColor", VALUES_COLOR);
        allAttrs.put("cardElevation", VALUES_DIMEN);
        allAttrs.put("checkedIcon", VALUES_BOOLEAN);
        allAttrs.put("checkedIconMargin", VALUES_DIMEN);
        allAttrs.put("checkedIconSize", null);
        allAttrs.put("checkedIconTint", VALUES_COLOR);
        allAttrs.put("checked", VALUES_BOOLEAN);
        allAttrs.put("buttonIcon", VALUES_DRAWABLE);
        allAttrs.put("buttonIconTint", VALUES_COLOR);
        allAttrs.put("buttonIconTintMode", VALUES_TINT_MODE);
        allAttrs.put("errorShown", VALUES_BOOLEAN);
        allAttrs.put("buttonIconTint", VALUES_COLOR);
        allAttrs.put("buttonIconTintMode", VALUES_TINT_MODE);
        allAttrs.put("errorShown", VALUES_BOOLEAN);
        allAttrs.put("dividerColor", VALUES_COLOR);
        allAttrs.put("dividerInsetEnd", null);
        allAttrs.put("dividerInsetStart", null);
        allAttrs.put("dividerThickness", null);
        allAttrs.put("buttonIconTint", VALUES_COLOR);
        allAttrs.put("buttonIconTintMode", VALUES_TINT_MODE);
        allAttrs.put("thumbIcon", VALUES_DRAWABLE);
        allAttrs.put("thumbIconSize", VALUES_DIMEN);
        allAttrs.put("thumbIconTint", VALUES_COLOR);
        allAttrs.put("thumbIconTintMode", VALUES_TINT_MODE);
        allAttrs.put("trackDecoration", null);
        allAttrs.put("trackDecorationTint", new String[] {"color"});
        allAttrs.put("trackDecorationTintMode", VALUES_TINT_MODE);
        allAttrs.put("title", VALUES_STRING);
        allAttrs.put("subTitle", VALUES_STRING);
        allAttrs.put("strokeColor", VALUES_COLOR);
        allAttrs.put("strokeWidth", VALUES_DIMEN);
        allAttrs.put("itemMinHeight", VALUES_DIMEN);
        allAttrs.put("menuGravity", null);
        allAttrs.put("itemBackground", VALUES_COLOR);
        allAttrs.put("itemHorizontalPadding", VALUES_DIMEN);
        allAttrs.put("itemIconPadding", VALUES_DIMEN);
        allAttrs.put("itemIconSize", VALUES_DIMEN);
        allAttrs.put("itemIconTint", VALUES_COLOR);
        allAttrs.put("itemIconTintMode", VALUES_TINT_MODE);
        allAttrs.put("itemMaxLines", null);
        allAttrs.put("itemTextAppearance", new String[] {"@style"});
        allAttrs.put("itemTextAppearanceActiveBoldEnabled", VALUES_BOOLEAN);
        allAttrs.put("itemTextColor", VALUES_COLOR);
        allAttrs.put("itemVerticalPadding", VALUES_DIMEN);
        allAttrs.put("minSeparation", null);
        allAttrs.put("stepSize", null);
        allAttrs.put("values", null);
        allAttrs.put("valueFrom", null);
        allAttrs.put("valueTo", null);
        allAttrs.put("haloRadius", null);
        allAttrs.put("haloColor", VALUES_STRING);
        allAttrs.put("labelBehavior", null);
        allAttrs.put("thumbColor", VALUES_COLOR);
        allAttrs.put("thumbElevation", VALUES_DIMEN);
        allAttrs.put("thumbHeight", null);
        allAttrs.put("thumbStrokeColor", VALUES_COLOR);
        allAttrs.put("thumbStrokeWidth", VALUES_DIMEN);
        allAttrs.put("thumbTrackGapSize", null);
        allAttrs.put("thumbWidth", null);
        allAttrs.put("tickColor", new String[] {"color"});
        allAttrs.put("tickColorActive", new String[] {"color"});
        allAttrs.put("tickColorInactive", new String[] {"color"});
        allAttrs.put("tickVisible", VALUES_BOOLEAN);
        allAttrs.put("trackColor", new String[] {"color"});
        allAttrs.put("trackColorActive", new String[] {"color"});
        allAttrs.put("trackColorInactive", new String[] {"color"});
        allAttrs.put("value", null);
        allAttrs.put("haloRadius", null);
        allAttrs.put("labelBehavior", null);
        allAttrs.put("tickVisible", VALUES_BOOLEAN);
        allAttrs.put("tabBackground", VALUES_DRAWABLE);
        allAttrs.put("tabContentStart", null); // not yet
        allAttrs.put("tabGravity", VALUES_GRAVITY);
        allAttrs.put("tabIndicatorAnimationMode", null); // not yet
        allAttrs.put("tabIndicatorColor", VALUES_COLOR);
        allAttrs.put("tabIndicatorFullWidth", VALUES_BOOLEAN);
        allAttrs.put("tabIndicatorGravity", VALUES_GRAVITY);
        allAttrs.put("tabIndicatorHeight", VALUES_BOOLEAN);
        allAttrs.put("tabInlineLabel", null); // not yet
        allAttrs.put("tabMaxWidth", VALUES_DIMEN); // not yet
        allAttrs.put("tabMinWidth", VALUES_DIMEN); // not yet
        allAttrs.put("tabMode", new String[] {"fixed", "scrollable"});
        allAttrs.put("tabPadding", VALUES_DIMEN);
        allAttrs.put("tabPaddingBottom", VALUES_DIMEN);
        allAttrs.put("tabPaddingEnd", VALUES_DIMEN);
        allAttrs.put("tabPaddingStart", VALUES_DIMEN);
        allAttrs.put("tabPaddingTop", VALUES_DIMEN);
        allAttrs.put("tabRippleColor", VALUES_COLOR);
        allAttrs.put("tabSelectedTextColor", VALUES_COLOR);
        allAttrs.put("tabTextColor", VALUES_COLOR);
        allAttrs.put("hint", VALUES_STRING);
        allAttrs.put("maxEms", null);
        allAttrs.put("minEms", null);
        allAttrs.put("boxStrokeErrorColor", VALUES_COLOR);
        allAttrs.put("boxStrokeWidth", VALUES_DIMEN);
        allAttrs.put("counterEnabled", null);
        allAttrs.put("counterMaxLength", null);
        allAttrs.put("counterOverflowTextAppearance", null);
        allAttrs.put("counterOverflowTextColor", VALUES_COLOR);
        allAttrs.put("counterTextColor", VALUES_COLOR);
        allAttrs.put("cursorColor", VALUES_COLOR);
        allAttrs.put("cursorErrorColor", VALUES_COLOR);
        allAttrs.put("endIconCheckable", VALUES_BOOLEAN);
        allAttrs.put("endIconContentDescription", VALUES_STRING);
        allAttrs.put("endIconDrawable", null);
        allAttrs.put("endIconMode", null);
        allAttrs.put("endIconTintMode", VALUES_TINT_MODE);
        allAttrs.put("errorContentDescription", VALUES_STRING);
        allAttrs.put("errorEnabled", VALUES_BOOLEAN);
        allAttrs.put("errorIconDrawable", VALUES_DRAWABLE);
        allAttrs.put("errorIconTint", VALUES_COLOR);
        allAttrs.put("errorIconTintMode", VALUES_TINT_MODE);
        allAttrs.put("helperTextEnabled", VALUES_BOOLEAN);
        allAttrs.put("hintEnabled", VALUES_BOOLEAN);
        allAttrs.put("hintTextColor", VALUES_COLOR);
        allAttrs.put("passwordToggleContentDescription", null);
        allAttrs.put("passwordToggleDrawable", VALUES_DRAWABLE);
        allAttrs.put("passwordToggleEnabled", VALUES_BOOLEAN);
        allAttrs.put("passwordToggleTint", VALUES_COLOR);
        allAttrs.put("passwordToggleTintMode", VALUES_TINT_MODE);
        allAttrs.put("placeholderTextColor", VALUES_COLOR);
        allAttrs.put("prefixTextColor", VALUES_COLOR);
        allAttrs.put("startIconCheckable", VALUES_BOOLEAN);
        allAttrs.put("startIconContentDescription", null);
        allAttrs.put("startIconDrawable", null);
        allAttrs.put("startIconTint", VALUES_COLOR);
        allAttrs.put("startIconTintMode", VALUES_TINT_MODE);
        allAttrs.put("suffixTextColor", VALUES_COLOR);
        allAttrs.put("textOn", VALUES_STRING);
        allAttrs.put("textOff", VALUES_STRING);
        allAttrs.put("disabledAlpha", null);
        allAttrs.put("dial", null);
        allAttrs.put("dialTint", VALUES_COLOR);
        allAttrs.put("dialTintMode", VALUES_TINT_MODE);
        allAttrs.put("hand_hour", null);
        allAttrs.put("hand_hourTint", VALUES_COLOR);
        allAttrs.put("hand_hourTintMode", VALUES_TINT_MODE);
        allAttrs.put("hand_minute", null);
        allAttrs.put("hand_minuteTint", VALUES_COLOR);
        allAttrs.put("hand_minuteTintMode", VALUES_TINT_MODE);
        allAttrs.put("firstDayOfWeek", null);
        allAttrs.put("hand_second", null);
        allAttrs.put("hand_secondTint", new String[] {"@style"});
        allAttrs.put("hand_secondTintMode", VALUES_TINT_MODE);
        allAttrs.put("timeZone", VALUES_STRING);
        allAttrs.put("dropDownHeight", VALUES_DIMEN);
        allAttrs.put("dropDownWidth", VALUES_DIMEN);
        allAttrs.put("dropDownSelector", VALUES_COLOR);
        allAttrs.put("dropDownVerticalOffset", VALUES_DIMEN);
        allAttrs.put("dropDownHorizontalOffset", VALUES_DIMEN);
        allAttrs.put("popupBackground", VALUES_COLOR);
        allAttrs.put("dateTextAppearance", new String[] {"@style"});
        allAttrs.put("firstDayOfWeek", null);
        allAttrs.put("maxDate", null);
        allAttrs.put("minDate", null);
        allAttrs.put("countDown", VALUES_BOOLEAN);
        allAttrs.put("format", null);
        allAttrs.put("columnWidth", VALUES_DIMEN);
        allAttrs.put("numColumns", null);
        allAttrs.put(
                "stretchMode",
                new String[] {"colulmWidth", "none", "spacingWidth", "spacingWidthUniform"});
        allAttrs.put("verticalSpacing", VALUES_DIMEN);
        allAttrs.put("adjustViewBounds", VALUES_BOOLEAN);
        allAttrs.put("baseline", VALUES_DIMEN);
        allAttrs.put("baselineAlignBottom", VALUES_BOOLEAN);
        allAttrs.put("cropToPadding", VALUES_BOOLEAN);
        allAttrs.put(
                "scaleType",
                new String[] {
                    "center",
                    "centerCrop",
                    "centerInside",
                    "fitCenter",
                    "fitEnd",
                    "fitStart",
                    "fitXY",
                    "matrix"
                });
        allAttrs.put("src", VALUES_DRAWABLE);
        allAttrs.put("tint", VALUES_COLOR);
        allAttrs.put(
                "hintMode",
                new String[] {"add", "multiply", "screen", "src_atop", "src_in", "src_over"});
        allAttrs.put("divider", null);
        allAttrs.put("dividerHeight", VALUES_DIMEN);
        allAttrs.put("footerDividersEnable", VALUES_BOOLEAN);
        allAttrs.put("headerDividersEnabled", VALUES_BOOLEAN);
        allAttrs.put("indeterminate", VALUES_BOOLEAN);
        allAttrs.put("indeterminateBehavior", new String[] {"cycle", "repeat"});
        allAttrs.put("indeterminateDrawable", VALUES_DRAWABLE);
        allAttrs.put("indeterminateDuration", null);
        allAttrs.put("indeterminateOnly", VALUES_BOOLEAN);
        allAttrs.put("indeterminateTint", VALUES_COLOR);
        allAttrs.put("indeterminateTintMode", VALUES_TINT_MODE);
        allAttrs.put("max", null);
        allAttrs.put("min", null);
        allAttrs.put("mirrorForRtl", VALUES_BOOLEAN);
        allAttrs.put("progress", null);
        allAttrs.put("progressBackgroundTint", VALUES_COLOR);
        allAttrs.put("progressBackgroundTintMode", VALUES_TINT_MODE);
        allAttrs.put("progressTint", VALUES_COLOR);
        allAttrs.put("progressTintMode", VALUES_TINT_MODE);
        allAttrs.put("econdaryProgress", null);
        allAttrs.put("secondaryProgressTint", VALUES_COLOR);
        allAttrs.put("secondaryProgressTintMode", VALUES_TINT_MODE);
        allAttrs.put("isIndicator", VALUES_BOOLEAN);
        allAttrs.put("numStars", null);
        allAttrs.put("rating", null);
        allAttrs.put("dropDownHeight", VALUES_DIMEN);
        allAttrs.put("dropDownWidth", VALUES_DIMEN);
        allAttrs.put("dropDownSelector", VALUES_COLOR);
        allAttrs.put("dropDownVerticalOffset", VALUES_DIMEN);
        allAttrs.put("dropDownHorizontalOffset", VALUES_DIMEN);
        allAttrs.put("spinnerMode", new String[] {"dialog", "dropdown"});
        allAttrs.put("showText", VALUES_BOOLEAN);
        allAttrs.put("textOn", VALUES_STRING);
        allAttrs.put("textOff", VALUES_STRING);
        allAttrs.put("format12Hour", null);
        allAttrs.put("format24Hour", null);
        allAttrs.put("timeZone", null);
        allAttrs.put("ems", null);
        allAttrs.put("minEms", null);
        allAttrs.put("enabled", VALUES_BOOLEAN);
        allAttrs.put("width", VALUES_DIMEN);
        allAttrs.put("height", VALUES_DIMEN);
        allAttrs.put("maxLength", null);
        allAttrs.put("maxLines", null);
        allAttrs.put("minLines", null);
        allAttrs.put("hint", VALUES_STRING);
        allAttrs.put(
                "inputType",
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
        allAttrs.put("lines", null);
        allAttrs.put("maxEms", null);
        allAttrs.put("maxHeight", VALUES_DIMEN);
        allAttrs.put("maxWidth", VALUES_DIMEN);
        allAttrs.put("minHeight", VALUES_DIMEN);
        allAttrs.put("minWidth", VALUES_DIMEN);
        allAttrs.put("shadowColor", VALUES_COLOR);
        allAttrs.put("shadowDx", null);
        allAttrs.put("shadowDy", null);
        allAttrs.put("shadowRadius", null);
        allAttrs.put("singleLine", VALUES_BOOLEAN);
        allAttrs.put("text", VALUES_STRING);
        allAttrs.put("textAppearance", new String[] {"@style"});
        allAttrs.put("textColor", VALUES_COLOR);
        allAttrs.put("textColorHighlight", VALUES_COLOR);
        allAttrs.put("textColorHint", VALUES_COLOR);
        allAttrs.put("textColorLink", VALUES_COLOR);
        allAttrs.put("textScaleX", null);
        allAttrs.put("textSize", VALUES_DIMEN);
        allAttrs.put("textStyle", new String[] {"bold", "italic", "normal"});
        allAttrs.put("typeface", new String[] {"monospace", "normal", "sans", "serif"});
    }
}
