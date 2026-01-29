package com.fishing.conditions.domain

import com.fishing.conditions.data.models.FishingSuitability
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species

object FsiCalculator {

    fun calculate(
        conditions: MarineConditions,
        species: Species
    ): FishingSuitability {
        val factors = mutableMapOf<String, Float>()

        // Water temperature suitability
        conditions.waterTemperature?.let { temp ->
            val tempScore = when {
                temp in species.preferredWaterTemp.min..species.preferredWaterTemp.max -> 1.0f
                temp in (species.preferredWaterTemp.min - 5)..(species.preferredWaterTemp.max + 5) -> 0.6f
                else -> 0.3f
            }
            factors["waterTemperature"] = tempScore
        }

        // Wave height suitability
        conditions.waveHeight?.let { wave ->
            val waveScore = when {
                wave < 1.0 -> 1.0f
                wave < 2.0 -> 0.7f
                wave < 3.0 -> 0.4f
                else -> 0.2f
            }
            factors["waveHeight"] = waveScore
        }

        // Wind speed suitability
        conditions.windSpeed?.let { wind ->
            val windScore = when {
                wind < 10.0 -> 1.0f
                wind < 20.0 -> 0.7f
                wind < 30.0 -> 0.4f
                else -> 0.2f
            }
            factors["windSpeed"] = windScore
        }

        val averageScore = if (factors.isNotEmpty()) {
            factors.values.average().toFloat()
        } else {
            0.5f
        }

        val rating = when {
            averageScore >= 0.8f -> FishingSuitability.Rating.EXCELLENT
            averageScore >= 0.6f -> FishingSuitability.Rating.GOOD
            averageScore >= 0.4f -> FishingSuitability.Rating.FAIR
            else -> FishingSuitability.Rating.POOR
        }

        return FishingSuitability(
            score = averageScore,
            rating = rating,
            factors = factors
        )
    }
}
