package jkas.androidpe.fragments.projectEditor.manager;

import android.text.Editable;
import android.text.TextWatcher;
import androidx.core.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jkas.androidpe.databinding.LayoutPeManagerViewActivitiesBinding;
import jkas.androidpe.databinding.LayoutPeManagerViewPropertiesActivityBinding;
import jkas.androidpe.explorer.dialog.DialogSelector;
import jkas.androidpe.logger.Logger;
import jkas.androidpe.projectUtils.dataCreator.ActivitiesCreator;
import jkas.androidpe.projectUtils.utils.ProjectsUtils;
import jkas.androidpe.resources.R;
import jkas.androidpe.databinding.LayoutPeManagerViewDialogPerfBinding;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.attrs.androidManifestTag.AttrActivities;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class ActivitiesPerformer {
    private final String SRC = "Activity Manager";
    private FragmentActivity C;
    private LayoutPeManagerViewDialogPerfBinding binding;
    private View finalView;
    private XmlManager manifest;
    private ArrayList<Element> listActivities = new ArrayList<>();
    private BottomSheetDialog alert;

    public ActivitiesPerformer(FragmentActivity c) {
        C = c;
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
        C.runOnUiThread(
                () -> {
                    binding.linData.removeAllViews();
                    alert.show();
                });
        ExecutorService exe = Executors.newSingleThreadExecutor();
        exe.submit(() -> loadData());
        exe.shutdown();
    }

    public int getCount() {
        return listActivities.size();
    }

    private void init() {
        events();
        defaultValues();
    }

    private void loadData() {
        defaultValues();
        ArrayList<Element> listActivities = manifest.getElementsByTagName("activity");
        for (Element e : listActivities) {
            ActivityBuilder ab = new ActivityBuilder(e);
            C.runOnUiThread(
                    () -> {
                        binding.linData.addView(ab.getView());
                    });
        }
    }

    private void events() {
        binding.btnAdd.setOnClickListener(
                (v) -> {
                    new DialogAddActivity();
                });
    }

    private void defaultValues() {
        String path =
                DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath()
                        + ProjectsPathUtils.ANDROID_MANIFEST_PATH;
        if (!Files.isFile(path)) {
            String codeManifest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<manifest/>";
            Files.writeFile(path, codeManifest);
        }

        manifest.initializeFromPath(path);

        if (manifest.getElement("manifest", 0) == null)
            manifest.getDocument().appendChild(manifest.getDocument().createElement("manifest"));

        listActivities = manifest.getElementsByTagName("activity");
        binding.linData.removeAllViews();
        binding.tvTitle.setText("Activities");
        binding.tvCurrent.setText(C.getString(R.string.current) + " : " + listActivities.size());
        binding.icIcon.setImageResource(R.drawable.ic_smartphone);
    }

    private class ActivityBuilder {
        private Element element;
        private LayoutPeManagerViewActivitiesBinding bind;
        private View view;

        public ActivityBuilder(Element e) {
            this.element = e;
            bind = LayoutPeManagerViewActivitiesBinding.inflate(LayoutInflater.from(C));
            view = bind.getRoot();
            view.setLayoutParams(CodeUtil.getLayoutParamsMW(8));
            init();
        }

        private void init() {
            try {
                loadData();
                events();
            } catch (Exception e) {
                C.runOnUiThread(
                        () -> {
                            Toast.makeText(C, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                view.setVisibility(View.GONE);
            }
        }

        private void events() {
            bind.icDelete.setOnClickListener(
                    (v) -> {
                        DialogBuilder.getDialogBuilder(
                                        C,
                                        C.getString(R.string.warning),
                                        C.getString(R.string.warning_delete)
                                                + " : "
                                                + bind.tvActivityName.getText().toString()
                                                + " ?")
                                .setPositiveButton(
                                        R.string.delete,
                                        (d, vi) -> {
                                            try {
                                                element.getParentNode().removeChild(element);
                                                manifest.saveAllModif();
                                                view.setVisibility(View.GONE);
                                                listActivities =
                                                        manifest.getElementsByTagName("activity");
                                                Logger.success(
                                                        SRC,
                                                        C.getString(R.string.file)
                                                                + " : "
                                                                + "AndroidManifest.xml",
                                                        "Activity : "
                                                                + bind.tvActivityName
                                                                        .getText()
                                                                        .toString(),
                                                        C.getString(R.string.deleted));
                                            } catch (Exception e) {
                                                Logger.success(
                                                        SRC,
                                                        C.getString(R.string.file)
                                                                + " : "
                                                                + "AndroidManifest.xml",
                                                        "Activity : "
                                                                + bind.tvActivityName
                                                                        .getText()
                                                                        .toString(),
                                                        C.getString(R.string.cant_delete));
                                            }
                                        })
                                .setNegativeButton(R.string.cancel, null)
                                .show();
                    });
            bind.btnShowAttributes.setOnClickListener(
                    (v) -> {
                        initAttr();
                    });
        }

        private void loadData() {
            ArrayList<Pair<String, String>> listPair =
                    XmlManager.getAllAttrNValuesFromElement(element);
            final String name =
                    Files.getNameFromAbsolutePath(
                            element.getAttribute("android:name").replace(".", "/"));
            C.runOnUiThread(
                    () -> {
                        bind.tvActivityName.setText(name);
                        bind.tvCurrent.setText(
                                C.getString(R.string.current) + " : " + listPair.size());
                    });
        }

        private void initAttr() {
            ScrollView sv = new ScrollView(C);
            LinearLayout linData = new LinearLayout(C);
            linData.setLayoutParams(CodeUtil.getLayoutParamsMW(8));
            linData.setOrientation(LinearLayout.VERTICAL);
            linData.setPadding(16, 16, 16, 16);
            sv.addView(linData);

            for (Pair<String, String> pair : XmlManager.getAllAttrNValuesFromElement(element)) {
                ViewProperty vp = new ViewProperty(pair.first, pair.second);
                C.runOnUiThread(
                        () -> {
                            linData.addView(vp.getView());
                        });
            }

            DialogBuilder.getDialogBuilder(
                            C,
                            C.getString(R.string.attributes),
                            C.getString(R.string.info_the_info_will_be_save_when_modify_text))
                    .setView(sv)
                    .setPositiveButton(
                            C.getString(R.string.add) + " : Attr",
                            (d, v) -> {
                                new DialogAddAttr();
                            })
                    .setNegativeButton(R.string.exit, null)
                    .show();
        }

        public View getView() {
            return view;
        }

        private class ViewProperty {
            private String attr, value;
            private LayoutPeManagerViewPropertiesActivityBinding bindAttr;
            private View view;

            public ViewProperty(String attr, String value) {
                this.attr = attr;
                this.value = value;

                initAttr();
            }

            private void initAttr() {
                bindAttr =
                        LayoutPeManagerViewPropertiesActivityBinding.inflate(
                                LayoutInflater.from(C));
                view = bindAttr.getRoot();

                values();
                events();
            }

            private void values() {
                C.runOnUiThread(
                        () -> {
                            bindAttr.TIL.setHint(attr);
                            bindAttr.textInput.setText(value);
                        });
            }

            private void events() {
                bindAttr.textInput.addTextChangedListener(
                        new TextWatcher() {
                            @Override
                            public void beforeTextChanged(
                                    CharSequence arg0, int arg1, int arg2, int arg3) {}

                            @Override
                            public void onTextChanged(
                                    CharSequence arg0, int arg1, int arg2, int arg3) {}

                            @Override
                            public void afterTextChanged(Editable arg0) {
                                save(arg0.toString());
                            }
                        });

                bindAttr.icDelete.setOnClickListener(
                        (v) -> {
                            DialogBuilder.getDialogBuilder(
                                            C,
                                            C.getString(R.string.warning),
                                            C.getString(R.string.warning_delete)
                                                    + " : "
                                                    + attr
                                                    + " ?")
                                    .setPositiveButton(
                                            R.string.delete,
                                            (d, vi) -> {
                                                try {
                                                    save(null);
                                                    Logger.info(
                                                            SRC,
                                                            C.getString(R.string.file)
                                                                    + " : "
                                                                    + "AndroidManifest.xml",
                                                            "Activity : "
                                                                    + bind.tvActivityName
                                                                            .getText()
                                                                            .toString(),
                                                            "Attr : "
                                                                    + attr
                                                                    + " "
                                                                    + C.getString(
                                                                            R.string.deleted));
                                                } catch (Exception e) {
                                                    Logger.error(
                                                            SRC,
                                                            C.getString(R.string.file)
                                                                    + " : "
                                                                    + "AndroidManifest.xml",
                                                            "Activity : <b>"
                                                                    + bind.tvActivityName
                                                                            .getText()
                                                                            .toString(),
                                                            "Attr : "
                                                                    + attr
                                                                    + " "
                                                                    + C.getString(
                                                                            R.string.cant_delete));
                                                }
                                            })
                                    .setNegativeButton(R.string.cancel, null)
                                    .show();
                        });
            }

            private void save(String value) {
                ExecutorService exe = Executors.newSingleThreadExecutor();
                exe.execute(
                        () -> {
                            if (value != null) element.setAttribute(attr, value);
                            else {
                                element.removeAttribute(attr);
                                C.runOnUiThread(
                                        () -> {
                                            view.setVisibility(View.GONE);
                                        });
                            }
                            loadData();
                            manifest.saveAllModif();
                        });
            }

            public View getView() {
                return view;
            }
        }

        private class DialogAddAttr {
            private View view;
            private LayoutPeManagerViewPropertiesActivityBinding txt1, txt2;
            private MaterialAlertDialogBuilder alert;
            private MaterialButton btnAssist;

            public DialogAddAttr() {
                try {
                    ini();
                } catch (Exception e) {
                    Toast.makeText(C, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            private void ini() {
                view();
                data();
                events();
                show();
            }

            private void show() {
                alert.setView(view);
                alert.setNegativeButton(R.string.cancel, null);
                alert.show();
            }

            private void events() {
                alert.setPositiveButton(
                        R.string.add,
                        (d, v) -> {
                            if (!txt1.textInput.getText().toString().contains(":")
                                    || txt1.textInput.getText().toString().isEmpty()
                                    || txt1.TIL.getError() != null) {
                                Toast.makeText(C, R.string.cant_save, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            element.setAttribute(
                                    txt1.textInput.getText().toString(),
                                    txt1.textInput.getText().toString());
                            manifest.saveAllModif();
                            loadData();
                        });
                btnAssist.setOnClickListener(
                        (v) -> {
                            DialogSelector ds =
                                    new DialogSelector(
                                            C,
                                            C.getString(R.string.attributes),
                                            AttrActivities.getAttrs().toArray(String[]::new),
                                            DialogSelector.SINGLE_SELECT_MODE);
                            ds.setOnItemSelected(
                                    (p) -> {
                                        txt1.textInput.setText(AttrActivities.getAttrs().get(p));
                                    });
                            ds.show();
                        });

                txt1.textInput.addTextChangedListener(
                        new TextWatcher() {

                            @Override
                            public void beforeTextChanged(
                                    CharSequence arg0, int arg1, int arg2, int arg3) {}

                            @Override
                            public void onTextChanged(
                                    CharSequence arg0, int arg1, int arg2, int arg3) {}

                            @Override
                            public void afterTextChanged(Editable arg0) {
                                if (isAttrExists(arg0.toString()))
                                    txt1.TIL.setError(C.getString(R.string.already_exists));
                                else txt1.TIL.setError(null);
                            }
                        });
            }

            private boolean isAttrExists(String attr) {
                for (Pair<String, String> pair : XmlManager.getAllAttrNValuesFromElement(element))
                    if (pair.first.equals(attr)) return true;
                return false;
            }

            private void data() {
                txt1.TIL.setHint(C.getString(R.string.attribute));
                txt2.TIL.setHint(C.getString(R.string.value));
                txt1.textInput.setText("");
                txt2.textInput.setText("");
            }

            private void view() {
                alert =
                        DialogBuilder.getDialogBuilder(
                                C,
                                C.getString(R.string.attributes),
                                "Activity : " + bind.tvActivityName.getText().toString());

                txt1 = LayoutPeManagerViewPropertiesActivityBinding.inflate(LayoutInflater.from(C));
                txt2 = LayoutPeManagerViewPropertiesActivityBinding.inflate(LayoutInflater.from(C));

                btnAssist = new MaterialButton(C);
                btnAssist.setPadding(8, 8, 8, 8);
                LayoutParams LP = new LayoutParams(LayoutParams.WRAP_CONTENT, 100);
                LP.setMargins(43, 16, 0, 8);
                btnAssist.setLayoutParams(LP);
                btnAssist.setText(C.getString(R.string.assist));
                btnAssist.setTextSize(12);

                // PreInit
                txt1.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(8));
                txt2.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(8));
                txt1.icDelete.setVisibility(View.GONE);
                txt2.icDelete.setVisibility(View.GONE);

                LinearLayout linR = new LinearLayout(C);
                linR.setLayoutParams(CodeUtil.getLayoutParamsMW(0));
                linR.setGravity(Gravity.CENTER);
                linR.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout linParent = new LinearLayout(C);
                linParent.setLayoutParams(CodeUtil.getLayoutParamsMW(0));
                linParent.setOrientation(LinearLayout.VERTICAL);
                linParent.addView((View) txt1.getRoot());
                linParent.addView((View) txt2.getRoot());
                linParent.addView((View) linR);
                linParent.addView((View) btnAssist);

                view = (View) linParent;
            }
        }
    }

    private class DialogAddActivity {
        private View view;
        private LayoutPeManagerViewPropertiesActivityBinding txt1, txt2, txt3;
        private RadioButton rJava, rKotlin, rCompose;
        private MaterialAlertDialogBuilder alert;
        private MaterialButton btnAssist;
        private String prefixRecommanded = "";

        public DialogAddActivity() {
            try {
                ini();
            } catch (Exception e) {
                Toast.makeText(C, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        private void ini() {
            view();
            searchForPrefixPkg();
            events();
            data();
            show();
        }

        private void searchForPrefixPkg() {
            String name = null;
            String pkg = DataRefManager.getInstance().currentAndroidModule.getPackageName();
            prefixRecommanded = "my.modulename";
            if (pkg != null && ResCodeUtils.isAValidePackageName(pkg))
                prefixRecommanded =
                        DataRefManager.getInstance().currentAndroidModule.getPackageName();
            if (!prefixRecommanded.endsWith(".activities")) prefixRecommanded += ".activities";
            try {
                for (Element e : manifest.getElementsByTagName("activity")) {
                    name = e.getAttribute("android:name");
                    if (name != null) break;
                }

                if (name == null) return;

                String path =
                        DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath();
                if (Files.isDirectory(path + ProjectsPathUtils.JAVA_PATH))
                    path += ProjectsPathUtils.JAVA_PATH;
                else if (Files.isDirectory(path + ProjectsPathUtils.KOTLIN_PATH))
                    path += ProjectsPathUtils.KOTLIN_PATH;

                String finalName = name;
                String finalPath = Files.getFilePath(path, finalName.replace(".", "/"), false);
                if (finalPath != null) {
                    String finalPkg = finalPath.substring(path.length());
                    finalPkg = Files.getPrefixPath(finalPkg);
                    finalPkg = finalPkg.replace("/", ".");
                    prefixRecommanded = finalPkg.startsWith(".") ? finalPkg.substring(1) : finalPkg;
                    if (!prefixRecommanded.endsWith(".activities"))
                        prefixRecommanded += ".activities";
                }
            } catch (Exception e) {
                Toast.makeText(C, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        private void show() {
            alert.setView(view);
            alert.setNegativeButton(R.string.cancel, null);
            alert.setPositiveButton(
                    R.string.add,
                    (d, v) -> {
                        String className = txt1.textInput.getText().toString();
                        String layoutName = txt2.textInput.getText().toString();
                        String pkg = txt3.textInput.getText().toString();

                        if (txt3.TIL.getError() != null) {
                            Toast.makeText(C, R.string.cant_save, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (className.isEmpty() || layoutName.isEmpty() || pkg.isEmpty()) {
                            Toast.makeText(C, R.string.cant_save, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (txt1.TIL.getError() != null) {
                            Toast.makeText(C, R.string.cant_save, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int type = ActivitiesCreator.TYPE_JAVA;
                        if (rKotlin.isChecked()) type = ActivitiesCreator.TYPE_KOTLIN;
                        if (rCompose.isChecked()) type = ActivitiesCreator.TYPE_COMPOSE;

                        ActivitiesCreator ac =
                                new ActivitiesCreator(C, pkg, className, layoutName, type);
                        ac.setOnCreateListener(
                                (created) -> {
                                    if (created) loadData();
                                });
                        ac.create();
                    });
            alert.show();
        }

        private void events() {
            rJava.setOnClickListener(
                    (v) -> {
                        rKotlin.setChecked(false);
                        rCompose.setChecked(false);
                    });

            rKotlin.setOnClickListener(
                    (v) -> {
                        rJava.setChecked(false);
                        rCompose.setChecked(false);
                    });

            rCompose.setOnClickListener(
                    (v) -> {
                        rJava.setChecked(false);
                        rKotlin.setChecked(false);
                    });

            txt1.textInput.addTextChangedListener(
                    new TextWatcher() {

                        @Override
                        public void beforeTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void onTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            String msg = null;
                            for (Element e : manifest.getElementsByTagName("activity")) {
                                String attr = e.getAttribute("android:name");
                                if (attr == null) continue;
                                if (attr.equals(arg0.toString())
                                        || attr.equals("." + arg0.toString())
                                        || attr.equals(
                                                txt3.textInput.getText().toString()
                                                        + "."
                                                        + arg0.toString())) {
                                    msg = C.getString(R.string.already_exists);
                                    break;
                                } else msg = null;
                            }
                            txt1.TIL.setError(msg);
                        }
                    });

            txt2.textInput.addTextChangedListener(
                    new TextWatcher() {

                        @Override
                        public void beforeTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void onTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            String clazz =
                                    ResCodeUtils.ResAndCodeFilesFixer.convertLayoutFileNameToClass(
                                            arg0.toString().replace(".xml", ""));
                            txt1.textInput.setText(clazz + "Activity");
                            if (arg0.toString().trim().length() == 0) {
                                txt2.TIL.setError(C.getString(R.string.no_valide));
                            } else {
                                String path =
                                        DataRefManager.getInstance()
                                                        .currentAndroidModule
                                                        .getProjectAbsolutePath()
                                                + ProjectsPathUtils.LAYOUT_PATH;
                                path += "/" + arg0.toString() + ".xml";
                                if (Files.isFile(path))
                                    txt2.TIL.setError(C.getString(R.string.already_exists));
                                else txt2.TIL.setError(null);
                            }
                        }
                    });

            txt3.textInput.addTextChangedListener(
                    new TextWatcher() {

                        @Override
                        public void beforeTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void onTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            if (ResCodeUtils.isAValidePackageName(arg0.toString()))
                                txt3.TIL.setError(null);
                            else txt3.TIL.setError(C.getString(R.string.invalide_entry));
                        }
                    });

            ResCodeUtils.ResAndCodeFilesFixer.fixXmlFileNameAndAssign(txt2.TIL, txt2.textInput);
        }

        private void data() {
            rJava.setText("Java");
            rKotlin.setText("Kotlin");
            rCompose.setText("Compose");

            txt1.TIL.setHint(C.getString(R.string.class_name));
            txt2.TIL.setHint(
                    C.getString(R.string.xml_file_name)
                            + " (.xml : "
                            + C.getString(R.string.added)
                            + ")");
            txt3.TIL.setHint(C.getString(R.string.package_name_prefix));
            txt3.textInput.setText(prefixRecommanded);
            txt1.textInput.setText("");
            txt2.textInput.setText("my_new");

            switch (ProjectsUtils.getModuleType()) {
                case ProjectsUtils.TYPE_JAVA:
                    rJava.setChecked(true);
                    rCompose.setVisibility(View.GONE);
                    break;
                case ProjectsUtils.TYPE_KOTLIN:
                    rKotlin.setChecked(true);
                    break;
                case ProjectsUtils.TYPE_COMPOSE:
                    rCompose.setChecked(true);
                    rJava.setVisibility(View.GONE);
                    rKotlin.setVisibility(View.GONE);
                    break;
            }
        }

        private void view() {
            alert =
                    DialogBuilder.getDialogBuilder(
                            C,
                            C.getString(R.string.activities),
                            "module : "
                                    + DataRefManager.getInstance().currentAndroidModule.getName());

            txt1 = LayoutPeManagerViewPropertiesActivityBinding.inflate(LayoutInflater.from(C));
            txt2 = LayoutPeManagerViewPropertiesActivityBinding.inflate(LayoutInflater.from(C));
            txt3 = LayoutPeManagerViewPropertiesActivityBinding.inflate(LayoutInflater.from(C));

            rJava = new RadioButton(C);
            rKotlin = new RadioButton(C);
            rCompose = new RadioButton(C);

            btnAssist = new MaterialButton(C);
            btnAssist.setPadding(8, 8, 8, 8);
            LayoutParams LP = new LayoutParams(LayoutParams.WRAP_CONTENT, 100);
            LP.setMargins(43, 16, 0, 8);
            btnAssist.setLayoutParams(LP);
            btnAssist.setText(C.getString(R.string.assist));
            btnAssist.setTextSize(12);

            // PreInit
            txt1.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(8));
            txt2.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(8));
            txt3.getRoot().setLayoutParams(CodeUtil.getLayoutParamsMW(8));
            txt1.icDelete.setVisibility(View.GONE);
            txt2.icDelete.setVisibility(View.GONE);
            txt3.icDelete.setVisibility(View.GONE);

            rJava.setPadding(8, 8, 16, 8);
            rKotlin.setPadding(8, 8, 16, 8);
            rCompose.setPadding(8, 8, 8, 8);

            LinearLayout linR = new LinearLayout(C);
            linR.setLayoutParams(CodeUtil.getLayoutParamsMW(0));
            linR.setGravity(Gravity.CENTER);
            linR.setOrientation(LinearLayout.HORIZONTAL);
            linR.addView((View) rJava);
            linR.addView((View) rKotlin);
            linR.addView((View) rCompose);

            LinearLayout linParent = new LinearLayout(C);
            linParent.setLayoutParams(CodeUtil.getLayoutParamsMW(0));
            linParent.setOrientation(LinearLayout.VERTICAL);
            linParent.addView((View) txt1.getRoot());
            linParent.addView((View) txt2.getRoot());
            linParent.addView((View) txt3.getRoot());
            linParent.addView((View) linR);
            // linParent.addView((View) btnAssist);

            view = (View) linParent;
        }
    }
}
