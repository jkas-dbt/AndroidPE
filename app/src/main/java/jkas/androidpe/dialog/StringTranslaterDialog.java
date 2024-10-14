package jkas.androidpe.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import java.util.ArrayList;
import jkas.androidpe.layoutUiDesigner.utils.Utils;
import jkas.androidpe.resources.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jkas.androidpe.constants.LanguageTranslate;
import jkas.androidpe.databinding.DialogStringTranslaterBinding;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resourcesUtils.utils.ProjectsPathUtils;
import jkas.androidpe.translater.TranslateAPI;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;

public class StringTranslaterDialog {
    private AppCompatActivity C;
    private ExecutorService exec = Executors.newCachedThreadPool();
    private DialogStringTranslaterBinding binding;
    private BottomSheetDialog BSD;
    private Map<String, String> listSelectedLangs = new HashMap<>();
    private ArrayList<String> listNames = new ArrayList<>();
    private ArrayList<String> listValues = new ArrayList<>();
    private ArrayList<Chip> listChip = new ArrayList<>();
    private String absolutePathToResValuesFolder;
    private boolean inProcess = false;

    public StringTranslaterDialog(Context c) {
        C = (AppCompatActivity) c;
        init();
        events();
        loadCheck();
        loadDefault(false);
    }

    public void show() {
        if (inProcess) {
            BSD.show();
            return;
        }
        if (DataRefManager.getInstance().currentAndroidModule == null) {
            Toast.makeText(C, R.string.no_module_is_selected, Toast.LENGTH_SHORT).show();
            return;
        }
        absolutePathToResValuesFolder =
                DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath()
                        + ProjectsPathUtils.VALUES_PATH;
        if (!Files.isDirectory(absolutePathToResValuesFolder)) {
            Toast.makeText(
                            C,
                            "res/values " + C.getString(R.string.not_found).toLowerCase(),
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        binding.tvModule.setText(DataRefManager.getInstance().currentAndroidModule.getPath());
        loadData();
        BSD.show();
        ArrayList<String> list =
                Files.listDir(
                        DataRefManager.getInstance().currentAndroidModule.getProjectAbsolutePath()
                                + ProjectsPathUtils.RES_PATH);
        for (var chip : listChip) {
            String path =
                    absolutePathToResValuesFolder
                            + "-"
                            + ((String) chip.getTag()).replace("-", "-r");
            for (var pS : list) if (pS.contains(path)) chip.setChecked(true);
        }
    }

    private void loadData() {
        listNames.clear();
        listValues.clear();
        for (var file : Files.listFile(absolutePathToResValuesFolder)) {
            XmlManager xml = new XmlManager(C);
            xml.initializeFromPath(file);
            for (var e : xml.getElementsByTagName("string")) {
                listNames.add(e.getAttribute("name"));
                listValues.add(e.getTextContent());
            }
        }
    }

    private void loadCheck() {
        listSelectedLangs.clear();
        binding.chipGroup.removeAllViews();
        for (final var pair : LanguageTranslate.list) {
            final Chip chip = new Chip(C);
            chip.setTag(pair.second);
            chip.setCheckable(true);
            chip.setText(CodeUtil.capitalizeFirstLetter(pair.first));
            chip.setOnCheckedChangeListener(
                    (c, checked) -> {
                        String first = pair.first;
                        String second = pair.second;
                        if (listSelectedLangs.containsKey(first.intern()))
                            listSelectedLangs.remove(first.intern());
                        if (checked) listSelectedLangs.put(first, second);
                        updateInfo();
                    });
            listChip.add(chip);
            binding.chipGroup.addView(chip);
        }
    }

    private void updateInfo() {
        binding.tvInfo.setText("(" + listSelectedLangs.size() + " | 65)");
    }

    private void init() {
        BSD = new BottomSheetDialog(C);
        binding = DialogStringTranslaterBinding.inflate(LayoutInflater.from(C));
        BottomSheetBehavior behavior = BSD.getBehavior();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setDraggable(false);
        BSD.setContentView((View) binding.getRoot());
    }

    private void events() {
        binding.icRefresh.setOnClickListener(v -> loadCheck());
        binding.btnTranslate.setOnClickListener(
                v -> {
                    if (listSelectedLangs.size() == 0)
                        Toast.makeText(C, R.string.select, Toast.LENGTH_SHORT).show();
                    else
                        DialogBuilder.getDialogBuilder(
                                        C,
                                        C.getString(R.string.warning),
                                        C.getString(R.string.warning_before_translate_string_res))
                                .setNegativeButton(R.string.cancel, null)
                                .setPositiveButton(R.string.translate, (d, sv) -> translate())
                                .show();
                });

        binding.btnLangs.setOnClickListener(v -> binding.vf.setDisplayedChild(0));
        binding.btnProcess.setOnClickListener(v -> binding.vf.setDisplayedChild(1));
    }

    private void translate() {
        finished = 0;
        loadDefault(true);
        binding.linInfoProcess.removeAllViews();
        binding.btnToggle.check(binding.btnProcess.getId());
        binding.vf.setDisplayedChild(1);
        for (var pair : CodeUtil.convertMapToPair(listSelectedLangs)) {
            FileToTranslate file = new FileToTranslate((String) pair.first, (String) pair.second);
            binding.linInfoProcess.addView(file.getView());
            exec.execute(() -> file.translate());
        }
    }

    private void loadDefault(boolean inProcess) {
        this.inProcess = inProcess;
        if (inProcess) {
            binding.progress.setVisibility(View.VISIBLE);
            binding.btnTranslate.setEnabled(false);
        } else {
            binding.progress.setVisibility(View.GONE);
            binding.btnTranslate.setEnabled(true);
        }
    }

    private void loadDefault() {
        if (finished == listSelectedLangs.size()) loadDefault(false);
    }

    private int finished = 0;

    private class FileToTranslate {
        private XmlManager xmlFile;
        private String to, toDesc, msg, pathXmlFile;
        private TranslateAPI translator;
        private TextView tv;
        private int success = 0, failed = 0;
        private int i = 0;

        public FileToTranslate(String toDesc, String to) {
            this.to = to;
            this.toDesc = toDesc;
            this.xmlFile = new XmlManager(C);
            tv = new TextView(C);
            tv.setLayoutParams(CodeUtil.getLayoutParamsMW(20));
            tv.setPadding(20, 20, 20, 20);
            tv.setGravity(Gravity.CENTER);
            tv.setText(". . .");
            Utils.drawDashPathStroke(tv);
            translator = new TranslateAPI("auto", to);
            translator.setTranslateListener(
                    new TranslateAPI.OnTranslateListener() {

                        @Override
                        public void onSuccess(String translatedText) {
                            addData(translatedText);
                            success++;
                            updateInfo();
                        }

                        @Override
                        public void onFailure(String ErrorText) {
                            addData(null);
                            failed++;
                            updateInfo();
                        }
                    });
            translator.addListData(listValues);
            pathXmlFile =
                    absolutePathToResValuesFolder + "-" + to.replace("-", "-r") + "/strings.xml";
            String newCode =
                    """
<?xml version="1.0" encoding="UTF-8"?>
<resources/>
            """;
            Files.writeFile(pathXmlFile, newCode);
            xmlFile.initializeFromPath(pathXmlFile);
        }

        private void addData(String translatedText) {
            Element resources = xmlFile.getElement("resources", 0);
            if (translatedText != null) { // add data has res
                Element string = xmlFile.getDocument().createElement("string");
                string.setAttribute("name", listNames.get(i));
                translatedText = translatedText.replace("<", "&lt;");
                translatedText = translatedText.replace(">", "&gt;");
                translatedText = translatedText.replace("&", "&amp;");
                translatedText = translatedText.replace("&amp;lt;", "&lt;");
                translatedText = translatedText.replace("&amp;gt;", "&gt;");
                translatedText = translatedText.replace("\"", "\\\"");
                translatedText = translatedText.replace("\'", "\\\'");
                translatedText = translatedText.replace("\\\\", "\\");
                string.setTextContent(translatedText);
                resources.appendChild(string);
            } else { // add data haw comment
                Comment comment = xmlFile.getDocument().createComment("error");
                comment.setTextContent(listNames.get(i) + "can not translate");
                resources.appendChild(comment);
            }
            xmlFile.saveAllModif();
        }

        private void updateInfo() {
            msg =
                    "from <b>res/values</b> to <b>"
                            + CodeUtil.capitalizeFirstLetter(toDesc)
                            + " ("
                            + to
                            + ")</b><br>"
                            + "Success : "
                            + success
                            + ", Failed : "
                            + failed
                            + "      ("
                            + (success + failed)
                            + " / "
                            + listValues.size()
                            + ")";
            i++;
            C.runOnUiThread(
                    () -> {
                        tv.setText(HtmlCompat.fromHtml(msg, HtmlCompat.FROM_HTML_MODE_COMPACT));
                        if ((success + failed) == listValues.size()) {
                            finished++;
                            loadDefault();
                        }
                    });
        }

        public void translate() {
            i = 0;
            success = 0;
            failed = 0;
            translator.translate();
        }

        public View getView() {
            return tv;
        }
    }
}
