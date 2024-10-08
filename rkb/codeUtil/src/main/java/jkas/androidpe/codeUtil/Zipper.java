package jkas.codeUtil;

import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.progress.ProgressMonitor;

/**
 * @auther JKas
 */
public class Zipper {
    private OnProgressListener listener;
    private String path = "", passWord = "", destPath = "";
    public ArrayList<String> excludeFolders;
    public ArrayList<String> excludeFiles;
    public AppCompatActivity activity;

    public Zipper(AppCompatActivity activity, String path) {
        this.activity = activity;
        this.path = path;
        excludeFolders = new ArrayList<>();
        excludeFiles = new ArrayList<>();
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public void addFolderExcluded(String folder) {
        excludeFolders.add(path + File.separator + folder);
    }

    public void addFileExcluded(String file) {
        excludeFiles.add(path + File.separator + file);
    }

    int i; // for removing data excluded in list.

    public void removeFolderExcluded(String folder) {
        i = 0;
        for (String s : excludeFolders)
            if (s.equals(folder)) {
                excludeFolders.remove(i);
                break;
            } else i++;
    }

    public void removeFileExcluded(String file) {
        i = 0;
        for (String s : excludeFiles)
            if (s.equals(file)) {
                excludeFiles.remove(i);
                break;
            } else i++;
    }

    public void archive() {
        Executors.newSingleThreadExecutor().submit(() -> launch());
    }

    public void launch() {
        Files.deleteFile(destPath);
        Files.makeDir(destPath.substring(0, destPath.lastIndexOf("/")));

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        parameters.setCompressionLevel(CompressionLevel.NORMAL);
        parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

        ZipFile zipFile = null;
        if (passWord.isEmpty()) zipFile = new ZipFile(destPath);
        else {
            zipFile = new ZipFile(destPath, passWord.toCharArray());
            parameters.setEncryptFiles(true);
            parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_128);
        }

        int dex = (Files.getExternalStorageDir() + File.separator).lastIndexOf(File.separator);
        String pathTmp =
                activity.getFilesDir() + "/home/.androidpe/.tmp/" + path.substring(dex + 1);
        try {
            Files.deleteDir(pathTmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((excludeFiles.size() > 0) || (excludeFolders.size() > 0)) {
            try {
                Files.copyDir(path, pathTmp);

                for (String s : excludeFolders) {
                    final String S = (pathTmp + s.replace(path, "")).replace("//", "/");
                    Files.deleteDir(S);
                }

                for (String s : excludeFiles) {
                    final String S = (pathTmp + s.replace(path, "")).replace("//", "/");
                    Files.deleteFile(S);
                }

                path = pathTmp;
            } catch (final Exception e) {
                activity.runOnUiThread(() -> listener.onError(e));
            }
        }

        zipFile.setRunInThread(true);
        try {
            zipFile.addFolder(new File(path), parameters);
        } catch (ZipException e) {
            e.printStackTrace();
            try {
                Files.deleteDir(pathTmp);
            } catch (Exception f) {
                f.printStackTrace();
            }
            activity.runOnUiThread(() -> listener.onError(e));
        }

        ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
        while (!progressMonitor.getState().equals(ProgressMonitor.State.READY))
            listener.onProgress(progressMonitor.getState());

        try {
            Files.deleteDir(pathTmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS))
            activity.runOnUiThread(() -> listener.onFinish());
    }

    public void setOnProgressListener(OnProgressListener listener) {
        this.listener = listener;
    }

    public interface OnProgressListener {
        public void onProgress(ProgressMonitor.State state);

        public void onFinish();

        public void onError(Exception e);
    }
}
