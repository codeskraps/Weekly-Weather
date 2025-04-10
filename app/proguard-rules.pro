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

# Missing classes rules
-keep class com.codeskraps.core.local.data.repository.** { *; }
-keep class com.codeskraps.core.local.di.** { *; }
-keep class com.codeskraps.core.local.domain.repository.** { *; }
-keep class com.codeskraps.core.location.di.** { *; }
-keep class com.codeskraps.core.location.domain.** { *; }
-keep class com.codeskraps.feature.common.di.** { *; }
-keep class com.codeskraps.feature.common.dispatcher.** { *; }
-keep class com.codeskraps.feature.common.mvi.** { *; }
-keep class com.codeskraps.feature.common.navigation.** { *; }
-keep class com.codeskraps.feature.geocoding.** { *; }
-keep class com.codeskraps.feature.weather.** { *; }
-keep class com.codeskraps.maps.** { *; }
-keep class com.codeskraps.umami.** { *; }

# Java language APIs
-dontwarn java.lang.invoke.StringConcatFactory

# Additionally, adding specific rules from the missing_rules.txt
-dontwarn com.codeskraps.core.local.data.repository.LocalGeocodingRepositoryImpl
-dontwarn com.codeskraps.core.local.data.repository.LocalResourceRepositoryImpl
-dontwarn com.codeskraps.core.local.di.LocalModule_ProvidesGeocodingDBFactory
-dontwarn com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
-dontwarn com.codeskraps.core.local.domain.repository.LocalResourceRepository
-dontwarn com.codeskraps.core.location.di.CoreLocationModule_ProvidesLocationTrackerFactory
-dontwarn com.codeskraps.core.location.domain.LocationTracker
-dontwarn com.codeskraps.feature.common.di.FeatureModule_ProvidesDispatcherProviderFactory
-dontwarn com.codeskraps.feature.common.di.FeatureModule_ProvidesResourcesFactory
-dontwarn com.codeskraps.feature.common.dispatcher.DispatcherProvider
-dontwarn com.codeskraps.feature.common.mvi.StateReducerFlow
-dontwarn com.codeskraps.feature.common.navigation.Screen$Geocoding
-dontwarn com.codeskraps.feature.common.navigation.Screen$Map
-dontwarn com.codeskraps.feature.common.navigation.Screen$Weather
-dontwarn com.codeskraps.feature.geocoding.data.remote.GeocodingApi
-dontwarn com.codeskraps.feature.geocoding.data.repository.GeocodingRepositoryImpl
-dontwarn com.codeskraps.feature.geocoding.di.FeatureModule_ProvidesGeocodingApiFactory
-dontwarn com.codeskraps.feature.geocoding.presentation.GeocodingViewModel
-dontwarn com.codeskraps.feature.geocoding.presentation.GeocodingViewModel_HiltModules$KeyModule
-dontwarn com.codeskraps.feature.geocoding.presentation.components.GeocodingScreenKt
-dontwarn com.codeskraps.feature.geocoding.presentation.mvi.GeoEvent
-dontwarn com.codeskraps.feature.geocoding.presentation.mvi.GeoState
-dontwarn com.codeskraps.feature.geocoding.repository.GeocodingRepository
-dontwarn com.codeskraps.feature.weather.data.remote.WeatherApi
-dontwarn com.codeskraps.feature.weather.data.repository.WeatherRepositoryImpl
-dontwarn com.codeskraps.feature.weather.di.FeatureModule_ProvidesWeatherApiFactory
-dontwarn com.codeskraps.feature.weather.domain.repository.WeatherRepository
-dontwarn com.codeskraps.feature.weather.presentation.WeatherViewModel
-dontwarn com.codeskraps.feature.weather.presentation.WeatherViewModel_HiltModules$KeyModule
-dontwarn com.codeskraps.feature.weather.presentation.components.WeatherScreenKt
-dontwarn com.codeskraps.feature.weather.presentation.mvi.WeatherEvent
-dontwarn com.codeskraps.feature.weather.presentation.mvi.WeatherState
-dontwarn com.codeskraps.maps.presentation.MapViewModel
-dontwarn com.codeskraps.maps.presentation.MapViewModel_HiltModules$KeyModule
-dontwarn com.codeskraps.maps.presentation.components.MapScreenKt
-dontwarn com.codeskraps.maps.presentation.mvi.MapEvent
-dontwarn com.codeskraps.maps.presentation.mvi.MapState
-dontwarn com.codeskraps.umami.di.CoreUmamiModule_ProvidesAnalyticsRepositoryFactory
-dontwarn com.codeskraps.umami.di.CoreUmamiModule_ProvidesDeviceIdRepositoryFactory
-dontwarn com.codeskraps.umami.domain.AnalyticsRepository
-dontwarn com.codeskraps.umami.domain.DeviceIdRepository