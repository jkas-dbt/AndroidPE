package jkas.androidpe.projectAnalyzer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import jkas.androidpe.databinding.LayoutCreateNewProjectBinding;
import jkas.androidpe.projectUtils.dataCreator.ProjectsCreator;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.utils.ResCodeUtils;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import org.w3c.dom.Element;
import rkb.datasaver.RKBDataAppSettings;

/*
 * @author JKas
 */
public class NewProject {
    private OnCreateProjectListener listener;
    private View V;
    private AppCompatActivity C;
    private BottomSheetDialog alert;
    private LayoutCreateNewProjectBinding binding;

    private ArrayList<String> listP2P = new ArrayList<>();

    private String pnp = "com.mycompany.";
    private int projectType = 0, langP, saveLocP, minSdkP, maxSdkP;

    public NewProject(AppCompatActivity c) {
        C = c;

        ini();
        events();
    }

    public void show() {
        alert.show();
        loadDefault();
    }

    public void dismiss() {
        alert.dismiss();
    }

    private void events() {
        binding.autoCompTVSaveLocation.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        isInfoValided();
                        binding.textInputName.setText(binding.textInputName.getText());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {}
                });
        binding.autoCompTVLanguage.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        langP = arg2;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {}
                });
        binding.autoCompTVSdkMin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        minSdkP = arg2;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {}
                });
        binding.autoCompTVSdkTarget.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        maxSdkP = arg2;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {}
                });

        binding.textInputName.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        binding.TILname.setError(null);
                        String s = binding.textInputName.getText().toString();
                        s = s.toLowerCase().trim();
                        if (!ResCodeUtils.isAValideFolderName(s))
                            binding.TILname.setError(C.getString(R.string.error));
                        else binding.textInputPackageName.setText(pnp + s);
                        isInfoValided();
                    }
                });

        ResCodeUtils.ResAndCodeFilesFixer.fixPkgNameAndAssign(
                binding.TILpackageName, binding.textInputPackageName);

        binding.btnCreate.setOnClickListener(
                (v) -> {
                    if (!isInfoValided()) return;
                    ExecutorService exe = Executors.newSingleThreadExecutor();
                    exe.submit(
                            () -> {
                                createProject();
                            });
                    exe.shutdown();
                    try {
                        exe.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String path =
                            Files.getExternalStorageDir()
                                    + "/"
                                    + binding.autoCompTVSaveLocation.getText().toString()
                                    + "/"
                                    + binding.textInputName.getText().toString();
                    listener.onCreate(path);
                    Toast.makeText(C, C.getString(R.string.project_created), Toast.LENGTH_SHORT)
                            .show();
                    loadDefault();
                });

        binding.lin1.setOnClickListener(
                (v) -> {
                    setLinBgNull();
                    setBgViewLin(binding.lin1);
                    projectType = ProjectsCreator.PROJECT_TYPE_COMPOSE;
                    binding.autoCompTVLanguage.setText("kotlin", false);
                    binding.TILProjectLanguage.setEnabled(false);
                    binding.cbKotlinScript.setChecked(true);
                    binding.cbKotlinScript.setEnabled(false);
                });

        binding.lin2.setOnClickListener(
                (v) -> {
                    setLinBgNull();
                    setBgViewLin(binding.lin2);
                    projectType = ProjectsCreator.PROJECT_TYPE_ANDROIDX;
                    binding.autoCompTVLanguage.setText("java", false);
                    binding.TILProjectLanguage.setEnabled(true);
                    binding.cbKotlinScript.setChecked(false);
                    binding.cbKotlinScript.setEnabled(true);
                });
    }

    private void createProject() {
        final ProjectsCreator pc = new ProjectsCreator(C);
        pc.setPrefixPathToProject(binding.autoCompTVSaveLocation.getText().toString());
        pc.setFolderName(binding.textInputName.getText().toString());
        pc.setPackageName(binding.textInputPackageName.getText().toString());
        pc.setLanguage(langP);
        pc.setSdkMin(minSdkP);
        pc.setSdkTarget(maxSdkP);
        pc.setProjectType(projectType);
        pc.setGradleScript(
                binding.cbKotlinScript.isChecked()
                        ? ProjectsCreator.SCRIPT_KOTLIN
                        : ProjectsCreator.SCRIPT_GROOVY);
        pc.writeProjects();
        loadDefaultData();
    }

    private boolean isInfoValided() {
        binding.btnCreate.setEnabled(false);
        if (!ResCodeUtils.isAValideFolderName(binding.textInputName.getText().toString()))
            return false;
        if (!ResCodeUtils.isAValidePackageName(binding.textInputPackageName.getText().toString()))
            return false;

        if (binding.autoCompTVSaveLocation.getText().toString().isEmpty()) return false;
        if (binding.autoCompTVLanguage.getText().toString().isEmpty()) return false;
        if (binding.autoCompTVSdkMin.getText().toString().isEmpty()) return false;
        if (binding.autoCompTVSdkTarget.getText().toString().isEmpty()) return false;

        if (Files.isDirectory(
                Files.getExternalStorageDir()
                        + "/"
                        + binding.autoCompTVSaveLocation.getText().toString()
                        + "/"
                        + binding.textInputName.getText().toString())) {
            binding.TILname.setError(C.getString(R.string.folder_exists));
            return false;
        }

        binding.btnCreate.setEnabled(true);
        return true;
    }

    private void loadDefault() {
        listP2P.clear();
        for (Element s : RKBDataAppSettings.xmlF.getElementsByTagName("path"))
            listP2P.add(s.getTextContent());
        if (listP2P.size() == 0) listP2P.add("AndroidPEProjects");

        binding.autoCompTVSaveLocation.setAdapter(
                new ArrayAdapter<String>(C, android.R.layout.simple_list_item_1, listP2P));

        loadDefaultData();
    }

    private void loadDefaultData() {
        binding.autoCompTVSaveLocation.setText(listP2P.get(0), false);
        binding.autoCompTVLanguage.setText("Java", false);
        binding.autoCompTVCppStandard.setText("Toolchain Default", false);
        binding.autoCompTVSdkMin.setText("Android 5.0 (Lollipop) : API level 21", false);
        binding.autoCompTVSdkTarget.setText("Android 13.0 (T) : API level 33", false);

        pnp = RKBDataAppSettings.getAppPnp();
        binding.textInputName.setText("MyApplication");
        binding.textInputPackageName.setText(
                pnp + binding.textInputName.getText().toString().toLowerCase());

        setLinBgNull();
        setBgViewLin(binding.lin2);

        saveLocP = 0;
        langP = 0;
        minSdkP = 0;
        maxSdkP = 12;
    }

    private void setLinBgNull() {
        binding.lin1.setBackgroundColor(Color.TRANSPARENT);
        binding.lin2.setBackgroundColor(Color.TRANSPARENT);
        binding.lin3.setBackgroundColor(Color.TRANSPARENT);
    }

    private void setBgViewLin(View v) {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setColor(SurfaceColors.SURFACE_5.getColor((Context) C));
        gradient.setCornerRadius(24f);
        gradient.setStroke(0, Color.TRANSPARENT);
        v.setBackground(gradient);
    }

    private void ini() {
        binding = LayoutCreateNewProjectBinding.inflate(LayoutInflater.from(C));
        V = binding.getRoot();
        V.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, CodeUtil.getDisplayHeightPixels(C)));
        alert = new BottomSheetDialog(C);
        alert.setContentView(V);
        BottomSheetBehavior behavior = alert.getBehavior();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setDraggable(false);

        if (!CodeUtil.isTheSystemThemeDark(C))
            binding.lottieAnim.setAnimation("icons/lottie/androidpe_icon_dark.json");
        else binding.lottieAnim.setAnimation("icons/lottie/androidpe_icon_light.json");

        binding.lottieAnim.setSpeed(3f);
    }

    public void setOnCreateProjectListener(OnCreateProjectListener listener) {
        this.listener = listener;
    }

    public interface OnCreateProjectListener {
        public void onCreate(String path);
    }
}
