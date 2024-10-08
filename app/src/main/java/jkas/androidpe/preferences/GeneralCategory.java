package jkas.androidpe.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.LocaleListCompat;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import jkas.androidpe.resources.R;
import androidx.preference.PreferenceCategory;
import rkb.datasaver.RKBDataAppSettings;
import jkas.androidpe.constants.LanguagesPreferences;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceScreen;

/**
 * @author JKas
 */
public class GeneralCategory {
    public static GeneralCategory INSTANCE; // before calling this object, check if it is not null

    private PreferenceScreen screen;
    private Context context;

    public static void init(Context context, PreferenceScreen screen) {
        INSTANCE = new GeneralCategory(context, screen);
    }

    private GeneralCategory(Context context, PreferenceScreen screen) {
        this.screen = screen;
        this.context = context;

        addData();
    }

    private void addData() {
        PreferenceCategory generalCategory = new PreferenceCategory(context);
        generalCategory.setKey("general_category");
        generalCategory.setTitle(R.string.general);
        generalCategory.setIconSpaceReserved(false);
        screen.addPreference(generalCategory);

        // UI MODE
        Preference uiMode = new Preference(context);
        uiMode.setKey("ui_mode");
        uiMode.setTitle("UI Mode");
        uiMode.setSummary(R.string.ui_mode_desc);
        uiMode.setIcon(R.drawable.ic_invert_colors);
        uiMode.setOnPreferenceClickListener(
                pref -> {
                    showRadioDialogTheme();
                    return true;
                });
        generalCategory.addPreference(uiMode);

        // MATERIAL YOU
        SwitchPreferenceCompat material = new SwitchPreferenceCompat(context);
        material.setKey("material_you");
        material.setTitle("Material You");
        material.setIcon(R.drawable.ic_palette_color);
        material.setSummary(R.string.material_you_desc);
        material.setChecked(RKBDataAppSettings.isAppMaterialEnabled());
        material.setOnPreferenceChangeListener(
                (pref, val) -> {
                    RKBDataAppSettings.setAppMaterialEnabled(val.toString());
                    return true;
                });
        generalCategory.addPreference(material);

        // LANGUAGE
        Preference language = new Preference(context);
        language.setKey("language");
        language.setTitle(R.string.language);
        language.setSummary(R.string.language_desc);
        Drawable img = context.getDrawable(R.drawable.ic_language);
        img.setTintList(
                ColorStateList.valueOf(ResourcesValuesFixer.getColor(context, "?colorPrimary")));
        language.setIcon(img);
        language.setOnPreferenceClickListener(
                pref -> {
                    showRadioDialogLang();
                    return true;
                });
        generalCategory.addPreference(language);
    }

    int defaultOption = 0;
    int defaultTheme = 0;
    int defaultLang = 0;

    private void showRadioDialogLang() {
        defaultOption = 0;
        defaultLang = 0;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(context.getString(R.string.select_language));
        String appLang = RKBDataAppSettings.getAppLanguage();
        int p = 0;
        for (String s : LanguagesPreferences.code) {
            if (s.equals(appLang)) {
                defaultOption = p;
                defaultLang = p;
                break;
            }
            p++;
        }

        builder.setSingleChoiceItems(
                LanguagesPreferences.name,
                defaultOption,
                (dialog, which) -> {
                    defaultOption = which;
                    dialog.cancel();
                });

        builder.setOnCancelListener(
                (v8) -> {
                    if (defaultLang != defaultOption) {
                        String lang = LanguagesPreferences.code[defaultOption];
                        RKBDataAppSettings.setAppLanguage(lang);
                        if (lang.equals("default")) lang = "en";
                        AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags(lang));
                        if (context instanceof AppCompatActivity) {
                            final AppCompatActivity activity = (AppCompatActivity) context;
                            Intent intent = activity.getIntent();
                            context.startActivity(intent);
                            activity.finish();
                        }
                    }
                });
        builder.show();
    }

    public void showRadioDialogTheme() {
        defaultOption = 0;
        defaultTheme = 0;
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(context.getString(R.string.select_theme));

        String[] options = {
            context.getString(R.string.follow_system),
            context.getString(R.string.light),
            context.getString(R.string.dark)
        };

        if (RKBDataAppSettings.getAppTheme().equals(RKBDataAppSettings.APP_THEME_LIGHT)) {
            defaultOption = 1;
        } else if (RKBDataAppSettings.getAppTheme().equals(RKBDataAppSettings.APP_THEME_DARK)) {
            defaultOption = 2;
        } else if (RKBDataAppSettings.getAppTheme()
                .equals(RKBDataAppSettings.APP_THEME_FOLLOW_SYSTEM)) {
            defaultOption = 0;
        }

        defaultTheme = defaultOption;

        builder.setSingleChoiceItems(
                options,
                defaultOption,
                (dialog, which) -> {
                    defaultOption = which;
                    dialog.cancel();
                });
        builder.setOnCancelListener(
                (v8) -> {
                    if (defaultTheme != defaultOption) {
                        switch (defaultOption) {
                            case 1:
                                RKBDataAppSettings.setAppTheme(RKBDataAppSettings.APP_THEME_LIGHT);
                                AppCompatDelegate.setDefaultNightMode(
                                        AppCompatDelegate.MODE_NIGHT_NO);
                                break;

                            case 2:
                                RKBDataAppSettings.setAppTheme(RKBDataAppSettings.APP_THEME_DARK);
                                AppCompatDelegate.setDefaultNightMode(
                                        AppCompatDelegate.MODE_NIGHT_YES);
                                break;

                            default:
                                RKBDataAppSettings.setAppTheme(
                                        RKBDataAppSettings.APP_THEME_FOLLOW_SYSTEM);
                                AppCompatDelegate.setDefaultNightMode(
                                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                break;
                        }
                    }
                });
        builder.show();
    }
}
