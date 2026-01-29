package com.fishing.conditions.data.api

import com.fishing.conditions.data.api.models.StormglassAstronomyResponse
import com.fishing.conditions.data.api.models.StormglassResponse
import com.fishing.conditions.data.api.models.StormglassTideResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StormglassApi {
    @GET("weather/point")
    suspend fun getMarineWeather(
        @Header("Authorization") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("params") params: String = "waveHeight,waveDirection,wavePeriod,windSpeed,windDirection,gust,currentSpeed,currentDirection,waterTemperature,airTemperature,pressure,cloudCover,humidity,visibility,precipitation",
        @Query("start") start: String? = null,
        @Query("end") end: String? = null
    ): StormglassResponse

    @GET("tide/extremes/point")
    suspend fun getTideExtremes(
        @Header("Authorization") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("start") start: String? = null,
        @Query("end") end: String? = null
    ): StormglassTideResponse

    @GET("astronomy/point")
    suspend fun getAstronomy(
        @Header("Authorization") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("start") start: String? = null,
        @Query("end") end: String? = null
    ): StormglassAstronomyResponse
}
