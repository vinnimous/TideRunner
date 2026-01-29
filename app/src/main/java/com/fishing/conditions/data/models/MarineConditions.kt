package com.fishing.conditions.data.models

import com.fishing.conditions.data.models.Species.MoonPhase
import com.fishing.conditions.data.models.Species.TidePhase

data class MarineConditions(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,

    // Water conditions
    val waterTemperature: Double?,
    val waveHeight: Double?,
    val waveDirection: Double?,
    val wavePeriod: Double?,
    val currentSpeed: Double?,
    val currentDirection: Double?,

    // Weather conditions
    val windSpeed: Double?,
    val windDirection: Double?,
    val windGust: Double?,
    val airTemperature: Double?,
    val pressure: Double?,
    val humidity: Double?,
    val cloudCover: Double?,
    val visibility: Double?,
    val precipitation: Double?,

    // Tide data
    val tideHeight: Double?,
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
        val height: Double,
        val type: String // "high" or "low"
    )

    data class SolunarPeriod(
        val startTime: Long,
        val endTime: Long,
        val type: String // "major" or "minor"
    )

    fun getFishingSuitability(species: Species): FishingSuitability {
        var score = 0f
        val factors = mutableMapOf<String, Float>()

        // Water temperature factor (0-25 points)
        waterTemperature?.let { temp ->
            val tempScore = when {
                temp < species.preferredWaterTemp.min || temp > species.preferredWaterTemp.max -> 0f
                temp == species.preferredWaterTemp.optimal -> 25f
                else -> {
                    val range = species.preferredWaterTemp.max - species.preferredWaterTemp.min
                    val distance = Math.abs(temp - species.preferredWaterTemp.optimal)
                    25f * (1f - (distance / range).toFloat()).coerceAtLeast(0f)
                }
            }
            factors["Water Temperature"] = tempScore
            score += tempScore
        }

        // Wind speed factor (0-15 points)
        windSpeed?.let { wind ->
            val windScore = when {
                wind < species.preferredWindSpeed.min || wind > species.preferredWindSpeed.max -> 5f
                wind <= species.preferredWindSpeed.max * 0.5 -> 15f
                else -> 10f
            }
            factors["Wind Conditions"] = windScore
            score += windScore
        }

        // Wave height factor (0-15 points)
        waveHeight?.let { wave ->
            val waveScore = when {
                wave < species.preferredWaveHeight.min || wave > species.preferredWaveHeight.max -> 5f
                wave <= species.preferredWaveHeight.max * 0.5 -> 15f
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
