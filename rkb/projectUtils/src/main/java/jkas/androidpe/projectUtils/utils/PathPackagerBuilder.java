package jkas.androidpe.projectUtils.utils;

import java.util.ArrayList;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class PathPackagerBuilder {
    private ArrayList<PathPackager> listPackages = new ArrayList<>();
    private ArrayList<PathPackager> listLayoutFiles = new ArrayList<>();
    private String rootPkgDir = "";
    private String rootResDir = "";

    public PathPackagerBuilder() {
        listPackages.clear();
        listLayoutFiles.clear();
    }

    public String getRootPkgDir() {
        return this.rootPkgDir;
    }

    public void setRootPkgDir(String rootPkgDir) {
        this.rootPkgDir = rootPkgDir;
    }

    public String getRootResDir() {
        return this.rootResDir;
    }

    public void setRootResDir(String rootResDir) {
        this.rootResDir = rootResDir;
    }

    public ArrayList<PathPackager> getListPackage() {
        return listPackages;
    }

    public ArrayList<PathPackager> getListLayoutFiles() {
        return listLayoutFiles;
    }

    public void searchForPackages() {
        listPackages.clear();
        searchForPackages(rootPkgDir);
    }

    public void searchListLayoutFiles() {
        listLayoutFiles.clear();
        searchForLayoutFiles(rootResDir);
    }

    private void searchForPackages(final String rootPkgDir) {
        for (final String s : Files.listDir(rootPkgDir)) searchForPackages(s);
        for (final String s : Files.listFile(rootPkgDir)) attachFileToPkg(s);
    }

    private void searchForLayoutFiles(final String root) {
        for (final String s : Files.listDir(root))
            if (s.matches(root+".*\\/layout.*")) {
                searchForLayoutFiles(s);
                for (String j : Files.listFile(s)) if (j.endsWith(".xml")) attachLayoutFileToPkg(j);
            }
    }

    private void attachLayoutFileToPkg(String filePath) {
        String srcPkg = buildPathRes(filePath);

        for (PathPackager pkg : listLayoutFiles)
            if (pkg.getPackage().equals(srcPkg)) {
                pkg.addFileToList(filePath);
                return;
            }

        final PathPackager pp = new PathPackager(srcPkg);
        pp.addFileToList(filePath);
        listLayoutFiles.add(pp);
    }

    private void attachFileToPkg(String filePath) {
        String srcPkg = buildPkg(filePath);

        for (PathPackager pkg : listPackages)
            if (pkg.getPackage().equals(srcPkg)) {
                pkg.addFileToList(filePath);
                return;
            }

        final PathPackager pp = new PathPackager(srcPkg);
        pp.addFileToList(filePath);
        listPackages.add(pp);
    }

    private String buildPkg(String filePath) {
        String pkg = Files.getPrefixPath(filePath);
        if (pkg.equals(rootPkgDir)) return "";
        pkg = pkg.substring(rootPkgDir.length() + 1).replace("/", ".");
        return pkg;
    }

    private String buildPathRes(String filePath) {
        String pkg = Files.getPrefixPath(filePath);
        if (pkg.equals(rootResDir)) return "";
        pkg = pkg.substring(rootResDir.length() + 1).replace("/", ".");
        return pkg;
    }
}
