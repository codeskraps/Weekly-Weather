# Keep all umami module classes when consumed by other modules
-keep class com.codeskraps.umami.** { *; }
-keep class com.codeskraps.umami.di.** { *; }
-keep class com.codeskraps.umami.domain.** { *; }

# Keep specifically problematic classes
-keep class com.codeskraps.umami.di.CoreUmamiModule_ProvidesAnalyticsRepositoryFactory { *; }
-keep class com.codeskraps.umami.di.CoreUmamiModule_ProvidesDeviceIdRepositoryFactory { *; }
-keep class com.codeskraps.umami.domain.AnalyticsRepository { *; }
-keep class com.codeskraps.umami.domain.DeviceIdRepository { *; }

# Java language APIs
-dontwarn java.lang.invoke.StringConcatFactory
