package jkas.androidpe.activities;

import jkas.androidpe.databinding.ActivityPreferencesBinding;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import jkas.androidpe.R;
import jkas.androidpe.fragments.preferences.PreferencesFragment;

/**
 * @author JKas
 */
public class PreferencesActivity extends AppCompatActivity {
    private Context context = this;
    private ActivityPreferencesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreferencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> super.onBackPressed());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new PreferencesFragment())
                .commit();
    }
}
