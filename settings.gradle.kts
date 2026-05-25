pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "android-multimodule-app"
include(":app")
include(":core:domain")
include(":core:database")
include(":core:network")
include(":core:data")
include(":core:ui")
include(":feature:auth")
include(":feature:dashboard")
include(":feature:ai")
include(":feature:settings")
