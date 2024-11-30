package jkas.androidpe.utils;

import jkas.androidpe.projectUtils.current.Environment;
import jkas.androidpe.resourcesUtils.bases.AttrValuesRefBase;
import jkas.androidpe.resourcesUtils.dataInitializer.DataRefManager;
import jkas.androidpe.resourcesUtils.requests.AndroidxClassesRequested;
import jkas.androidpe.resourcesUtils.requests.MaterialClassesRequested;
import jkas.androidpe.resourcesUtils.requests.ProjectDataRequested;

/**
 * @author JKas
 */
public class ForRes {
    public static void initEventsResquest() {
        ProjectDataRequested.getInstance()
                .setOnDataNeeded(
                        new ProjectDataRequested.OnDataNeeded() {
                            @Override
                            public String onProjectAbsolutePathNeeded() {
                                return DataRefManager.getInstance().P.getAbsolutePath();
                            }

                            @Override
                            public String onTmpPathNeeded() {
                                return Environment.DEFAULT_ANDROIDPE_TMP_DATA;
                            }
                        });

        AndroidxClassesRequested.getInstance()
                .setOnDataNeeded(
                        new AndroidxClassesRequested.OnDataNeeded() {
                            @Override
                            public Class onAndroidXAnimClassNeeded() {
                                return androidx.appcompat.R.anim.class;
                            }

                            @Override
                            public Class onAndroidXAttrClassNeeded() {
                                return androidx.appcompat.R.attr.class;
                            }

                            @Override
                            public Class onAndroidXBoolClassNeeded() {
                                return androidx.appcompat.R.bool.class;
                            }

                            @Override
                            public Class onAndroidXColorClassNeeded() {
                                return androidx.appcompat.R.color.class;
                            }

                            @Override
                            public Class onAndroidXDimenClassNeeded() {
                                return androidx.appcompat.R.dimen.class;
                            }

                            @Override
                            public Class onAndroidXDrawableClassNeeded() {
                                return androidx.appcompat.R.drawable.class;
                            }

                            @Override
                            public Class onAndroidXIntegerClassNeeded() {
                                return androidx.appcompat.R.integer.class;
                            }

                            @Override
                            public Class onAndroidXLayoutClassNeeded() {
                                return androidx.appcompat.R.layout.class;
                            }

                            @Override
                            public Class onAndroidXStringClassNeeded() {
                                return androidx.appcompat.R.string.class;
                            }

                            @Override
                            public Class onAndroidXStyleClassNeeded() {
                                return androidx.appcompat.R.style.class;
                            }
                        });

        MaterialClassesRequested.getInstance()
                .setOnDataNeeded(
                        new MaterialClassesRequested.OnDataNeeded() {
                            @Override
                            public Class onMaterialAnimClassNeeded() {
                                return com.google.android.material.R.anim.class;
                            }

                            @Override
                            public Class onMaterialAttrClassNeeded() {
                                return com.google.android.material.R.attr.class;
                            }

                            @Override
                            public Class onMaterialBoolClassNeeded() {
                                return com.google.android.material.R.bool.class;
                            }

                            @Override
                            public Class onMaterialColorClassNeeded() {
                                return com.google.android.material.R.color.class;
                            }

                            @Override
                            public Class onMaterialDimenClassNeeded() {
                                return com.google.android.material.R.dimen.class;
                            }

                            @Override
                            public Class onMaterialDrawableClassNeeded() {
                                return com.google.android.material.R.drawable.class;
                            }

                            @Override
                            public Class onMaterialIntegerClassNeeded() {
                                return com.google.android.material.R.integer.class;
                            }

                            @Override
                            public Class onMaterialLayoutClassNeeded() {
                                return com.google.android.material.R.layout.class;
                            }

                            @Override
                            public Class onMaterialStringClassNeeded() {
                                return com.google.android.material.R.string.class;
                            }

                            @Override
                            public Class onMaterialStyleClassNeeded() {
                                return com.google.android.material.R.style.class;
                            }
                        });

        AttrValuesRefBase.initRef();
    }
}
