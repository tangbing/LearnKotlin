plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    application
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

application {
    // Default entry; you can change to LearnKotlinSimpleKt
    mainClass.set("com.example.service_demo.LearnKotlinKt")
}
