package com.fishing.conditions.di

import android.content.Context
import androidx.room.Room
import com.fishing.conditions.data.api.*
import com.fishing.conditions.data.cache.MarineDataDao
import com.fishing.conditions.data.cache.MarineDataDatabase
import com.fishing.conditions.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoaaRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenMeteoRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenWeatherRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StormglassRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SolunarRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IPGeolocationRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @NoaaRetrofit
    fun provideNoaaRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.NOAA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNoaaApi(@NoaaRetrofit retrofit: Retrofit): NoaaApi {
        return retrofit.create(NoaaApi::class.java)
    }

    @Provides
    @Singleton
    @OpenMeteoRetrofit
    fun provideOpenMeteoRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.OPEN_METEO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenMeteoApi(@OpenMeteoRetrofit retrofit: Retrofit): OpenMeteoApi {
        return retrofit.create(OpenMeteoApi::class.java)
    }

    @Provides
    @Singleton
    @OpenWeatherRetrofit
    fun provideOpenWeatherRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.OPEN_WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenWeatherApi(@OpenWeatherRetrofit retrofit: Retrofit): OpenWeatherApi {
        return retrofit.create(OpenWeatherApi::class.java)
    }

    @Provides
    @Singleton
    @StormglassRetrofit
    fun provideStormglassRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.STORMGLASS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideStormglassApi(@StormglassRetrofit retrofit: Retrofit): StormglassApi {
        return retrofit.create(StormglassApi::class.java)
    }

    @Provides
    @Singleton
    @SolunarRetrofit
    fun provideSolunarRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.SOLUNAR_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSolunarApi(@SolunarRetrofit retrofit: Retrofit): SolunarApi {
        return retrofit.create(SolunarApi::class.java)
    }

    @Provides
    @Singleton
    @IPGeolocationRetrofit
    fun provideIPGeolocationRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.IPGEOLOCATION_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideIPGeolocationApi(@IPGeolocationRetrofit retrofit: Retrofit): IPGeolocationApi {
        return retrofit.create(IPGeolocationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarineDataDatabase(@ApplicationContext context: Context): MarineDataDatabase {
        return Room.databaseBuilder(
            context,
            MarineDataDatabase::class.java,
            "marine_data_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMarineDataDao(database: MarineDataDatabase): MarineDataDao {
        return database.marineDataDao()
    }
}
