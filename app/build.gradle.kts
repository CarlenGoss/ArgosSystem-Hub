plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.carlengosez.open_camera_visor_house"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.carlengosez.open_camera_visor_house"
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
    buildFeatures {
        viewBinding = true
    }}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    dependencies {
        implementation("androidx.core:core-ktx:1.17.0")
        implementation("androidx.appcompat:appcompat:1.7.1")
        implementation("com.google.android.material:material:1.13.0")
        implementation("androidx.constraintlayout:constraintlayout:2.2.1")

        // Navigation Component
        implementation("androidx.navigation:navigation-fragment-ktx:2.9.7")
        implementation("androidx.navigation:navigation-ui-ktx:2.9.7")

        // Media3 (ExoPlayer para el video)
        implementation("androidx.media3:media3-exoplayer:1.9.2")
        implementation("androidx.media3:media3-ui:1.9.2")

        // Escáner QR (ZXing)
        implementation("com.journeyapps:zxing-android-embedded:4.3.0")

        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.3.0")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
        // Para convertir objetos a texto y guardarlos
        implementation("com.google.code.gson:gson:2.10.1")
    }
}