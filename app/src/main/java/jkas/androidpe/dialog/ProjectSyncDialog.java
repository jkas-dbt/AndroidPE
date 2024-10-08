package jkas.androidpe.dialog;

import java.util.ArrayList;
import jkas.androidpe.databinding.UiPreferenceDialogSyncProjectBinding;
import jkas.codeUtil.Files;
import java.io.File;
import android.widget.Toast;
import jkas.androidpe.explorer.SelectFF;
import jkas.androidpe.resources.R;
import android.content.Context;
import android.view.LayoutInflater;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import org.w3c.dom.Element;
import android.view.View;
import jkas.androidpe.databinding.LayoutPathToProjectBinding;
import jkas.codeUtil.CodeUtil;
import rkb.datasaver.RKBDataAppSettings;

/**
 * @author JKas
 */
public class ProjectSyncDialog {
    private Context C;
    private UiPreferenceDialogSyncProjectBinding binding;
    private MaterialAlertDialogBuilder builder;

    public ProjectSyncDialog(Context c) {
        C = c;

        init();
        event();
        loadData();
    }

    public void show() {
        builder.show();
    }

    private void loadData() {
        loadPathToProjects();
    }

    private void event() {
        binding.btnAddPath.setOnClickListener(
                (v) -> {
                    final SelectFF selector = new SelectFF(C);
                    selector.setSelectorType(SelectFF.FOLDER_SELECTOR);
                    selector.showView();
                    selector.loadData();
                    selector.setOnSaveListener((l) -> assignPath(l));
                });
    }

    private void assignPath(ArrayList<String> list) {
        int added = 0;
        for (String path : list) {
            path = path.replace(Files.getExternalStorageDir() + File.separator, "");
            boolean brk;
            brk = false;
            for (Element e : RKBDataAppSettings.xmlF.getElementsByTagName("path")) {
                if (e.getTextContent().equals(path)) {
                    brk = true;
                    return;
                }
            }

            if (brk) continue;

            Element p2p = RKBDataAppSettings.xmlF.getElement("p2p", 0);
            Element pathE = RKBDataAppSettings.xmlF.getDocument().createElement("path");
            pathE.setTextContent(path);
            p2p.appendChild(pathE);
            RKBDataAppSettings.xmlF.saveAllModif();
            added++;
        }

        loadPathToProjects();
        Toast.makeText(C, added + " " + C.getString(R.string.added), Toast.LENGTH_SHORT).show();
    }

    public void loadPathToProjects() {
        binding.linPath.removeAllViews();
        for (Element path : RKBDataAppSettings.xmlF.getElementsByTagName("path")) {
            binding.linPath.addView(viewPathToFolder(path));
        }

        int size = RKBDataAppSettings.xmlF.getElementsByTagName("path").size();
        if (size == 0) binding.tvInfo.setText(R.string.not_found);
        else binding.tvInfo.setText(C.getString(R.string.found) + " : " + size);
    }

    private View viewPathToFolder(final Element path) {
        final LayoutPathToProjectBinding bindingP2P =
                LayoutPathToProjectBinding.inflate(LayoutInflater.from(C));
        bindingP2P.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(8));
        bindingP2P.tvPath.setText(path.getTextContent());
        bindingP2P.imgDelete.setOnClickListener(
                (v9) -> {
                    path.getParentNode().removeChild(path);
                    RKBDataAppSettings.xmlF.saveAllModif();
                    bindingP2P.getRoot().setVisibility(View.GONE);
                });
        return bindingP2P.getRoot();
    }

    private void init() {
        binding = UiPreferenceDialogSyncProjectBinding.inflate(LayoutInflater.from(C));
        builder =
                DialogBuilder.getDialogBuilder(C, C.getString(R.string.project_synchronizer), null);
        builder.setView(binding.getRoot());
    }
}
