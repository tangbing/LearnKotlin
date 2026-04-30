plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass = "com.example.webviewtest.CoroutinesTestKt"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
