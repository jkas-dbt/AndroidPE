package jkas.androidpe.explorer.views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.text.HtmlCompat;
import jkas.androidpe.explorer.CodeEditorExplorer;
import jkas.androidpe.explorer.databinding.TreeNodeViewHolderBinding;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import android.content.res.ColorStateList;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.utils.ViewUtils;
import jkas.codeUtil.Files;
import android.view.Menu;
import jkas.codeUtil.CodeUtil;
import jkas.androidpe.explorer.dialog.DialogRename;
import jkas.androidpe.explorer.dialog.DialogDeletePaths;

/**
 * @author JKas
 */
public class TreeViewFile {
    private OnAnyEventListener listener;
    private Context C;
    private PopupMenu menu;
    private String path;
    private View view;
    private TreeNodeViewHolderBinding binding;
    private int explorerType;

    public TreeViewFile(Context C, String path, int explorerType) {
        this.C = C;
        this.path = path;
        this.explorerType = explorerType;
        this.binding = TreeNodeViewHolderBinding.inflate(LayoutInflater.from(C));
        this.view = (View) binding.getRoot();

        init();
        events();
    }

    private void events() {
        binding.linView.setOnClickListener(v -> listener.onClick(path));
        binding.linView.setOnLongClickListener(
                v -> {
                    showMenu();
                    return true;
                });
    }

    public void setText(String txt) {
        binding.tvDesc.setText(HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

    public ImageView getIcon() {
        return binding.icIcon;
    }

    private void init() {
        binding.vfChevron.setVisibility(View.INVISIBLE);
        // binding.icChevron.setVisibility(View.INVISIBLE);
        if (explorerType == CodeEditorExplorer.TYPE_PROJECT) initDataPopupMenuProject();
        else initDataPopupMenuAndroid();

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
                .add(Menu.NONE, 1, Menu.NONE, R.string.open_with)
                .setIcon(R.drawable.ic_share);
        // .setIconTintList(colorList);
        menu.getMenu()
                .add(Menu.NONE, 2, Menu.NONE, R.string.copy_path)
                .setIcon(R.drawable.ic_copy_content);
        // .setIconTintList(colorList);
        menu.getMenu().add(Menu.NONE, 3, Menu.NONE, R.string.rename).setIcon(R.drawable.ic_edit);
        // .setIconTintList(colorList);
        menu.getMenu().add(Menu.NONE, 4, Menu.NONE, R.string.delete).setIcon(R.drawable.ic_delete);
        // .setIconTintList(colorList);
        menu.setOnMenuItemClickListener(
                menuItem -> {
                    if (menuItem.getItemId() == 1) Files.openFileWithOtherApp(C, path);
                    else if (menuItem.getItemId() == 2) CodeUtil.copyTextToClipBoard(C, path);
                    else if (menuItem.getItemId() == 3) {
                        final String oldPath = path;
                        new DialogRename(C, path)
                                .setOnChangeDetected(
                                        (finalPath, fileName) -> {
                                            if (fileName != null) {
                                                binding.tvDesc.setText(fileName);
                                                path = finalPath;
                                                listener.onRename(oldPath, finalPath);
                                            }
                                        });
                    } else if (menuItem.getItemId() == 4) {
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
        public void onClick(String path);

        public void onRename(String oldPath, String newPath);

        public void onDelete(String path);
    }
}
