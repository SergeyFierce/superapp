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
include(":core-database")
include(":core-datastore")
include(":core-jobs")
include(":core-navigation")
include(":core-ui")
include(":platform-api")
include(":platform-commands")
include(":platform-events")
include(":platform-runtime")
include(":platform-navigation")
include(":platform-ui")
include(":feature-home")
include(":feature-services")
include(":feature-search")
include(":feature-settings")
include(":service-planner")
include(":service-notes")
include(":service-finance")
include(":service-diary")
