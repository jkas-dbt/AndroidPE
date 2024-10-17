package jkas.androidpe.mainEditorView;

import android.view.View;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;
import jkas.androidpe.codeEditor.CodeEditorView;
import jkas.androidpe.layoutUiDesigner.LayoutUiDesigner;
import jkas.androidpe.menuItemDesigner.MenuItemDesigner;
import jkas.androidpe.projectUtils.utils.ValuesTools;
import jkas.androidpe.resValuesModifier.ResValuesModifier;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;

/**
 * This contains 4 child views. these children are distributed according to the type of file passed
 * as a parameter. If the file passed as a parameter corresponds to at least two types of views,
 * then the user can also modify his code or text by assistance depending on the selected view.
 */

/**
 * @author by JKas
 */
public class MainEditorView {
    private static final int VIEW_CODE_EDITOR = 0;
    private static final int VIEW_CODE_ASSIST = 1;

    public static final int CAUSE_TEXT_CHANED = 0;
    public static final int CAUSE_FULL_SCREEN_NEEDED = 1;
    public static final int CAUSE_FULL_SCREEN_EXIT_NEEDED = 2;

    private OnAnyChangedListener listener;
    private AppCompatActivity C;
    private ViewFlipper viewFlipper;
    private String path;

    private CodeEditorView CEV;
    private MenuItemDesigner MID;
    private LayoutUiDesigner LUD;
    private ResValuesModifier RVM;

    private int positionView = VIEW_CODE_EDITOR;
    private int codeType = -1;
    private boolean assitLoaded = false;

    public MainEditorView(AppCompatActivity C, String path) {
        this.C = C;
        init();
        refreshWithNewPath(path);
    }

    public void refreshWithNewPath(String path) {
        this.path = path;
        if (path.endsWith(".java") || path.endsWith(".js"))
            codeType = CodeEditorView.JAVA_CODE_TYPE;
        else if (path.endsWith(".json")) codeType = CodeEditorView.JSON_CODE_TYPE;
        else if (path.endsWith(".py")) codeType = CodeEditorView.PYTHON_CODE_TYPE;
        else if (path.endsWith(".xml")) codeType = CodeEditorView.XML_CODE_TYPE;
        else if (path.endsWith(".html") || path.endsWith(".htm"))
            codeType = CodeEditorView.HTML_CODE_TYPE;
        else if (path.endsWith(".kt") || path.endsWith(".kts"))
            codeType = CodeEditorView.KOTLIN_CODE_TYPE;
        else if (path.endsWith(".gradle")) codeType = CodeEditorView.GROOVY_CODE_TYPE;
        loadCodeEditor();
    }

    private void loadCodeEditor() {
        CEV = new CodeEditorView(C, Files.readFile(path), codeType);
        CEV.setOnEventListener(() -> listener.onChanged(CAUSE_TEXT_CHANED));
        viewFlipper.removeAllViews();
        viewFlipper.addView(CEV.getView());
        viewFlipper.setDisplayedChild(0);
    }

    public void reload() {
        updateData();
    }

    public void reloadCode() {
        CEV.parseCode(Files.readFile(path));
        reload();
    }

    public void saveContent() {
        CEV.saveContent(path);
    }

    public void restoreContent() {
        if (CEV.loadContent(path)) {
            if (!CEV.getCode().equals(Files.readFile(path))) {
                listener.onChanged(CAUSE_TEXT_CHANED);
            }
        }
    }

    public void refreshDesign() {
        if (LUD != null) LUD.refreshData();
    }

    public boolean isDesignEnabled() {
        return positionView == VIEW_CODE_ASSIST ? true : false;
    }

    public boolean canUndo() {
        return CEV.canUndo();
    }

    public boolean canRedo() {
        return CEV.canRedo();
    }

    public String getTextContent() {
        return CEV.getCode();
    }

    public void undo() {
        if (canUndo()) {
            CEV.undo();
            updateData();
        }
    }

    public void redo() {
        if (canRedo()) {
            CEV.redo();
            updateData();
        }
    }

    private void listener() {
        if (LUD != null)
            LUD.setOnEventListener(
                    new LayoutUiDesigner.OnEventListener() {

                        @Override
                        public void onFullScreenNeeded(boolean setFullScreen) {
                            listener.onChanged(
                                    setFullScreen
                                            ? CAUSE_FULL_SCREEN_NEEDED
                                            : CAUSE_FULL_SCREEN_EXIT_NEEDED);
                        }

                        @Override
                        public void onCodeChanged(String code) {
                            CEV.parseCode(code);
                        }
                    });

        if (RVM != null) RVM.setOnEventListener(code -> CEV.parseCode(code));
        if (MID != null) MID.setOnEventListener(code -> CEV.parseCode(code));
    }

    public boolean isThereUnsavedContent() {
        if (positionView == VIEW_CODE_ASSIST) return false;
        return (!Files.readFile(path).equals(CEV.getCode()));
    }

    public void flipToAnotherView() {
        if (!assitLoaded) {
            prepareData();
            appendChild();
            listener();
        }
        if (viewFlipper.getChildCount() == 1) return;
        if (positionView == VIEW_CODE_EDITOR) positionView = VIEW_CODE_ASSIST;
        else if (positionView == VIEW_CODE_ASSIST) positionView = VIEW_CODE_EDITOR;
        viewFlipper.setDisplayedChild(positionView);
        updateData();
    }

    public void updateData() {
        if (positionView == VIEW_CODE_ASSIST) {
            if (MID != null) MID.parseCode(CEV.getCode());
            else if (LUD != null) LUD.parseCode(CEV.getCode());
            else if (RVM != null) RVM.parseCode(CEV.getCode());
        }
    }

    public void save() {
        Files.writeFile(path, CEV.getCode());
    }

    private void prepareData() {
        if (!ValuesTools.PathController.isResFile(path)) return;
        String parent = Files.getParentNameFromAbsolutePath(path);
        if (parent.startsWith("menu")) MID = new MenuItemDesigner(C);
        else if (parent.startsWith("layout")) LUD = new LayoutUiDesigner(C);
        else if (parent.startsWith("values")) RVM = new ResValuesModifier(C);
    }

    private void appendChild() {
        if (MID != null) viewFlipper.addView(MID.getView());
        else if (LUD != null) viewFlipper.addView(LUD.getView());
        else if (RVM != null) viewFlipper.addView(RVM.getView());
        assitLoaded = viewFlipper.getChildCount() > 1;
    }

    public String getPath() {
        return path;
    }

    public String getTextForTab() {
        return Files.getNameFromAbsolutePath(path);
    }

    private void init() {
        viewFlipper = new ViewFlipper(C);
        viewFlipper.setLayoutParams(CodeUtil.getLayoutParamsMM(0));
    }

    public View getView() {
        return (View) viewFlipper;
    }

    public void setOnAnyChangedListener(OnAnyChangedListener listener) {
        this.listener = listener;
    }

    public interface OnAnyChangedListener {
        public void onChanged(int cause);
    }
}
