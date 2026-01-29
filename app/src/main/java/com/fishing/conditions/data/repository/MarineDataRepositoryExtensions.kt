package com.fishing.conditions.data.repository

import com.fishing.conditions.data.models.FishingSuitability
import com.fishing.conditions.data.models.MarineConditions

fun MarineConditions.calculateFishingSuitability(): FishingSuitability {
    val factors = mutableMapOf<String, Float>()

    waterTemperature?.let { temp ->
        factors["temperature"] = when (temp) {
            in 15.0..25.0 -> 1.0f
            in 10.0..30.0 -> 0.7f
            else -> 0.3f
        }
    }

    waveHeight?.let { wave ->
        factors["waves"] = when {
            wave < 1.0 -> 1.0f
            wave < 2.0 -> 0.7f
            else -> 0.3f
        }
    }

    val averageScore = factors.values.average().toFloat()
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
