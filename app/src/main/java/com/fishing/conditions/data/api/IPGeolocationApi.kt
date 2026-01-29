package com.fishing.conditions.data.api

import com.fishing.conditions.data.api.models.IPGeolocationAstronomyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IPGeolocationApi {
    @GET("astronomy")
    suspend fun getAstronomy(
        @Query("apiKey") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("long") longitude: Double,
        @Query("date") date: String? = null // Optional: YYYY-MM-DD format
    ): IPGeolocationAstronomyResponse
}
