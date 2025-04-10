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

# Java language APIs
-dontwarn java.lang.invoke.StringConcatFactory