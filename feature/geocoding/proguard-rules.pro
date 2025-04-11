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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Added to fix R8 warning for missing StringConcatFactory
-dontwarn java.lang.invoke.StringConcatFactory

# Keep Hilt generated classes
-keep class com.codeskraps.feature.geocoding.presentation.GeocodingViewModel_HiltModules$* { *; }
-keep class com.codeskraps.feature.geocoding.presentation.GeocodingViewModel_Factory { *; }
-keep class com.codeskraps.feature.geocoding.presentation.GeocodingViewModel_MembersInjector { *; }

# Keep specific Hilt ViewModel components
-keep class com.codeskraps.feature.geocoding.presentation.GeocodingViewModel_HiltModules$BindsModule { *; }
-keep class com.codeskraps.feature.geocoding.presentation.GeocodingViewModel_HiltModules$KeyModule { *; }

# Keep Hilt ViewModel factory and members injector
-keep class com.codeskraps.feature.geocoding.presentation.GeocodingViewModel_Factory { *; }
-keep class com.codeskraps.feature.geocoding.presentation.GeocodingViewModel_MembersInjector { *; }

# Keep Koin module classes
-keep class com.codeskraps.feature.geocoding.di.** { *; }
-keep class com.codeskraps.feature.geocoding.di.FeatureModuleKt { *; }

# Keep Compose screen classes
-keep class com.codeskraps.feature.geocoding.presentation.components.** { *; }
-keep class com.codeskraps.feature.geocoding.presentation.components.GeocodingScreenKt { *; }
-keep class com.codeskraps.feature.geocoding.presentation.components.GeocodingScreenKt$* { *; }

# Keep Compose-related classes
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.material.** { *; }
-keep class androidx.compose.material3.** { *; }

# Keep Moshi-related classes
-keep class com.codeskraps.feature.geocoding.data.remote.** { *; }
-keep class com.squareup.moshi.** { *; }
-keep class com.squareup.moshi.internal.** { *; }
-keep class com.squareup.moshi.internal.Util { *; }
-keep class com.squareup.moshi.internal.Util$* { *; }

# Keep Moshi annotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, RuntimeVisibleTypeAnnotations

# Keep Retrofit-related classes
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Less aggressive obfuscation
-keepnames class com.codeskraps.feature.geocoding.** { *; }
-keepnames class com.codeskraps.feature.geocoding.data.** { *; }
-keepnames class com.codeskraps.feature.geocoding.domain.** { *; }
-keepnames class com.codeskraps.feature.geocoding.presentation.** { *; }

# Keep Kotlin metadata
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, RuntimeVisibleTypeAnnotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}