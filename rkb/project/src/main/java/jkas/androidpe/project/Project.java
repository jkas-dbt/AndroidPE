package jkas.androidpe.project;

import java.io.File;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class Project {
    private String projectDir, folderName, packageName, iconePath = "";

    public Project(String projectDir, String folderName) {
        this.projectDir = projectDir;
        this.folderName = folderName;
    }

    public String getProjectDir() {
        return projectDir;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getAbsolutePath() {
        if (getProjectDir() == null || getProjectDir().isEmpty())
            return Files.getExternalStorageDir() + File.separator + getFolderName();
        else
            return Files.getExternalStorageDir()
                    + File.separator
                    + getProjectDir()
                    + File.separator
                    + getFolderName();
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getIconPath() {
        return this.iconePath;
    }

    public void setIconPath(String iconePath) {
        this.iconePath = iconePath;
    }
}
