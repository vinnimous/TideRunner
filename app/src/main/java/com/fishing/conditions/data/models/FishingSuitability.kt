package com.fishing.conditions.data.models

data class FishingSuitability(
    val score: Float,
    val rating: Rating,
    val factors: Map<String, Float>,
    val hsi: Float? = null,
    val hsiComponents: Map<String, Float>? = null
) {
    enum class Rating {
        POOR, FAIR, GOOD, EXCELLENT
    }
}
