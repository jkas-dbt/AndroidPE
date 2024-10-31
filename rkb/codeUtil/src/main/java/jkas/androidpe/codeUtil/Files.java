package jkas.codeUtil;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import androidx.activity.result.ActivityResultLauncher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * @auther JKas
 */
public class Files {

    public static String getFileSize(String filePath) {
        if (!isFile(filePath)) return null;

        String fileSizeType = "byte";
        double fileSize = (double) new File(filePath).length();

        if (fileSize > 1024) {
            fileSize = fileSize / 1024;
            fileSizeType = "kb";
        }

        if (fileSize > 1024) {
            fileSize = fileSize / 1024;
            fileSizeType = "mb";
        }

        if (fileSize > 1024) {
            fileSize = fileSize / 1024;
            fileSizeType = "gb";
        }

        return String.format("%.2f", fileSize).replace(",", ".").replace(".00", "")
                + " "
                + fileSizeType;
    }

    public static String getFilePath(String rootDir, String fileName, boolean withExtension) {
        fileName = fileName.startsWith("/") ? fileName.substring(1) : fileName;
        File directory = new File(rootDir);
        if (!directory.exists()) return null;
        if (!directory.isDirectory()) return null;
        File[] files = directory.listFiles();
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (!withExtension && file.isFile()) path = path.substring(0, path.lastIndexOf("."));
            if (path.endsWith("/" + fileName)) if (file.isFile()) return file.getAbsolutePath();
            if (file.isDirectory()) {
                final String filePath =
                        getFilePath(file.getAbsolutePath(), fileName, withExtension);
                if (filePath != null) return filePath;
            }
        }
        return null;
    }

    public static String getParentNameFromAbsolutePath(String path) {
        return getNameFromAbsolutePath(getPrefixPath(path));
    }

    public static String getNameFromAbsolutePath(String abPath) {
        int index = abPath.lastIndexOf("/");
        return abPath.substring(index + 1, abPath.length());
    }

    public static String getPrefixPath(String abPath) {
        int index = abPath.lastIndexOf("/");
        return abPath.substring(0, index);
    }

    public static String getFileType(String fileName) {
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }

    public static boolean rename(String abPath, String reName) {
        return new File(abPath).renameTo(new File(getPrefixPath(abPath) + "/" + reName));
    }

    public static void createNewFile(String path) {
        int lastSep = path.lastIndexOf(File.separator);
        if (lastSep > 0) {
            String dirPath = path.substring(0, lastSep);
            makeDir(dirPath);
        }

        File file = new File(path);

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String path) {
        StringBuilder sb = new StringBuilder();
        FileReader fr = null;
        try {
            fr = new FileReader(new File(path));

            char[] buff = new char[1024];
            int length = 0;

            while ((length = fr.read(buff)) > 0) {
                sb.append(new String(buff, 0, length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public static String readFileFromAssets(Context context, String path) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input;
        try {
            input = context.getAssets().open(path);

            byte buf[] = new byte[1024];
            int i;

            while ((i = input.read(buf)) != -1) {
                output.write(buf, 0, i);
            }
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public static void writeFile(String path, String str) {
        createNewFile(path);
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(new File(path), false);
            fileWriter.write(str);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyDir(String srcPath, String destPath) throws IOException {
        if (isDirectory(srcPath))
            org.apache.commons.io.FileUtils.copyDirectory(new File(srcPath), new File(destPath));
    }

    public static void moveDir(String sourcePath, String destPath) throws IOException {
        copyDir(sourcePath, destPath);
        deleteDir(sourcePath);
    }

    public static boolean deleteDir(String path) {
        try {
            if (isDirectory(path)) org.apache.commons.io.FileUtils.deleteDirectory(new File(path));
            return true;
        } catch (IOException err) {
            err.printStackTrace();
        }
        return false;
    }

    public static void copyFile(String srcPath, String destPath) throws IOException {
        if (isFile(srcPath))
            org.apache.commons.io.FileUtils.copyFile(new File(srcPath), new File(destPath));
    }

    public static void moveFile(String sourcePath, String destPath) throws IOException {
        copyFile(sourcePath, destPath);
        deleteFile(sourcePath);
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) return;
        if (file.isFile()) {
            file.delete();
            return;
        }
    }

    public static void copyFileFromAssetsToDir(
            Context context, String pathSourceAssets, String pathDest) {
        Files.makeDir(Files.getPrefixPath(pathDest));
        try {
            InputStream inputStream = context.getAssets().open(pathSourceAssets);
            OutputStream outputStream = new FileOutputStream(new File(pathDest));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyDirFromAssetsToDir(Context context, String sourcePath, String destPath) {
        try {
            FileUtils.copy(
                    new FileInputStream(
                            "/data/data/" + context.getPackageName() + "/files/" + sourcePath),
                    new FileOutputStream(destPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFileFromRawToDir(Context context, int resourceR, String destPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getResources().openRawResource(resourceR);
            out = new FileOutputStream(destPath);

            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getAllDirAndFilesFromAssets(Context context, String path) {
        String[] allDir = new String[0];

        try {
            allDir = context.getAssets().list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allDir;
    }

    private static boolean isExistFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    private static boolean isExistDir(String path) {
        File file = new File(path);
        return file.isDirectory();
    }

    public static boolean makeDir(String path) {
        if (!isExistFile(path)) {
            File file = new File(path);
            file.mkdirs();
            return true;
        }
        return false;
    }

    public static void listDir(String path, ArrayList<String> list) {
        File dir = new File(path);
        if (!dir.exists() || dir.isFile()) return;
        File[] listFiles = dir.listFiles();
        if (listFiles == null || listFiles.length <= 0) return;
        if (list == null) return;
        list.clear();
        for (File file : listFiles) if (file.isDirectory()) list.add(file.getAbsolutePath());
        Collections.sort(list);
    }

    public static ArrayList<String> listDir(String path) {
        final ArrayList<String> list = new ArrayList<>();
        File dir = new File(path);
        if (!dir.exists() || dir.isFile()) return list;
        File[] listFiles = dir.listFiles();
        if (listFiles == null || listFiles.length <= 0) return new ArrayList<>();
        if (list == null) return null;
        list.clear();
        for (File file : listFiles) if (file.isDirectory()) list.add(file.getAbsolutePath());
        Collections.sort(list);
        return list;
    }

    public static void listFile(String path, ArrayList<String> list) {
        File dir = new File(path);
        if (!dir.exists() || dir.isFile()) return;
        File[] listFiles = dir.listFiles();
        if (listFiles == null || listFiles.length <= 0) return;
        if (list == null) return;
        list.clear();
        for (File file : listFiles) if (file.isFile()) list.add(file.getAbsolutePath());
        Collections.sort(list);
    }

    public static ArrayList<String> listFile(String path) {
        ArrayList<String> list = new ArrayList<>();
        File dir = new File(path);
        if (!dir.exists() || dir.isFile()) return list;
        File[] listFiles = dir.listFiles();
        if (listFiles == null || listFiles.length <= 0) return list;
        if (list == null) return new ArrayList<>();
        list.clear();
        for (File file : listFiles) if (file.isFile()) list.add(file.getAbsolutePath());
        Collections.sort(list);
        return list;
    }

    public static void listPath(String path, ArrayList<String> list) {
        File dir = new File(path);
        if (!dir.exists() || dir.isFile()) return;
        File[] listFiles = dir.listFiles();
        if (listFiles == null || listFiles.length <= 0) return;
        if (list == null) return;
        list.clear();
        for (File file : listFiles) list.add(file.getAbsolutePath());
        Collections.sort(list);
    }

    public static ArrayList<String> listPath(String path) {
        final ArrayList<String> list = new ArrayList<>();
        File dir = new File(path);
        if (!dir.exists() || dir.isFile()) return list;
        File[] listFiles = dir.listFiles();
        if (listFiles == null || listFiles.length <= 0) return list;
        if (list == null) return new ArrayList<>();
        list.clear();
        for (File file : listFiles) list.add(file.getAbsolutePath());
        Collections.sort(list);
        return list;
    }

    public static boolean isDirectory(String path) {
        if (!isExistDir(path)) return false;
        return new File(path).isDirectory();
    }

    public static boolean isFile(String path) {
        if (!isExistFile(path)) return false;
        return new File(path).isFile();
    }

    public static long getFileLength(String path) {
        if (!isExistFile(path)) return 0;
        return new File(path).length();
    }

    public static String getExternalStorageDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getPackageDataDir(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath();
    }

    public static String getPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
    }

    public static String convertUriToFilePath(final Context context, final Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split("\\:");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);

                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                }

                final Uri contentUri =
                        ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                Long.valueOf(id));

                path = getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = MediaStore.Audio.Media._ID + "=?";
                final String[] selectionArgs = new String[] {split[1]};

                path = getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            path = getDataColumn(context, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }

        if (path != null) {
            try {
                return URLDecoder.decode(path, "UTF-8");
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static String getDataColumn(
            Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;

        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};

        try {
            cursor =
                    context.getContentResolver()
                            .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void saveBitmap(Bitmap bitmap, String destPath) {
        FileOutputStream out = null;
        Files.createNewFile(destPath);
        try {
            out = new FileOutputStream(new File(destPath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getScaledBitmap(String path, int max) {
        Bitmap src = BitmapFactory.decodeFile(path);

        int width = src.getWidth();
        int height = src.getHeight();
        float rate = 0.0f;

        if (width > height) {
            rate = max / (float) width;
            height = (int) (height * rate);
            width = max;
        } else {
            rate = max / (float) height;
            width = (int) (width * rate);
            height = max;
        }

        return Bitmap.createScaledBitmap(src, width, height, true);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampleBitmapFromPath(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static void resizeBitmapFileRetainRatio(String fromPath, String destPath, int max) {
        if (!isExistFile(fromPath)) return;
        Bitmap bitmap = getScaledBitmap(fromPath, max);
        saveBitmap(bitmap, destPath);
    }

    public static void resizeBitmapFileToSquare(String fromPath, String destPath, int max) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Bitmap bitmap = Bitmap.createScaledBitmap(src, max, max, true);
        saveBitmap(bitmap, destPath);
    }

    public static void resizeBitmapFileToCircle(String fromPath, String destPath) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Bitmap bitmap =
                Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(src.getWidth() / 2, src.getHeight() / 2, src.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);

        saveBitmap(bitmap, destPath);
    }

    public static void resizeBitmapFileWithRoundedBorder(
            String fromPath, String destPath, int pixels) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Bitmap bitmap =
                Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);

        saveBitmap(bitmap, destPath);
    }

    public static void cropBitmapFileFromCenter(String fromPath, String destPath, int w, int h) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);

        int width = src.getWidth();
        int height = src.getHeight();

        if (width < w && height < h) return;

        int x = 0;
        int y = 0;

        if (width > w) x = (width - w) / 2;

        if (height > h) y = (height - h) / 2;

        int cw = w;
        int ch = h;

        if (w > width) cw = width;

        if (h > height) ch = height;

        Bitmap bitmap = Bitmap.createBitmap(src, x, y, cw, ch);
        saveBitmap(bitmap, destPath);
    }

    public static void rotateBitmapFile(String fromPath, String destPath, float angle) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap =
                Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        saveBitmap(bitmap, destPath);
    }

    public static void scaleBitmapFile(String fromPath, String destPath, float x, float y) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Matrix matrix = new Matrix();
        matrix.postScale(x, y);

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
        saveBitmap(bitmap, destPath);
    }

    public static void skewBitmapFile(String fromPath, String destPath, float x, float y) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Matrix matrix = new Matrix();
        matrix.postSkew(x, y);

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
        saveBitmap(bitmap, destPath);
    }

    public static void setBitmapFileColorFilter(String fromPath, String destPath, int color) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth() - 1, src.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, p);
        saveBitmap(bitmap, destPath);
    }

    public static void setBitmapFileBrightness(String fromPath, String destPath, float brightness) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        ColorMatrix cm =
                new ColorMatrix(
                        new float[] {
                            1,
                            0,
                            0,
                            0,
                            brightness,
                            0,
                            1,
                            0,
                            0,
                            brightness,
                            0,
                            0,
                            1,
                            0,
                            brightness,
                            0,
                            0,
                            0,
                            1,
                            0
                        });

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0, 0, paint);
        saveBitmap(bitmap, destPath);
    }

    public static void setBitmapFileContrast(String fromPath, String destPath, float contrast) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        ColorMatrix cm =
                new ColorMatrix(
                        new float[] {
                            contrast, 0, 0, 0, 0, 0, contrast, 0, 0, 0, 0, 0, contrast, 0, 0, 0, 0,
                            0, 1, 0
                        });

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0, 0, paint);

        saveBitmap(bitmap, destPath);
    }

    public static File createNewPictureFile(Context context) {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = date.format(new Date()) + ".jpg";
        File file =
                new File(
                        context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath()
                                + File.separator
                                + fileName);
        return file;
    }

    public static void selectFolder(ActivityResultLauncher<Intent> activityRL) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        activityRL.launch(i);
    }

    public static void selectFile(ActivityResultLauncher<Intent> activityRL) {
        final Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activityRL.launch(i);
    }

    public static void selectImageFile(ActivityResultLauncher<Intent> activityRL) {
        final Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activityRL.launch(i);
    }

    public static void selectAudioFile(ActivityResultLauncher<Intent> activityRL) {
        final Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*audio/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activityRL.launch(i);
    }

    public static void selectTextFile(ActivityResultLauncher<Intent> activityRL) {
        final Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*text/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activityRL.launch(i);
    }

    public static void openFileWithOtherApp(Context context, String path) {
        File file = new File(path);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);
        if (type == null) {
            type = "*/*";
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), type);
        context.startActivity(intent);
    }

    public static String getFileType(Context context, String path) {
        File file = new File(path);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);
        if (type == null) {
            type = "*/*";
        }
        return type;
    }

    public static class FileItem {
        public static final String VIDEO = "video";
        public static final String CODE = "code";
        public static final String AUDIO = "audio";
        public static final String ARCHIVE = "archive";
        public static final String ANDROID = "android";
        public static final String PIC = "picture";
        public static final String DOC = "doc";

        private static String text =
                ",txt,xml,conf,prop,cpp,h,java,class,log,json,js,php,"
                        + "css,py,c,cpp,cfg,ini,bat,mf,mtd,lua,html,htm,kt,"
                        + "gradle,pro,properties,";
        private static String video = ",3gp,asf,avi,mp4,mpe,mpeg,mpg,mpg4,m4u,m4v,mov,rmvb,";
        private static String doc = ",docx,doc,md,xls,xlsx,ppt,pptx,pdf,";
        private static String audio =
                ",m3u,m4a,m4b,m4p,mp2,mp3,mpga,ogg,wav,wma,wmv,3gpp,flac,amr,";
        private static String pic = ",jpg,jpeg,png,gif,webp,exif,tiff,bmp,psd,svg,";
        private static String android = ",apk,aab,";
        private static String archive = ",zip,rar,7z,tar,jar,gz,xz,";

        public static String getType(String path) {
            String ext = path.contains(".") ? path.substring(path.lastIndexOf(".") + 1) : "";
            ext = "," + ext + ",";
            if (pic.contains(ext)) {
                return PIC;
            } else if (video.contains(ext)) {
                return VIDEO;
            } else if (text.contains(ext)) {
                return CODE;
            } else if (doc.contains(ext)) {
                return DOC;
            } else if (audio.contains(ext)) {
                return AUDIO;
            } else if (archive.contains(ext)) {
                return ARCHIVE;
            } else if (android.contains(ext)) {
                return ANDROID;
            }
            return CODE;
        }
    }
}
