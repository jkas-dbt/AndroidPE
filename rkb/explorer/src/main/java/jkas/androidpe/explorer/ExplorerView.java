package jkas.androidpe.explorer;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.elevation.SurfaceColors;
import jkas.androidpe.explorer.adapter.ExplorerViewAdapter;
import jkas.androidpe.explorer.adapter.ExplorerViewAdapter;
import jkas.androidpe.explorer.databinding.ExplorerViewBinding;
import jkas.androidpe.explorer.dialog.DialogDeletePaths;
import jkas.androidpe.explorer.dialog.DialogNewFile;
import jkas.androidpe.explorer.dialog.DialogNewFolder;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.adapters.BottomMarginItemDecorationAdapter;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.codeUtil.Files;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * @author JKas
 */
public class ExplorerView {
    private final String SRC = "Explorer";
    public static int FOLDER = 0;
    public static int FILE = 1;

    private OnRequestListener listenerRequest;
    private OnAnyChangeDetected listener;
    private ExplorerViewBinding binding;
    private ExplorerViewAdapter adapter;
    private FragmentActivity C;
    private String rootLookedAt = Files.getExternalStorageDir();
    private String path = null;
    private String success = "", error = ""; // for INTERFACE DialogDeletePaths requests.
    private boolean copyClicked, cutClicked;
    private SelectFF selectFF;

    public ExplorerView(FragmentActivity c) {
        C = c;
        adapter = new ExplorerViewAdapter(C);
        binding = ExplorerViewBinding.inflate(LayoutInflater.from(C));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(C));
        binding.recyclerView.setAdapter(adapter);

        selectFF = new SelectFF(C);
        selectFF.lookRoot(Files.getExternalStorageDir());
        selectFF.setSelectMode(SelectFF.SELECT_MULTIPLE);

        themeLinTools();
        loadAllOnListener();
        events();
        setDefault();

        rootLookedAt = DataRefManager.getInstance().P.getAbsolutePath();
    }

    private void events() {
        binding.btnSelectMode.setOnClickListener(
                (v) -> {
                    if (adapter.getCopyCutClicked()) {
                        binding.tvTop.setText(C.getString(R.string.in_process) + " ...");
                        Executors.newSingleThreadExecutor()
                                .execute(
                                        () -> {
                                            final String localPath = path;
                                            String paths;
                                            paths = "";
                                            try {
                                                for (String s : adapter.getAllSelectedPath()) {
                                                    if (Files.isDirectory(s)) {
                                                        Files.copyDir(
                                                                s,
                                                                localPath
                                                                        + "/"
                                                                        + Files
                                                                                .getNameFromAbsolutePath(
                                                                                        s));
                                                        if (cutClicked) Files.deleteDir(s);
                                                        paths +=
                                                                "\n"
                                                                        + C.getString(
                                                                                R.string.folder)
                                                                        + ":"
                                                                        + s;
                                                    } else if (Files.isFile(s)) {
                                                        Files.copyFile(
                                                                s,
                                                                localPath
                                                                        + "/"
                                                                        + Files
                                                                                .getNameFromAbsolutePath(
                                                                                        s));
                                                        if (cutClicked) Files.deleteFile(s);
                                                        paths +=
                                                                "\n"
                                                                        + C.getString(
                                                                                R.string.folder)
                                                                        + ":"
                                                                        + s;
                                                    }
                                                }
                                                C.runOnUiThread(() -> binding.tvTop.setText("..."));
                                                Logger.success(
                                                        SRC, (copyClicked ? "copy" : "cut"), paths);
                                            } catch (Exception e) {
                                                C.runOnUiThread(() -> binding.tvTop.setText(""));
                                                Toast.makeText(
                                                                C,
                                                                C.getString(
                                                                        R.string
                                                                                .msg_error_when_parsing_values),
                                                                Toast.LENGTH_SHORT)
                                                        .show();
                                                Logger.warn(
                                                        SRC,
                                                        C.getString(
                                                                        R.string
                                                                                .msg_error_when_parsing_values)
                                                                + " : Copy/Cut");
                                            }
                                            C.runOnUiThread(() -> setDefault());
                                        });
                    } else {
                        setSelectMode();
                    }
                });

        binding.btnDownload.setOnClickListener(
                (v) -> {
                    if (adapter.getSelectMode()) {
                        if (adapter.getAllSelectedPath().size() == 0) {
                            setDefault();
                            return;
                        }
                        PopupMenu popupMenu = new PopupMenu(C, v);
                        popupMenu.setForceShowIcon(true);
                        popupMenu
                                .getMenu()
                                .add(Menu.NONE, 1, Menu.NONE, R.string.delete)
                                .setIcon(C.getDrawable(R.drawable.ic_delete));
                        popupMenu
                                .getMenu()
                                .add(Menu.NONE, 2, Menu.NONE, R.string.cancel)
                                .setIcon(C.getDrawable(R.drawable.ic_cancel));
                        popupMenu.setOnMenuItemClickListener(
                                (item) -> {
                                    switch (item.getItemId()) {
                                        case 1:
                                            success = "";
                                            error = "";
                                            final DialogDeletePaths ddp = new DialogDeletePaths(C);
                                            ddp.setListPath(adapter.getAllSelectedPath());
                                            ddp.show();
                                            ddp.setOnDeleteDetected(
                                                    new DialogDeletePaths.OnDeleteDetected() {

                                                        @Override
                                                        public void onDelete(String p, int status) {
                                                            if (status == DialogDeletePaths.DELETED)
                                                                success += "\n" + p;
                                                            else if (status
                                                                    == DialogDeletePaths
                                                                            .NOT_DELETED)
                                                                error += "\n" + p;
                                                        }

                                                        @Override
                                                        public void onFinish() {
                                                            setDefault();
                                                            adapter.setSelectMode(false);
                                                            notifyProcessFinished(
                                                                    "List Foldes/Files" + success,
                                                                    C.getString(R.string.deleted));
                                                            Logger.success(
                                                                    SRC,
                                                                    C.getString(R.string.deleted)
                                                                            + ":",
                                                                    success);
                                                        }
                                                    });
                                            break;
                                        case 2:
                                            setDefault();
                                            break;
                                    }
                                    return true;
                                });
                        popupMenu.show();
                    } else {
                        PopupMenu popupMenu = new PopupMenu(C, v);
                        popupMenu.setForceShowIcon(true);
                        /*popupMenu
                        .getMenu()
                        .add(Menu.NONE, 1, Menu.NONE, R.string.clone_repository)
                        .setIcon(C.getDrawable(R.drawable.ic_git));*/
                        popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, R.string.imports);
                        popupMenu.setOnMenuItemClickListener(
                                (item) -> {
                                    switch (item.getItemId()) {
                                        case 1:
                                            Toast.makeText(C, "soon", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 2:
                                            showImportMenu();
                                            break;
                                    }
                                    return true;
                                });
                        popupMenu.show();
                    }
                });

        binding.btnCopy.setOnClickListener(
                (v) -> {
                    if (!adapter.getSelectMode()) return;
                    if (adapter.getAllSelectedPath().size() > 0) onCopyCut(true);
                    else Toast.makeText(C, C.getString(R.string.select), Toast.LENGTH_SHORT).show();
                });
        binding.btnCut.setOnClickListener(
                (v) -> {
                    if (!adapter.getSelectMode()) return;
                    if (adapter.getAllSelectedPath().size() > 0) onCopyCut(false);
                    else Toast.makeText(C, C.getString(R.string.select), Toast.LENGTH_SHORT).show();
                });

        binding.btnAdd.setOnClickListener(
                (v) -> {
                    PopupMenu popupMenu = new PopupMenu(C, binding.btnDownload);
                    popupMenu.setForceShowIcon(true);
                    if (!path.matches(".*" + ProjectsPathUtils.RES_PATH))
                        popupMenu
                                .getMenu()
                                .add(Menu.NONE, 1, Menu.NONE, R.string.file)
                                .setIcon(C.getDrawable(R.drawable.ic_file_txt));
                    popupMenu
                            .getMenu()
                            .add(Menu.NONE, 2, Menu.NONE, R.string.folder)
                            .setIcon(C.getDrawable(R.drawable.ic_folder));
                    popupMenu.setOnMenuItemClickListener(
                            (item) -> {
                                switch (item.getItemId()) {
                                    case 1:
                                        showDialogNewFile();
                                        break;
                                    case 2:
                                        showDialogNewFolder();
                                        break;
                                }
                                return true;
                            });
                    popupMenu.show();
                });
    }

    private void showDialogNewFile() {
        new DialogNewFile(C, path).setOnCreateListener((fileName) -> initPath(path));
    }

    private void showDialogNewFolder() {
        new DialogNewFolder(C, path)
                .setOnChangeDetected(
                        (txt) -> {
                            if (txt != null) initPath(path);
                            else {
                                Logger.error(
                                        SRC,
                                        C.getString(R.string.error),
                                        txt + " : " + C.getString(R.string.cant_save));
                                return;
                            }
                            Logger.success(SRC, txt + " : " + C.getString(R.string.created));
                        });
    }

    private void showImportMenu() {
        PopupMenu popupMenu = new PopupMenu(C, binding.btnDownload);
        popupMenu.setForceShowIcon(true);
        popupMenu
                .getMenu()
                .add(Menu.NONE, 1, Menu.NONE, R.string.file)
                .setIcon(R.drawable.ic_file_txt);
        popupMenu
                .getMenu()
                .add(Menu.NONE, 2, Menu.NONE, R.string.folder)
                .setIcon(R.drawable.ic_folder);
        popupMenu.setOnMenuItemClickListener(
                (item) -> {
                    switch (item.getItemId()) {
                        case 1:
                            selectFF.setSelectorType(SelectFF.FILE_SELECTOR);
                            break;
                        case 2:
                            selectFF.setSelectorType(SelectFF.FOLDER_SELECTOR);
                            break;
                    }
                    selectFF.showView();
                    selectFF.loadData();
                    return true;
                });
        popupMenu.show();

        selectFF.setOnSaveListener(
                (list) -> {
                    Executors.newSingleThreadExecutor()
                            .execute(
                                    () -> {
                                        copyData(list);
                                    });
                });
    }

    private void copyData(ArrayList<String> list) {
        try {
            for (String s : list) {
                if (Files.isFile(s))
                    Files.copyFile(s, path + File.separator + Files.getNameFromAbsolutePath(s));
                else Files.copyDir(s, path + File.separator + Files.getNameFromAbsolutePath(s));

                Logger.success(
                        SRC,
                        Files.getNameFromAbsolutePath(s) + " : " + C.getString(R.string.imported),
                        C.getString(R.string.from) + " : " + Files.getPrefixPath(s),
                        C.getString(R.string.to) + " : " + path);
            }
            C.runOnUiThread(() -> initPath(path));
        } catch (Exception e) {
            Logger.error(SRC, C.getString(R.string.cant_load_data), e.getMessage());
        }
    }

    public void initPath(String p) {
        path = p;
        adapter.init(p);
    }

    public boolean goBack() {
        if (adapter.getSelectMode()) {
            setDefault();
            return true;
        }
        if (rootLookedAt.equals(path)) return false;
        initPath(Files.getPrefixPath(path));
        return true;
    }

    public void lookRoot(String path) {
        rootLookedAt = path;
    }

    public View getViewExplorer() {
        return (View) binding.getRoot();
    }

    public String getCurrentPath() {
        return path;
    }

    private void loadAllOnListener() {
        adapter.setOnAnyChangeDetected(
                new ExplorerViewAdapter.OnAnyChangeDetected() {

                    @Override
                    public void onFileClick(@NonNull View view, @NonNull String path) {
                        listener.onFileClick(view, path);
                    }

                    @Override
                    public void onPathChanged(String p) {
                        listener.onPathChanged(p);
                        path = p;

                        int margin = 200;
                        binding.recyclerView.addItemDecoration(
                                new BottomMarginItemDecorationAdapter(margin));
                    }
                });
        adapter.setOnRequestListener(() -> listenerRequest.onRefresh());
    }

    private void setDefault() {
        binding.btnCopy.setAlpha(0.5f);
        binding.btnCut.setAlpha(0.5f);
        binding.btnDownload.setImageResource(R.drawable.ic_paperclip_horizontal);
        binding.btnSelectMode.setImageResource(R.drawable.ic_grid_outline);
        setDefaultValuesBoolean();
        adapter.setSelectMode(false);
    }

    private void setSelectMode() {
        binding.btnCopy.setAlpha(1.0f);
        binding.btnCut.setAlpha(1.0f);
        binding.btnDownload.setImageResource(R.drawable.ic_cancel);
        setDefaultValuesBoolean();
        adapter.setSelectAll();
    }

    private void onCopyCut(boolean copy) {
        binding.btnCopy.setAlpha(0.5f);
        binding.btnCut.setAlpha(0.5f);
        binding.btnSelectMode.setImageResource(R.drawable.ic_paste_content);
        binding.btnDownload.setImageResource(R.drawable.ic_cancel);
        setDefaultValuesBoolean();

        copyClicked = copy;
        cutClicked = !copy;

        adapter.copyCutClicked();
    }

    private void setDefaultValuesBoolean() {
        copyClicked = false;
        cutClicked = false;
    }

    private void themeLinTools() {
        {
            GradientDrawable gradient = new GradientDrawable();
            gradient.setColors(
                    new int[] {
                        SurfaceColors.SURFACE_0.getColor(C), Color.TRANSPARENT, Color.TRANSPARENT
                    });
            binding.tvTop.setBackground(gradient);
        }

        {
            GradientDrawable gradient = new GradientDrawable();
            gradient.setColors(
                    new int[] {
                        Color.TRANSPARENT, Color.TRANSPARENT, SurfaceColors.SURFACE_0.getColor(C)
                    });
            binding.linBottomTools.setBackground(gradient);
        }
    }

    private int cycle = 1;

    private void notifyProcessFinished(String fileOrFolder, String typeEvent) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(C, "process_cm")
                        .setSmallIcon(R.drawable.androidpe_app_icon_dark)
                        .setContentTitle(fileOrFolder + " : " + typeEvent)
                        .setContentText(typeEvent + " : " + C.getString(R.string.finished))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(C);
        int notificationId = cycle;
        notificationManager.notify(notificationId, builder.build());
        C.runOnUiThread(
                () -> {
                    Toast.makeText(C, R.string.saved, Toast.LENGTH_SHORT).show();
                });
        cycle++;
    }

    private void notifyProcessError() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(C, "process_cm")
                        .setSmallIcon(R.drawable.androidpe_app_icon_dark)
                        .setContentTitle("Archiving Error")
                        .setContentText(C.getString(R.string.msg_error_archiving_process))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(C);
        int notificationId = cycle;
        notificationManager.notify(notificationId, builder.build());
    }

    public void setOnAnyChangeDetected(OnAnyChangeDetected listener) {
        this.listener = listener;
    }

    public interface OnAnyChangeDetected {
        public void onFileClick(@NonNull View view, @NonNull String path);

        public void onPathChanged(String path);
    }

    private interface OnSelectModeListener {
        void onSelect();

        void selectAll();
    }

    public void setOnRequestListener(OnRequestListener listener) {
        this.listenerRequest = listener;
    }

    public interface OnRequestListener {
        public void onRefresh();
    }
}
