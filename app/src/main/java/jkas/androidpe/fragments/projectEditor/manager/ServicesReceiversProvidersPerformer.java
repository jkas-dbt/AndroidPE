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
import jkas.androidpe.projectUtils.dataCreator.FilesRef;
import jkas.androidpe.projectUtils.utils.ProjectsUtils;
import jkas.androidpe.resources.R;
import jkas.androidpe.databinding.LayoutPeManagerViewDialogPerfBinding;
import jkas.androidpe.resourcesUtils.attrs.androidManifestTag.AttrReceivers;
import jkas.androidpe.resourcesUtils.attrs.androidManifestTag.AttrProviders;
import jkas.androidpe.resourcesUtils.attrs.androidManifestTag.AttrServices;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/**
 * @author JKas
 */
public class ServicesReceiversProvidersPerformer {
    public static final int SERVICES_TAG_TYPE = 0;
    public static final int RECEIVERS_TAG_TYPE = 1;
    public static final int PROVIDERS_TAG_TYPE = 2;

    private FragmentActivity C;
    private LayoutPeManagerViewDialogPerfBinding binding;
    private View finalView;
    private XmlManager manifest;
    private ArrayList<Element> listElementsTag = new ArrayList<>();
    private BottomSheetDialog alert;

    private int TAG_TYPE;

    public ServicesReceiversProvidersPerformer(FragmentActivity c, int tagType) {
        C = c;
        TAG_TYPE = tagType;

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
        exe.execute(
                () -> {
                    loadData();
                });
        exe.shutdown();
    }

    public int getCount() {
        return listElementsTag.size();
    }

    private void init() {
        events();
        defaultValues();
    }

    private void loadData() {
        defaultValues();
        loadElement();
        for (Element e : listElementsTag) {
            ElementTagBuilder ab = new ElementTagBuilder(e);
            C.runOnUiThread(
                    () -> {
                        binding.linData.addView(ab.getView());
                    });
        }
    }

    private void events() {
        binding.btnAdd.setOnClickListener(
                (v) -> {
                    new DialogAddElementTag();
                });
    }

    private void loadElement() {
        if (TAG_TYPE == SERVICES_TAG_TYPE)
            listElementsTag = manifest.getElementsByTagName("service");
        else if (TAG_TYPE == RECEIVERS_TAG_TYPE)
            listElementsTag = manifest.getElementsByTagName("receiver");
        else if (TAG_TYPE == PROVIDERS_TAG_TYPE)
            listElementsTag = manifest.getElementsByTagName("provider");
    }

    private String getTagType() {
        if (TAG_TYPE == RECEIVERS_TAG_TYPE) return "receiver";
        else if (TAG_TYPE == PROVIDERS_TAG_TYPE) return "provider";
        return "service";
    }

    private String getTagName() {
        if (TAG_TYPE == RECEIVERS_TAG_TYPE) return "BroadcastReceivers";
        else if (TAG_TYPE == PROVIDERS_TAG_TYPE) return "ContentProviders";
        return "Services";
    }

    private String[] getListAttr() {
        if (TAG_TYPE == RECEIVERS_TAG_TYPE) return AttrReceivers.getAllDataAsArray();
        else if (TAG_TYPE == PROVIDERS_TAG_TYPE) return AttrProviders.getAllDataAsArray();
        return AttrServices.getAllDataAsArray();
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

        loadElement();

        binding.linData.removeAllViews();
        binding.tvTitle.setText(getTagName());
        binding.tvCurrent.setText(C.getString(R.string.current) + " : " + listElementsTag.size());
        if (TAG_TYPE == SERVICES_TAG_TYPE)
            binding.icIcon.setImageResource(R.drawable.ic_powershell);
        else if (TAG_TYPE == RECEIVERS_TAG_TYPE)
            binding.icIcon.setImageResource(R.drawable.ic_powershell);
    }

    private class ElementTagBuilder {
        private Element element;
        private LayoutPeManagerViewActivitiesBinding bind;
        private View view;

        public ElementTagBuilder(Element e) {
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
                                                listElementsTag =
                                                        manifest.getElementsByTagName(getTagType());
                                                Logger.success(
                                                        getTagName() + " Manager",
                                                        C.getString(R.string.file)
                                                                + " : "
                                                                + "AndroidManifest.xml",
                                                        getTagType()
                                                                + " : "
                                                                + bind.tvActivityName
                                                                        .getText()
                                                                        .toString()
                                                                + " "
                                                                + C.getString(R.string.deleted));
                                            } catch (Exception e) {
                                                Logger.error(
                                                        getTagName() + " Manager",
                                                        C.getString(R.string.file)
                                                                + " : "
                                                                + "AndroidManifest.xml",
                                                        getTagType()
                                                                + " : "
                                                                + bind.tvActivityName
                                                                        .getText()
                                                                        .toString()
                                                                + " "
                                                                + C.getString(
                                                                        R.string.cant_delete));
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
                C.runOnUiThread(() -> linData.addView(vp.getView()));
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
                                                            getTagName() + " Manager",
                                                            C.getString(R.string.file)
                                                                    + " : "
                                                                    + "AndroidManifest.xml<br>"
                                                                    + getTagType()
                                                                    + " : "
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
                                                            getTagName() + " Manager",
                                                            C.getString(R.string.file)
                                                                    + " : "
                                                                    + "AndroidManifest.xml<br>"
                                                                    + getTagType()
                                                                    + " : "
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
                                            getListAttr(),
                                            DialogSelector.SINGLE_SELECT_MODE);
                            ds.setOnItemSelected(
                                    (p) -> {
                                        txt1.textInput.setText(getListAttr()[p].replace("_", ":"));
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
                                getTagName() + " : " + bind.tvActivityName.getText().toString());

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

    private class DialogAddElementTag {
        private View view;
        private LayoutPeManagerViewPropertiesActivityBinding txt1, txt2;
        private RadioButton rJava, rKotlin;
        private MaterialAlertDialogBuilder alert;
        private MaterialButton btnAssist;
        private String prefixRecommanded = "";
        private String prefixPathToElementType;

        public DialogAddElementTag() {
            try {
                ini();
            } catch (Exception e) {
                Toast.makeText(C, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        private void ini() {
            prefixPathToElementType =
                    DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath();
            if (Files.isDirectory(prefixPathToElementType + ProjectsPathUtils.KOTLIN_PATH))
                prefixPathToElementType += ProjectsPathUtils.KOTLIN_PATH;
            else prefixPathToElementType += ProjectsPathUtils.JAVA_PATH;

            view();
            searchForPrefixPkg();
            events();
            data();
            show();
        }

        private void searchForPrefixPkg() {
            boolean found = false;
            String name = null;
            String pkg = DataRefManager.getInstance().currentAndroidModule.getPackageName();
            prefixRecommanded = "my.modulename";
            if (pkg != null && ResCodeUtils.isAValidePackageName(pkg)) prefixRecommanded = pkg;
            if (!prefixRecommanded.endsWith("." + getTagType() + "s"))
                prefixRecommanded += "." + getTagType() + "s";
            try {
                if (pkg == null || pkg.equals("...")) {
                    for (Element e : manifest.getElementsByTagName(getTagType())) {
                        name = e.getAttribute("android:name");
                        if (name != null) {
                            if (name.contains("."))
                                name = Files.getNameFromAbsolutePath(name.replace(".", "/"));
                            found = true;
                            break;
                        }
                    }

                    if (!found) return;

                    String path =
                            DataRefManager.getInstance()
                                    .currentAndroidModule
                                    .getProjectAbsolutePath();
                    if (Files.isDirectory(path + ProjectsPathUtils.JAVA_PATH))
                        path += ProjectsPathUtils.JAVA_PATH;
                    else if (Files.isDirectory(path + ProjectsPathUtils.KOTLIN_PATH))
                        path += ProjectsPathUtils.KOTLIN_PATH;

                    String finalName = name;

                    if (name.startsWith("/")) finalName = name.substring(1);

                    String finalPath = Files.getFilePath(path, finalName, false);
                    if (finalPath != null) {
                        String finalPkg = finalPath.substring(path.length());
                        finalPkg = Files.getPrefixPath(finalPkg);
                        finalPkg = finalPkg.replace("/", ".");
                        prefixRecommanded = finalPkg;
                    }

                    if (prefixRecommanded.startsWith(".") && prefixRecommanded.length() > 1)
                        prefixRecommanded = prefixRecommanded.substring(1);
                    if (prefixRecommanded.length() > 0)
                        prefixRecommanded += "." + getTagType() + "s";
                }
            } catch (Exception e) {
                // Toast.makeText(C, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        private void show() {
            alert.setView(view);
            alert.setNegativeButton(R.string.cancel, null);
            alert.setPositiveButton(
                    R.string.add,
                    (d, v) -> {
                        String s1 = txt1.textInput.getText().toString();
                        String s2 = txt2.textInput.getText().toString();

                        if (s1.isEmpty() || s2.isEmpty()) {
                            Toast.makeText(C, R.string.cant_save, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (txt1.TIL.getError() != null) {
                            Toast.makeText(C, R.string.cant_save, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int type = ActivitiesCreator.TYPE_JAVA;
                        if (rKotlin.isChecked()) type = ActivitiesCreator.TYPE_KOTLIN;
                        createElement();
                    });
            alert.show();
        }

        private void createElement() {
            Element tagManifest = manifest.getElement("manifest", 0);
            if (manifest.getElement("application", 0) == null) {
                Element newApp = manifest.getDocument().createElement("application");
                tagManifest.appendChild(newApp);
                manifest.saveAllModif();
            }

            Element app = manifest.getElement("application", 0);
            Element newService = manifest.getDocument().createElement(getTagType());
            String className = txt1.textInput.getText().toString();
            String pkg = txt2.textInput.getText().toString();
            newService.setAttribute("android:name", pkg + "." + className);
            app.appendChild(newService);
            manifest.saveAllModif();

            prefixPathToElementType += "/" + pkg.replace(".", "/") + "/" + className;
            String code = "";

            switch (TAG_TYPE) {
                case SERVICES_TAG_TYPE:
                    if (rKotlin.isChecked()) {
                        code = FilesRef.SourceCode.Kotlin.CODEservice;
                        prefixPathToElementType += ".kt";
                    } else {
                        code = FilesRef.SourceCode.Java.CODEservice;
                        prefixPathToElementType += ".java";
                    }
                    break;
                case RECEIVERS_TAG_TYPE:
                    if (rKotlin.isChecked()) {
                        code = FilesRef.SourceCode.Kotlin.CODEreceiver;
                        prefixPathToElementType += ".kt";
                    } else {
                        code = FilesRef.SourceCode.Java.CODEreceiver;
                        prefixPathToElementType += ".java";
                    }
                    break;
                case PROVIDERS_TAG_TYPE:
                    if (rKotlin.isChecked()) {
                        code = FilesRef.SourceCode.Kotlin.CODEprovider;
                        prefixPathToElementType += ".kt";
                    } else {
                        code = FilesRef.SourceCode.Java.CODEprovider;
                        prefixPathToElementType += ".java";
                    }
                    break;
            }

            code = code.replace("$PACKAGE$", pkg).replace("$CLASS_NAME$", className);
            Files.writeFile(prefixPathToElementType, code);
            loadData();

            Logger.success(
                    getTagName() + " Manager",
                    C.getString(R.string.module)
                            + " : "
                            + DataRefManager.getInstance().currentAndroidModule.getPath(),
                    getTagName()
                            + " : "
                            + txt1.textInput.getText().toString()
                            + " "
                            + C.getString(R.string.added));
        }

        private void events() {
            rJava.setOnClickListener(
                    (v) -> {
                        rKotlin.setChecked(false);
                    });

            rKotlin.setOnClickListener(
                    (v) -> {
                        rJava.setChecked(false);
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
                                if (attr.equals(arg0.toString())) {
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
                            if (ResCodeUtils.isAValidePackageName(arg0.toString()))
                                txt2.TIL.setError(null);
                            else txt2.TIL.setError(C.getString(R.string.invalide_entry));
                        }
                    });
        }

        private void data() {
            rJava.setText("Java");
            rKotlin.setText("Kotlin");

            txt1.TIL.setHint(C.getString(R.string.class_name));
            txt2.TIL.setHint(
                    C.getString(R.string.xml_file_name)
                            + " (.xml : "
                            + C.getString(R.string.added)
                            + ")");

            txt1.textInput.setText("My" + getTagName().substring(0, getTagName().length() - 1));
            txt2.TIL.setHint(C.getString(R.string.package_name_prefix));
            txt2.textInput.setText(prefixRecommanded);

            switch (ProjectsUtils.getModuleType()) {
                case ProjectsUtils.TYPE_JAVA:
                    rJava.setChecked(true);
                    break;
                case ProjectsUtils.TYPE_KOTLIN:
                    rKotlin.setChecked(true);
                    break;
            }
        }

        private void view() {
            alert =
                    DialogBuilder.getDialogBuilder(
                            C,
                            getTagName(),
                            "module : "
                                    + DataRefManager.getInstance().currentAndroidModule.getName());

            txt1 = LayoutPeManagerViewPropertiesActivityBinding.inflate(LayoutInflater.from(C));
            txt2 = LayoutPeManagerViewPropertiesActivityBinding.inflate(LayoutInflater.from(C));

            rJava = new RadioButton(C);
            rKotlin = new RadioButton(C);

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

            rJava.setPadding(8, 8, 16, 8);
            rKotlin.setPadding(8, 8, 16, 8);

            LinearLayout linR = new LinearLayout(C);
            linR.setLayoutParams(CodeUtil.getLayoutParamsMW(0));
            linR.setGravity(Gravity.CENTER);
            linR.setOrientation(LinearLayout.HORIZONTAL);
            linR.addView((View) rJava);
            linR.addView((View) rKotlin);

            LinearLayout linParent = new LinearLayout(C);
            linParent.setLayoutParams(CodeUtil.getLayoutParamsMW(0));
            linParent.setOrientation(LinearLayout.VERTICAL);
            linParent.addView((View) txt1.getRoot());
            linParent.addView((View) txt2.getRoot());
            linParent.addView((View) linR);
            // linParent.addView((View) btnAssist);

            view = (View) linParent;
        }
    }
}
