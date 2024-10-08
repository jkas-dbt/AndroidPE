package jkas.androidpe.fragments.preferences;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import jkas.androidpe.preferences.AboutCategory;
import jkas.androidpe.preferences.EditorCategory;
import jkas.androidpe.preferences.GeneralCategory;
import jkas.androidpe.preferences.ProjectCategory;

/**
 * @author JKas
 */
public class PreferencesFragment extends PreferenceFragmentCompat {
    private Context context;
    private PreferenceScreen screen;
    private AppCompatActivity activity;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        context = getContext();
        setPreferenceScreen(createPreferenceScreen());
    }

    private PreferenceScreen createPreferenceScreen() {
        screen = getPreferenceManager().createPreferenceScreen(context);
        screen.removeAll();
        GeneralCategory.init(context, screen);
        EditorCategory.init(context, screen);
        ProjectCategory.init(context, screen);
        AboutCategory.init(context, screen);
        setPreferenceScreen(screen);
        return screen;
    }
}
