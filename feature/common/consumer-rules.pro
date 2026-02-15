# Keep all common module classes when consumed by other modules
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
-keep class com.codeskraps.feature.common.navigation.Screen$Map { *; }
-keep class com.codeskraps.feature.common.navigation.Screen$Weather { *; }
-keep class com.codeskraps.feature.common.navigation.Screen$Settings { *; }

# Java language APIs
-dontwarn java.lang.invoke.StringConcatFactory
