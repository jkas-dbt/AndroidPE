package jkas.androidpe.fragments.projectEditor;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import jkas.androidpe.databinding.FragmentPeMainBinding;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.resources.R;
import jkas.androidpe.projectUtils.utils.PathPackager;
import jkas.androidpe.projectUtils.utils.PathPackagerBuilder;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.androidpe.views.projectEditor.PathPackagerView;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class MainFragment extends Fragment {
    private final String SRC = "Main Editor";
    private FragmentActivity C;
    private FragmentPeMainBinding binding;
    private ArrayList<Element> listActivities;

    private int rkbCounterPKG, rkbCounterFILES;
    private int rkbCounterPKG2, rkbCounterFILES2;
    
    @Override
    public View onCreateView(
            LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        C = getActivity();
        binding = FragmentPeMainBinding.inflate(_inflater);
        init();
        return binding.getRoot();
    }

    @Override
    @MainThread
    @CallSuper
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void refresh() {
        if (isVisible() && binding != null) loadData();
    }

    private void loadData() {
        setAllDataEmpty();
        if (DataRefManager.getInstance().currentAndroidModule == null) {
            Logger.error(SRC, "Data cannot be loaded. No modules are selected.");
            return;
        }
        Executors.newSingleThreadExecutor()
                .execute(
                        () -> {
                            loadDataActivity();
                            loadDataLayout();
                        });
    }

    public void setAllDataEmpty() {
        binding.linDataSC.removeAllViews();
        binding.linDataLayouts.removeAllViews();
        binding.tvFilesPkgCount.setText(getString(R.string.packages) + " : 0");
        binding.tvFilesCount.setText(getString(R.string.file) + " : 0");
        binding.tvFilesPkgCount2.setText(getString(R.string.packages) + " : 0");
        binding.tvFilesCount2.setText(getString(R.string.file) + " : 0");
    }

    private void loadDataLayout() {
        try {
            String path =
                    DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath()
                            + ProjectsPathUtils.RES_PATH;
            PathPackagerBuilder PPB = new PathPackagerBuilder();
            PPB.setRootResDir(path);
            PPB.searchListLayoutFiles();

            rkbCounterPKG2 = 0;
            rkbCounterFILES2 = 0;

            for (PathPackager PP : PPB.getListLayoutFiles()) {
                PathPackagerView PPV = new PathPackagerView(C, PP, PathPackagerView.LAYOUTS);
                C.runOnUiThread(
                        () -> {
                            binding.linDataLayouts.addView(PPV.getView());
                            binding.tvFilesPkgCount2.setText(
                                    getString(R.string.packages) + " : " + rkbCounterPKG2);
                            binding.tvFilesCount2.setText(
                                    getString(R.string.file) + " : " + rkbCounterFILES2);
                        });
                rkbCounterPKG2++;
                rkbCounterFILES2 += PP.getListFile().size();
            }
        } catch (Exception e) {
            Logger.warn(SRC, C.getString(R.string.msg_error_when_parsing_values), e.getMessage());
        }
    }

    private void loadDataActivity() {
        final ArrayList<String> listPkg = new ArrayList<>();
        try {
            String prefixPath =
                    DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath();
            if (Files.isDirectory(prefixPath + ProjectsPathUtils.JAVA_PATH))
                listPkg.add(prefixPath + ProjectsPathUtils.JAVA_PATH);
            if (Files.isDirectory(prefixPath + ProjectsPathUtils.KOTLIN_PATH))
                listPkg.add(prefixPath + ProjectsPathUtils.KOTLIN_PATH);
            if (Files.isDirectory(prefixPath + ProjectsPathUtils.CPP_PATH))
                listPkg.add(prefixPath + ProjectsPathUtils.CPP_PATH);

            rkbCounterPKG = 0;
            rkbCounterFILES = 0;

            for (String path : listPkg) {
                PathPackagerBuilder PPB = new PathPackagerBuilder();
                PPB.setRootPkgDir(path);
                PPB.searchForPackages();

                for (PathPackager PP : PPB.getListPackage()) {
                    PathPackagerView PPV =
                            new PathPackagerView(C, PP, PathPackagerView.SOURCE_CODE);
                    C.runOnUiThread(
                            () -> {
                                binding.linDataSC.addView(PPV.getView());
                                binding.tvFilesPkgCount.setText(
                                        getString(R.string.packages) + " : " + rkbCounterPKG);
                                binding.tvFilesCount.setText(
                                        getString(R.string.file) + " : " + rkbCounterFILES);
                            });
                    rkbCounterPKG++;
                    rkbCounterFILES += PP.getListFile().size();
                }
            }
        } catch (Exception e) {
            Logger.warn(SRC, C.getString(R.string.msg_error_when_parsing_values), e.getMessage());
        }
    }

    private void init() {
        if (CodeUtil.isScreenLandscape(C)) {
            viewGradientTop();
            viewGradientBottom();
        }
    }

    private void viewGradientTop() {
        {
            GradientDrawable gradient = new GradientDrawable();
            gradient.setColors(
                    new int[] {
                        ResourcesValuesFixer.getColor(C, "?colorSurface"), Color.TRANSPARENT
                    });
            gradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            binding.viewClassGradientTop.setBackground(gradient);
        }

        {
            GradientDrawable gradient = new GradientDrawable();
            gradient.setColors(
                    new int[] {
                        ResourcesValuesFixer.getColor(C, "?colorSurface"), Color.TRANSPARENT
                    });
            gradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            binding.viewLayoutGradientTop.setBackground(gradient);
        }
    }

    private void viewGradientBottom() {
        {
            GradientDrawable gradient = new GradientDrawable();
            gradient.setColors(
                    new int[] {
                        Color.TRANSPARENT, ResourcesValuesFixer.getColor(C, "?colorSurface")
                    });
            gradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            binding.viewClassGradientBottom.setBackground(gradient);
        }

        {
            GradientDrawable gradient = new GradientDrawable();
            gradient.setColors(
                    new int[] {
                        Color.TRANSPARENT, ResourcesValuesFixer.getColor(C, "?colorSurface")
                    });
            gradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            binding.viewLayoutGradientBottom.setBackground(gradient);
        }
    }
}
