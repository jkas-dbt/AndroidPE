package jkas.androidpe.project;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jkas.codeUtil.Files;

/**
 * @author JKas
 */
public class AndroidModule extends ModuleProject {
    private String packageName;
    private String versionCode;
    private String versionName;
    private ArrayList<String> refToOthersModules;

    public AndroidModule(@NonNull String name, @NonNull String path, @NonNull String projectDir) {
        super(name, path, projectDir);
        refToOthersModules = new ArrayList<>();
        setPackageName();
    }

    public String getPackageName() {
        return this.packageName;
    }

    private void setPackageName() {
        packageName = ".";
        String path2Gradle = getProjectAbsolutePath() + "/build.gradle";
        if (!Files.isFile(path2Gradle)) path2Gradle += ".kts";
        if (!Files.isFile(path2Gradle)) return;

        String gradleCode = Files.readFile(path2Gradle);
        if (gradleCode == null) return;

        String namespace = "namespace.*\\'.*\\'";
        String namespaceKts = "namespace.*\\\".*\\\"";
        String applicationId = "applicationId.*\\'.*\\'";
        String applicationIdKts = "applicationId.*\\\".*\\\"";

        Pattern pattern = Pattern.compile(namespace);
        Matcher matcher = pattern.matcher(gradleCode);
        if (matcher.find()) {
            packageName = matcher.group().split("\\'")[1];
        }

        pattern = Pattern.compile(namespaceKts);
        matcher = pattern.matcher(gradleCode);
        if (matcher.find()) {
            packageName = matcher.group().split("\\\"")[1];
        }

        pattern = Pattern.compile(applicationId);
        matcher = pattern.matcher(gradleCode);
        if (matcher.find()) {
            packageName = matcher.group().split("\\'")[1];
        }

        pattern = Pattern.compile(applicationIdKts);
        matcher = pattern.matcher(gradleCode);
        if (matcher.find()) {
            packageName = matcher.group().split("\\\"")[1];
        }

        this.packageName = packageName;
    }
    
    public ArrayList<String> getRefToOthersModules() {
        return this.refToOthersModules;
    }

    public void setRefToOthersModules(ArrayList<String> refToOthersModules) {
        this.refToOthersModules = refToOthersModules;
    }
}
