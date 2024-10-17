package jkas.androidpe.preferences;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import jkas.androidpe.dialog.ProjectSyncDialog;
import jkas.androidpe.resources.R;
import androidx.preference.PreferenceScreen;
import android.content.Context;
import androidx.preference.PreferenceCategory;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;
import jkas.androidpe.resourcesUtils.dialog.DialogEditText;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import rkb.datasaver.RKBDataAppSettings;

/**
 * @author JKas
 */
public class ProjectCategory {
    public static ProjectCategory INSTANCE; // before calling this object, check if it is not null

    private PreferenceScreen screen;
    private Context context;

    public static void init(Context context, PreferenceScreen screen) {
        INSTANCE = new ProjectCategory(context, screen);
    }

    private ProjectCategory(Context context, PreferenceScreen screen) {
        this.screen = screen;
        this.context = context;

        addData();
    }

    private void addData() {
        PreferenceCategory projectCategory = new PreferenceCategory(context);
        projectCategory.setKey("project_category");
        projectCategory.setTitle(R.string.project);
        projectCategory.setIconSpaceReserved(false);
        screen.addPreference(projectCategory);

        // PACKAGE NAME PREFIX
        Preference pkgNamePrefix = new Preference(context);
        pkgNamePrefix.setKey("pkg_name_prefix");
        pkgNamePrefix.setTitle(R.string.package_name_prefix);
        pkgNamePrefix.setSummary(R.string.pkg_name_prefix_desc);
        pkgNamePrefix.setIcon(R.drawable.ic_cube_outline);
        pkgNamePrefix.setOnPreferenceClickListener(
                pref -> {
                    final DialogEditText dialog = new DialogEditText(context, true);
                    dialog.setTitle(context.getString(R.string.rename));
                    dialog.setDefaultValue(RKBDataAppSettings.getAppPnp());
                    dialog.setHint(context.getString(R.string.package_name_prefix));
                    dialog.setMsgError(context.getString(R.string.invalide_entry));
                    dialog.setPattern(ResCodeUtils.PATTERN_PKG);
                    dialog.setOnChangeDetected(
                            new DialogEditText.OnChangeDetected() {
                                String text = RKBDataAppSettings.getAppPnp();

                                @Override
                                public void onTextChanged(String text) {
                                    this.text = text;
                                }

                                @Override
                                public void onValueConfirmed(boolean match) {
                                    if (match) RKBDataAppSettings.setAppPnp(text);
                                }
                            });
                    dialog.show();
                    return true;
                });
        projectCategory.addPreference(pkgNamePrefix);

        // O-P-D
        SwitchPreferenceCompat opd = new SwitchPreferenceCompat(context);
        opd.setKey("o_p_d");
        opd.setTitle(R.string.open_a_project_directly);
        opd.setSummary(R.string.open_last_project);
        Drawable imgOpd = context.getDrawable(R.drawable.ic_folder_open);
        imgOpd.setTintList(
                ColorStateList.valueOf(ResourcesValuesFixer.getColor(context, "?colorPrimary")));
        opd.setIcon(imgOpd);
        opd.setChecked(false);
        opd.setEnabled(false);
        projectCategory.addPreference(opd);

        // PROJECT OPENING
        SwitchPreferenceCompat projectOpening = new SwitchPreferenceCompat(context);
        projectOpening.setKey("project_opening");
        projectOpening.setTitle(R.string.project_opening);
        projectOpening.setSummary(R.string.project_opening_description);
        Drawable imgOpen = context.getDrawable(R.drawable.ic_folder_open);
        imgOpen.setTintList(
                ColorStateList.valueOf(ResourcesValuesFixer.getColor(context, "?colorPrimary")));
        projectOpening.setIcon(imgOpen);
        projectOpening.setChecked(true);
        projectOpening.setEnabled(false);
        projectCategory.addPreference(projectOpening);

        // PACKAGE SYNC
        Preference projectSync = new Preference(context);
        projectSync.setKey("project_sync");
        projectSync.setTitle(R.string.project_synchronizer);
        projectSync.setSummary(R.string.project_synchronizer_description);
        Drawable imgSync = context.getDrawable(R.drawable.ic_folder_sync);
        imgSync.setTintList(
                ColorStateList.valueOf(ResourcesValuesFixer.getColor(context, "?colorPrimary")));
        projectSync.setIcon(imgSync);
        projectSync.setOnPreferenceClickListener(
                pref -> {
                    new ProjectSyncDialog(context).show();
                    return true;
                });
        projectCategory.addPreference(projectSync);
    }
}
