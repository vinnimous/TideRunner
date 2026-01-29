package com.fishing.conditions.data.models

data class FishingSuitability(
    val score: Float,
    val rating: Rating,
    val factors: Map<String, Float>
) {
    enum class Rating {
        POOR, FAIR, GOOD, EXCELLENT
    }
}
