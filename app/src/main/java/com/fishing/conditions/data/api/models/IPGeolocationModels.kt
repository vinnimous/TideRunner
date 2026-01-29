package com.fishing.conditions.data.api.models

import com.google.gson.annotations.SerializedName

data class IPGeolocationAstronomyResponse(
    @SerializedName("location")
    val location: AstronomyLocation?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("current_time")
    val currentTime: String?,
    @SerializedName("sunrise")
    val sunrise: String?,
    @SerializedName("sunset")
    val sunset: String?,
    @SerializedName("sun_status")
    val sunStatus: String?,
    @SerializedName("solar_noon")
    val solarNoon: String?,
    @SerializedName("day_length")
    val dayLength: String?,
    @SerializedName("sun_altitude")
    val sunAltitude: Double?,
    @SerializedName("sun_distance")
    val sunDistance: Double?,
    @SerializedName("sun_azimuth")
    val sunAzimuth: Double?,
    @SerializedName("moonrise")
    val moonrise: String?,
    @SerializedName("moonset")
    val moonset: String?,
    @SerializedName("moon_status")
    val moonStatus: String?,
    @SerializedName("moon_altitude")
    val moonAltitude: Double?,
    @SerializedName("moon_distance")
    val moonDistance: Double?,
    @SerializedName("moon_azimuth")
    val moonAzimuth: Double?,
    @SerializedName("moon_parallactic_angle")
    val moonParallacticAngle: Double?,
    @SerializedName("current_moon_phase")
    val currentMoonPhase: MoonPhaseInfo?
)

data class AstronomyLocation(
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)

data class MoonPhaseInfo(
    @SerializedName("phase")
    val phase: String?, // "New Moon", "Waxing Crescent", "First Quarter", etc.
    @SerializedName("illumination")
    val illumination: String?, // Percentage as string
    @SerializedName("age")
    val age: Double? // Days since new moon
)
