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

# Keep all local domain models and repositories
-keep class com.codeskraps.core.local.data.repository.** { *; }
-keep class com.codeskraps.core.local.di.** { *; }
-keep class com.codeskraps.core.local.domain.model.** { *; }
-keep class com.codeskraps.core.local.domain.repository.** { *; }

# Keep specifically problematic classes
-keep class com.codeskraps.core.local.data.repository.LocalGeocodingRepositoryImpl { *; }
-keep class com.codeskraps.core.local.data.repository.LocalResourceRepositoryImpl { *; }
-keep class com.codeskraps.core.local.di.LocalModule_ProvidesGeocodingDBFactory { *; }
-keep class com.codeskraps.core.local.domain.model.GeoLocation { *; }
-keep class com.codeskraps.core.local.domain.repository.LocalGeocodingRepository { *; }
-keep class com.codeskraps.core.local.domain.repository.LocalResourceRepository { *; }

# Java language APIs
-dontwarn java.lang.invoke.StringConcatFactory

# Less aggressive obfuscation
-keepnames class com.codeskraps.core.local.** { *; }
-keepnames class com.codeskraps.core.local.data.** { *; }
-keepnames class com.codeskraps.core.local.domain.** { *; }
-keepnames class com.codeskraps.core.local.di.** { *; }

# Keep Kotlin metadata
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, RuntimeVisibleTypeAnnotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}