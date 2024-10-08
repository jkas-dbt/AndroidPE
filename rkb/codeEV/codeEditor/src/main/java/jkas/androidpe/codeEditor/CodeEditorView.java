package jkas.androidpe.codeEditor;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.elevation.SurfaceColors;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.text.Content;
import jkas.androidpe.codeEditor.databinding.LayoutCodeEditorBinding;
import jkas.androidpe.codeEditor.schemes.EditorColorSchemeSetter;
import jkas.androidpe.codeEditor.viewModels.ContentViewModel;
import jkas.androidpe.projectUtils.dataCreator.FilesRef;
import jkas.androidpe.projectUtils.utils.ValuesTools;
import jkas.androidpe.resourcesUtils.utils.ResFormatter;
import jkas.codeUtil.Files;

/**
 * @author Jkas
 */
public class CodeEditorView {
    public static final int XML_CODE_TYPE = 0;
    public static final int HTML_CODE_TYPE = 1;
    public static final int JAVA_CODE_TYPE = 2;
    public static final int KOTLIN_CODE_TYPE = 3;
    public static final int PYTHON_CODE_TYPE = 4;
    public static final int JSON_CODE_TYPE = 5;
    public static final int GROOVY_CODE_TYPE = 6;

    private String textContent = "";
    private OnEventListener listener;
    private Context C;
    private LayoutCodeEditorBinding binding;
    private int codeType;
    private boolean isXmlFile = false;

    public CodeEditorView(Context c, String text, int codeType) {
        this.C = c;
        this.codeType = codeType;
        this.textContent = text;
        this.isXmlFile = (codeType == XML_CODE_TYPE);
        init();
        formatCode();
    }

    private void formatCode() {
        if (!isXmlFile) return;
        String formattedCode = ResFormatter.formatXmlCode(textContent);
        if (!textContent.equals(formattedCode)) {
            int linesCount = binding.codeEditor.getText().getLineCount() - 1;
            binding.codeEditor
                    .getText()
                    .replace(
                            0,
                            0,
                            linesCount,
                            binding.codeEditor.getText().getColumnCount(linesCount),
                            formattedCode);
        }
    }

    public void saveContent(String path) {
        ContentViewModel.getInstance().addData(path, binding.codeEditor.getText());
    }

    public boolean loadContent(String path) {
        final Content content = ContentViewModel.getInstance().getData(path);
        if (content != null) {
            binding.codeEditor.setText(content);
            textContent = content.toString();
        }
        return (content != null);
    }

    public boolean canUndo() {
        return binding.codeEditor.canUndo();
    }

    public boolean canRedo() {
        return binding.codeEditor.canRedo();
    }

    public void undo() {
        if (canUndo()) binding.codeEditor.undo();
    }

    public void redo() {
        if (canRedo()) binding.codeEditor.redo();
    }

    public View getView() {
        return (View) binding.getRoot();
    }

    private void init() {
        binding = LayoutCodeEditorBinding.inflate(LayoutInflater.from(C));
        setTextContent();
        setSymbolInputView();
        events();
        theme();
    }

    public void parseCode(String code) {
        if (isXmlFile) code = ResFormatter.formatXmlCode(code);
        if (!code.equals(textContent)) {
            int linesCount = binding.codeEditor.getText().getLineCount() - 1;
            binding.codeEditor
                    .getText()
                    .replace(
                            0,
                            0,
                            linesCount,
                            binding.codeEditor.getText().getColumnCount(linesCount),
                            code);
        }
    }

    private void events() {
        binding.codeEditor.subscribeEvent(
                SelectionChangeEvent.class,
                (event, unsubscribe) -> {
                    if (event.getCause() == SelectionChangeEvent.CAUSE_TEXT_MODIFICATION) {
                        textContent = event.getEditor().getText().toString();
                        if (listener != null) listener.onTextChanged();
                    }
                });
    }

    private void theme() {
        new EditorColorSchemeSetter(C, binding.codeEditor, codeType);
    }

    private void setTextContent() {
        binding.codeEditor.setText(textContent);
        binding.codeEditor.setTextSize(12);
        binding.codeEditor.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        binding.codeEditor.setTypefaceText(
                Typeface.createFromAsset(C.getAssets(), FilesRef.Fonts.PATHjetbrains_mono_ttf));
    }

    private void setSymbolInputView() {
        binding.symbolInput.setBackgroundColor(SurfaceColors.SURFACE_0.getColor(C));
        binding.symbolInput.bindEditor(binding.codeEditor);
        String[] symbol = null, symbolReplaced = null;
        if (isXmlFile) {
            symbol = "tab,<,>,/,=,\",:,@,+,(,),;,.,?,|,&,[,],{,},_,-".split(",");
            symbolReplaced = "\t,<,>,/,=,\",:,@,+,(,),;,.,?,|,&,[,],{,},_,-".split(",");
        } else {
            symbol = "tab,{,},(,),;,=,\",|,&,!,[,],<,>,+,-,/,*,?,:,_".split(",");
            symbolReplaced = "\t,{,},(,),;,=,\",|,&,!,[,],<,>,+,-,/,*,?,:,_".split(",");
        }
        binding.symbolInput.addSymbols(symbol, symbolReplaced);
    }

    public String getCode() {
        textContent = binding.codeEditor.getText().toString();
        return textContent;
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        public void onTextChanged();
    }
}
