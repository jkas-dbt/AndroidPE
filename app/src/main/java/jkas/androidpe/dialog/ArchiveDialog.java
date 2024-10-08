package jkas.androidpe.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import jkas.androidpe.resources.R;
import jkas.androidpe.databinding.DialogArchiveBinding;
import jkas.androidpe.databinding.LayoutPathToProjectBinding;
import jkas.androidpe.explorer.SelectFF;
import jkas.androidpe.project.Project;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.Zipper;
import net.lingala.zip4j.progress.ProgressMonitor;

/**
 * @author JKas
 */
public class ArchiveDialog {
    private DialogArchiveBinding bindingArchive;
    private MaterialAlertDialogBuilder alert;
    private AppCompatActivity C;
    private Project P;
    private Zipper data;

    public ArchiveDialog(Context c, Project p) {
        C = (AppCompatActivity) c;
        P = p;

        ini();
    }

    private void ini() {
        bindingArchive = DialogArchiveBinding.inflate(LayoutInflater.from(C));
        alert = new MaterialAlertDialogBuilder(C);
        alert.setView(bindingArchive.getRoot());
        alert.setNegativeButton(R.string.cancel, null);
        alert.setPositiveButton(
                R.string.archive,
                (a1, a2) -> {
                    if (bindingArchive.switchExcludeBuildFolder.isChecked()) {
                        data.addFolderExcluded(
                                Files.getExternalStorageDir()
                                        + File.separator
                                        + P.getProjectDir()
                                        + File.separator
                                        + P.getFolderName()
                                        + File.separator
                                        + "app/build");
                    }
                    data.setDestPath(
                            Files.getExternalStorageDir()
                                    + File.separator
                                    + P.getProjectDir()
                                    + File.separator
                                    + P.getFolderName()
                                    + File.separator
                                    + ".androidpe"
                                    + File.separator
                                    + P.getFolderName()
                                    + ".zip");

                    notifyProcessLaunched();
                    data.archive();
                });
        alert.show();
        eventsArchive();
    }

    private void eventsArchive() {
        final String pathRoot =
                Files.getExternalStorageDir()
                        + File.separator
                        + P.getProjectDir()
                        + File.separator
                        + P.getFolderName();

        data = new Zipper(C, pathRoot);
        data.setOnProgressListener(
                new Zipper.OnProgressListener() {
                    @Override
                    public void onProgress(ProgressMonitor.State state) {
                        // w8ll be impl... soon
                    }

                    @Override
                    public void onFinish() {
                        notifyProcessFinished();
                    }

                    @Override
                    public void onError(Exception e) {
                        notifyProcessError();
                    }
                });

        bindingArchive.editTextPassWord.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        String s = bindingArchive.editTextPassWord.getText().toString();
                        data.setPassWord(s);
                    }
                });

        final SelectFF selector = new SelectFF(C);
        selector.setSelectorType(SelectFF.FOLDER_SELECTOR);
        selector.setSelectMode(SelectFF.SELECT_MULTIPLE);
        selector.lookRoot(pathRoot);
        bindingArchive.btnAddFolder.setOnClickListener(
                (b1) -> {
                    selector.attachListSelected(data.excludeFolders);
                    selector.setOnSaveListener(
                            (l) -> {
                                /*bindingArchive.linExcludedFolders.removeAllViews();
                                data.excludeFolders.clear();*/
                                for (final String s : l) {
                                    if (Files.isDirectory(s)) {
                                        final LayoutPathToProjectBinding b =
                                                LayoutPathToProjectBinding.inflate(
                                                        LayoutInflater.from(C));
                                        b.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(8));
                                        int index =
                                                (pathRoot + File.separator)
                                                        .lastIndexOf(File.separator);
                                        b.tvPath.setText(s.substring(index + 1));
                                        b.imgDelete.setOnClickListener(
                                                (v) -> {
                                                    data.removeFolderExcluded(s);
                                                    b.getRoot().setVisibility(View.GONE);
                                                });
                                        bindingArchive.linExcludedFolders.addView(
                                                (View) b.getRoot());
                                        data.addFolderExcluded(s);
                                    }
                                }
                            });
                    selector.showView();
                    selector.loadData();
                });

        final SelectFF selector2 = new SelectFF(C);
        selector2.setSelectorType(SelectFF.FILE_SELECTOR);
        selector2.setSelectMode(SelectFF.SELECT_MULTIPLE);
        selector2.lookRoot(pathRoot);
        bindingArchive.btnAddFile.setOnClickListener(
                (b2) -> {
                    selector2.showView();
                    selector2.attachListSelected(data.excludeFiles);
                    selector2.loadData();
                    selector2.setOnSaveListener(
                            (l2) -> {
                                /*bindingArchive.linExcludedFiles.removeAllViews();
                                data.excludeFiles.clear();*/
                                for (final String s : l2) {
                                    if (Files.isFile(s)) {
                                        final LayoutPathToProjectBinding b =
                                                LayoutPathToProjectBinding.inflate(
                                                        LayoutInflater.from(C));
                                        b.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(8));
                                        int index =
                                                (pathRoot + File.separator)
                                                        .lastIndexOf(File.separator);
                                        b.tvPath.setText(s.substring(index + 1));
                                        b.imgDelete.setOnClickListener(
                                                (v2) -> {
                                                    data.removeFolderExcluded(s);
                                                    b.getRoot().setVisibility(View.GONE);
                                                });
                                        bindingArchive.linExcludedFiles.addView((View) b.getRoot());
                                        data.addFileExcluded(s);
                                    }
                                }
                            });
                });
    }

    // ALL NOTIFICATIONS
    private void notifyProcessLaunched() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(C, "process")
                        .setSmallIcon(R.drawable.androidpe_app_icon_dark)
                        .setContentTitle(C.getString(R.string.project) + " : " + P.getFolderName())
                        .setContentText(
                                C.getString(R.string.archiving)
                                        + " : "
                                        + C.getString(R.string.is_in_progress))
                        .setProgress(0, 0, true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(C);
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }

    private void notifyProcessFinished() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(C, "process")
                        .setSmallIcon(R.drawable.androidpe_app_icon_dark)
                        .setContentTitle(C.getString(R.string.project) + " : " + P.getFolderName())
                        .setContentText(C.getString(R.string.finished))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(C);
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }

    private void notifyProcessError() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(C, "process")
                        .setSmallIcon(R.drawable.androidpe_app_icon_dark)
                        .setContentTitle("Archiving Error")
                        .setContentText(C.getString(R.string.msg_error_archiving_process))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(C);
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }
}
