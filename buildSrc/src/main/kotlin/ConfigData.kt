import org.gradle.api.JavaVersion

object ConfigData {
    const val minSdk = 26
    const val targetSdk = 34
    const val compileSdk = 34

    val javaVersion = JavaVersion.VERSION_17
    const val javaTarget = "17"

    const val kotlinCompiler = "1.5.8"
}
