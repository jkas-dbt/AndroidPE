package jkas.androidpe.resourcesUtils.dialog;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import jkas.androidpe.resources.R;

/**
 * @author JKas
 */
public class DialogProgressIndeterminate {

    public static AlertDialog getAlertDialog(Context C) {
        final TextView tvInfo = new TextView(C);
        tvInfo.setText(R.string.please_wait);
        tvInfo.setPadding(20, 16, 16, 16);

        final ProgressBar progress = new ProgressBar(C);
        progress.setIndeterminate(true);
        progress.setProgress(100);

        final LinearLayout linView = new LinearLayout(C);
        linView.setOrientation(LinearLayout.HORIZONTAL);
        linView.setGravity(Gravity.CENTER_VERTICAL);
        linView.setPadding(20, 16, 16, 16);
        linView.addView(progress);
        linView.addView(tvInfo);

        return DialogBuilder.getDialogBuilder(C, null, null)
                .setView(linView)
                .create();
    }
}
