pluginManagement {
    repositories {
        google()
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

rootProject.name = "weather"
include(":app")
include(":feature:common")
include(":feature:weather")
include(":feature:geocoding")
include(":feature:maps")
include(":core:location")
include(":core:local")
include(":core:umami")
