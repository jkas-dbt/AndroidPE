package jkas.androidpe.projectAnalyzer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.elevation.SurfaceColors;
import java.util.concurrent.Executors;
import jkas.androidpe.activities.ProjectEditorActivity;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.databinding.LayoutProjectViewBinding;
import jkas.androidpe.dialog.ArchiveDialog;
import jkas.androidpe.project.Project;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.Images;
import jkas.codeUtil.XmlManager;
import java.io.File;
import java.util.ArrayList;

/**
 * @author JKas
 */
public class ProjectView {
    private boolean moreOptionChecked = false;
    private Context C;
    private Project P;
    private LayoutProjectViewBinding bindingPV;
    private View view;

    public ProjectView(Context c, Project p) {
        C = c;
        P = p;

        bindingPV = LayoutProjectViewBinding.inflate(LayoutInflater.from(C));
        view = (View) bindingPV.getRoot();

        ini();
        setBgView();
        icon();
        onClick();
    }

    public String getIconPath() {
        return P.getIconPath();
    }

    private void onClick() {
        bindingPV.icMoreSettings.setOnClickListener(
                (v) -> {
                    PopupMenu popupMenu = new PopupMenu(C, v);
                    popupMenu.setForceShowIcon(true);
                    popupMenu
                            .getMenu()
                            .add(Menu.NONE, 1, Menu.NONE, R.string.archive)
                            .setIcon(C.getDrawable(R.drawable.ic_package))
                            .setIconTintList(
                                    ColorStateList.valueOf(
                                            ResourcesValuesFixer.getColor(C, "?colorOnSurface")));
                    popupMenu
                            .getMenu()
                            .add(Menu.NONE, 2, Menu.NONE, R.string.delete)
                            .setIcon(C.getDrawable(R.drawable.ic_delete));
                    popupMenu.setOnMenuItemClickListener(
                            item -> {
                                switch (item.getItemId()) {
                                    case 1:
                                        new ArchiveDialog(C, P);
                                        break;
                                    case 2:
                                        deleteProject();
                                        break;
                                }
                                return true;
                            });
                    popupMenu.show();
                });

        bindingPV
                .getRoot()
                .setOnClickListener(
                        (v4) -> {
                            if (Files.isDirectory(P.getAbsolutePath())) {
                                DataRefManager.getInstance().P = P;
                                CodeUtil.startActivity(C, ProjectEditorActivity.class);
                            } else {
                                bindingPV.getRoot().setVisibility(View.GONE);
                                Toast.makeText(C, R.string.not_found, Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    private void deleteProject() {
        final String path = P.getAbsolutePath();
        if (!Files.isDirectory(path)) {
            DialogBuilder.showDialog(C, null, C.getString(R.string.cant_delete_this_project));
            view.setVisibility(View.GONE);
            return;
        }

        final MaterialAlertDialogBuilder builder =
                DialogBuilder.getDialogBuilder(
                        C,
                        C.getString(R.string.warning),
                        C.getString(R.string.warning_delete) + " : " + P.getFolderName() + " ?");
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(
                R.string.delete,
                (b1, b2) -> {
                    try {
                        Executors.newSingleThreadExecutor().execute(() -> Files.deleteDir(path));
                        view.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(
                                        C,
                                        C.getString(R.string.cant_delete_this_project),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        builder.show();
    }

    private void icon() {
        XmlManager xmlFile = new XmlManager(C);
        xmlFile.initializeFromPath(
                P.getAbsolutePath()
                        + File.separator
                        + "app"
                        + ProjectsPathUtils.ANDROID_MANIFEST_PATH);
        if (!xmlFile.isInitialized) return;

        String icName =
                ""
                        + xmlFile.getElement("application", 0)
                                .getAttribute("android:icon")
                                .split("\\/")[1];

        if (icName == null) return;

        String pathToRes = P.getAbsolutePath() + "/app" + ProjectsPathUtils.RES_PATH;

        if (Files.isDirectory(pathToRes)) {
            ArrayList<String> listDir = new ArrayList<>();
            Files.listDir(pathToRes, listDir);
            for (String dir : listDir) {
                ArrayList<String> listFiles = new ArrayList<>();
                Files.listFile(dir, listFiles);
                String p = dir.substring(dir.lastIndexOf(File.separator) + 1);
                if (p.startsWith("drawable") || p.startsWith("mipmap")) {
                    for (String file : listFiles) {
                        String finalFile = file.substring(0, file.lastIndexOf("."));
                        if (finalFile.endsWith(File.separator + icName)) {
                            P.setIconPath(file);
                            Images.setImageFromDir(file, bindingPV.icIconProject);
                        }
                    }
                }
            }
        }
    }

    private void ini() {
        bindingPV.tvFolderName.setText(P.getFolderName());
        bindingPV.tvPackageName.setText(P.getPackageName());
        bindingPV.tvPathToProject.setText(P.getProjectDir());

        GridLayout.LayoutParams param =
                new GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f));
        int dim = (int) ResourcesValuesFixer.getDimen(C, "8dp");
        param.setMargins(dim, dim / 2, dim, dim / 2);
        view.setLayoutParams(param);

        bindingPV.icMoreSettings.setImageTintList(
                ColorStateList.valueOf(ResourcesValuesFixer.getColor(C, "?colorOnSurface")));
    }

    public View getView() {
        return view;
    }

    public void setBgView() {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setCornerRadius(25f);
        gradient.setColor(SurfaceColors.SURFACE_1.getColor(C));
        ColorStateList colorStateList = ColorStateList.valueOf(SurfaceColors.SURFACE_5.getColor(C));
        RippleDrawable ripple = new RippleDrawable(colorStateList, gradient, null);
        view.setBackground(ripple);
        view.setElevation(5);
    }
}
