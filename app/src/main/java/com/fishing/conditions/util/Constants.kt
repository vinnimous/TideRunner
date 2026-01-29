package com.fishing.conditions.util

object Constants {
    // API Base URLs
    const val NOAA_BASE_URL = "https://api.tidesandcurrents.noaa.gov/"
    const val OPEN_METEO_BASE_URL = "https://api.open-meteo.com/"
    const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/"
    const val STORMGLASS_BASE_URL = "https://api.stormglass.io/v2/"
    const val SOLUNAR_BASE_URL = "https://api.solunar.org/"
    const val IPGEOLOCATION_BASE_URL = "https://api.ipgeolocation.io/"

    // API Keys (configure in local.properties if needed)
    // STORMGLASS_API_KEY - Free tier: 10 calls/day
    // IPGEOLOCATION_API_KEY - Free tier available
    // OPENWEATHER_API_KEY - Free tier available

    // Note: Open-Meteo and Solunar do NOT require API keys!

    const val CACHE_EXPIRATION_HOURS = 6
    const val DEFAULT_ZOOM = 10f
    const val DEFAULT_LATITUDE = 37.7749  // San Francisco Bay
    const val DEFAULT_LONGITUDE = -122.4194

    object Permissions {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
