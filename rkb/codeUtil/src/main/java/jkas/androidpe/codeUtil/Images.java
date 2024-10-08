package jkas.codeUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.VectorDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Random;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * @auther JKas
 */
public class Images {
    public static void setImageFromAsset(Context context, String path, ImageView image) {
        try {
            InputStream ims = context.getAssets().open(path);
            Drawable d = Drawable.createFromStream(ims, null);
            image.setImageDrawable(d);
            ims.close();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void setImageSize(String path, int width, int height) {
        Bitmap src = BitmapFactory.decodeFile(path);
        Bitmap.createScaledBitmap(src, width, height, false);
    }

    public static boolean setImageFromDir(String path, ImageView image) {
        return setImageFromDir(path, image, 1024, 1024);
    }

    public static boolean setImageFromDir(String path, ImageView image, int a, int b) {
        if (path.toLowerCase().endsWith(".svg")) {
            SVGImageView svgImg = new SVGImageView(image.getContext());
            setImageFromDirSvgFile(path, svgImg);
            image.setImageDrawable(svgImg.getDrawable());
            return true;
        } else if (path.toLowerCase().endsWith(".xml")) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                FileReader fileReader = new FileReader(path);
                Reader reader = (Reader) fileReader;
                parser.setInput(reader);
                reader.close();

                FileInputStream fis = new FileInputStream(new File(path));
                Drawable drawable =
                        VectorDrawable.createFromXml(image.getContext().getResources(), parser);

                image.setImageDrawable(drawable);
                return true;
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                return false;
            }
        }
        image.setImageBitmap(Files.decodeSampleBitmapFromPath(path, a, b));
        return true;
    }

    public static void setImageFromDirSvgFile(String path, SVGImageView svgImageView) {
        try {
            File svgFile = new File(path);
            SVG svg = SVG.getFromInputStream(new FileInputStream(svgFile));
            svgImageView.setSVG(svg);
        } catch (SVGParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        }
    }

    public static void setSvgFileColor(Context context, String path, String color) {
        XmlManager xml = new XmlManager(context);
        if (xml.initializeFromPath(path))
            for (Element e : xml.getElementsByTagName("path")) e.setAttribute("fill", color);
        xml.saveAllModif();
    }

    public static void saveSvgAsPng(int width, int height, String pathSource, String pathDest) {
        OutputStream outputStream = null;

        try {
            final SVG svg = SVG.getFromInputStream(new FileInputStream(pathSource));
            svg.setDocumentWidth(width);
            svg.setDocumentHeight(height);

            final PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());

            final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            final Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            // Save the Bitmap as a PNG file
            File file = new File(pathDest.substring(0, pathDest.lastIndexOf("/")));
            if (!file.isDirectory()) {
                file.mkdir();
            }

            outputStream = new FileOutputStream(pathDest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (SVGParseException e) {
        } catch (IOException i) {
        }
    }

    private static void saveSvgAsVector(Context context, String pathSource, String pathDest) {
        // save .svg file as vector
    }

    private static void copyImageFromResourceToPath(Context context, int resourceR, String destPath)
            throws FileNotFoundException {
        ImageView img = new ImageView(context);
        img.setImageResource(resourceR);

        LinearLayout lin = new LinearLayout(context);
        lin.addView(img);
        lin.setDrawingCacheEnabled(true);

        Bitmap bitmap = lin.getDrawingCache();

        File newDir = new File(destPath);
        Random gen = new Random();
        int n = 10000;
        n = gen.nextInt(n);
        String fotoname = destPath.split("/")[destPath.split("/").length - 1];
        File file = new File(newDir, fotoname);

        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

        try {
            out.flush();
            out.close();
        } catch (Exception e) {

        }
    }
}
