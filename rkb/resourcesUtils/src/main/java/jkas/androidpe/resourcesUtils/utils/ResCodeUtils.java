package jkas.androidpe.resourcesUtils.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.ImageView;
import com.caverock.androidsvg.SVGImageView;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jkas.androidpe.resources.R;
import jkas.androidpe.resourcesUtils.requests.ProjectDataRequested;
import jkas.codeUtil.CodeUtil;
import jkas.codeUtil.Files;
import jkas.codeUtil.Images;
import jkas.codeUtil.XmlManager;
import org.w3c.dom.Element;

/*
 * @author JKas
 */
public class ResCodeUtils {
    public static final String PATTERN_PKG = "[A-Za-z][A-Za-z0-9_]*(\\.[A-Za-z][A-Za-z0-9_]*)*$";

    public static void setImageTreeView(ImageView img, String path) {
        if (path == null || img == null) return;
        img.setImageTintList(
                ColorStateList.valueOf(
                        getColorFromResolveAttribute(
                                img.getContext(), android.R.attr.colorPrimary)));
        if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.PIC))
            img.setImageResource(R.drawable.ic_insert_photo);
        else if (Files.isDirectory(path)) img.setImageResource(R.drawable.ic_folder);
        else if (path.toLowerCase().endsWith(".c")) img.setImageResource(R.drawable.ic_language_c);
        else if (path.toLowerCase().endsWith(".cpp"))
            img.setImageResource(R.drawable.ic_language_cpp);
        else if (path.toLowerCase().endsWith(".cs"))
            img.setImageResource(R.drawable.ic_language_csharp);
        else if (path.toLowerCase().endsWith(".css"))
            img.setImageResource(R.drawable.ic_language_css3);
        else if (path.toLowerCase().endsWith(".html"))
            img.setImageResource(R.drawable.ic_language_html5);
        else if (path.toLowerCase().endsWith(".htm"))
            img.setImageResource(R.drawable.ic_language_html5);
        else if (path.toLowerCase().endsWith(".java"))
            img.setImageResource(R.drawable.ic_language_java);
        else if (path.toLowerCase().endsWith(".js"))
            img.setImageResource(R.drawable.ic_language_javascript);
        else if (path.toLowerCase().endsWith(".kt"))
            img.setImageResource(R.drawable.ic_language_kotlin);
        else if (path.toLowerCase().endsWith(".gradle.kts"))
            img.setImageResource(R.drawable.ic_gradle_kts);
        else if (path.toLowerCase().endsWith(".py"))
            img.setImageResource(R.drawable.ic_language_python);
        else if (path.toLowerCase().endsWith(".xml"))
            if (Files.readFile(path).trim().contains("<vector"))
                img.setImageResource(R.drawable.ic_insert_photo);
            else img.setImageResource(R.drawable.ic_xml);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.ARCHIVE))
            img.setImageResource(R.drawable.ic_archive);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.DOC))
            img.setImageResource(R.drawable.ic_microsoft_office);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.VIDEO))
            img.setImageResource(R.drawable.ic_video);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.AUDIO))
            img.setImageResource(R.drawable.ic_music);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.ANDROID))
            img.setImageResource(R.drawable.ic_android);
        else if (path.endsWith(".gradle")) img.setImageResource(R.drawable.ic_gradle);
        else if (path.endsWith("/gradlew") || path.endsWith("/gradlew.bat"))
            img.setImageResource(R.drawable.ic_powershell);
        else if (path.endsWith(".properties")) img.setImageResource(R.drawable.ic_file_cog_outline);
        else img.setImageResource(R.drawable.ic_file_txt);

        if (path.toLowerCase().endsWith(".kt") || path.toLowerCase().endsWith(".kts"))
            img.setPadding(16, 16, 16, 16);
    }

    public static void setImage(ImageView img, String path) {
        if (path == null || img == null) return;
        img.setImageTintList(null);
        if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.PIC))
            Picasso.get()
                    .load(new File(path))
                    .resize(43, 43)
                    .placeholder(R.drawable.ic_file_txt)
                    .centerCrop()
                    .into(img);
        else
            img.setImageTintList(
                    ColorStateList.valueOf(
                            getColorFromResolveAttribute(
                                    img.getContext(), android.R.attr.colorPrimary)));

        if (Files.isDirectory(path)) img.setImageResource(R.drawable.ic_folder);
        else if (path.toLowerCase().endsWith(".c")) img.setImageResource(R.drawable.ic_language_c);
        else if (path.toLowerCase().endsWith(".cpp"))
            img.setImageResource(R.drawable.ic_language_cpp);
        else if (path.toLowerCase().endsWith(".cs"))
            img.setImageResource(R.drawable.ic_language_csharp);
        else if (path.toLowerCase().endsWith(".css"))
            img.setImageResource(R.drawable.ic_language_css3);
        else if (path.toLowerCase().endsWith(".html"))
            img.setImageResource(R.drawable.ic_language_html5);
        else if (path.toLowerCase().endsWith(".htm"))
            img.setImageResource(R.drawable.ic_language_html5);
        else if (path.toLowerCase().endsWith(".java"))
            img.setImageResource(R.drawable.ic_language_java);
        else if (path.toLowerCase().endsWith(".js"))
            img.setImageResource(R.drawable.ic_language_javascript);
        else if (path.toLowerCase().endsWith(".kt"))
            img.setImageResource(R.drawable.ic_language_kotlin);
        else if (path.toLowerCase().endsWith(".gradle.kts"))
            img.setImageResource(R.drawable.ic_gradle_kts);
        else if (path.toLowerCase().endsWith(".py"))
            img.setImageResource(R.drawable.ic_language_python);
        else if (path.toLowerCase().endsWith(".xml"))
            if (Files.readFile(path).trim().contains("<vector"))
                img.setImageDrawable(ImageXmlUtil.createDrawableFromPath(img.getContext(), path));
            else img.setImageResource(R.drawable.ic_xml);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.ARCHIVE))
            img.setImageResource(R.drawable.ic_archive);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.DOC))
            img.setImageResource(R.drawable.ic_microsoft_office);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.PIC))
            Picasso.get().load(new File(path)).resize(43, 43).centerCrop().into(img);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.VIDEO))
            img.setImageResource(R.drawable.ic_video);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.AUDIO))
            img.setImageResource(R.drawable.ic_music);
        else if (Files.FileItem.getType(path.toLowerCase()).equals(Files.FileItem.ANDROID))
            img.setImageResource(R.drawable.ic_android);
        else if (path.endsWith(".gradle")) img.setImageResource(R.drawable.ic_gradle);
        else if (path.endsWith("/gradlew") || path.endsWith("/gradlew.bat"))
            img.setImageResource(R.drawable.ic_powershell);
        else if (path.endsWith(".properties")) img.setImageResource(R.drawable.ic_file_cog_outline);
        else img.setImageResource(R.drawable.ic_file_txt);

        if (path.toLowerCase().endsWith(".kt") || path.toLowerCase().endsWith(".kts"))
            img.setPadding(16, 16, 16, 16);
    }

    public static int getColorFromResolveAttribute(Context c, int res) {
        TypedValue tvMaterial = MaterialAttributes.resolve(c, res);
        TypedValue typedValue = new TypedValue();
        if (tvMaterial != null)
            if (c.getTheme().resolveAttribute(res, tvMaterial, true)) return tvMaterial.data;
        if (c.getTheme().resolveAttribute(res, typedValue, true)) return typedValue.data;
        return Color.TRANSPARENT;
    }

    public static int getDimenFromResolveAttribute(Context c, int res) {
        TypedValue tvMaterial = MaterialAttributes.resolve(c, res);
        TypedValue typedValue = new TypedValue();
        TypedValue finalTV = typedValue;
        if (tvMaterial != null)
            if (c.getTheme().resolveAttribute(res, tvMaterial, true)) finalTV = tvMaterial;
        if (c.getTheme().resolveAttribute(res, typedValue, true)) finalTV = typedValue;
        return TypedValue.complexToDimensionPixelSize(
                        finalTV.data, c.getResources().getDisplayMetrics())
                / 10
                * 4;
    }

    public static String getValueFromResolveAttribute(Context c, int res) {
        TypedValue tvMaterial = MaterialAttributes.resolve(c, res);
        TypedValue typedValue = new TypedValue();
        TypedValue finalTV = typedValue;
        if (tvMaterial != null)
            if (c.getTheme().resolveAttribute(res, tvMaterial, true)) finalTV = tvMaterial;
        if (c.getTheme().resolveAttribute(res, typedValue, true)) finalTV = typedValue;
        return (String) finalTV.coerceToString();
    }

    public static void setImage(SVGImageView imgSVG, String path) {
        Images.setImageFromDirSvgFile(path, imgSVG);
    }

    public static boolean isAValidePackageName(String pnp) {
        return Pattern.compile("^[A-Za-z][A-Za-z0-9_]*(\\.[A-Za-z][A-Za-z0-9_]*)*$")
                .matcher(pnp)
                .matches();
    }

    public static boolean isAValideFolderName(String name) {
        if (Pattern.compile("^[a-zA-Z_]([A-Za-z0-9_-]*)*$")
                .matcher(name.replace("/", ""))
                .matches()) return true;
        else return false;
    }

    public static boolean isAValideNameForSimplePath(String input) {
        if (input.trim().isEmpty()) return false;
        String patternString = "[/*:?|<>]";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);
        return !matcher.find();
    }

    public static String getInfoXmlFolderName(Context C, String text) {
        Pattern pattern = Pattern.compile("^[a-z_]([a-z0-9_-]*)*$");
        Matcher matcher = pattern.matcher(text.replace("/", ""));
        if (!matcher.find()) return C.getString(R.string.invalide_name);
        return null;
    }

    public static String getInfoXmlFileName(Context C, String text) {
        Pattern pattern = Pattern.compile("^[a-z][a-z0-9_]*\\.xml");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) return C.getString(R.string.invalide_name);
        return null;
    }

    public static String getInfoCodeFolderName(Context C, String text) {
        Pattern pattern = Pattern.compile("^[A-Za-z_]([A-Za-z0-9_]*)*$");
        Matcher matcher = pattern.matcher(text.replace("/", ""));
        if (!matcher.find()) return C.getString(R.string.invalide_name);
        return null;
    }

    public static String getInfoCodeFileName(Context C, String text) {
        Pattern pattern = Pattern.compile("^[A-Za-z_][A-Za-z0-9_]*\\.[A-Za-z_][A-Za-z0-9_]*$");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) return C.getString(R.string.invalide_name);
        return null;
    }

    public static String removeJavaComment(String code) {
        String fileContent = code;
        String commentRegex = "(?s)/\\*.*?\\*/|//.*?$";
        Pattern pattern = Pattern.compile(commentRegex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(fileContent);
        code = matcher.replaceAll("");
        return code;
    }

    public static class ResAndCodeFilesFixer {
        public static void fixCodeFileNameAndAssign(
                final TextInputLayout layout, final TextInputEditText text) {
            text.removeTextChangedListener(null);
            text.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void onTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void afterTextChanged(Editable editable) {
                            layout.setError(null);
                            if (editable.toString().length() == 0) return;
                            String n = fixCodeFileName(editable.toString());
                            if (n.equals(editable.toString())) return;
                            else {
                                int p = text.getSelectionStart();
                                text.setText(n);
                                if (p >= n.length()) text.setSelection(n.length());
                                else text.setSelection(p);
                            }
                            if (n.startsWith(".") || n.endsWith("_") || n.endsWith(".")) {
                                layout.setError(
                                        layout.getContext().getString(R.string.invalide_name));
                            }
                        }
                    });
        }

        public static void fixXmlFileNameAndAssign(
                final TextInputLayout layout, final TextInputEditText text) {
            text.removeTextChangedListener(null);
            text.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void onTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void afterTextChanged(Editable editable) {
                            try {
                                layout.setError(null);
                                if (editable.toString().length() == 0) return;
                                String n = fixXmlFileName(editable.toString());
                                if (n.equals(editable.toString())) return;
                                else {
                                    int p = text.getSelectionStart();
                                    text.setText(n);
                                    if (p >= n.length()) text.setSelection(n.length());
                                    else text.setSelection(p);
                                }
                                if (n.startsWith(".") || n.endsWith("_") || n.endsWith(".")) {
                                    layout.setError(
                                            layout.getContext().getString(R.string.invalide_name));
                                }
                            } catch (Exception e) {
                                layout.setError(
                                        layout.getContext().getString(R.string.invalide_entry));
                                e.printStackTrace();
                            }
                        }
                    });
        }

        public static void fixPkgNameAndAssign(
                final TextInputLayout layout, final TextInputEditText text) {
            text.removeTextChangedListener(null);
            text.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void onTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void afterTextChanged(Editable editable) {
                            try {
                                layout.setError(null);
                                if (editable.toString().trim().length() == 0) return;
                                String n = fixPackageName(editable.toString());
                                if (n.equals(editable.toString())) return;
                                else {
                                    int p = text.getSelectionStart();
                                    text.setText(n);
                                    if (p >= n.length()) text.setSelection(n.length());
                                    else text.setSelection(p);
                                }
                                if (n.startsWith("_")
                                        || n.startsWith(".")
                                        || n.endsWith("_")
                                        || n.endsWith(".")) {
                                    layout.setError(
                                            layout.getContext().getString(R.string.invalide_name));
                                }
                            } catch (Exception e) {
                                layout.setError(
                                        layout.getContext().getString(R.string.invalide_entry));
                                e.printStackTrace();
                            }
                        }
                    });
        }

        public static void fixXmlIdNameAndAssign(
                final TextInputLayout layout, final TextInputEditText text) {
            text.removeTextChangedListener(null);
            text.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void onTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void afterTextChanged(Editable editable) {
                            try {
                                layout.setError(null);
                                if (editable.toString().trim().length() == 0) return;
                                String n = fixXmlIdName(editable.toString());
                                if (n.equals(editable.toString())) return;
                                else {
                                    int p = text.getSelectionStart();
                                    text.setText(n);
                                    if (p >= n.length()) text.setSelection(n.length());
                                    else text.setSelection(p);
                                }
                                if (n.startsWith(".") || n.endsWith("_") || n.endsWith(".")) {
                                    layout.setError(
                                            layout.getContext().getString(R.string.invalide_name));
                                }
                            } catch (Exception e) {
                                layout.setError(
                                        layout.getContext().getString(R.string.invalide_entry));
                                e.printStackTrace();
                            }
                        }
                    });
        }

        public static void fixPathNameAndAssign(
                final TextInputLayout layout, final TextInputEditText text) {
            text.removeTextChangedListener(null);
            text.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void onTextChanged(
                                CharSequence arg0, int arg1, int arg2, int arg3) {}

                        @Override
                        public void afterTextChanged(Editable editable) {
                            try {
                                layout.setError(null);
                                if (editable.toString().trim().length() == 0) return;
                                String n = fixPathName(editable.toString());
                                if (n.equals(editable.toString())) return;
                                else {
                                    int p = text.getSelectionStart();
                                    text.setText(n);
                                    if (p >= n.length()) text.setSelection(n.length());
                                    else text.setSelection(p);
                                }
                            } catch (Exception e) {
                                layout.setError(
                                        layout.getContext().getString(R.string.invalide_entry));
                                e.printStackTrace();
                            }
                        }
                    });
        }

        public static String convertLayoutFileNameToClass(String layoutFileName) {
            String finalValues = "";
            String fromFix = fixXmlFileName(layoutFileName);
            if (fromFix != null) layoutFileName = fromFix;
            try {
                for (String s : layoutFileName.split("_")) {
                    if (s.length() == 0) continue;
                    finalValues += s.substring(0, 1).toUpperCase();
                    finalValues += s.substring(1);
                }
                return finalValues;
            } catch (Exception e) {
                return null;
            }
        }

        public static String fixPathName(String name) {
            String result = "";
            for (char c : name.toCharArray())
                if (("[/*:?|<>]").contains(String.valueOf(c))) result += "_";
                else result += String.valueOf(c);
            result = result.replace("__", "_");
            return result;
        }

        public static String fixXmlFileName(String name) {
            String result = "";
            for (char c : name.toLowerCase().toCharArray())
                if (!String.valueOf(c).matches("[a-z0-9_\\.]")) result += "_";
                else result += String.valueOf(c);
            result = result.replace("..", ".").replace("__", "_");
            result = result;
            return result;
        }

        public static String fixXmlIdName(String name) {
            String result = "";
            for (char c : name.toCharArray())
                if (!String.valueOf(c).matches("[a-zA-Z0-9_]")) result += "_";
                else result += String.valueOf(c);
            result = result.replace("__", "_");
            return result;
        }

        public static String fixCodeFileName(String name) {
            String result = "";
            for (char c : name.toCharArray())
                if (String.valueOf(c).matches("[a-zA-Z0-9_\\.]")) result += String.valueOf(c);
            result = result.replace("..", ".").replace("__", "_");
            if (result.startsWith(".")) result = result.substring(1);
            return result;
        }

        public static String fixPackageName(String pnp) {
            if (pnp.matches(PATTERN_PKG)) return pnp;
            String result = "";
            for (char c : pnp.toCharArray())
                if (!String.valueOf(c).matches("[A-Za-z0-9_]+")) result += ".";
                else result += String.valueOf(c);
            return result;
        }
    }

    private static class ImageXmlUtil {
        public static Drawable createDrawableFromPath(Context C, String path) {
            String p = null;
            if (path.toLowerCase().endsWith(".svg")) p = path;
            else p = convertXmlFileToSvg(C, path);
            SVGImageView img = new SVGImageView(C);
            Images.setImageFromDirSvgFile(p, img);
            return img.getDrawable();
        }

        private static String convertXmlFileToSvg(Context C, String path) {
            XmlManager svgFile = createSvgFile(C);
            XmlManager xmlFile = new XmlManager(C);
            if (!xmlFile.initializeFromPath(path)) return null;
            for (Element element : xmlFile.getElementsByTagName("path")) {
                Element pathTag = svgFile.getDocument().createElement("path");
                pathTag.setAttribute("d", element.getAttribute("android:pathData"));
                int color = CodeUtil.isTheSystemThemeDark(C) ? Color.WHITE : Color.BLACK;
                String colorHex = Integer.toHexString(color);
                if (colorHex.length() > 6) colorHex = colorHex.substring(2);
                pathTag.setAttribute("fill", "#" + colorHex);
                for (Element e : svgFile.getElementsByTagName("svg")) e.appendChild(pathTag);
            }
            svgFile.saveAllModif();
            return svgFile.getPath();
        }

        private static XmlManager createSvgFile(Context C) {
            String path = ProjectDataRequested.getInstance().listener.onTmpPathNeeded();
            path += "/svgImg/image.svg";
            Files.writeFile(path, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<svg/>");

            XmlManager file = new XmlManager(C);
            file.initializeFromPath(path);

            for (Element svgTag : file.getElementsByTagName("svg")) {
                svgTag.setAttribute("xmlns", "http://www.w3.org/2000/svg");
                svgTag.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
                svgTag.setAttribute("version", "1.1");
                svgTag.setAttribute("id", "mdi-ab-testing");
                svgTag.setAttribute("width", "24");
                svgTag.setAttribute("height", "24");
                svgTag.setAttribute("viewBox", "0 0 24 24");
            }
            file.saveAllModif();
            return file;
        }
    }
}
