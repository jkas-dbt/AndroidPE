package jkas.androidpe.layoutUiDesigner.palette;

import android.graphics.Paint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import jkas.androidpe.layoutUiDesigner.utils.Utils;
import jkas.androidpe.resourcesUtils.utils.ResourcesValuesFixer;

/**
 * @author JKas
 */
public class DefaultView extends TextView {
    private String text = "null";
    private int minSize;
    private Paint paint;

    public DefaultView(Context ctx, String pkg) {
        super(ctx);
        text = pkg;
        setId(View.generateViewId());
        int padding = (int) ResourcesValuesFixer.getDimen(ctx, "8dp");
        setPadding(padding, padding, padding, padding);
        setDisplayText();
        Utils.drawDashPathStroke(this);
    }

    public void setDisplayText() {
        this.setText(text);
        this.setTypeface(Typeface.MONOSPACE);
        this.setGravity(Gravity.CENTER);
    }
}
