# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep all maps related classes
-keep class com.codeskraps.maps.** { *; }
-keep class com.codeskraps.maps.presentation.** { *; }
-keep class com.codeskraps.maps.presentation.components.** { *; }
-keep class com.codeskraps.maps.presentation.mvi.** { *; }

# Keep specifically the problematic classes
-keep class com.codeskraps.maps.presentation.MapViewModel { *; }
-keep class com.codeskraps.maps.presentation.MapViewModel_HiltModules$KeyModule { *; }
-keep class com.codeskraps.maps.presentation.components.MapScreenKt { *; }
-keep class com.codeskraps.maps.presentation.mvi.MapEvent { *; }
-keep class com.codeskraps.maps.presentation.mvi.MapState { *; }

# Keep dependencies
-keep class com.codeskraps.core.local.domain.model.** { *; }
-keep class com.codeskraps.core.local.domain.repository.** { *; }
-keep class com.codeskraps.core.location.domain.** { *; }
-keep class com.codeskraps.umami.domain.** { *; }

# Dontwarn rules
-dontwarn com.codeskraps.core.local.domain.model.GeoLocation
-dontwarn com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
-dontwarn com.codeskraps.core.local.domain.repository.LocalResourceRepository
-dontwarn com.codeskraps.core.location.domain.LocationTracker
-dontwarn com.codeskraps.umami.domain.AnalyticsRepository
-dontwarn java.lang.invoke.StringConcatFactory