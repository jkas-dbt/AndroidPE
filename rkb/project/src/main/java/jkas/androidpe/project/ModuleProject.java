package jkas.androidpe.project;

/**
 * @author JKas
 */
class ModuleProject {
    private String name, path, projectPath;

    public ModuleProject(String name, String path, String projectPath) {
        this.name = name;
        this.path = path;
        this.projectPath = projectPath;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String getProjectAbsolutePath() {
        return this.projectPath;
    }
}
