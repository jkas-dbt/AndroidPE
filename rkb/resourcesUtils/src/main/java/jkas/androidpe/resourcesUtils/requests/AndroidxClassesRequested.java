package jkas.androidpe.resourcesUtils.requests;

/**
 * @author JKas
 */
public class AndroidxClassesRequested {
	private static AndroidxClassesRequested instance;
    public OnDataNeeded listener;

    public static AndroidxClassesRequested getInstance() {
        if (instance == null) instance = new AndroidxClassesRequested();
        return instance;
    }

    private AndroidxClassesRequested() {
        listener = null;
    }

    public void setOnDataNeeded(OnDataNeeded listener) {
        this.listener = listener;
    }
    
    public interface OnDataNeeded {
        public Class onAndroidXAnimClassNeeded();

        public Class onAndroidXAttrClassNeeded();

        public Class onAndroidXBoolClassNeeded();

        public Class onAndroidXColorClassNeeded();

        public Class onAndroidXDimenClassNeeded();

        public Class onAndroidXDrawableClassNeeded();

        public Class onAndroidXIntegerClassNeeded();

        public Class onAndroidXLayoutClassNeeded();

        public Class onAndroidXStringClassNeeded();

        public Class onAndroidXStyleClassNeeded();
    }
}