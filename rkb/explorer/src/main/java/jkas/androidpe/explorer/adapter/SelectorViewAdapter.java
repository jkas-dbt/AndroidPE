package jkas.androidpe.explorer.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.view.View;
import com.google.android.material.elevation.SurfaceColors;
import java.util.ArrayList;
import android.view.LayoutInflater;
import java.util.concurrent.Executors;
import jkas.androidpe.explorer.SelectFF;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resources.R;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class SelectorViewAdapter extends RecyclerView.Adapter<SelectorViewAdapter.ItemViewHolder> {
    private FragmentActivity C;

    private String folderPath = Files.getExternalStorageDir();
    private String rootPath = Files.getExternalStorageDir();

    private int selectMode;
    private int selectType;

    private OnFilesSelected listener;

    private ArrayList<String> listPathSelected = new ArrayList<>();
    private ArrayList<String> fileList = new ArrayList<>();

    private String PATH;

    public SelectorViewAdapter(FragmentActivity c) {
        super();
        C = c;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                jkas.androidpe.explorer.R.layout.layout_folder_file_selected, null);
        view.setLayoutParams(CodeUtil.getLayoutParamsMW(16));
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ItemViewHolder holder, int position) {
        final String currentPath = fileList.get(position);
        holder.tv1.setText(Files.getNameFromAbsolutePath(currentPath));
        holder.tv2.setText(Files.getFileSize(currentPath));
        ResCodeUtils.setImage(holder.icon, currentPath);

        holder.cb.setVisibility(View.GONE);
        holder.cb.setChecked(false);
        if (selectType != SelectFF.NOT_SELECTABLE)
            if (selectType == SelectFF.FOLDER_SELECTOR)
                if (Files.isDirectory(currentPath)) holder.cb.setVisibility(View.VISIBLE);
                else holder.cb.setVisibility(View.GONE);
            else if (selectType == SelectFF.FILE_SELECTOR)
                if (Files.isFile(currentPath)) holder.cb.setVisibility(View.VISIBLE);
                else holder.cb.setVisibility(View.GONE);

        if (holder.cb.getVisibility() == View.VISIBLE)
            for (String p : listPathSelected)
                if (p.equals(currentPath)) {
                    holder.cb.setChecked(true);
                    return;
                }

        holder.view.setOnClickListener(
                (v) -> {
                    if (Files.isDirectory(currentPath))
                        if (!holder.cb.isChecked()) init(currentPath);
                        else Toast.makeText(C, R.string.selected, Toast.LENGTH_SHORT).show();
                });

        holder.cb.setOnClickListener(
                (v) -> {
                    if (selectMode == SelectFF.SELECT_SINGLE) {
                        listener.onFileSelected(currentPath);
                        return;
                    }

                    if (holder.cb.isChecked()) {
                        if (!listPathSelected.contains(currentPath))
                            listPathSelected.add(currentPath);
                    } else
                        for (int i = 0; i < listPathSelected.size(); i++)
                            if (listPathSelected.get(i).equals(currentPath)) {
                                listPathSelected.remove(i);
                                break;
                            }
                });

        if (Files.getNameFromAbsolutePath(currentPath).startsWith(".")) {
            holder.icon.setAlpha(0.5f);
        } else {
            holder.icon.setAlpha(1.0f);
        }

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

    public void init(String path) {
        PATH = path;
        listener.onPathChanged(path);
        fileList.clear();
        fileList.addAll(Files.listDir(path));
        fileList.addAll(Files.listFile(path));
        notifyDataSetChanged();
    }

    public void setListSelected(ArrayList<String> listSelected) {
        listPathSelected = listSelected;
    }

    public void setSelectorMode(int selectorMode) {
        selectMode = selectorMode;
    }

    public void setSelectType(int typeSelector) {
        selectType = typeSelector;
    }

    public ArrayList<String> getAllSelectedPath() {
        return listPathSelected;
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView tv1, tv2;
        CheckBox cb;
        View view;

        public ItemViewHolder(View view) {
            super(view);
            this.view = view;
            icon = view.findViewById(jkas.androidpe.explorer.R.id.icon);
            tv1 = view.findViewById(jkas.androidpe.explorer.R.id.tv1);
            tv2 = view.findViewById(jkas.androidpe.explorer.R.id.tv2);
            cb = view.findViewById(jkas.androidpe.explorer.R.id.cb);
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

    public void setOnFilesSelected(OnFilesSelected listener) {
        this.listener = listener;
    }

    public interface OnFilesSelected {
        public void onFileSelected(@NonNull String path);

        public void onPathChanged(@NonNull String path);
    }
}
