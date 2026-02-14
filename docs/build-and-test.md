# Build and Test

## Prerequisites

- Android SDK (compileSdk 35, minSdk 26)
- Java 19
- Google Maps API key in `secrets.properties` (for the maps feature)

## Build Commands

```bash
./gradlew assembleDebug          # debug APK (app ID: com.arklan.weather.debug)
./gradlew assembleRelease        # release APK (minified with R8, resources shrunk)
./gradlew clean                  # clean all build outputs
./gradlew build                  # full build including lint
```

Output APKs are named `weekly-weather-v{versionName}.{versionCode}.apk`.

## Build Variants

| Variant | App ID Suffix | Minified | Notes |
|---------|--------------|----------|-------|
| debug   | `.debug`     | No       | Version name suffixed with `-DEBUG` |
| release | (none)       | Yes      | R8 + resource shrinking enabled |

## Version Management

Version code/name are set directly in `app/build.gradle.kts:18-19`. SDK targets are centralized in `buildSrc/src/main/kotlin/com/codeskraps/weather/ConfigData.kt`.

## Testing

```bash
# All unit tests
./gradlew testDebugUnitTest

# Single module
./gradlew :feature:weather:testDebugUnitTest
./gradlew :feature:geocoding:testDebugUnitTest
./gradlew :core:local:testDebugUnitTest

# Instrumented tests (requires emulator/device)
./gradlew connectedDebugAndroidTest

# Single module instrumented
./gradlew :app:connectedDebugAndroidTest
```

Coroutine-heavy tests use `TestDispatcherProvider` from `feature:common/src/test/` to swap dispatchers.

## Dependency Management

All versions centralized in `gradle/libs.versions.toml`. Reference in build scripts as `libs.{alias}`.

Plugins are applied via `alias(libs.plugins.xxx)` in each module's `build.gradle.kts`.

## ProGuard / R8

Rules in `app/proguard-rules.pro`. Key points:
- All `com.codeskraps.**` packages are kept for Koin reflection
- Koin modules and ViewModel constructors must be kept
- Legacy Hilt rules remain (from pre-migration) — safe to clean up eventually
- Moshi JSON adapters require keep rules for `@Json`-annotated DTOs

## Secrets

`secrets.properties` (git-ignored) holds the Google Maps API key, injected via the `secrets-gradle-plugin`. Do not commit this file.
