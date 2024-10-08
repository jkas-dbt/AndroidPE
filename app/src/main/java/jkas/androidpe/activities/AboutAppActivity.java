package jkas.androidpe.activities;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.view.MenuItem;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.google.android.material.elevation.SurfaceColors;
import jkas.androidpe.resources.R;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import jkas.androidpe.databinding.ActivityAboutAppBinding;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.codeUtil.CodeUtil;

/**
 * @author JKas
 */
public class AboutAppActivity extends AppCompatActivity {
    private ActivityAboutAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> super.onBackPressed());
        init();
    }

    private void init() {
        icon();
        events();
    }

    private void events() {
        binding.linTelegram.setOnClickListener(
                v -> {
                    startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/android_pe")));
                });

        binding.linGmail.setOnClickListener(
                v -> {
                    Intent Page = new Intent();
                    Page.setAction(Intent.ACTION_VIEW);
                    Page.setData(Uri.parse("mailto:jkas.dbt@gmail.com"));
                    startActivity(Page);
                });

        binding.linGithub.setOnClickListener(
                v -> {
                    startActivity(
                            new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://github.com/jkas-dbt/AndroidPE.git")));
                });
    }

    private void icon() {
        if (CodeUtil.isTheSystemThemeDark(this))
            binding.lottieIcon.setAnimation("icons/lottie/androidpe_icon_dark.json");
        else binding.lottieIcon.setAnimation("icons/lottie/androidpe_icon_light.json");
        binding.lottieIcon.setSpeed(4.3f);
        binding.lottieIcon.addValueCallback(
                new KeyPath("Group 1", "Path 1"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(
                                SurfaceColors.SURFACE_0.getColor(AboutAppActivity.this),
                                PorterDuff.Mode.SRC_ATOP);
                    }
                });
    }
}
