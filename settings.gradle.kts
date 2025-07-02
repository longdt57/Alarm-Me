pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Alarm-Me"
include(":app")
include(":alarm")

include(":core:designsystem")
include(":core:data")
include(":test")
include(":core:core-ktx")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
