plugins {
    id("com.android.library")
}

android {
    namespace = "$PACKAGE_NAME$"
    compileSdk = 34
    buildToolsVersion = "34.0.4"

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
    
}

dependencies {
    //implementation code
}