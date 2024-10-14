package jkas.androidpe.fragments.projectEditor.manager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.concurrent.Executors;
import jkas.androidpe.databinding.LayoutPeManagerViewDialogPerfBinding;
import jkas.androidpe.databinding.LayoutPeManagerViewPropertiesActivityBinding;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.resources.R;
import jkas.androidpe.explorer.dialog.DialogSelector;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.resourcesUtils.attrs.androidManifestTag.UsesPermissions;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;
import java.util.ArrayList;

/**
 * @author JKas
 */
public class PermissionsPerformer {
    private final String SRC = "Permission Manager";
    private FragmentActivity C;
    private LayoutPeManagerViewDialogPerfBinding binding;
    private View finalView;
    private XmlManager manifest;
    private ArrayList<Element> listPermissions = new ArrayList<>();
    private BottomSheetDialog alert;

    public PermissionsPerformer(FragmentActivity c) {
        this.C = c;
        binding = LayoutPeManagerViewDialogPerfBinding.inflate(LayoutInflater.from(C));
        finalView = binding.getRoot();
        manifest = new XmlManager(C);

        alert = new BottomSheetDialog(C);
        alert.setContentView(finalView);
        BottomSheetBehavior behavior = alert.getBehavior();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setDraggable(false);
        init();
    }

    public void show() {
        alert.show();
        loadData();
    }

    private void init() {
        defaultValues();
        events();
    }

    private void loadData() {
        defaultValues();
        binding.icIcon.setImageResource(R.drawable.ic_shield_key);
        binding.tvTitle.setText("Permissions");
        binding.tvCurrent.setText(C.getString(R.string.current) + " : " + listPermissions.size());
        binding.btnAdd.setText(R.string.modify);
        Executors.newSingleThreadExecutor()
                .execute(
                        () -> {
                            for (Element e : listPermissions) {
                                PerView pv = new PerView(e);
                                C.runOnUiThread(
                                        () -> {
                                            binding.linData.addView(pv.getView());
                                        });
                            }
                        });
    }

    private void defaultValues() {
        String pathToManifest =
                DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath();
        pathToManifest += ProjectsPathUtils.ANDROID_MANIFEST_PATH;

        if (manifest.initializeFromPath(pathToManifest) == false) {
            String codeManifest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<manifest/>";
            Files.writeFile(pathToManifest, codeManifest);
            manifest.initializeFromPath(pathToManifest);
        }
        if (manifest.getElement("manifest", 0) == null)
            manifest.getDocument().appendChild(manifest.getDocument().createElement("manifest"));

        listPermissions = manifest.getElementsByTagName("uses-permission");
        binding.linData.removeAllViews();
    }

    private void events() {
        binding.btnAdd.setOnClickListener(
                (v) -> {
                    DialogSelector select =
                            new DialogSelector(
                                    C,
                                    "Permissions",
                                    UsesPermissions.getAllDataAsArray(),
                                    DialogSelector.MULTI_SELECT_MODE);
                    select.setSelected(getSelected());
                    select.setOnMultiItemSelected(
                            (listB) -> {
                                performData(listB);
                            });
                    select.show();
                });
    }

    private void performData(final boolean[] listB) {
        Element app = manifest.getElement("application", 0);
        for (Element per : manifest.getElementsByTagName("uses-permission"))
            per.getParentNode().removeChild(per);
        manifest.saveAllModif();

        int i = -1;
        String perAdded = "";
        for (boolean valided : listB) {
            i++;
            if (!valided) continue;
            Element up = manifest.getDocument().createElement("uses-permission");
            up.setAttribute(
                    "android:name", "android.permission." + UsesPermissions.getAllDataAsArray()[i]);
            if (app != null) app.getParentNode().insertBefore(up, app);
            else manifest.getElement("manifest", 0).appendChild(up);

            perAdded += "<br>" + UsesPermissions.getAllDataAsArray()[i];
        }
        manifest.saveAllModif();
        loadData();

        if (!perAdded.isEmpty())
            Logger.success(
                    SRC, "Permissions : " + C.getString(R.string.added) + "(" + perAdded + ")");
        else {
            boolean verif = false;
            for (boolean b : getSelected())
                if (b) {
                    verif = true;
                    break;
                }

            if (verif) Logger.success(SRC, "All Permissions removed");
            else
                Toast.makeText(
                                C,
                                "0 Permissions " + C.getString(R.string.added),
                                Toast.LENGTH_SHORT)
                        .show();
        }
    }

    private boolean[] getSelected() {
        boolean[] lb = new boolean[UsesPermissions.getAllDataAsArray().length];
        int i = 0;
        for (String s : UsesPermissions.getAllDataAsArray()) {
            lb[i] = false;
            for (Element e : manifest.getElementsByTagName("uses-permission")) {
                if (e.getAttribute("android:name").endsWith("." + s)) {
                    lb[i] = true;
                    break;
                }
            }
            i++;
        }
        return lb;
    }

    public int getCount() {
        return listPermissions.size();
    }

    // sub-class for permission views
    private class PerView {
        LayoutPeManagerViewPropertiesActivityBinding txt;
        Element E;
        public View view;

        public PerView(Element e) {
            E = e;
            ini();
        }

        private void ini() {
            String per =
                    Files.getNameFromAbsolutePath(E.getAttribute("android:name").replace(".", "/"));
            txt = LayoutPeManagerViewPropertiesActivityBinding.inflate(LayoutInflater.from(C));

            view = (View) txt.getRoot();
            view.setLayoutParams(CodeUtil.getLayoutParamsMW(8));

            txt.TIL.setHint(null);
            txt.textInput.setText(per);
            txt.textInput.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void onTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            if (!per.equals(arg0.toString())) txt.textInput.setText(per);
                        }
                    });
            txt.icDelete.setOnClickListener(
                    (v) -> {
                        DialogBuilder.getDialogBuilder(
                                        C,
                                        C.getString(R.string.warning),
                                        C.getString(R.string.warning_delete) + "\n" + per)
                                .setNegativeButton(R.string.cancel, null)
                                .setPositiveButton(
                                        R.string.delete,
                                        (d, f) -> {
                                            E.getParentNode().removeChild(E);
                                            manifest.saveAllModif();
                                            loadData();
                                        })
                                .show();
                    });
        }

        public View getView() {
            return view;
        }
    }
}
