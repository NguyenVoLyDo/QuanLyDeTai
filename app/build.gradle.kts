plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.quanlydetai"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.quanlydetai"
        minSdk = 23
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
    implementation(libs.recyclerview)
    implementation(libs.swiperefreshlayout)
    dependencies {


        implementation(libs.appcompat)
        implementation(libs.material)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
        implementation(platform("com.google.firebase:firebase-bom:34.3.0"))
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-firestore")
    }
}