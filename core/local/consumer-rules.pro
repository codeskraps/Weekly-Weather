# Keep all local domain models and repositories when consumed by other modules
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
