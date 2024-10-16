package jkas.androidpe.explorer.dialog;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import jkas.androidpe.project.AndroidModule;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resources.R;

/**
 * @author JKas
 */
public class DialogSelectModule {
    public static final int SINGLE_SELECT_MODE = 0;
    public static final int MULTI_SELECT_MODE = 1;
    public static final int MODULES_REF_SELECT_MODE = 2;

    private OnModuleSelected listener;
    private OnMultiModuleSelected listenerMulti;
    private OnModuleRefSelected listenerModulesSelected;

    private Context C;

    private int defaultOption = 0;
    private int selectedOption = 0;
    private int selectMode = SINGLE_SELECT_MODE;

    private AlertDialog alert;

    public DialogSelectModule(Context c, int mode) {
        C = c;
        selectMode = mode;
        if (mode == SINGLE_SELECT_MODE) initSingle();
        else if (mode == MULTI_SELECT_MODE) initMulti();
        else if (mode == MODULES_REF_SELECT_MODE) initModulesRefSelected();
    }

    private void initModulesRefSelected() {
        try {
            final ArrayList<String> listAvailable = new ArrayList<>();

            for (AndroidModule am : DataRefManager.getInstance().listAndroidModule) {
                if (am.getPath().equals(DataRefManager.getInstance().currentAndroidModule.getPath())) continue;
                boolean verif = false;
                for (String s : DataRefManager.getInstance().currentAndroidModule.getRefToOthersModules()) {
                    if (s.equals(am.getPath())) {
                        verif = true;
                        break;
                    }
                }
                if (verif) continue;
                listAvailable.add(am.getPath());
            }

            if (listAvailable.size() == 0) {
                DialogBuilder.getDialogBuilder(
                                C, null, C.getString(R.string.info_no_data_to_display_was_found))
                        .show();
                return;
            }

            String[] listModules = new String[listAvailable.size()];
            boolean[] checkedItems = new boolean[listAvailable.size()];
            int i = 0;
            for (String s : listAvailable) {
                listModules[i] = s;
                checkedItems[i] = false;
                i++;
            }

            DialogBuilder.getDialogBuilder(C, C.getString(R.string.modules), null)
                    .setMultiChoiceItems(
                            listModules,
                            checkedItems,
                            (dialog, swich, isChecked) -> {
                                checkedItems[swich] = isChecked;
                            })
                    .setPositiveButton(
                            R.string.add,
                            (d, v) -> {
                                int j = 0;
                                ArrayList<String> listRefToModules = new ArrayList<>();
                                for (String s : listModules) {
                                    if (checkedItems[j]) listRefToModules.add(s);
                                    j++;
                                }
                                if (listRefToModules.size() == 0)
                                    Toast.makeText(C, R.string.no_data_selected, Toast.LENGTH_SHORT)
                                            .show();
                                else listenerModulesSelected.onSelected(listRefToModules);
                            })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        } catch (Exception err) {
            Toast.makeText(C, err.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initMulti() {
        try {
            String[] listModules = new String[DataRefManager.getInstance().listAndroidModule.size()];
            boolean[] checkedItems = new boolean[listModules.length];

            int i = 0;
            for (AndroidModule am : DataRefManager.getInstance().listAndroidModule) {
                checkedItems[i] = false;
                if (am.getPath().equals(DataRefManager.getInstance().currentAndroidModule.getPath())) {
                    listModules[i] =
                            DataRefManager.getInstance().listAndroidModule.get(i).getPath() + " (Current)";
                    i++;
                    continue;
                }
                listModules[i] = DataRefManager.getInstance().listAndroidModule.get(i).getPath();

                for (String m : DataRefManager.getInstance().currentAndroidModule.getRefToOthersModules())
                    if (am.getPath().equals(m)) {
                        checkedItems[i] = true;
                        break;
                    }
                i++;
            }

            DialogBuilder.getDialogBuilder(C, C.getString(R.string.modules), null)
                    .setMultiChoiceItems(
                            listModules,
                            checkedItems,
                            (dialog, swich, isChecked) -> {
                                if (!listModules[swich].equals(
                                        DataRefManager.getInstance().currentAndroidModule.getPath()))
                                    checkedItems[swich] = isChecked;
                            })
                    .setPositiveButton(
                            R.string.add,
                            (d, v) -> {
                                int j = 0;
                                ArrayList<String> listRefToModules = new ArrayList<>();
                                for (String s : listModules) {
                                    if (checkedItems[j]) listRefToModules.add(s);
                                    j++;
                                }
                                listenerMulti.onMultiSelected(listRefToModules);
                            })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        } catch (Exception err) {
            Toast.makeText(C, err.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initSingle() {
        String[] listModules = new String[DataRefManager.getInstance().listAndroidModule.size()];

        for (int i = 0; i < DataRefManager.getInstance().listAndroidModule.size(); i++) {
            listModules[i] = DataRefManager.getInstance().listAndroidModule.get(i).getPath();
            try {
                if (DataRefManager.getInstance().currentAndroidModule
                        .getPath()
                        .equals(DataRefManager.getInstance().listAndroidModule.get(i).getPath()))
                    defaultOption = i;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        alert =
                DialogBuilder.getDialogBuilder(C, C.getString(R.string.modules), null)
                        .setSingleChoiceItems(
                                listModules,
                                defaultOption,
                                (dialog, which) -> {
                                    selectedOption = which;
                                    if (selectedOption != defaultOption)
                                        listener.selected(selectedOption);
                                    defaultOption = selectedOption;
                                    alert.cancel();
                                })
                        .create();
        alert.show();
    }

    public void setOnModuleSelected(OnModuleSelected listener) {
        this.listener = listener;
    }

    public void setOnMultiModuleSelected(OnMultiModuleSelected listener) {
        this.listenerMulti = listener;
    }

    public void setOnModulesRefSelected(OnModuleRefSelected listener) {
        this.listenerModulesSelected = listener;
    }

    public interface OnModuleSelected {
        public void selected(int modulePosition);
    }

    public interface OnMultiModuleSelected {
        public void onMultiSelected(ArrayList<String> listPosition);
    }

    public interface OnModuleRefSelected {
        public void onSelected(ArrayList<String> listSelected);
    }
}
