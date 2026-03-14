plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "ru.topskiy.superapp.platform.api"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.navigation.compose)
}
