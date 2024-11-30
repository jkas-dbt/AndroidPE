package jkas.androidpe.preferences;

import android.content.Intent;
import android.net.Uri;
import jkas.androidpe.activities.AboutAppActivity;
import jkas.androidpe.resources.R;
import androidx.preference.PreferenceScreen;
import android.content.Context;
import androidx.preference.PreferenceCategory;
import androidx.preference.Preference;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.codeUtil.CodeUtil;

/**
 * @author JKas
 */
public class AboutCategory {
    public static AboutCategory INSTANCE; // before calling this object, check if it is not null

    private PreferenceScreen screen;
    private Context context;

    public static void init(Context context, PreferenceScreen screen) {
        INSTANCE = new AboutCategory(context, screen);
    }

    private AboutCategory(Context context, PreferenceScreen screen) {
        this.screen = screen;
        this.context = context;

        addData();
    }

    private void addData() {
        PreferenceCategory aboutCategory = new PreferenceCategory(context);
        aboutCategory.setKey("project_category");
        aboutCategory.setTitle(R.string.about);
        aboutCategory.setIcon(R.drawable.ic_information_outline);
        screen.addPreference(aboutCategory);

        // CHNAGELOG
        Preference changelog = new Preference(context);
        changelog.setKey("changelog");
        changelog.setTitle(R.string.changelog);
        changelog.setSummary(R.string.changelog_desc);
        changelog.setIconSpaceReserved(false);
        changelog.setOnPreferenceClickListener(
                pref -> {
                    context.startActivity(
                            new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(
                                            "https://github.com/jkas-dbt/AndroidPE/releases/tag/v1.76")));
                    return true;
                });
        aboutCategory.addPreference(changelog);

        // ABOUT APP
        Preference aboutApp = new Preference(context);
        aboutApp.setKey("about_app");
        aboutApp.setTitle(R.string.about_androidpe);
        aboutApp.setSummary(R.string.about_androidpe_desc);
        aboutApp.setIconSpaceReserved(false);
        aboutApp.setOnPreferenceClickListener(
                pref -> {
                    CodeUtil.startActivity(context, AboutAppActivity.class);
                    return true;
                });
        aboutCategory.addPreference(aboutApp);
    }
}
