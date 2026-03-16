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
include(":core:core-common")
include(":core:core-database")
include(":core:core-datastore")
include(":core:core-jobs")
include(":core:core-navigation")
include(":core:core-ui")
include(":platform:platform-api")
include(":platform:platform-commands")
include(":platform:platform-events")
include(":platform:platform-runtime")
include(":platform:platform-navigation")
include(":platform:platform-ui")
include(":features:feature-home")
include(":features:feature-services")
include(":features:feature-search")
include(":features:feature-settings")
include(":services:service-planner")
include(":services:service-notes")
include(":services:service-finance")
include(":services:service-diary")
