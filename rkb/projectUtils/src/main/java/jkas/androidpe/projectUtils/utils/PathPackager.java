package jkas.androidpe.projectUtils.utils;

import java.util.ArrayList;

/**
 * @author JKas
 */
public class PathPackager {
    private String pkg;
    private ArrayList<String> listFile = new ArrayList<>();

    public PathPackager(String pkg) {
        this.pkg = pkg;
        listFile.clear();
    }

    public String getPackage() {
        return pkg;
    }

    public ArrayList<String> getListFile() {
        return listFile;
    }

    public void addFileToList(String fileDir) {
        listFile.add(fileDir);
    }
}
