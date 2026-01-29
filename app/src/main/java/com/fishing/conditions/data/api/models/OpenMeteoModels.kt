package com.fishing.conditions.data.api.models

import com.google.gson.annotations.SerializedName

// Marine Weather Response
data class OpenMeteoMarineResponse(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("hourly")
    val hourly: OpenMeteoMarineHourly,
    @SerializedName("hourly_units")
    val hourlyUnits: Map<String, String>?
)

data class OpenMeteoMarineHourly(
    @SerializedName("time")
    val time: List<String>,
    @SerializedName("wave_height")
    val waveHeight: List<Double?>?,
    @SerializedName("wave_direction")
    val waveDirection: List<Double?>?,
    @SerializedName("wave_period")
    val wavePeriod: List<Double?>?,
    @SerializedName("wind_wave_height")
    val windWaveHeight: List<Double?>?,
    @SerializedName("wind_wave_direction")
    val windWaveDirection: List<Double?>?,
    @SerializedName("swell_wave_height")
    val swellWaveHeight: List<Double?>?,
    @SerializedName("swell_wave_direction")
    val swellWaveDirection: List<Double?>?,
    @SerializedName("ocean_current_velocity")
    val currentVelocity: List<Double?>?,
    @SerializedName("ocean_current_direction")
    val currentDirection: List<Double?>?,
    @SerializedName("sea_surface_temperature")
    val seaSurfaceTemperature: List<Double?>?
)

// Standard Weather Response (for land-based conditions)
data class OpenMeteoResponse(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("hourly")
    val hourly: OpenMeteoHourly,
    @SerializedName("current")
    val current: OpenMeteoCurrent?
)

data class OpenMeteoHourly(
    @SerializedName("time")
    val time: List<String>,
    @SerializedName("temperature_2m")
    val temperature: List<Double?>?,
    @SerializedName("relative_humidity_2m")
    val humidity: List<Double?>?,
    @SerializedName("wind_speed_10m")
    val windSpeed: List<Double?>?,
    @SerializedName("wind_direction_10m")
    val windDirection: List<Double?>?,
    @SerializedName("wind_gusts_10m")
    val windGusts: List<Double?>?,
    @SerializedName("pressure_msl")
    val pressure: List<Double?>?,
    @SerializedName("cloud_cover")
    val cloudCover: List<Double?>?,
    @SerializedName("visibility")
    val visibility: List<Double?>?,
    @SerializedName("precipitation")
    val precipitation: List<Double?>?
)

data class OpenMeteoCurrent(
    @SerializedName("time")
    val time: String,
    @SerializedName("temperature_2m")
    val temperature: Double?,
    @SerializedName("wind_speed_10m")
    val windSpeed: Double?,
    @SerializedName("wind_direction_10m")
    val windDirection: Double?
)
