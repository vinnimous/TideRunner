package com.fishing.conditions.data.models

import com.fishing.conditions.data.models.Species.MoonPhase
import com.fishing.conditions.data.models.Species.TidePhase
import kotlin.math.abs

data class MarineConditions(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,

    // Water conditions (originally in metric)
    val waterTemperature: Double?, // Celsius
    val waveHeight: Double?,       // meters
    val waveDirection: Double?,
    val wavePeriod: Double?,
    val currentSpeed: Double?,     // m/s
    val currentDirection: Double?,

    // Weather conditions
    val windSpeed: Double?,        // m/s
    val windDirection: Double?,
    val windGust: Double?,
    val airTemperature: Double?,   // Celsius
    val pressure: Double?,
    val humidity: Double?,
    val cloudCover: Double?,
    val visibility: Double?,
    val precipitation: Double?,

    // Tide data
    val tideHeight: Double?,       // meters
    val nextHighTide: TideEvent?,
    val nextLowTide: TideEvent?,
    val currentTidePhase: TidePhase?,

    // Astronomical data
    val sunrise: Long?,
    val sunset: Long?,
    val moonrise: Long?,
    val moonset: Long?,
    val moonPhase: MoonPhase?,
    val moonIllumination: Double?,

    // Solunar data
    val majorPeriods: List<SolunarPeriod>?,
    val minorPeriods: List<SolunarPeriod>?,
    val solunarScore: Int?,

    // Metadata
    val dataSource: String,
    val forecastHours: Int = 0
) {

    data class TideEvent(
        val time: Long,
        val height: Double, // meters
        val type: String
    )

    data class SolunarPeriod(
        val startTime: Long,
        val endTime: Long,
        val type: String
    )

    // Convert metric -> imperial
    private fun cToF(celsius: Double) = celsius * 9 / 5 + 32
    private fun metersToFeet(meters: Double) = meters * 3.28084
    private fun msToMph(ms: Double) = ms * 2.23694

    fun getFishingSuitability(species: Species): FishingSuitability {
        var score = 0f
        val factors = mutableMapOf<String, Float>()

        // Water temperature factor (0-25 points)
        waterTemperature?.let { tempC ->
            val tempF = cToF(tempC)
            val tempScore = when {
                tempF < cToF(species.preferredWaterTemp.min) || tempF > cToF(species.preferredWaterTemp.max) -> 0f
                tempF == cToF(species.preferredWaterTemp.optimal) -> 25f
                else -> {
                    val range = cToF(species.preferredWaterTemp.max) - cToF(species.preferredWaterTemp.min)
                    val distance = abs(tempF - cToF(species.preferredWaterTemp.optimal))
                    25f * (1f - (distance / range).toFloat()).coerceAtLeast(0f)
                }
            }
            factors["Water Temperature"] = tempScore
            score += tempScore
        }

        // Wind speed factor (0-15 points)
        windSpeed?.let { windMs ->
            val windMph = msToMph(windMs)
            val windScore = when {
                windMph < msToMph(species.preferredWindSpeed.min) || windMph > msToMph(species.preferredWindSpeed.max) -> 5f
                windMph <= msToMph(species.preferredWindSpeed.max) * 0.5 -> 15f
                else -> 10f
            }
            factors["Wind Conditions"] = windScore
            score += windScore
        }

        // Wave height factor (0-15 points)
        waveHeight?.let { waveM ->
            val waveFt = metersToFeet(waveM)
            val waveScore = when {
                waveFt < metersToFeet(species.preferredWaveHeight.min) || waveFt > metersToFeet(species.preferredWaveHeight.max) -> 5f
                waveFt <= metersToFeet(species.preferredWaveHeight.max) * 0.5 -> 15f
                else -> 10f
            }
            factors["Wave Conditions"] = waveScore
            score += waveScore
        }

        // Moon phase factor (0-15 points)
        moonPhase?.let { phase ->
            val moonScore = if (species.preferredMoonPhase.contains(phase)) 15f else 8f
            factors["Moon Phase"] = moonScore
            score += moonScore
        }

        // Tide phase factor (0-15 points)
        currentTidePhase?.let { tide ->
            val tideScore = if (species.preferredTidePhase.contains(tide)) 15f else 8f
            factors["Tide Phase"] = tideScore
            score += tideScore
        }

        // Solunar activity factor (0-15 points)
        solunarScore?.let { solunar ->
            val solunarFactor = (solunar / 5f * 15f).coerceAtMost(15f)
            factors["Solunar Activity"] = solunarFactor
            score += solunarFactor
        }

        // Normalize score to 0-100
        val normalizedScore = score.coerceIn(0f, 100f)

        val rating = when {
            normalizedScore >= 75f -> FishingSuitability.Rating.EXCELLENT
            normalizedScore >= 60f -> FishingSuitability.Rating.GOOD
            normalizedScore >= 40f -> FishingSuitability.Rating.FAIR
            else -> FishingSuitability.Rating.POOR
        }

        return FishingSuitability(
            score = normalizedScore,
            rating = rating,
            factors = factors
        )
    }
}
