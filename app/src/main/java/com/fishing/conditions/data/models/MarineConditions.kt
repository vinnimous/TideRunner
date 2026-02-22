package com.fishing.conditions.data.models

import com.fishing.conditions.data.models.Species.MoonPhase
import com.fishing.conditions.data.models.Species.TidePhase
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
        var maxPossible = 0f
        val factors = mutableMapOf<String, Float>()

        // Water temperature factor (0-25 points)
        // Gaussian is centered on species optimal °F; naturally penalises out-of-range temps
        // without a hard cliff — e.g. 1°F outside range still scores ~8/25 instead of 0.
        waterTemperature?.let { tempF ->
            maxPossible += 25f
            val sigma = ((species.preferredWaterTemp.max - species.preferredWaterTemp.min) / 3.5)
                .coerceAtLeast(1.0)
            val tempScore = (gaussianWeight(tempF, species.preferredWaterTemp.optimal, sigma) * 25f).toFloat()
            factors["Water Temperature"] = tempScore
            score += tempScore
        }

        // Wind speed factor (0-15 points)
        // Gaussian centred on the species' preferred mid-range; tapers smoothly above max.
        windSpeed?.let { windMph ->
            maxPossible += 15f
            val windOptimal = (species.preferredWindSpeed.min + species.preferredWindSpeed.max) / 2.0
            val sigma = ((species.preferredWindSpeed.max - species.preferredWindSpeed.min) / 3.0)
                .coerceAtLeast(1.0)
            val windScore = (gaussianWeight(windMph, windOptimal, sigma) * 15f).toFloat()
            factors["Wind Conditions"] = windScore
            score += windScore
        }

        // Wave height factor (0-10 points)
        waveHeight?.let { waveFt ->
            maxPossible += 10f
            val waveOptimal = (species.preferredWaveHeight.min + species.preferredWaveHeight.max) / 2.0
            val sigma = ((species.preferredWaveHeight.max - species.preferredWaveHeight.min) / 3.0)
                .coerceAtLeast(0.1)
            val waveScore = (gaussianWeight(waveFt, waveOptimal, sigma) * 10f).toFloat()
            factors["Wave Conditions"] = waveScore
            score += waveScore
        }

        // Moon phase factor (0-15 points) — graduated, not binary
        moonPhase?.let { phase ->
            maxPossible += 15f
            val moonScore = when {
                species.preferredMoonPhase.contains(phase) -> 15f
                phase == Species.MoonPhase.NEW_MOON || phase == Species.MoonPhase.FULL_MOON -> 10f
                phase == Species.MoonPhase.FIRST_QUARTER || phase == Species.MoonPhase.LAST_QUARTER -> 7f
                else -> 4f  // crescent / gibbous — not zero, just lower
            }
            factors["Moon Phase"] = moonScore
            score += moonScore
        }

        // Tide phase factor (0-15 points) — graduated by tide energy
        currentTidePhase?.let { tide ->
            maxPossible += 15f
            val tideScore = when {
                species.preferredTidePhase.contains(tide) -> 15f
                tide == Species.TidePhase.RISING_TIDE -> 12f
                tide == Species.TidePhase.FALLING_TIDE -> 10f
                tide == Species.TidePhase.HIGH_TIDE -> 8f
                else -> 5f  // LOW_TIDE — least active for most species
            }
            factors["Tide Phase"] = tideScore
            score += tideScore
        }

        // Solunar activity factor (0-20 points)
        // API returns 1-4 (1=poor … 4=excellent); Gaussian peak at 4.
        solunarScore?.let { solunar ->
            maxPossible += 20f
            val solunarFactor = (gaussianWeight(solunar.toDouble(), 4.0, 1.0) * 20f).toFloat()
                .coerceAtLeast(2f)
            factors["Solunar Activity"] = solunarFactor
            score += solunarFactor
        }

        // Normalize against actual available data so missing fields don't drag the score down.
        // E.g. if wave data is unavailable, max is 80 not 100, and we scale accordingly.
        val normalizedScore = if (maxPossible > 0f) {
            (score / maxPossible * 100f).coerceIn(0f, 100f)
        } else {
            0f
        }

        val rating = when {
            normalizedScore >= 70f -> FishingSuitability.Rating.EXCELLENT
            normalizedScore >= 50f -> FishingSuitability.Rating.GOOD
            normalizedScore >= 30f -> FishingSuitability.Rating.FAIR
            else -> FishingSuitability.Rating.POOR
        }

        return FishingSuitability(
            score = normalizedScore,
            rating = rating,
            factors = factors
        )
    }

    private fun gaussianWeight(x: Double, mean: Double, sigma: Double): Double {
        if (sigma == 0.0) return if (x == mean) 1.0 else 0.0
        return exp(-((x - mean).pow(2)) / (2 * sigma * sigma))
    }
}
