package com.fishing.conditions.data.api.models

import com.google.gson.annotations.SerializedName

data class SolunarResponse(
    @SerializedName("date")
    val date: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("moonPhase")
    val moonPhase: SolunarMoonPhase?,
    @SerializedName("moonrise")
    val moonrise: String?,
    @SerializedName("moonset")
    val moonset: String?,
    @SerializedName("moonTransit")
    val moonTransit: String?,
    @SerializedName("sunrise")
    val sunrise: String?,
    @SerializedName("sunset")
    val sunset: String?,
    @SerializedName("day_length")
    val dayLength: String?,
    @SerializedName("sun_transit")
    val sunTransit: String?,
    @SerializedName("majorPeriods")
    val majorPeriods: List<SolunarPeriod>?,
    @SerializedName("minorPeriods")
    val minorPeriods: List<SolunarPeriod>?,
    @SerializedName("score")
    val score: Int?
)

data class SolunarPeriod(
    @SerializedName("start")
    val start: String,
    @SerializedName("end")
    val end: String,
    @SerializedName("type")
    val type: String? // "major" or "minor"
)

data class SolunarMoonPhase(
    @SerializedName("phase")
    val phase: String?, // "New Moon", "Full Moon", etc.
    @SerializedName("illumination")
    val illumination: Double?, // 0-100
    @SerializedName("age")
    val age: Double? // Days since new moon
)
