// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    alias(libs.plugins.gradle.versions)
//    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.dagger.hilt) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.serialization) apply false
//    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinx.kover) apply false
    id("io.gitlab.arturbosch.detekt") version libs.versions.detektVersion.get()
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

detekt {
    toolVersion = libs.versions.detektVersion.get()

    source = files(
        "app/src/main/java",
        "core/designsystem/src/main/java",
        "core/data/src/main/java",
        "core/core-ktx/src/main/java",
        "gituser/src/main/java",
        "photosample/src/main/java",
        "sample/src/main/java",
        "test/src/main/java"
    )
    parallel = false
    config = files("detekt-config.yml")
    buildUponDefaultConfig = false
    disableDefaultRuleSets = false

    debug = false
    ignoreFailures = false

    ignoredBuildTypes = listOf(libs.versions.release.get())
    ignoredFlavors = listOf(libs.versions.prod.get())
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_17.toString()
    reports {
        xml {
            outputLocation.set(file("build/reports/detekt/detekt.xml"))
        }
        html {
            outputLocation.set(file("build/reports/detekt/detekt.html"))
        }
    }
}
