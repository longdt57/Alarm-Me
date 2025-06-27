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

rootProject.name = "Compose"
include(":app")
include(":gituser")
include(":photosample")
include(":sample")

include(":core:designsystem")
include(":core:data")
include(":test")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":core:core-ktx")
