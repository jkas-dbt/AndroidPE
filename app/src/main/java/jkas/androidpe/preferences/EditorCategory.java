package jkas.androidpe.preferences;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import androidx.preference.PreferenceScreen;
import android.content.Context;
import androidx.preference.PreferenceCategory;
import androidx.preference.Preference;
import android.content.Intent;
import android.net.Uri;
import jkas.androidpe.explorer.dialog.DialogSelector;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.CodeUtil;
import rkb.datasaver.RKBDataAppSettings;

/**
 * @author JKas
 */
public class EditorCategory {
    public static EditorCategory INSTANCE; // before calling this object, check if it is not null

    private PreferenceScreen screen;
    private Context context;

    public static void init(Context context, PreferenceScreen screen) {
        INSTANCE = new EditorCategory(context, screen);
    }

    private EditorCategory(Context context, PreferenceScreen screen) {
        this.screen = screen;
        this.context = context;

        addData();
    }

    private void addData() {
        PreferenceCategory editorCategory = new PreferenceCategory(context);
        editorCategory.setKey("editor");
        editorCategory.setTitle(R.string.editor);
        editorCategory.setIconSpaceReserved(false);
        screen.addPreference(editorCategory);

        // AUTO SAVE
        Preference autoSave = new Preference(context);
        autoSave.setKey("autoSave");
        autoSave.setTitle(R.string.auto_save);
        autoSave.setSummary(R.string.auto_save_code_time);
        Drawable img = context.getDrawable(R.drawable.ic_save);
        img.setTintList(
                ColorStateList.valueOf(ResourcesValuesFixer.getColor(context, "?colorPrimary")));
        autoSave.setIcon(img);
        autoSave.setOnPreferenceClickListener(
                pref -> {
                    showDialogAutoSave();
                    return true;
                });
        editorCategory.addPreference(autoSave);

        // ABOUT APP
        /*Preference autoSaveTiming = new Preference(context);
        autoSaveTiming.setKey("about_app");
        autoSaveTiming.setTitle(R.string.auto);
        autoSaveTiming.setSummary(R.string.about_androidpe_desc);
        autoSaveTiming.setIcon(R.drawable.ic_save_cog);
        autoSaveTiming.setOnPreferenceClickListener(
                pref -> {
                    return true;
                });
        editorCategory.addPreference(autoSaveTiming);*/
    }

    private void showDialogAutoSave() {
        try {

            String[] listOption = new String[] {"none", "10 sec", "15 sec", "20 sec", "30 sec"};
            String[] finalOption =
                    new String[] {
                        RKBDataAppSettings.AUTO_SAVE_NONE,
                        RKBDataAppSettings.AUTO_SAVE_10_SEC,
                        RKBDataAppSettings.AUTO_SAVE_15_SEC,
                        RKBDataAppSettings.AUTO_SAVE_20_SEC,
                        RKBDataAppSettings.AUTO_SAVE_30_SEC,
                    };

            int i = 0, p = 0;
            String saved = RKBDataAppSettings.getCodeAutoSavedTime();
            for (String option : finalOption) {
                if (option.equals(saved)) {
                    p = i;
                    break;
                }
                i++;
            }

            DialogSelector dialog =
                    new DialogSelector(
                            context,
                            context.getString(R.string.auto_save),
                            listOption,
                            DialogSelector.SINGLE_SELECT_MODE);
            dialog.setSinglePosition(p);
            dialog.setOnItemSelected(
                    position -> RKBDataAppSettings.setCodeAutoSavedTime(finalOption[position]));
            dialog.show();
        } catch (Exception err) {
            Toast.makeText(context, err.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
