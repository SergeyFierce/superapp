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

rootProject.name = "superapp"
include(":app")
include(":core-common")
include(":core-events")
include(":core-commands")
include(":core-services")
include(":core-database")
include(":core-preferences")
include(":core-jobs")
include(":core-navigation")
include(":core-ui")
include(":feature-planner")
include(":feature-notes")
include(":feature-finance")
