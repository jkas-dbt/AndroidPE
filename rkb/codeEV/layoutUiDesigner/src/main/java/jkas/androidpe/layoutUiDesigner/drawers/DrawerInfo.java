package jkas.androidpe.layoutUiDesigner.drawers;

import android.content.Context;
import androidx.drawerlayout.widget.DrawerLayout;
import android.widget.LinearLayout;
import android.view.View;
import android.view.LayoutInflater;
import jkas.androidpe.layoutUiDesigner.databinding.LayoutInfoBinding;
import jkas.androidpe.layoutUiDesigner.databinding.LayoutInfoItemBinding;
import jkas.androidpe.logger.LogMsg;
import jkas.androidpe.logger.LoggerLayoutUI;
import jkas.androidpe.resourcesUtils.utils.ViewUtils;
import jkas.codeUtil.CodeUtil;

/**
 * @author JKas
 */
public class DrawerInfo {
    private String TAG = "";
    private Context C;
    private LayoutInfoBinding binding;

    public DrawerLayout drawerLayout;
    public LinearLayout mainView;

    public DrawerInfo(Context c, LinearLayout mainView, DrawerLayout drawerLayout, String tag) {
        this.C = c;
        this.mainView = mainView;
        this.drawerLayout = drawerLayout;
        this.TAG = tag;

        init();
        events();
    }

    public View getView() {
        return binding.getRoot();
    }

    private void events() {
        LoggerLayoutUI.get(TAG)
                .setLogListener(logMsg -> binding.linInfo.addView(InfoView.getView(C, logMsg), 0));
        binding.toggleButton.addOnButtonCheckedListener(
                (mbt, idBtn, checked) -> {
                    if (!checked) return;
                    if (idBtn == binding.btnTreeView.getId())
                        binding.viewFlipper.setDisplayedChild(0);
                    else binding.viewFlipper.setDisplayedChild(1);
                });
    }

    public void clearInfo() {
        binding.linInfo.removeAllViews();
    }

    public void showTreeView(View v) {
        binding.linTree.removeAllViews();
        binding.linTree.addView(v);
    }

    public void showTree() {
        binding.toggleButton.check(binding.btnTreeView.getId());
    }

    public void showInfo() {
        binding.toggleButton.check(binding.btnInfo.getId());
    }

    private void init() {
        binding = LayoutInfoBinding.inflate(LayoutInflater.from(C));
    }

    private static class InfoView {
        public static View getView(Context C, LogMsg logMsg) {
            LayoutInfoItemBinding binding = LayoutInfoItemBinding.inflate(LayoutInflater.from(C));
            binding.tvLevel.setText(logMsg.level);
            binding.tvSrc.setText(logMsg.src);
            binding.tvMsg.setText(logMsg.message);
            ViewUtils.setBgCornerRadius(binding.tvLevel, C.getColor(logMsg.resColor));
            binding.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(16));
            return binding.getRoot();
        }
    }
}
