package jkas.androidpe.explorer.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVGImageView;
import com.google.android.material.elevation.SurfaceColors;

import jkas.androidpe.explorer.dialog.DialogDeletePaths;
import jkas.androidpe.explorer.dialog.DialogRename;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.Images;
import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * @author JKas
 */
public class ExplorerViewAdapter extends RecyclerView.Adapter<ExplorerViewAdapter.ItemViewHolder> {
    private FragmentActivity C;

    private final String SRC = "Explorer";
    private boolean selectMode = false;
    private boolean selectAll = false;
    private boolean copyCutClicked = false;

    private OnRequestListener listenerRequest;
    private OnAnyChangeDetected listener;

    private ArrayList<String> listPathSelected = new ArrayList<>();
    private ArrayList<String> fileList = new ArrayList<>();

    private String PATH;

    public ExplorerViewAdapter(FragmentActivity c) {
        super();
        C = c;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                jkas.androidpe.explorer.R.layout.layout_folder_file_selected2,
                                null);
        view.setLayoutParams(CodeUtil.getLayoutParamsMW(16));
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ItemViewHolder holder, int position) {
        final String currentPath = fileList.get(position);
        holder.tv1.setText(Files.getNameFromAbsolutePath(currentPath));
        holder.tv2.setText(Files.getFileSize(currentPath));
        if (currentPath.toLowerCase().endsWith(".svg")) {
            Images.setImageFromDirSvgFile(currentPath, holder.iconSvg);
            holder.icon.setVisibility(View.GONE);
            holder.iconSvg.setVisibility(View.VISIBLE);
        } else {
            ResCodeUtils.setImage(holder.icon, currentPath);
            holder.icon.setVisibility(View.VISIBLE);
            holder.iconSvg.setVisibility(View.GONE);
        }
        holder.ic_more.setOnClickListener(v -> showMoreSettings(v, currentPath, holder));
        holder.view.setOnClickListener(
                v -> {
                    if (copyCutClicked)
                        if (Files.isFile(currentPath)) {
                            Toast.makeText(C, R.string.not_a_folder, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    if (selectMode) performCheckChanged(currentPath, holder.cb);
                    else if (Files.isDirectory(currentPath)) init(currentPath);
                    else listener.onFileClick(v, currentPath);
                });

        holder.cb.setChecked(selectAll);
        holder.cb.setOnClickListener(
                (v) -> {
                    performCheckChanged(currentPath, holder.cb);
                });

        if (selectMode) {
            holder.cb.setVisibility(View.VISIBLE);
            holder.ic_more.setVisibility(View.GONE);
            Executors.newSingleThreadExecutor()
                    .execute(
                            () -> {
                                for (String s : listPathSelected)
                                    if (s.equals(currentPath)) {
                                        C.runOnUiThread(() -> holder.cb.setChecked(true));
                                        break;
                                    }
                            });
        } else {
            holder.cb.setVisibility(View.GONE);
            holder.ic_more.setVisibility(View.VISIBLE);
            holder.cb.setChecked(false);
        }

        if (Files.getNameFromAbsolutePath(currentPath).startsWith(".")) holder.icon.setAlpha(0.5f);
        else holder.icon.setAlpha(1.0f);

        if (Files.isDirectory(currentPath))
            Executors.newSingleThreadExecutor()
                    .execute(
                            () -> {
                                int sizeFolders = Files.listDir(currentPath).size();
                                int sizeFiles = Files.listFile(currentPath).size();
                                C.runOnUiThread(
                                        () -> {
                                            holder.tv2.setText(
                                                    C.getString(R.string.folder)
                                                            + " : "
                                                            + sizeFolders
                                                            + "  |  "
                                                            + C.getString(R.string.file)
                                                            + " : "
                                                            + sizeFiles);
                                        });
                            });
    }

    // lacal String var for path to file type (folder/file)
    String path2File = "";

    // for showMoreSettings method
    private void showMoreSettings(View view, final String path2FF, final ItemViewHolder holder) {
        path2File = path2FF;
        final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu
                .getMenu()
                .add(Menu.NONE, 1, Menu.NONE, R.string.copy_path)
                .setIcon(R.drawable.ic_copy_content);
        popupMenu
                .getMenu()
                .add(Menu.NONE, 2, Menu.NONE, R.string.delete)
                .setIcon(R.drawable.ic_delete);
        if (Files.isFile(path2File))
            popupMenu
                    .getMenu()
                    .add(Menu.NONE, 3, Menu.NONE, R.string.open_with)
                    .setIcon(R.drawable.ic_share);
        popupMenu
                .getMenu()
                .add(Menu.NONE, 4, Menu.NONE, R.string.rename)
                .setIcon(R.drawable.ic_edit);
        popupMenu.setOnMenuItemClickListener(
                (item) -> {
                    switch (item.getItemId()) {
                        case 1:
                            CodeUtil.copyTextToClipBoard(C, path2File);
                            break;
                        case 2:
                            final DialogDeletePaths ddp = new DialogDeletePaths(C);
                            ddp.setPath(path2FF);
                            ddp.show();
                            ddp.setOnDeleteDetected(
                                    new DialogDeletePaths.OnDeleteDetected() {

                                        @Override
                                        public void onDelete(String p, int status) {
                                            if (status == DialogDeletePaths.DELETED)
                                                Logger.success(
                                                        SRC,
                                                        p
                                                                + " ("
                                                                + C.getString(R.string.deleted)
                                                                + ")");
                                            else notifyProcessError();
                                        }

                                        @Override
                                        public void onFinish() {
                                            notifyProcessFinished(
                                                    Files.getNameFromAbsolutePath(path2File),
                                                    C.getString(R.string.deleting));
                                            init(PATH);
                                        }
                                    });
                            break;
                        case 3:
                            Files.openFileWithOtherApp(C, path2File);
                            break;
                        case 4:
                            new DialogRename(C, path2File)
                                    .setOnChangeDetected(
                                            (path, name) -> {
                                                if (name != null) {
                                                    holder.tv1.setText(name);
                                                    Logger.success(
                                                            SRC, C.getString(R.string.saved));
                                                    init(PATH);
                                                } else {
                                                    Logger.info(
                                                            SRC,
                                                            C.getString(R.string.cant_be_modify));
                                                }
                                            });
                            break;
                    }
                    return true;
                });

        popupMenu.setForceShowIcon(true);
        popupMenu.show();
    }

    private void performCheckChanged(final String path, final CheckBox cb) {
        if (!selectMode) return;
        if (!copyCutClicked)
            if (cb.isChecked()) {
                if (!listPathSelected.contains(path)) listPathSelected.add(path);
            } else
                for (int i = 0; i < listPathSelected.size(); i++)
                    if (listPathSelected.get(i).equals(path)) {
                        listPathSelected.remove(i);
                        break;
                    }
    }

    public void init(String path) {
        PATH = path;
        listener.onPathChanged(path);
        fileList.clear();
        fileList.addAll(Files.listDir(path));
        fileList.addAll(Files.listFile(path));
        notifyDataSetChanged();
    }

    public void setSelectMode(boolean s) {
        listPathSelected.clear();
        if (PATH == null) return;
        selectMode = s;
        copyCutClicked = false;
        selectAll = false;
        init(PATH);
    }

    public void setSelectAll() {
        listPathSelected.clear();
        if (PATH == null) return;
        if (!selectMode) {
            setSelectMode(true);
            return;
        }
        selectAll = !selectAll;
        if (selectAll) {
            listPathSelected.addAll(Files.listDir(PATH));
            listPathSelected.addAll(Files.listFile(PATH));
        }
        init(PATH);
    }

    public void copyCutClicked() {
        if (PATH == null) return;
        selectMode = false;
        selectAll = false;
        copyCutClicked = true;
        fileList.clear();
        fileList.addAll(Files.listDir(PATH));
        fileList.addAll(Files.listFile(PATH));
        notifyDataSetChanged();
    }

    public boolean getCopyCutClicked() {
        return copyCutClicked;
    }

    public boolean getSelectAll() {
        return selectAll;
    }

    public boolean getSelectMode() {
        return selectMode;
    }

    public ArrayList<String> getAllSelectedPath() {
        return listPathSelected;
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon, ic_more;
        SVGImageView iconSvg;
        TextView tv1, tv2;
        CheckBox cb;
        View view;

        public ItemViewHolder(View view) {
            super(view);
            this.view = view;
            icon = view.findViewById(jkas.androidpe.explorer.R.id.icon);
            iconSvg = view.findViewById(jkas.androidpe.explorer.R.id.iconSVG);
            tv1 = view.findViewById(jkas.androidpe.explorer.R.id.tv1);
            tv2 = view.findViewById(jkas.androidpe.explorer.R.id.tv2);
            cb = view.findViewById(jkas.androidpe.explorer.R.id.cb);
            ic_more = view.findViewById(jkas.androidpe.explorer.R.id.ic_more);
            initBG();
        }

        private void initBG() {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(SurfaceColors.SURFACE_2.getColor(view.getContext()));
            gd.setCornerRadius(20f);
            view.setBackground(gd);
            view.setElevation(5f);
        }
    }

    /** Only NOTIFICATIONS */
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
        public void onFileClick(View view, String path);

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
