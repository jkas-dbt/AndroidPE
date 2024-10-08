package jkas.androidpe.explorer.dialog;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import jkas.androidpe.resourcesUtils.dialog.DialogBuilder;
import jkas.androidpe.resources.R;

/**
 * @author JKas
 */
public class DialogSelector {
    public static final int SINGLE_SELECT_MODE = 0;
    public static final int MULTI_SELECT_MODE = 1;

    private OnItemSelected listener;
    private OnMultiItemSelected listenerMulti;
    private Context C;
    private int selectMode = SINGLE_SELECT_MODE;
    private String[] listData;
    private String titleData;
    private boolean[] listB;
    private MaterialAlertDialogBuilder madb;

    public DialogSelector(Context c, String title, String[] list, int mode) {
        C = c;
        selectMode = mode;
        titleData = title;
        listData = list;
        madb = DialogBuilder.getDialogBuilder(C, titleData, null);
        switch (mode) {
            case SINGLE_SELECT_MODE:
                initSingle(0);
                break;
            case MULTI_SELECT_MODE:
                initMulti();
                break;
            default:
                Toast.makeText(C, "Any mode has been selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMulti() {
        madb.setPositiveButton(R.string.save, (d, v) -> listenerMulti.onMultiSelected(listB));
    }

    public DialogSelector setSelected(boolean[] listSelcted) {
        listB = listSelcted;
        madb.setMultiChoiceItems(listData, listB, (dial, p, b) -> listB[p] = b);
        return this;
    }

    private void initSingle(int p) {
        madb.setSingleChoiceItems(
                listData,
                p,
                (dialog, which) -> {
                    listener.selected(which);
                    dialog.cancel();
                });
    }

    public void show() {
        madb.show();
    }

    public void setSinglePosition(int p) {
        initSingle(p);
    }

    public void setOnItemSelected(OnItemSelected listener) {
        this.listener = listener;
    }

    public void setOnMultiItemSelected(OnMultiItemSelected listener) {
        this.listenerMulti = listener;
    }

    public interface OnItemSelected {
        public void selected(int position);
    }

    public interface OnMultiItemSelected {
        /*
         *Return a Boolean array of the same size as the array that was passed as a parameter...,
         *indexing (TRUE) on the positions of the selected choice.
         */
        public void onMultiSelected(boolean[] listPosition);
    }
}
