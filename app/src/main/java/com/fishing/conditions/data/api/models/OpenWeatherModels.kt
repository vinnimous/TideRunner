package com.fishing.conditions.data.api.models

import com.google.gson.annotations.SerializedName

data class OpenWeatherResponse(
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("main")
    val main: Main,
    @SerializedName("wind")
    val wind: Wind
)

data class Coord(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)

data class Main(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("humidity")
    val humidity: Double
)

data class Wind(
    @SerializedName("speed")
    val speed: Double,
    @SerializedName("deg")
    val deg: Double
)
