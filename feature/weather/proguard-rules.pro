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

# Added to fix R8 warning for missing StringConcatFactory
-dontwarn java.lang.invoke.StringConcatFactory

# Keep Hilt generated classes
-keep class com.codeskraps.feature.weather.presentation.WeatherViewModel_HiltModules$* { *; }
-keep class com.codeskraps.feature.weather.presentation.WeatherViewModel_Factory { *; }
-keep class com.codeskraps.feature.weather.presentation.WeatherViewModel_MembersInjector { *; }

# Keep specific Hilt ViewModel components
-keep class com.codeskraps.feature.weather.presentation.WeatherViewModel_HiltModules$BindsModule { *; }
-keep class com.codeskraps.feature.weather.presentation.WeatherViewModel_HiltModules$KeyModule { *; }

# Keep Hilt ViewModel factory and members injector
-keep class com.codeskraps.feature.weather.presentation.WeatherViewModel_Factory { *; }
-keep class com.codeskraps.feature.weather.presentation.WeatherViewModel_MembersInjector { *; }