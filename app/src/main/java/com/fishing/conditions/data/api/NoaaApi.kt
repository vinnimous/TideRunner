package com.fishing.conditions.data.api

import com.fishing.conditions.data.api.models.NoaaCurrentsResponse
import com.fishing.conditions.data.api.models.NoaaDataResponse
import com.fishing.conditions.data.api.models.NoaaPredictionsResponse
import com.fishing.conditions.data.api.models.NoaaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NoaaApi {
    // Get nearby stations
    @GET("mdapi/prod/webapi/stations.json")
    suspend fun getStations(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("radius") radius: Double = 50.0 // km
    ): NoaaResponse

    // Get tide predictions
    @GET("api/prod/datagetter")
    suspend fun getTidePredictions(
        @Query("station") stationId: String,
        @Query("product") product: String = "predictions",
        @Query("begin_date") beginDate: String,
        @Query("end_date") endDate: String,
        @Query("datum") datum: String = "MLLW",
        @Query("units") units: String = "metric",
        @Query("time_zone") timeZone: String = "gmt",
        @Query("format") format: String = "json",
        @Query("interval") interval: String = "hilo" // High/low only
    ): NoaaPredictionsResponse

    // Get water level observations
    @GET("api/prod/datagetter")
    suspend fun getWaterLevel(
        @Query("station") stationId: String,
        @Query("product") product: String = "water_level",
        @Query("begin_date") beginDate: String,
        @Query("end_date") endDate: String,
        @Query("datum") datum: String = "MLLW",
        @Query("units") units: String = "metric",
        @Query("time_zone") timeZone: String = "gmt",
        @Query("format") format: String = "json"
    ): NoaaDataResponse

    // Get current predictions
    @GET("api/prod/datagetter")
    suspend fun getCurrents(
        @Query("station") stationId: String,
        @Query("product") product: String = "currents",
        @Query("begin_date") beginDate: String,
        @Query("end_date") endDate: String,
        @Query("units") units: String = "metric",
        @Query("time_zone") timeZone: String = "gmt",
        @Query("format") format: String = "json"
    ): NoaaCurrentsResponse
}
