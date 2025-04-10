# Keep all maps related classes when consumed by other modules
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
