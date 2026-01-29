package com.fishing.conditions.data.api.models

import com.google.gson.annotations.SerializedName

// Weather & Marine Data Response
data class StormglassResponse(
    @SerializedName("hours")
    val hours: List<StormglassHour>
)

data class StormglassHour(
    @SerializedName("time")
    val time: String,
    @SerializedName("waterTemperature")
    val waterTemperature: StormglassValue?,
    @SerializedName("waveHeight")
    val waveHeight: StormglassValue?,
    @SerializedName("waveDirection")
    val waveDirection: StormglassValue?,
    @SerializedName("wavePeriod")
    val wavePeriod: StormglassValue?,
    @SerializedName("windSpeed")
    val windSpeed: StormglassValue?,
    @SerializedName("windDirection")
    val windDirection: StormglassValue?,
    @SerializedName("gust")
    val gust: StormglassValue?,
    @SerializedName("currentSpeed")
    val currentSpeed: StormglassValue?,
    @SerializedName("currentDirection")
    val currentDirection: StormglassValue?,
    @SerializedName("airTemperature")
    val airTemperature: StormglassValue?,
    @SerializedName("pressure")
    val pressure: StormglassValue?,
    @SerializedName("cloudCover")
    val cloudCover: StormglassValue?,
    @SerializedName("humidity")
    val humidity: StormglassValue?,
    @SerializedName("visibility")
    val visibility: StormglassValue?,
    @SerializedName("precipitation")
    val precipitation: StormglassValue?
)

data class StormglassValue(
    @SerializedName("sg")
    val sg: Double?,
    @SerializedName("noaa")
    val noaa: Double?,
    @SerializedName("meteo")
    val meteo: Double?
) {
    // Get the first available value
    val value: Double?
        get() = sg ?: noaa ?: meteo
}

// Tide Extremes Response
data class StormglassTideResponse(
    @SerializedName("data")
    val data: List<StormglassTideExtreme>
)

data class StormglassTideExtreme(
    @SerializedName("time")
    val time: String,
    @SerializedName("height")
    val height: Double,
    @SerializedName("type")
    val type: String // "high" or "low"
)

// Astronomy Response
data class StormglassAstronomyResponse(
    @SerializedName("data")
    val data: List<StormglassAstronomyDay>
)

data class StormglassAstronomyDay(
    @SerializedName("time")
    val time: String,
    @SerializedName("astronomy")
    val astronomy: StormglassAstronomy
)

data class StormglassAstronomy(
    @SerializedName("sunrise")
    val sunrise: String?,
    @SerializedName("sunset")
    val sunset: String?,
    @SerializedName("moonrise")
    val moonrise: String?,
    @SerializedName("moonset")
    val moonset: String?,
    @SerializedName("moonPhase")
    val moonPhase: StormglassMoonPhase?
)

data class StormglassMoonPhase(
    @SerializedName("current")
    val current: StormglassMoonPhaseInfo
)

data class StormglassMoonPhaseInfo(
    @SerializedName("text")
    val text: String,
    @SerializedName("value")
    val value: Double // 0-1 (0 = new moon, 0.5 = full moon)
)
