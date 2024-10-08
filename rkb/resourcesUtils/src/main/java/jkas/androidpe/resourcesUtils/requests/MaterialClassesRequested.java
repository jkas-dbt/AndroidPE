package jkas.androidpe.resourcesUtils.requests;

/**
 * @author JKas
 */
public class MaterialClassesRequested {
    private static MaterialClassesRequested instance;
    public OnDataNeeded listener;

    public static MaterialClassesRequested getInstance() {
        if (instance == null) instance = new MaterialClassesRequested();
        return instance;
    }

    private MaterialClassesRequested() {
        listener = null;
    }

    public void setOnDataNeeded(OnDataNeeded listener) {
        this.listener = listener;
    }

    public interface OnDataNeeded {
        public Class onMaterialAnimClassNeeded();

        public Class onMaterialAttrClassNeeded();

        public Class onMaterialBoolClassNeeded();

        public Class onMaterialColorClassNeeded();

        public Class onMaterialDimenClassNeeded();

         public Class onMaterialDrawableClassNeeded();

        public Class onMaterialIntegerClassNeeded();

        public Class onMaterialLayoutClassNeeded();

        public Class onMaterialStringClassNeeded();

        public Class onMaterialStyleClassNeeded();
    }
}
