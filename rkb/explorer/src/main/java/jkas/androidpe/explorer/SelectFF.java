package jkas.androidpe.explorer;

import android.app.*;
import android.content.Context;
import android.graphics.Rect;
import android.os.*;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import jkas.androidpe.explorer.adapter.SelectorViewAdapter;
import jkas.androidpe.explorer.databinding.LayoutSelectFolderOrFileBinding;
import jkas.androidpe.resources.R;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import java.util.ArrayList;

/**
 * @author JKas
 */
public class SelectFF {
    private OnSaveListener listener;

    public static int NOT_SELECTABLE = 0;
    public static int FOLDER_SELECTOR = 1;
    public static int FILE_SELECTOR = 2;
    public static int SELECT_SINGLE = 1;
    public static int SELECT_MULTIPLE = 2;

    public LayoutSelectFolderOrFileBinding binding;
    private AppCompatActivity C;
    private View V;
    private BottomSheetDialog alert;
    private String folderPath = Files.getExternalStorageDir();
    private String rootPath = Files.getExternalStorageDir();
    private SelectorViewAdapter adapter;
    private ArrayList<String> listPath = new ArrayList<>();

    public SelectFF(Context c) {
        C = (AppCompatActivity) c;
        ini();
        loadListener();
        onClick();
    }

    private void loadListener() {
        adapter.setOnFilesSelected(
                new SelectorViewAdapter.OnFilesSelected() {
                    @Override
                    public void onFileSelected(String path) {
                        listPath.clear();
                        listPath.add(path);
                        listener.onDataSaved(listPath);
                        alert.cancel();
                    }

                    @Override
                    public void onPathChanged(String path) {
                        binding.tvInfo.setText(path.replace("/", " ⟩ "));
                        folderPath = path;
                        int margin = 200;
                        binding.recyclerView.addItemDecoration(
                                new BottomMarginItemDecoration(margin));
                    }
                });
    }

    private void onClick() {
        binding.cardGoBack.setOnClickListener(
                (v) -> {
                    try {
                        if (!folderPath.equals(rootPath)) {
                            folderPath = Files.getPrefixPath(folderPath);
                            loadData(folderPath);
                        }
                    } catch (Exception e) {
                        Toast.makeText(C, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        binding.cardSave.setOnClickListener(
                (v1) -> {
                    try {
                        SelectFF.this.listPath = adapter.getAllSelectedPath();
                        listener.onDataSaved(listPath);
                        alert.cancel();
                    } catch (Exception e) {
                        Toast.makeText(C, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void ini() {
        binding = LayoutSelectFolderOrFileBinding.inflate(LayoutInflater.from(C));
        V = binding.getRoot();
        V.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, CodeUtil.getDisplayHeightPixels(C)));
        alert = new BottomSheetDialog(C);
        alert.setContentView(V);
        BottomSheetBehavior behavior = alert.getBehavior();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setDraggable(false);

        if (!CodeUtil.isTheSystemThemeDark(C))
            binding.lottieAnim.setAnimation("icons/lottie/androidpe_icon_dark.json");
        else binding.lottieAnim.setAnimation("icons/lottie/androidpe_icon_light.json");
        binding.lottieAnim.setSpeed(3f);

        adapter = new SelectorViewAdapter(C);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(C));
        binding.recyclerView.setAdapter(adapter);
    }

    public void setSelectMode(int sMode) {
        adapter.setSelectorMode(sMode);
        if (sMode == SELECT_SINGLE) binding.cardSave.setVisibility(View.GONE);
        else binding.cardSave.setVisibility(View.VISIBLE);
    }

    public void lookRoot(String path) {
        rootPath = path;
        folderPath = path;
    }

    public String getLookRoot() {
        return rootPath;
    }

    public void setSelectorType(int type) {
        adapter.setSelectType(type);
    }

    public void attachListSelected(final ArrayList<String> list) {
        listPath.clear();
        listPath.addAll(list);
    }

    public void loadDataFromStorageRoot() {
        folderPath = Files.getExternalStorageDir();
        loadData(folderPath);
    }

    public void loadData() {
        loadData(folderPath);
    }

    private void loadData(String path) {
        if (!Files.isDirectory(path)) {
            Toast.makeText(C, C.getString(R.string.cant_access), Toast.LENGTH_LONG).show();
            Toast.makeText(C, C.getString(R.string.deleted_moved_or_renamed), Toast.LENGTH_LONG)
                    .show();
            alert.cancel();
            return;
        }
        adapter.init(path);
    }

    public void showView() {
        alert.show();
    }

    public class BottomMarginItemDecoration extends RecyclerView.ItemDecoration {
        private int margin;

        public BottomMarginItemDecoration(int margin) {
            this.margin = margin;
        }

        @Override
        public void getItemOffsets(
                Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = margin;
            }
        }
    }

    public void setOnSaveListener(OnSaveListener listener) {
        this.listener = listener;
    }

    public interface OnSaveListener {
        public void onDataSaved(final ArrayList<String> excludedPaths);
    }
}
