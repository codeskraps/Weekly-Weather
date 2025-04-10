# Keep all location module classes when consumed by other modules
-keep class com.codeskraps.core.location.** { *; }
-keep class com.codeskraps.core.location.di.** { *; }
-keep class com.codeskraps.core.location.domain.** { *; }

# Keep specifically problematic classes
-keep class com.codeskraps.core.location.di.CoreLocationModule_ProvidesLocationTrackerFactory { *; }
-keep class com.codeskraps.core.location.domain.LocationTracker { *; }

# Java language APIs
-dontwarn java.lang.invoke.StringConcatFactory
