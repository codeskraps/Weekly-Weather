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

# Keep all location module classes
-keep class com.codeskraps.core.location.** { *; }
-keep class com.codeskraps.core.location.di.** { *; }
-keep class com.codeskraps.core.location.domain.** { *; }

# Keep specifically problematic classes
-keep class com.codeskraps.core.location.di.CoreLocationModule_ProvidesLocationTrackerFactory { *; }
-keep class com.codeskraps.core.location.domain.LocationTracker { *; }

# Keep Koin module classes
-keep class com.codeskraps.core.location.di.** { *; }
-keep class com.codeskraps.core.location.di.FeatureModuleKt { *; }

# Less aggressive obfuscation
-keepnames class com.codeskraps.core.location.** { *; }
-keepnames class com.codeskraps.core.location.data.** { *; }
-keepnames class com.codeskraps.core.location.domain.** { *; }
-keepnames class com.codeskraps.core.location.di.** { *; }

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