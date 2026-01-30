package com.fishing.conditions.data.models

import com.fishing.conditions.data.models.Species.MoonPhase
import com.fishing.conditions.data.models.Species.TidePhase
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow

data class MarineConditions(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,

    // Water conditions (in imperial units)
    val waterTemperature: Double?, // Fahrenheit
    val waveHeight: Double?,       // feet
    val waveDirection: Double?,
    val wavePeriod: Double?,
    val currentSpeed: Double?,     // mph
    val currentDirection: Double?,

    // Weather conditions
    val windSpeed: Double?,        // mph
    val windDirection: Double?,
    val windGust: Double?,         // mph
    val airTemperature: Double?,   // Fahrenheit
    val pressure: Double?,         // hPa (leave as is for now)
    val humidity: Double?,
    val cloudCover: Double?,
    val visibility: Double?,       // miles
    val precipitation: Double?,    // inches

    // Tide data
    val tideHeight: Double?,       // feet
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
        val height: Double, // feet
        val type: String
    )

    data class SolunarPeriod(
        val startTime: Long,
        val endTime: Long,
        val type: String
    )

    fun getFishingSuitability(species: Species): FishingSuitability {
        var score = 0f
        val factors = mutableMapOf<String, Float>()

        // Water temperature factor (0-25 points)
        waterTemperature?.let { tempF ->
            val tempMinF = cToF(species.preferredWaterTemp.min)
            val tempMaxF = cToF(species.preferredWaterTemp.max)
            val tempOptimalF = cToF(species.preferredWaterTemp.optimal)
            val sigma = (tempMaxF - tempMinF) / 3.0
            val tempScore = if (tempF < tempMinF || tempF > tempMaxF) {
                0f
            } else {
                (gaussianWeight(tempF, tempOptimalF, sigma) * 25f).toFloat()
            }
            factors["Water Temperature"] = tempScore
            score += tempScore
        }

        // Wind speed factor (0-15 points)
        windSpeed?.let { windMph ->
            val windOptimal = (species.preferredWindSpeed.min + species.preferredWindSpeed.max) / 2.0
            val sigma = (species.preferredWindSpeed.max - species.preferredWindSpeed.min) / 3.0
            val windScore = if (windMph < species.preferredWindSpeed.min || windMph > species.preferredWindSpeed.max) {
                0f
            } else {
                (gaussianWeight(windMph, windOptimal, sigma) * 15f).toFloat()
            }
            factors["Wind Conditions"] = windScore
            score += windScore
        }

        // Wave height factor (0-15 points)
        waveHeight?.let { waveFt ->
            val waveOptimal = (species.preferredWaveHeight.min + species.preferredWaveHeight.max) / 2.0
            val sigma = (species.preferredWaveHeight.max - species.preferredWaveHeight.min) / 3.0
            val waveScore = if (waveFt < species.preferredWaveHeight.min || waveFt > species.preferredWaveHeight.max) {
                0f
            } else {
                (gaussianWeight(waveFt, waveOptimal, sigma) * 15f).toFloat()
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

    private fun cToF(c: Double) = c * 9 / 5 + 32

    private fun gaussianWeight(x: Double, mean: Double, sigma: Double): Double {
        return exp(-((x - mean).pow(2)) / (2 * sigma * sigma))
    }
}
