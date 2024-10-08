package jkas.androidpe.explorer.views;

import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.widget.PopupMenu;
import android.view.View;
import androidx.core.text.HtmlCompat;
import jkas.androidpe.explorer.CodeEditorExplorer;
import jkas.androidpe.explorer.databinding.TreeNodeViewHolderBinding;
import android.view.LayoutInflater;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import android.content.res.ColorStateList;
import jkas.androidpe.resources.R;
import android.view.Menu;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.androidpe.resourcesUtils.utils.ViewUtils;
import jkas.codeUtil.CodeUtil;
import jkas.androidpe.explorer.dialog.DialogNewFile;
import jkas.androidpe.explorer.dialog.DialogNewFolder;
import jkas.androidpe.explorer.dialog.DialogRename;
import jkas.androidpe.explorer.dialog.DialogDeletePaths;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class TreeViewFolder {
    private OnAnyEventListener listener;
    private Context C;
    private PopupMenu menu;
    private String path;
    private View view;
    private TreeNodeViewHolderBinding binding;
    private int explorerType;
    public boolean opened;

    public TreeViewFolder(Context C, String path, int explorerType) {
        this.C = C;
        this.path = path;
        this.explorerType = explorerType;
        binding = TreeNodeViewHolderBinding.inflate(LayoutInflater.from(C));
        this.view = (View) binding.getRoot();
        this.opened = false;

        init();
        events();
    }

    private void resetData() {
        binding.linChild.removeAllViews();
    }

    private void events() {
        binding.linView.setOnClickListener(
                v -> {
                    opened = !opened;
                    resetData();
                    if (opened) listener.onLoad(path);
                    updateChevron();
                });

        binding.linView.setOnLongClickListener(
                v -> {
                    showMenu();
                    return true;
                });
    }

    public void addChild(View v) {
        binding.linChild.addView(v);
    }

    public void setLoading(boolean load) {
        binding.vfChevron.setDisplayedChild(load ? 1 : 0);
        updateChevron();
    }

    private void updateChevron() {
        binding.icChevron.setImageResource(
                opened ? R.drawable.ic_chevron_down : R.drawable.ic_chevron_right);
        binding.icIcon.setImageResource(opened ? R.drawable.ic_folder_open : R.drawable.ic_folder);

        if (opened) {
            binding.icIcon.setImageTintList(
                    ColorStateList.valueOf(ResourcesValuesFixer.getColor(C, "?colorPrimary")));
        } else {
            binding.icIcon.setImageTintList(
                    ColorStateList.valueOf(ResourcesValuesFixer.getColor(C, "?colorOnSurface")));
        }
    }

    public void setText(String txt) {
        binding.tvDesc.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

    public String getPath() {
        return path;
    }

    private void init() {
        if (explorerType == CodeEditorExplorer.TYPE_PROJECT) initDataPopupMenuProject();
        else initDataPopupMenuAndroid();

        // default values
        binding.icIcon.setImageResource(R.drawable.ic_folder);
        binding.vfChevron.setDisplayedChild(0);

        ViewUtils.setBackgroundRipple(binding.linView, Color.TRANSPARENT, Color.GRAY);
    }

    private void initDataPopupMenuAndroid() {
        // TODO: Implement this method
    }

    private void initDataPopupMenuProject() {
        int colorIcon = ResCodeUtils.getColorFromResolveAttribute(C, android.R.attr.colorPrimary);
        ColorStateList colorList =
                ColorStateList.valueOf(ResCodeUtils.getColorFromResolveAttribute(C, colorIcon));
        menu = new PopupMenu(C, view);
        menu.setForceShowIcon(true);
        menu.getMenu()
                .add(Menu.NONE, 1, Menu.NONE, R.string.copy_path)
                .setIcon(R.drawable.ic_copy_content);
        // .setIconTintList(colorList);
        if (!path.matches(".*" + ProjectsPathUtils.RES_PATH))
            menu.getMenu()
                    .add(Menu.NONE, 2, Menu.NONE, R.string.new_file)
                    .setIcon(R.drawable.ic_file_txt);
        // .setIconTintList(colorList);
        menu.getMenu()
                .add(Menu.NONE, 3, Menu.NONE, R.string.new_folder)
                .setIcon(R.drawable.ic_folder);
        // .setIconTintList(colorList);
        menu.getMenu().add(Menu.NONE, 4, Menu.NONE, R.string.rename).setIcon(R.drawable.ic_edit);
        // .setIconTintList(colorList);
        menu.getMenu().add(Menu.NONE, 5, Menu.NONE, R.string.delete).setIcon(R.drawable.ic_delete);
        // .setIconTintList(colorList);
        menu.setOnMenuItemClickListener(
                menuItem -> {
                    if (menuItem.getItemId() == 1) CodeUtil.copyTextToClipBoard(C, path);
                    else if (menuItem.getItemId() == 2) {
                        new DialogNewFile(C, path)
                                .setOnCreateListener(
                                        (fileName) -> {
                                            resetData();
                                            listener.onLoad(path);
                                        });
                    } else if (menuItem.getItemId() == 3) {
                        new DialogNewFolder(C, path)
                                .setOnChangeDetected(
                                        (fileName) -> {
                                            resetData();
                                            listener.onLoad(path);
                                        });
                    } else if (menuItem.getItemId() == 4) {
                        final String oldPath = this.path;
                        new DialogRename(C, path)
                                .setOnChangeDetected(
                                        (finalPath, folderName) -> {
                                            path = finalPath;
                                            binding.tvDesc.setText(
                                                    Files.getNameFromAbsolutePath(path));
                                            if (folderName != null)
                                                listener.onRename(oldPath, path);
                                        });
                    } else if (menuItem.getItemId() == 5) {
                        final DialogDeletePaths ddp = new DialogDeletePaths(C);
                        ddp.setPath(path);
                        ddp.setOnDeleteDetected(
                                new DialogDeletePaths.OnDeleteDetected() {

                                    @Override
                                    public void onDelete(String path, int processStatus) {
                                        listener.onDelete(path);
                                        view.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFinish() {
                                        view.setVisibility(View.GONE);
                                    }
                                });
                        ddp.show();
                    }
                    return true;
                });
    }

    private void showMenu() {
        menu.show();
    }

    public View getView() {
        return view;
    }

    public void setOnAnyEventListener(OnAnyEventListener listener) {
        this.listener = listener;
    }

    public interface OnAnyEventListener {
        public void onLoad(String path);

        public void onRename(String oldPath, String newPath);

        public void onDelete(String path);
    }
}
