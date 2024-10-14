package jkas.androidpe.explorer.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import jkas.androidpe.project.AndroidModule;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dialog.DialogProgressIndeterminate;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class DialogDeletePaths {
    public static final int DELETED = 0;
    public static final int NOT_DELETED = 1;

    private OnDeleteDetected listener;
    private Context C;
    private ArrayList<String> listPaths;
    private String path;
    private MaterialAlertDialogBuilder alert;

    public DialogDeletePaths(Context c) {
        C = c;
        alert = DialogBuilder.getDialogBuilder(C, C.getString(R.string.warning), null);
        event();
    }

    public void show() {
        alert.show();
    }

    public void setPath(String path2File) {
        path = path2File;
        alert.setMessage(
                C.getString(R.string.warning_delete)
                        + " ["
                        + Files.getNameFromAbsolutePath(path)
                        + "]");

        if (path2File.equals(DataRefManager.getInstance().P.getAbsolutePath())) {
            alert.setMessage(
                    C.getString(R.string.warning_projects_deleting)
                            + "\n"
                            + C.getString(R.string.warning_delete)
                            + " ["
                            + Files.getNameFromAbsolutePath(path)
                            + "]");
            return;
        }

        boolean verifIfError = false;
        for (AndroidModule am : DataRefManager.getInstance().listAndroidModule)
            if (!am.getProjectAbsolutePath().equals(path2File))
                for (String s : am.getRefToOthersModules()) {
                    String p =
                            DataRefManager.getInstance().P.getAbsolutePath() + s.replace(":", "/");
                    if (p.equals(path2File))
                        alert.setMessage(
                                C.getString(R.string.bad_idea)
                                        + "\n"
                                        + C.getString(R.string.info_there_are_module_depend_on_it)
                                        + " "
                                        + C.getString(R.string.warning_module_deleting));
                }
            else if (am.getProjectAbsolutePath().equals(path2File)) verifIfError = true;
    }

    public void setListPath(ArrayList<String> listPaths) {
        alert.setMessage(
                C.getString(R.string.warning_delete)
                        + " ["
                        + listPaths.size()
                        + " "
                        + C.getString(R.string.elements)
                        + "]");
        this.listPaths = listPaths;
    }

    private void event() {
        alert.setNegativeButton(R.string.cancel, null);
        alert.setPositiveButton(
                R.string.delete,
                (j, d) -> {
                    if (path != null) delete(path);
                    if (listPaths != null) delete(listPaths);
                });
    }

    private void delete(ArrayList<String> listPath2Files) {
        String sensitivePaths = "";
        boolean verifIfError = false;
        for (String path2File : listPath2Files) {
            for (AndroidModule am : DataRefManager.getInstance().listAndroidModule)
                if (!am.getProjectAbsolutePath().equals(path2File))
                    for (String s : am.getRefToOthersModules()) {
                        String p =
                                DataRefManager.getInstance().P.getAbsolutePath()
                                        + s.replace(":", "/");
                        if (p.equals(path2File)) verifIfError = true;
                        sensitivePaths += "\n" + p;
                    }
                else if (am.getProjectAbsolutePath().equals(path2File)) verifIfError = true;

            if (path2File.equals(DataRefManager.getInstance().P.getAbsolutePath())) {
                DialogBuilder.getDialogBuilder(
                                C,
                                C.getString(R.string.warning),
                                C.getString(R.string.warning_projects_deleting))
                        .setCancelable(false)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.delete, (d, v) -> launchDeleteTask(path2File))
                        .show();
                return;
            }
        }

        if (verifIfError)
            DialogBuilder.getDialogBuilder(
                            C,
                            C.getString(R.string.warning),
                            C.getString(R.string.bad_idea)
                                    + "\n"
                                    + C.getString(R.string.info_there_are_module_depend_on_it)
                                    + " "
                                    + C.getString(R.string.warning_module_deleting))
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.delete, (d, v) -> launchDeleteTask(listPath2Files))
                    .show();
        else launchDeleteTask(listPath2Files);
    }

    private void delete(String path2File) {
        boolean verifIfError = false;
        for (AndroidModule am : DataRefManager.getInstance().listAndroidModule)
            if (!am.getProjectAbsolutePath().equals(path2File))
                for (String s : am.getRefToOthersModules()) {
                    String p =
                            DataRefManager.getInstance().P.getAbsolutePath() + s.replace(":", "/");
                    if (p.equals(path2File)) verifIfError = true;
                }
            else if (am.getProjectAbsolutePath().equals(path2File)) verifIfError = true;

        if (path2File.equals(DataRefManager.getInstance().P.getAbsolutePath())) {
            DialogBuilder.getDialogBuilder(
                            C,
                            C.getString(R.string.warning),
                            C.getString(R.string.warning_projects_deleting))
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.delete, (d, v) -> launchDeleteTask(path2File))
                    .show();
            return;
        }

        if (verifIfError)
            DialogBuilder.getDialogBuilder(
                            C,
                            C.getString(R.string.warning),
                            C.getString(R.string.bad_idea)
                                    + "\n"
                                    + C.getString(R.string.info_there_are_module_depend_on_it)
                                    + " "
                                    + C.getString(R.string.warning_module_deleting))
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.delete, (d, v) -> launchDeleteTask(path2File))
                    .show();
        else launchDeleteTask(path2File);
    }

    private void launchDeleteTask(String path) {
        AlertDialog alert = DialogProgressIndeterminate.getAlertDialog(C);
        alert.show();
        new Handler(Looper.getMainLooper())
                .postDelayed(
                        () -> {
                            performDelete(path);
                            listener.onFinish();
                            alert.dismiss();
                        },
                        100);
    }

    private void launchDeleteTask(ArrayList<String> list) {
        AlertDialog alert = DialogProgressIndeterminate.getAlertDialog(C);
        alert.show();
        new Handler(Looper.getMainLooper())
                .postDelayed(
                        () -> {
                            for (String path2File : list) performDelete(path2File);
                            listener.onFinish();
                            alert.dismiss();
                        },
                        100);
    }

    private void performDelete(String path2File) {
        try {
            if (Files.isDirectory(path2File)) Files.deleteDir(path2File);
            else Files.deleteFile(path2File);
            listener.onDelete(path2File, DELETED);
        } catch (Exception e) {
            listener.onDelete(path2File, NOT_DELETED);
        }
    }

    public void setOnDeleteDetected(OnDeleteDetected listener) {
        this.listener = listener;
    }

    public interface OnDeleteDetected {
        public void onDelete(String path, int processStatus);

        public void onFinish();
    }
}
