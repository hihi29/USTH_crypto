plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "vn.edu.usth.cryptoapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "vn.edu.usth.cryptoapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.material:material:1.12.0")

    implementation("androidx.core:core-ktx:1.13.1") // ok to keep even in Java apps
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // Thư viện mạng (Ví dụ: OkHttp)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    // Thư viện xử lý JSON (Ví dụ: Gson)
    implementation("com.google.code.gson:gson:2.10.1")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Volley
    implementation("com.android.volley:volley:1.2.1")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}