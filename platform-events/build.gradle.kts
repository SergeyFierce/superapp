plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":platform-api"))
    implementation(libs.kotlinx.coroutines.core)
}
