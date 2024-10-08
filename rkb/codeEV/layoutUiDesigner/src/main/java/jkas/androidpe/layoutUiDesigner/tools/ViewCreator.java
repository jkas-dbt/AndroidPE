package jkas.androidpe.layoutUiDesigner.tools;

import android.content.Context;
import android.view.View;
import jkas.androidpe.layoutUiDesigner.palette.ViewDesignBuilder;
import jkas.androidpe.logger.LoggerLayoutUI;

/**
 * @author JKas
 */
public class ViewCreator {
    private static LoggerLayoutUI debug;

    private static void initLogger(String tag) {
        debug = LoggerLayoutUI.get(tag);
    }

    public static View create(String tag, Context C, String pkg, boolean isParent) {
        return create(tag, C, pkg, -1, isParent);
    }

    public static View create(String tag, Context C, String pkg, int attr, boolean isParent) {
        initLogger(tag);
        if (pkg.equals("include")) {
            if (debug != null) debug.w("ViewCreator", pkg + " : is not yet supported.");
            isParent = false;
        }

        ViewDesignBuilder vdb = new ViewDesignBuilder(C, pkg, attr, isParent);
        vdb.setOnViewNotFound(
                msg -> {
                    if (debug != null)
                        debug.w(
                                "ViewCreator",
                                pkg,
                                " : was not created. we returned a default view.\n\nException "
                                        + msg);
                });
        return vdb.getView();
    }
}
