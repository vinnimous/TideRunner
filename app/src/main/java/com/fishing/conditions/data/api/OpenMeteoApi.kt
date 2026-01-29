package com.fishing.conditions.data.api

import com.fishing.conditions.data.api.models.OpenMeteoMarineResponse
import com.fishing.conditions.data.api.models.OpenMeteoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,wind_speed_10m,wind_direction_10m,wind_gusts_10m,pressure_msl,cloud_cover,visibility,precipitation",
        @Query("current") current: String = "temperature_2m,wind_speed_10m,wind_direction_10m",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 3
    ): OpenMeteoResponse

    @GET("v1/marine")
    suspend fun getMarineForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "wave_height,wave_direction,wave_period,wind_wave_height,wind_wave_direction,swell_wave_height,swell_wave_direction,ocean_current_velocity,ocean_current_direction,sea_surface_temperature",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 3
    ): OpenMeteoMarineResponse
}
