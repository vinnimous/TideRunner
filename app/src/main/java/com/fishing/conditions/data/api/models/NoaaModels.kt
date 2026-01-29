package com.fishing.conditions.data.api.models

import com.google.gson.annotations.SerializedName

// Tide/Water Level Data Response
data class NoaaDataResponse(
    @SerializedName("metadata")
    val metadata: NoaaMetadata?,
    @SerializedName("data")
    val data: List<NoaaDataPoint>?
)

data class NoaaMetadata(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lon")
    val lon: String?
)

data class NoaaDataPoint(
    @SerializedName("t")
    val time: String, // ISO format timestamp
    @SerializedName("v")
    val value: String, // Value (height, speed, etc.)
    @SerializedName("s")
    val sigma: String?, // Standard deviation
    @SerializedName("f")
    val flags: String?, // Quality flags
    @SerializedName("q")
    val quality: String? // Quality indicator
)

// Tide Predictions Response
data class NoaaPredictionsResponse(
    @SerializedName("predictions")
    val predictions: List<NoaaTidePrediction>?
)

data class NoaaTidePrediction(
    @SerializedName("t")
    val time: String,
    @SerializedName("v")
    val value: String, // Height in specified units
    @SerializedName("type")
    val type: String? // "H" for high, "L" for low
)

// Currents Data Response
data class NoaaCurrentsResponse(
    @SerializedName("current_predictions")
    val predictions: NoaaCurrentPredictions?
)

data class NoaaCurrentPredictions(
    @SerializedName("cp")
    val data: List<NoaaCurrentDataPoint>?
)

data class NoaaCurrentDataPoint(
    @SerializedName("Time")
    val time: String,
    @SerializedName("Velocity_Major")
    val velocityMajor: String?,
    @SerializedName("meanFloodDir")
    val meanFloodDir: String?,
    @SerializedName("meanEbbDir")
    val meanEbbDir: String?
)

// Stations Metadata Response
data class NoaaResponse(
    @SerializedName("stations")
    val stations: List<NoaaStation>
)

data class NoaaStation(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lng")
    val longitude: Double,
    @SerializedName("state")
    val state: String?,
    @SerializedName("type")
    val type: String?, // "waterlevels", "currents", etc.
    @SerializedName("distance")
    val distance: Double? // Distance from query point
)
