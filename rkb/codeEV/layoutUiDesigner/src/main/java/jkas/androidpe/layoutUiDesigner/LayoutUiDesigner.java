package jkas.androidpe.layoutUiDesigner;

import static jkas.androidpe.layoutUiDesigner.utils.Utils.drawDashPath;
import static jkas.androidpe.layoutUiDesigner.utils.Utils.drawDashPathStroke;
import static jkas.androidpe.layoutUiDesigner.utils.Utils.drawDashPathStrokeSelected;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Looper;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.widget.CodeEditor;
import jkas.androidpe.layoutUiDesigner.databinding.LayoutLayoutUiDesignerBinding;
import jkas.androidpe.layoutUiDesigner.dialog.DialogBottomSheetAttrSetter;
import jkas.androidpe.layoutUiDesigner.drag.DragListener;
import jkas.androidpe.layoutUiDesigner.drawers.DrawerInfo;
import jkas.androidpe.layoutUiDesigner.drawers.DrawerPalettes;
import jkas.androidpe.layoutUiDesigner.palette.MainView;
import jkas.androidpe.layoutUiDesigner.tools.AndroidXmlParser;
import jkas.androidpe.layoutUiDesigner.tools.RefViewElement;
import jkas.androidpe.layoutUiDesigner.tools.ViewCreator;
import jkas.androidpe.layoutUiDesigner.utils.DragAndDropUtils;
import jkas.androidpe.layoutUiDesigner.utils.Utils;
import jkas.androidpe.layoutUiDesigner.utils.Utils.CurrentSettings;
import jkas.androidpe.logger.LoggerLayoutUI;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.dialog.DialogProgressIndeterminate;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resourcesUtils.utils.ResFormatter;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.androidpe.resourcesUtils.utils.ViewUtils;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;

import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class LayoutUiDesigner {
    private final String TAG;

    private DialogBottomSheetAttrSetter dialogAttSetter;
    private LoggerLayoutUI debug;
    private OnEventListener listener;
    private Context C;
    private LayoutLayoutUiDesignerBinding binding;
    private AndroidXmlParser androidXmlParser;
    private DrawerPalettes drawerPalettes;
    private DrawerInfo drawerInfo;
    private String CODE = "";

    private boolean reloadViewNeeded = false;
    private boolean firstLoad = false;
    private boolean fullscreen = false;
    private boolean viewEnable = false;
    private boolean enteredDelete = false;
    private boolean bClick = true;
    private float startX = 0f;
    private float startY = 0f;
    private float endX = 0f;
    private float endY = 0f;
    private float diffX = 0f;
    private float diffY = 0f;

    public LayoutUiDesigner(Context c) {
        this.C = c;

        TAG = Utils.createTagFromFileName("LayoutUiDesigner");
        LoggerLayoutUI.addCach(TAG);
        init();
        defaultSettings();
        eventsBase();
        listeners();
        setDefaultMainView();
    }

    public void rotateView() {
        reloadViewNeeded = true;
        binding = LayoutLayoutUiDesignerBinding.inflate(LayoutInflater.from(C));
        defaultSettings();
        eventsBase();
        listeners();
        setDefaultMainView();
    }

    public void parseCode(String code) {
        CODE = code;
        refreshData();
    }

    private void parseCode() {
        drawerInfo.clearInfo();
        androidXmlParser.parseXmlCode(CODE);
        binding.tvInfo.setText("...");
        binding.imgDelete.setVisibility(View.GONE);
        reloadViewNeeded = false;
    }

    private void setDefaultMainView() {
        androidXmlParser.with(binding.linContainer);
        drawerInfo.showTreeView(androidXmlParser.getTreeView());
    }

    private void listeners() {
        androidXmlParser.setTaskListener(
                new AndroidXmlParser.OnTaskListener() {

                    @Override
                    public String tagNeeded() {
                        return TAG;
                    }

                    @Override
                    public void onStart() {
                        debug.i("AndroidXmlParser", "View creation in progress");
                    }

                    @Override
                    public void onFinish() {
                        debug.i("AndroidXmlParser", "View construction completed.");
                    }

                    @Override
                    public void onViewAdded(View v, boolean root) {
                        setViewEvent(v, root);
                    }
                });

        drawerPalettes.setOnTaskRequested(
                new DrawerPalettes.OnAnyTaskRequested() {

                    @Override
                    public XmlManager onXmlFileNeeded() {
                        return androidXmlParser.getXmlManager();
                    }

                    @Override
                    public void onNewViewAppendByAdd(boolean success) {
                        if (!success) return;
                        save();
                        refreshData();
                    }
                });

        dialogAttSetter.setOnTaskRequested(
                new DialogBottomSheetAttrSetter.OnAnyTaskRequested() {

                    @Override
                    public boolean onAdd(Element e, int addType) {
                        View v = ViewCreator.create(TAG, C, e.getNodeName(), false);
                        if (addType == DialogBottomSheetAttrSetter.ADD_INSIDE) {
                            if (!(v instanceof ViewGroup)) {
                                DialogBuilder.getDialogBuilder(
                                                C,
                                                C.getString(R.string.warning),
                                                C.getString(R.string.warning_add_view_constraints))
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                                return false;
                            }
                        }
                        if (addType == DialogBottomSheetAttrSetter.ADD_BEFORE) {
                            if (e.getParentNode() == null) {
                                Toast.makeText(
                                                C,
                                                "There is no way to add before Root",
                                                Toast.LENGTH_SHORT)
                                        .show();
                                return false;
                            }
                        }

                        drawerPalettes.setAddRequested(e, addType);
                        binding.drawerLayout.closeDrawer(binding.rightDrawer);
                        binding.drawerLayout.openDrawer(binding.leftDrawer);

                        return true;
                    }

                    @Override
                    public void onEdit(View v, Element e) {
                        androidXmlParser.setAttr(v, e);
                    }

                    @Override
                    public void onSave() {
                        save();
                    }

                    @Override
                    public void onRefresh() {
                        save();
                        refreshData();
                    }

                    @Override
                    public RefViewElement onRefViewElementNeeded() {
                        return androidXmlParser.getRefViewElement();
                    }

                    @Override
                    public AndroidXmlParser onAndroidXmlParser() {
                        return androidXmlParser;
                    }
                });

        binding.imgDelete.setOnClickListener(
                v -> {
                    refreshData();
                    binding.imgDelete.setVisibility(View.GONE);
                });

        binding.imgDelete.setOnDragListener(
                (v, e) -> {
                    switch (e.getAction()) {
                        case DragEvent.ACTION_DRAG_ENTERED:
                            updateDeleteEvent(true);
                            enteredDelete = true;
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            updateDeleteEvent(false);
                            break;

                        case DragEvent.ACTION_DROP:
                            try {
                                if (!CurrentSettings.addByDrag && enteredDelete) {
                                    View view = (View) e.getLocalState();
                                    Element element =
                                            androidXmlParser
                                                    .getRefViewElement()
                                                    .getListRef()
                                                    .get(view);
                                    element.getParentNode().removeChild(element);
                                    save();
                                    refreshAllData();
                                    binding.imgDelete.setVisibility(View.GONE);
                                    Toast.makeText(C, R.string.deleted, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception err) {
                                Toast.makeText(
                                                C,
                                                "Drop Delete : " + err.getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                            binding.imgDelete.setVisibility(View.GONE);
                            enteredDelete = false;
                    }
                    return true;
                });
    }

    private void setViewEvent(final View view, final boolean root) {
        if (!CurrentSettings.isDrawStrokeEnabled) return;

        if (view instanceof ViewGroup && !(view instanceof AdapterView))
            view.setOnDragListener(getDragListener());
        else {
            String n = view.getClass().getName();
            final String name = n.substring(n.lastIndexOf(".") + 1);
            if (name.contains("EditText") || name.contains("AutoComp"))
                view.setOnDragListener(
                        (v, e) -> {
                            switch (e.getAction()) {
                                case DragEvent.ACTION_DRAG_ENTERED:
                                    binding.tvInfo.setText(
                                            name
                                                    + " Does not support (can cause views to be reloaded)");
                                    break;

                                case DragEvent.ACTION_DROP:
                                    refreshData();
                                    break;
                            }
                            return true;
                        });
        }

        final GestureDetector gestureDetector =
                new GestureDetector(
                        C,
                        new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public void onLongPress(MotionEvent event) {
                                if (bClick) {
                                    CurrentSettings.addByDrag = false;
                                    if (!root) DragAndDropUtils.startDragAndDrop(view, null, view);
                                }
                            }
                        });

        view.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_MOVE:
                                endX = event.getX();
                                endY = event.getY();

                                diffX = Math.abs(startX - endX);
                                diffY = Math.abs(startY - endY);
                                if (diffX > 7.6 || diffY > 7.6) {
                                    drawDashPathStroke(v);
                                    bClick = false;

                                    if (v.getParent() != null) {
                                        View vp = (View) v.getParent();
                                        if (!(vp instanceof MainView)) drawDashPathStroke(vp);
                                    }
                                }
                        }

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                drawDashPathStrokeSelected(v);
                                startX = event.getX();
                                startY = event.getY();
                                bClick = true;
                                if (!root) gestureDetector.onTouchEvent(event);
                                return true;

                            case MotionEvent.ACTION_UP:
                                drawDashPathStroke(v);
                                endX = event.getX();
                                endY = event.getY();
                                diffX = Math.abs(startX - endX);
                                diffY = Math.abs(startY - endY);
                                if ((diffX <= 7.6 || diffY <= 7.6) && bClick) onViewClicked(v);
                                bClick = false;
                                break;

                            case MotionEvent.ACTION_CANCEL:
                                drawDashPathStroke(v);
                                bClick = false;
                        }
                        return false;
                    }
                });
    }

    private DragListener getDragListener() {
        final DragListener listener = new DragListener();
        listener.setOnEventListener(
                new DragListener.OnEventListener() {
                    @Override
                    public void onDragStarted() {
                        binding.imgDelete.setVisibility(View.VISIBLE);
                        updateDeleteEvent(false);
                    }

                    @Override
                    public void onDragFinish() {
                        binding.imgDelete.setVisibility(View.GONE);
                        save();
                        androidXmlParser.updateTreeView();
                    }

                    @Override
                    public AndroidXmlParser onAndroidXmlParserNeeded() {
                        return androidXmlParser;
                    }

                    @Override
                    public void onAddView(View view) {
                        setViewEvent(view, false);
                        save();
                        binding.imgDelete.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDragError() {
                        refreshData();
                        binding.imgDelete.setVisibility(View.GONE);
                        androidXmlParser.updateTreeView();
                    }

                    @Override
                    public void reloadViews() {
                        refreshAllData();
                        binding.imgDelete.setVisibility(View.GONE);
                        androidXmlParser.updateTreeView();
                    }

                    @Override
                    public void info(String infoToPrint) {
                        if (infoToPrint == null) {
                            binding.tvInfo.setText("...");
                        } else binding.tvInfo.setText(infoToPrint);
                    }
                });
        return listener;
    }

    private void updateDeleteEvent(boolean entered) {
        if (CurrentSettings.addByDrag) {
            binding.imgDelete.setImageResource(R.drawable.ic_cancel);
            if (entered) {
                ViewUtils.setBgCornerRadius(binding.imgDelete, C.getColor(R.color.warning));
                binding.imgDelete.setImageTintList(
                        ColorStateList.valueOf(ResourcesValuesFixer.getColor(C, "?colorSurface")));
            } else {
                ViewUtils.setBgCornerRadius(
                        binding.imgDelete, ResourcesValuesFixer.getColor(C, "?colorOnSurface"));
                binding.imgDelete.setImageTintList(
                        ColorStateList.valueOf(ResourcesValuesFixer.getColor(C, "?colorSurface")));
            }
        } else {
            binding.imgDelete.setImageResource(R.drawable.ic_delete);
            if (entered) {
                ViewUtils.setBgCornerRadius(binding.imgDelete, C.getColor(R.color.error));
                binding.imgDelete.setImageTintList(
                        ColorStateList.valueOf(
                                ResourcesValuesFixer.getColor(C, "?colorOnSurface")));
            } else {
                ViewUtils.setBgCornerRadius(
                        binding.imgDelete, ResourcesValuesFixer.getColor(C, "?colorOnSurface"));
                binding.imgDelete.setImageTintList(
                        ColorStateList.valueOf(C.getColor(R.color.error)));
            }
        }
    }

    private void onViewClicked(final View v) {
        Element e = androidXmlParser.getRefViewElement().getElement(v);
        if (e.getNodeName().equals("include"))
            Toast.makeText(C, "Not yet supported ", Toast.LENGTH_SHORT).show();
        else if (e != null) dialogAttSetter.show(e);
        else Toast.makeText(C, "Unknown view", Toast.LENGTH_SHORT).show();
    }

    private void eventsBase() {
        binding.drawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {

                    @Override
                    public void onDrawerSlide(View arg0, float arg1) {}

                    @Override
                    public void onDrawerOpened(View v) {
                        if (v == binding.leftDrawer)
                            binding.imgAllViews.setImageTintList(
                                    ColorStateList.valueOf(
                                            ResCodeUtils.getColorFromResolveAttribute(
                                                    C, android.R.attr.colorPrimary)));
                        else if (v == binding.rightDrawer)
                            binding.imgFileTree.setImageTintList(
                                    ColorStateList.valueOf(
                                            ResCodeUtils.getColorFromResolveAttribute(
                                                    C, android.R.attr.colorPrimary)));
                    }

                    @Override
                    public void onDrawerClosed(View v) {
                        if (v == binding.leftDrawer) {
                            binding.imgAllViews.setImageTintList(null);
                            drawerPalettes.setAddRequested(null, -1);
                        } else if (v == binding.rightDrawer) {
                            binding.imgFileTree.setImageTintList(null);
                        }
                    }

                    @Override
                    public void onDrawerStateChanged(int arg0) {}
                });

        binding.imgAllViews.setOnClickListener(
                v -> {
                    if (binding.drawerLayout.isDrawerOpen(binding.leftDrawer)) {
                        binding.drawerLayout.closeDrawer(binding.leftDrawer);
                    } else {
                        binding.drawerLayout.closeDrawers();
                        binding.drawerLayout.openDrawer(binding.leftDrawer);
                    }
                });

        binding.imgFileTree.setOnClickListener(
                v2 -> {
                    if (binding.drawerLayout.isDrawerOpen(binding.rightDrawer)) {
                        binding.drawerLayout.closeDrawer(binding.rightDrawer);
                    } else {
                        binding.drawerLayout.closeDrawers();
                        binding.drawerLayout.openDrawer(binding.rightDrawer);
                    }
                    drawerInfo.showTree();
                });

        binding.imgInfo.setOnClickListener(
                v3 -> {
                    if (binding.drawerLayout.isDrawerOpen(binding.leftDrawer)) {
                        binding.drawerLayout.closeDrawer(binding.leftDrawer);
                    }
                    if (!binding.drawerLayout.isDrawerOpen(binding.rightDrawer)) {
                        binding.drawerLayout.openDrawer(binding.rightDrawer);
                    }

                    drawerInfo.showInfo();
                });

        binding.imgLayers.setOnClickListener(
                v4 -> {
                    PopupMenu menu = new PopupMenu(C, v4);
                    menu.inflate(jkas.androidpe.layoutUiDesigner.R.menu.layout_display_type);
                    menu.setForceShowIcon(true);
                    menu.setOnMenuItemClickListener(
                            m -> {
                                if (m.getItemId()
                                        == jkas.androidpe.layoutUiDesigner.R.id.menu_design) {
                                    CurrentSettings.isDrawStrokeEnabled = false;
                                    refreshData();
                                } else if (m.getItemId()
                                        == jkas.androidpe.layoutUiDesigner.R.id.menu_edit) {
                                    CurrentSettings.isDrawStrokeEnabled = true;
                                    refreshData();
                                }
                                return true;
                            });
                    menu.show();
                });

        binding.imgFull.setOnClickListener(
                v -> {
                    fullscreen = !fullscreen;
                    if (fullscreen)
                        binding.imgFull.setImageResource(
                                jkas.androidpe.resources.R.drawable.ic_fullscreen_exit);
                    else
                        binding.imgFull.setImageResource(
                                jkas.androidpe.resources.R.drawable.ic_fullscreen);
                    listener.onFullScreenNeeded(fullscreen);

                    binding.drawerLayout.setDrawerLockMode(
                            fullscreen
                                    ? DrawerLayout.LOCK_MODE_UNDEFINED
                                    : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                });

        binding.imgRefresh.setOnClickListener(v -> refreshData());
    }

    private void defaultSettings() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerPalettes = new DrawerPalettes(C, binding.linContainer, binding.drawerLayout);
        drawerPalettes.showDefault();
        drawerInfo = new DrawerInfo(C, binding.linContainer, binding.drawerLayout, TAG);
        binding.leftDrawer.addView(drawerPalettes.getView());
        binding.rightDrawer.addView(drawerInfo.getView());
        updateDeleteEvent(false);
    }

    private void init() {
        binding = LayoutLayoutUiDesignerBinding.inflate(LayoutInflater.from(C));
        dialogAttSetter = new DialogBottomSheetAttrSetter(C);
        androidXmlParser = new AndroidXmlParser(C, dialogAttSetter);
        debug = LoggerLayoutUI.get(TAG);
    }

    public void refreshData() {
        if (!viewEnable) return;

        final AlertDialog progress = DialogProgressIndeterminate.getAlertDialog(C);
        progress.show();
        new Handler(Looper.getMainLooper())
                .postDelayed(
                        () -> {
                            refreshAllData();
                            progress.cancel();
                        },
                        100);
    }

    private void refreshAllData() {
        parseCode();
        binding.imgDelete.setVisibility(View.GONE);
    }

    public View getView() {
        viewEnable = true;
        return (View) binding.getRoot();
    }

    private void save() {
        if (!androidXmlParser.dontTryToLoadView && androidXmlParser.getXmlManager().isInitialized) {
            androidXmlParser.getXmlManager().saveAllModif();
            CODE = androidXmlParser.getXmlManager().code;
            listener.onCodeChanged(CODE);
        }
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        public void onFullScreenNeeded(boolean setFullScreen);

        public void onCodeChanged(String code);
    }
}
