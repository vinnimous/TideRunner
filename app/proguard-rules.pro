# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep data classes for Retrofit/Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.fishing.conditions.data.api.models.** { *; }
-keep class com.fishing.conditions.data.models.** { *; }
-keep class com.fishing.conditions.data.cache.entities.** { *; }

# Retrofit
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Compose
-keep class androidx.compose.** { *; }
