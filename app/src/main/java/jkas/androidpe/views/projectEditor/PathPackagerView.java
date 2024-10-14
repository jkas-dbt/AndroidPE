package jkas.androidpe.views.projectEditor;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.elevation.SurfaceColors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jkas.androidpe.activities.CodeEditorActivity;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.androidpe.resources.R;
import jkas.androidpe.databinding.LayoutPeMainSclPkgBinding;
import jkas.androidpe.databinding.LayoutPeMainSclPkgViewDataBinding;
import jkas.androidpe.projectUtils.utils.PathPackager;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class PathPackagerView {
    private final String SRC = "Main Editor";
    public static int SOURCE_CODE = 0;
    public static int LAYOUTS = 1;
    private FragmentActivity C;
    private PathPackager PP;
    private LayoutPeMainSclPkgBinding binding;
    private View view;
    private int currentType;

    public PathPackagerView(FragmentActivity c, PathPackager pp, int type) {
        C = c;
        PP = pp;
        currentType = type;
        init();
    }

    public View getView() {
        view.setLayoutParams(CodeUtil.getLayoutParamsMW(8));
        return view;
    }

    private void init() {
        binding = LayoutPeMainSclPkgBinding.inflate(LayoutInflater.from(C));
        view = (View) binding.getRoot();
        view.setBackgroundTintList(ColorStateList.valueOf(SurfaceColors.SURFACE_3.getColor(C)));
        binding.icDataViewer.setImageTintList(
                ColorStateList.valueOf(
                        ResCodeUtils.getColorFromResolveAttribute(
                                C, com.google.android.material.R.attr.colorOnSurface)));
        initData();
        iniEvents();
    }

    private void iniEvents() {
        binding.linExpendable.setOnClickListener(
                (v) -> {
                    if (binding.linData.getVisibility() == View.GONE) {
                        binding.linData.setVisibility(View.VISIBLE);
                        binding.icDataViewer.setRotation(180f);
                        loadFiles();
                    } else {
                        binding.linData.removeAllViews();
                        binding.linData.setVisibility(View.GONE);
                        binding.icDataViewer.setRotation(0f);
                    }
                });
    }

    private void loadFiles() {
        binding.linData.removeAllViews();

        ExecutorService ES = Executors.newSingleThreadExecutor();
        ES.execute(
                () -> {
                    for (String s : PP.getListFile()) {
                        if (!Files.isFile(s)) continue;
                        ViewData vd = new ViewData(s);
                        C.runOnUiThread(
                                () -> {
                                    binding.linData.addView(vd.getView());
                                });
                    }
                });
        ES.shutdown();
    }

    private void initData() {
        try {
            C.runOnUiThread(
                    () -> {
                        binding.tvTitle.setText(
                                PP.getPackage().isEmpty() ? "[root]" : PP.getPackage());
                    });
        } catch (Exception e) {
            Logger.warn(SRC, C.getString(R.string.msg_error_when_parsing_values), e.getMessage());
        }
    }

    private class ViewData {
        private LayoutPeMainSclPkgViewDataBinding bind;
        private String fullPathOfFile;

        public ViewData(String filePath) {
            fullPathOfFile = filePath;
            bind = LayoutPeMainSclPkgViewDataBinding.inflate(LayoutInflater.from(C));
            ResCodeUtils.setImage(bind.icIcon, fullPathOfFile);
            C.runOnUiThread(
                    () -> {
                        bind.tv1.setText(
                                Files.getNameFromAbsolutePath(
                                        Files.getNameFromAbsolutePath(fullPathOfFile)));
                        bind.tv2.setText(Files.getFileSize(fullPathOfFile));
                    });
            bind.getRoot()
                    .setOnClickListener(
                            (v) -> {
                                DataRefManager.getInstance().filePathSelected = fullPathOfFile;
                                CodeUtil.startActivity(C, CodeEditorActivity.class);
                            });
        }

        public View getView() {
            bind.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(8));
            return (View) bind.getRoot();
        }
    }
}
