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

# Keep all common module classes
-keep class com.codeskraps.feature.common.** { *; }
-keep class com.codeskraps.feature.common.di.** { *; }
-keep class com.codeskraps.feature.common.dispatcher.** { *; }
-keep class com.codeskraps.feature.common.mvi.** { *; }
-keep class com.codeskraps.feature.common.navigation.** { *; }

# Keep specifically the problematic classes
-keep class com.codeskraps.feature.common.di.FeatureModule_ProvidesDispatcherProviderFactory { *; }
-keep class com.codeskraps.feature.common.di.FeatureModule_ProvidesResourcesFactory { *; }
-keep class com.codeskraps.feature.common.dispatcher.DispatcherProvider { *; }
-keep class com.codeskraps.feature.common.mvi.StateReducerFlow { *; }
-keep class com.codeskraps.feature.common.navigation.Screen$Geocoding { *; }
-keep class com.codeskraps.feature.common.navigation.Screen$Map { *; }
-keep class com.codeskraps.feature.common.navigation.Screen$Weather { *; }

# Keep Koin module classes
-keep class com.codeskraps.feature.common.di.** { *; }
-keep class com.codeskraps.feature.common.di.FeatureModuleKt { *; }

# Keep Compose-related classes
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.material.** { *; }
-keep class androidx.compose.material3.** { *; }

# Less aggressive obfuscation
-keepnames class com.codeskraps.feature.common.** { *; }
-keepnames class com.codeskraps.feature.common.di.** { *; }
-keepnames class com.codeskraps.feature.common.dispatcher.** { *; }
-keepnames class com.codeskraps.feature.common.mvi.** { *; }
-keepnames class com.codeskraps.feature.common.navigation.** { *; }

# Keep Kotlin metadata
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, RuntimeVisibleTypeAnnotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Java language APIs
-dontwarn java.lang.invoke.StringConcatFactory