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

# Keep all location module classes
-keep class com.codeskraps.core.location.** { *; }
-keep class com.codeskraps.core.location.di.** { *; }
-keep class com.codeskraps.core.location.domain.** { *; }

# Keep specifically problematic classes
-keep class com.codeskraps.core.location.di.CoreLocationModule_ProvidesLocationTrackerFactory { *; }
-keep class com.codeskraps.core.location.domain.LocationTracker { *; }

# Java language APIs
-dontwarn java.lang.invoke.StringConcatFactory