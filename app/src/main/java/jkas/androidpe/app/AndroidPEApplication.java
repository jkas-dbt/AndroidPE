package jkas.androidpe.app;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.core.os.LocaleListCompat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import jkas.androidpe.projectUtils.dataCreator.FilesRef;
import jkas.androidpe.projectUtils.current.Environment;
import jkas.androidpe.utils.DataRKB;
import jkas.androidpe.utils.ForRes;
import jkas.codeUtil.Files;
import rkb.datasaver.RKBDataAppSettings;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.color.DynamicColors;
import android.app.NotificationManager;
import android.app.NotificationChannel;

/**
 * @author JKas
 */
public class AndroidPEApplication extends Application {
    public static Context applicationContext;
    public static AndroidPEApplication sApp;
    private static Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            sApp = this;
            applicationContext = this;

            try {
                Files.deleteDir(Environment.DEFAULT_ANDROIDPE_TMP_DATA);
            } catch (Exception e) {
                e.printStackTrace();
            }

            createNotifProcess();
            createNotifProcessCopyMove();
            FilesRef.initData(applicationContext);
            new DataRKB().init(applicationContext);
            init();

            ForRes.initEventsResquest();

            CrashHandler.init(this);
        } catch (Exception e) {
            Toast.makeText(applicationContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void write(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[1024 * 8];
        int len;
        while ((len = input.read(buf)) != -1) {
            output.write(buf, 0, len);
        }
    }

    public static void write(File file, byte[] data) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        FileOutputStream output = new FileOutputStream(file);
        try {
            write(input, output);
        } finally {
            closeIO(input, output);
        }
    }

    public static String toString(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, output);
        try {
            return output.toString("UTF-8");
        } finally {
            closeIO(input, output);
        }
    }

    public static void closeIO(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                if (closeable != null) closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static String getArch() {
        if (isAarch64()) {
            return "arm64-v8a";
        } else if (isArmv7a()) {
            return "armeabi-v7a";
        }
        return null;
    }

    public static boolean isAbiSupported() {
        return isAarch64() || isArmv7a();
    }

    public static boolean isArmv7a() {
        return Arrays.asList(Build.SUPPORTED_ABIS).contains("armeabi-v7a");
    }

    public static boolean isAarch64() {
        return Arrays.asList(Build.SUPPORTED_ABIS).contains("arm64-v8a");
    }

    private void init() {
        defaultTheme();
        defaultLanguage();
    }

    private void defaultLanguage() {
        String lang = RKBDataAppSettings.getAppLanguage();
        if (lang.equals("default")) lang = "en";
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang));
    }

    private void defaultTheme() {
        String srcTheme = RKBDataAppSettings.getAppTheme();
        if (srcTheme.equals("system")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (srcTheme.equals("dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (srcTheme.equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if (RKBDataAppSettings.isAppMaterialEnabled()) {
            DynamicColors.applyToActivitiesIfAvailable(this);
        }
    }

    private void createNotifProcess() {
        CharSequence name = "Process Task";
        String description = "...";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("process", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void createNotifProcessCopyMove() {
        CharSequence name = "Process Task CopyMove";
        String description = "...";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("process_cm", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public static AndroidPEApplication getApp() {
        return sApp;
    }
}
