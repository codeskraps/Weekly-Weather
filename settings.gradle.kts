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
        maven { url = uri("https://git.codeskraps.com/codeskraps/MavenRepo/raw/main") }
    }
}

rootProject.name = "weather"
include(":app")
include(":feature:common")
include(":feature:weather")
include(":feature:maps")
include(":feature:settings")
include(":core:location")
include(":core:local")
