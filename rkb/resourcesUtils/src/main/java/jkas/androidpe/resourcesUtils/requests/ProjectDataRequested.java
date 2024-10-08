package jkas.androidpe.resourcesUtils.requests;

/**
 * @author JKas
 */
public class ProjectDataRequested {
    private static ProjectDataRequested instance;
    public OnDataNeeded listener;

    public static ProjectDataRequested getInstance() {
        if (instance == null) instance = new ProjectDataRequested();
        return instance;
    }

    private ProjectDataRequested() {
        listener = null;
    }

    public void setOnDataNeeded(OnDataNeeded listener) {
        this.listener = listener;
    }

    public interface OnDataNeeded {
        public String onProjectAbsolutePathNeeded();

        public String onTmpPathNeeded();
    }
}
