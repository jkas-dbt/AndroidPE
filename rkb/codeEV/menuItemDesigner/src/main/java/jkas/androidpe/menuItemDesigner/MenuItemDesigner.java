package jkas.androidpe.menuItemDesigner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import androidx.appcompat.view.menu.MenuBuilder;
import com.google.android.material.elevation.SurfaceColors;
import jkas.androidpe.menuItemDesigner.databinding.LayoutMenuItemDesignerBinding;
import jkas.androidpe.projectUtils.current.Environment;
import jkas.androidpe.resourcesUtils.dataBuilder.MenuItemCreator;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;
import android.widget.LinearLayout;
import jkas.androidpe.menuItemDesigner.load.DataLoader;
import jkas.codeUtil.CodeUtil;

/**
 * @author JKas
 */
public class MenuItemDesigner {
    private String TAG;
    private OnEventListener listener;
    private static final int INIT_EDITOR = 0;
    private static final int INIT_TEMPLATE = 1;

    private Context C;
    private LayoutMenuItemDesignerBinding binding;
    private boolean viewEnable = false;
    private MainEditor mainEditor;
    private String CODE, tmpPath;

    public MenuItemDesigner(Context c) {
        this.C = c;
        TAG = "resValue" + View.generateViewId();
        tmpPath = Environment.DEFAULT_ANDROIDPE_TMP_DATA + "/" + TAG + "/xmlFile.xml";
        init();
        events();
    }

    private void events() {
        binding.btnEditor.setOnClickListener(vE -> initDataView(INIT_EDITOR));
        binding.btnTemplate.setOnClickListener(vT -> initDataView(INIT_TEMPLATE));
        binding.btnTest.setOnClickListener(
                vTest -> {
                    MenuItemCreator.createPopupMenu(C, vTest, tmpPath).show();
                });
    }

    private void initDataView(final int initType) {
        if (!mainEditor.getXmlManager().isInitialized) return;
        if (initType == INIT_TEMPLATE) {
            binding.toggleBtn.check(R.id.btnTemplate);
            binding.viewFlipper.setDisplayedChild(1);
            loadTemplate();
        } else if (initType == INIT_EDITOR) {
            binding.toggleBtn.check(R.id.btnEditor);
            binding.viewFlipper.setDisplayedChild(0);
            loadEditor();
        }
    }

    private void loadEditor() {
        mainEditor.refresh();
    }

    private void loadTemplate() {
        try {
            MenuItemCreator.buildMenu(C, binding.toolbar.getMenu(), tmpPath);
            MenuItemCreator.buildMenu(C, binding.bottomNav.getMenu(), tmpPath);
        } catch (Exception e) {
            // perhaps a child was detected in an item available in the code.
            // and it's related to BottomNavigation which doesn't support this generally.
        }
    }

    private void toolBarTop(Menu menu) {
        MenuBuilder menuBuilder = new MenuBuilder(C);
    }

    private void defaultValues() {
        if (!mainEditor.getXmlManager().isInitialized) {
            binding.viewFlipper.setDisplayedChild(2);
            binding.toggleBtn.clearChecked();
            binding.btnEditor.setEnabled(false);
            binding.btnTemplate.setEnabled(false);
            return;
        } else binding.toggleBtn.check(R.id.btnEditor);
        binding.btnEditor.setEnabled(true);
        binding.btnTemplate.setEnabled(true);

        binding.linTreeView.removeAllViews();
        binding.linTreeView.addView(mainEditor.getView());

        initDataView(INIT_EDITOR);
    }

    public void parseCode(String code) {
        CODE = code;
        Files.writeFile(tmpPath, CODE);
        if (!viewEnable) return;
        mainEditor = new MainEditor(C, tmpPath);
        mainEditor.setOnEventListener(cd -> listener.onTextChanged(cd));
        defaultValues();
    }

    private void init() {
        binding = LayoutMenuItemDesignerBinding.inflate(LayoutInflater.from(C));
        binding.toolbar.setBackgroundColor(SurfaceColors.SURFACE_1.getColor(C));
        binding.bottomNav.setBackgroundColor(SurfaceColors.SURFACE_1.getColor(C));
    }

    public View getView() {
        viewEnable = true;
        return (View) binding.getRoot();
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        public void onTextChanged(String code);
    }

    public static class MainEditor {
        private OnEventListener listener;
        private static MainEditor INSTANCE;
        public Context C;
        private XmlManager itemFile;
        public Element currentElement;
        public LinearLayout parent;
        public String path;
        private View view;

        public static MainEditor getInstance() {
            return INSTANCE;
        }

        public MainEditor(Context C, String path) {
            this.C = C;
            this.path = path;
            itemFile = new XmlManager(C);
            itemFile.initializeFromPath(path);
            init();
            refresh();
            INSTANCE = this;
        }

        private void loadData() {
            DataLoader dl = new DataLoader(C, itemFile);
            parent.addView(dl.getRootView());
        }

        public void refresh() {
            parent.removeAllViews();
            loadData();
        }

        public void init() {
            parent = new LinearLayout(C);
            parent.setLayoutParams(CodeUtil.getLayoutParamsMM(0));
            view = (View) parent;
        }

        public XmlManager getXmlManager() {
            return itemFile;
        }

        public View getView() {
            return view;
        }

        public void save() {
            itemFile.saveAllModif();
            if (listener != null) listener.onTextChanged(Files.readFile(path));
        }

        public void setOnEventListener(OnEventListener listener) {
            this.listener = listener;
        }

        public interface OnEventListener {
            public void onTextChanged(String code);
        }
    }
}
