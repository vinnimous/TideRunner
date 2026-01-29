package com.fishing.conditions.data.repository

import com.fishing.conditions.data.api.*
import com.fishing.conditions.data.cache.MarineDataDao
import com.fishing.conditions.data.cache.entities.MarineDataEntity
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarineDataRepository @Inject constructor(
    private val marineDataDao: MarineDataDao,
    private val noaaApi: NoaaApi,
    private val openMeteoApi: OpenMeteoApi,
    private val openWeatherApi: OpenWeatherApi,
    private val stormglassApi: StormglassApi,
    private val solunarApi: SolunarApi
) {

    suspend fun getMarineConditions(latitude: Double, longitude: Double): MarineConditions? {
        return try {
            // Try cache first
            val cached = marineDataDao.getMarineData(latitude, longitude)
            if (cached != null && !isCacheExpired(cached.timestamp)) {
                return entityToMarineConditions(cached)
            }

            // Fetch fresh data from APIs
            fetchMarineConditionsFromAPIs(latitude, longitude)
        } catch (e: Exception) {
            // Fall back to cached data even if expired
            marineDataDao.getMarineData(latitude, longitude)?.let {
                entityToMarineConditions(it)
            }
        }
    }

    private suspend fun fetchMarineConditionsFromAPIs(
        latitude: Double,
        longitude: Double
    ): MarineConditions = coroutineScope {

        // Fetch data from multiple APIs concurrently
        val openMeteoDeferred = async {
            try {
                openMeteoApi.getMarineForecast(latitude, longitude)
            } catch (e: Exception) {
                null
            }
        }

        val weatherDeferred = async {
            try {
                openMeteoApi.getForecast(latitude, longitude)
            } catch (e: Exception) {
                null
            }
        }

        val solunarDeferred = async {
            try {
                val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
                val date = dateFormat.format(Date())
                val tz = TimeZone.getDefault().rawOffset / (1000 * 60 * 60)
                solunarApi.getSolunarData(latitude, longitude, date, tz)
            } catch (e: Exception) {
                null
            }
        }

        // Wait for results
        val marineData = openMeteoDeferred.await()
        val weatherData = weatherDeferred.await()
        val solunarData = solunarDeferred.await()

        // Extract current hour data (first hour in forecast)
        val marineHourly = marineData?.hourly
        val weatherHourly = weatherData?.hourly

        val now = System.currentTimeMillis()

        // Parse solunar periods
        val majorPeriods = solunarData?.majorPeriods?.map { period ->
            MarineConditions.SolunarPeriod(
                startTime = parseTimeString(period.start) ?: now,
                endTime = parseTimeString(period.end) ?: now,
                type = "major"
            )
        }

        val minorPeriods = solunarData?.minorPeriods?.map { period ->
            MarineConditions.SolunarPeriod(
                startTime = parseTimeString(period.start) ?: now,
                endTime = parseTimeString(period.end) ?: now,
                type = "minor"
            )
        }

        // Parse moon phase
        val moonPhase = parseMoonPhase(solunarData?.moonPhase?.phase)

        // Create comprehensive marine conditions
        val conditions = MarineConditions(
            latitude = latitude,
            longitude = longitude,
            timestamp = now,

            // Water conditions from Open-Meteo Marine
            waterTemperature = marineHourly?.seaSurfaceTemperature?.firstOrNull(),
            waveHeight = marineHourly?.waveHeight?.firstOrNull(),
            waveDirection = marineHourly?.waveDirection?.firstOrNull(),
            wavePeriod = marineHourly?.wavePeriod?.firstOrNull(),
            currentSpeed = marineHourly?.currentVelocity?.firstOrNull(),
            currentDirection = marineHourly?.currentDirection?.firstOrNull(),

            // Weather conditions from Open-Meteo Weather
            windSpeed = weatherHourly?.windSpeed?.firstOrNull(),
            windDirection = weatherHourly?.windDirection?.firstOrNull(),
            windGust = weatherHourly?.windGusts?.firstOrNull(),
            airTemperature = weatherHourly?.temperature?.firstOrNull(),
            pressure = weatherHourly?.pressure?.firstOrNull(),
            humidity = weatherHourly?.humidity?.firstOrNull(),
            cloudCover = weatherHourly?.cloudCover?.firstOrNull(),
            visibility = weatherHourly?.visibility?.firstOrNull(),
            precipitation = weatherHourly?.precipitation?.firstOrNull(),

            // Tide data (would come from NOAA or Stormglass - placeholder for now)
            tideHeight = null,
            nextHighTide = null,
            nextLowTide = null,
            currentTidePhase = null,

            // Astronomical data from Solunar
            sunrise = parseTimeString(solunarData?.sunrise),
            sunset = parseTimeString(solunarData?.sunset),
            moonrise = parseTimeString(solunarData?.moonrise),
            moonset = parseTimeString(solunarData?.moonset),
            moonPhase = moonPhase,
            moonIllumination = solunarData?.moonPhase?.illumination?.let { it / 100.0 },

            // Solunar data
            majorPeriods = majorPeriods,
            minorPeriods = minorPeriods,
            solunarScore = solunarData?.score,

            // Metadata
            dataSource = "Open-Meteo + Solunar",
            forecastHours = 0
        )

        // Cache the data
        cacheMarineConditions(conditions)

        conditions
    }

    private fun entityToMarineConditions(entity: MarineDataEntity): MarineConditions {
        return MarineConditions(
            latitude = entity.latitude,
            longitude = entity.longitude,
            timestamp = entity.timestamp,
            waterTemperature = entity.waterTemperature,
            waveHeight = entity.waveHeight,
            windSpeed = entity.windSpeed,
            airTemperature = entity.airTemperature,
            waveDirection = null,
            wavePeriod = null,
            currentSpeed = null,
            currentDirection = null,
            windDirection = null,
            windGust = null,
            pressure = null,
            humidity = null,
            cloudCover = null,
            visibility = null,
            precipitation = null,
            tideHeight = null,
            nextHighTide = null,
            nextLowTide = null,
            currentTidePhase = null,
            sunrise = null,
            sunset = null,
            moonrise = null,
            moonset = null,
            moonPhase = null,
            moonIllumination = null,
            majorPeriods = null,
            minorPeriods = null,
            solunarScore = null,
            dataSource = "Cache",
            forecastHours = 0
        )
    }

    private suspend fun cacheMarineConditions(conditions: MarineConditions) {
        val entity = MarineDataEntity(
            latitude = conditions.latitude,
            longitude = conditions.longitude,
            waterTemperature = conditions.waterTemperature,
            waveHeight = conditions.waveHeight,
            windSpeed = conditions.windSpeed,
            airTemperature = conditions.airTemperature,
            timestamp = conditions.timestamp
        )
        marineDataDao.insertMarineData(entity)
    }

    private fun isCacheExpired(timestamp: Long): Boolean {
        val sixHoursInMillis = 6 * 60 * 60 * 1000
        return System.currentTimeMillis() - timestamp > sixHoursInMillis
    }

    private fun parseTimeString(timeStr: String?): Long? {
        if (timeStr == null) return null
        return try {
            // Try parsing as HH:mm format
            val sdf = SimpleDateFormat("HH:mm", Locale.US)
            val today = Calendar.getInstance()
            val time = sdf.parse(timeStr) ?: return null
            val cal = Calendar.getInstance()
            cal.time = time
            today.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
            today.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
            today.timeInMillis
        } catch (e: Exception) {
            null
        }
    }

    private fun parseMoonPhase(phaseStr: String?): Species.MoonPhase? {
        return when (phaseStr?.lowercase()) {
            "new moon" -> Species.MoonPhase.NEW_MOON
            "waxing crescent" -> Species.MoonPhase.WAXING_CRESCENT
            "first quarter" -> Species.MoonPhase.FIRST_QUARTER
            "waxing gibbous" -> Species.MoonPhase.WAXING_GIBBOUS
            "full moon" -> Species.MoonPhase.FULL_MOON
            "waning gibbous" -> Species.MoonPhase.WANING_GIBBOUS
            "last quarter", "third quarter" -> Species.MoonPhase.LAST_QUARTER
            "waning crescent" -> Species.MoonPhase.WANING_CRESCENT
            else -> null
        }
    }

    fun getAllMarineConditions(): Flow<List<MarineConditions>> {
        return marineDataDao.getAllMarineData().map { entities ->
            entities.map { entity -> entityToMarineConditions(entity) }
        }
    }

    suspend fun refreshMarineData(latitude: Double, longitude: Double) {
        fetchMarineConditionsFromAPIs(latitude, longitude)
    }
}
