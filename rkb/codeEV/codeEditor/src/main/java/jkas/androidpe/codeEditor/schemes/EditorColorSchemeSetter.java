package jkas.androidpe.codeEditor.schemes;

import android.content.Context;
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import java.io.InputStreamReader;
import jkas.androidpe.codeEditor.CodeEditorView;
import jkas.codeUtil.CodeUtil;
import org.eclipse.tm4e.core.registry.IGrammarSource;
import org.eclipse.tm4e.core.registry.IThemeSource;

/**
 * @author Jkas
 */
public class EditorColorSchemeSetter {
    private Context C;
    private CodeEditor codeEditor;
    private String lang;
    private String config = "textmate/$LANG$/language-configuration.json";
    private String tmLang = "textmate/$LANG$/syntaxes/$LANG$.tmLanguage.json";
    private int codeType = -1;

    public EditorColorSchemeSetter(Context C, CodeEditor codeEditor, int codeType) {
        this.C = C;
        this.codeEditor = codeEditor;
        this.codeType = codeType;
        setColorScheme();
    }

    private void setColorScheme() {
        codeEditor.setColorScheme(
                CodeUtil.isTheSystemThemeDark(C)
                        ? new EditorColorSchemeDark()
                        : new EditorColorSchemeLight());
        if (codeType == CodeEditorView.JAVA_CODE_TYPE) lang = "java";
        else if (codeType == CodeEditorView.JSON_CODE_TYPE) lang = "json";
        else if (codeType == CodeEditorView.PYTHON_CODE_TYPE) lang = "python";
        else if (codeType == CodeEditorView.XML_CODE_TYPE) lang = "xml";
        else if (codeType == CodeEditorView.HTML_CODE_TYPE) lang = "html";
        else if (codeType == CodeEditorView.KOTLIN_CODE_TYPE) {
            lang = "kotlin";
            tmLang = tmLang.replace(".json", "");
        } else if (codeType == CodeEditorView.GROOVY_CODE_TYPE) {
            lang = "groovy";
            tmLang = tmLang.replace(".json", "");
        } else return;
        config = config.replace("$LANG$", lang);
        tmLang = tmLang.replace("$LANG$", lang);

        try {
            String colorTheme =
                    CodeUtil.isTheSystemThemeDark(C)
                            ? "textmate/darcula.json"
                            : "textmate/QuietLight.tmTheme";
            final IThemeSource themeSource =
                    IThemeSource.fromInputStream(C.getAssets().open(colorTheme), colorTheme, null);
            codeEditor.setColorScheme((EditorColorScheme) TextMateColorScheme.create(themeSource));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            IGrammarSource iGram =
                    IGrammarSource.fromInputStream(C.getAssets().open(tmLang), tmLang, null);
            InputStreamReader iSR = new InputStreamReader(C.getAssets().open(config));
            IThemeSource iTS = ((TextMateColorScheme) codeEditor.getColorScheme()).getThemeSource();
            TextMateLanguage language = TextMateLanguage.create(iGram, iSR, iTS);
            language.setAutoCompleteEnabled(true);
            codeEditor.setEditorLanguage(language);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!CodeUtil.isTheSystemThemeDark(C)) return;
        // Color Scheme assign new color
        TextMateColorScheme colorScheme = (TextMateColorScheme) codeEditor.getColorScheme();
        colorScheme.setColor(EditorColorScheme.LINE_NUMBER, 0xff606366);
        colorScheme.setColor(EditorColorScheme.BLOCK_LINE, 0xff575757);
        colorScheme.setColor(EditorColorScheme.BLOCK_LINE_CURRENT, 0xdd575757);
        colorScheme.setColor(EditorColorScheme.KEYWORD, 0xffa43d00);
    }
}
