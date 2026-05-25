# ProGuard & R8 Obfuscation/Shrinking Rules

# ------------------------------------------------------------------------------
# 1. Dagger Hilt Rules
# ------------------------------------------------------------------------------
# Keep Hilt generated classes and annotations intact
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Keep Hilt entry points and modules
-keep class * implements dagger.hilt.internal.GeneratedComponent
-keep class * implements dagger.hilt.internal.GeneratedComponentManager
-keep class * implements dagger.hilt.internal.UnsafeCasts
-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }

# Keep Dagger/Hilt creators
-keep class *_*_HiltModules* { *; }
-dontwarn dagger.hilt.internal.aggregatedroot.**

# ------------------------------------------------------------------------------
# 2. Room Database Rules
# ------------------------------------------------------------------------------
# Keep Room Entities and their properties (columns) intact
-keep @androidx.room.Entity class * { *; }

# Keep Room DAOs and database definition
-keep @androidx.room.Dao interface * { *; }
-keep class * extends androidx.room.RoomDatabase { *; }

# Keep generated Room classes (SQLite DB helper implementations)
-keep class * extends androidx.room.RoomDatabase
-keep class *_*_Impl { *; }
-dontwarn androidx.room.paging.**

# ------------------------------------------------------------------------------
# 3. Gson & Network DTOs Rules
# ------------------------------------------------------------------------------
# Keep DTO models from core:network to avoid field-renaming parsing crashes
-keep class com.example.multimodule.core.network.model.** { *; }

# Keep Gson SerializedName annotations and fields
-keepattributes *Annotation*,Signature
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Retrofit interface and API declarations
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @retrofit2.http.** <methods>;
}
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# ------------------------------------------------------------------------------
# 4. Kotlin Coroutines & Flows
# ------------------------------------------------------------------------------
# Keep Coroutines internal machinery from being stripped
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**
