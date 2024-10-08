package jkas.androidpe.resourcesUtils.dialog;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * @author JKas
 */
public class DialogBuilder {

    public static void showDialog(Context c, String title, String msg) {
        new MaterialAlertDialogBuilder(c).setTitle(title).setMessage(msg).show();
    }

    public static MaterialAlertDialogBuilder getDialogBuilder(
            @NonNull Context c, String title, String msg) {
        return new MaterialAlertDialogBuilder(c).setTitle(title).setMessage(msg);
    }
}
