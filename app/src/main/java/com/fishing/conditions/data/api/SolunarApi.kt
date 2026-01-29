package com.fishing.conditions.data.api

import com.fishing.conditions.data.api.models.SolunarResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SolunarApi {
    // Path pattern: /solunar/{lat},{lon},{YYYYMMDD},{tz}
    @GET("solunar/{lat},{lon},{date},{tz}")
    suspend fun getSolunarData(
        @Path("lat") latitude: Double,
        @Path("lon") longitude: Double,
        @Path("date") date: String, // Format: YYYYMMDD
        @Path("tz") timezoneOffset: Int // Numeric timezone offset (e.g., -5 for EST)
    ): SolunarResponse
}
