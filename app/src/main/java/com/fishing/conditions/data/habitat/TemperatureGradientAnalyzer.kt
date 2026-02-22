package com.fishing.conditions.data.habitat

import kotlin.math.sqrt

class TemperatureGradientAnalyzer {

    data class TemperatureGrid(
        val latitudes: List<Double>,
        val longitudes: List<Double>,
        val temperatures: List<List<Double>>
    )

    data class TemperatureBreak(
        val latIndex: Int,
        val lonIndex: Int,
        val gradientMagnitude: Double,
        val direction: Double // degrees
    )

    fun analyzeTemperatureGradient(grid: TemperatureGrid): List<TemperatureBreak> {
        val temps = grid.temperatures
        val rows = temps.size
        val cols = temps[0].size

        val breaks = mutableListOf<TemperatureBreak>()

        for (i in 1 until rows - 1) {
            for (j in 1 until cols - 1) {
                // Calculate temperature gradient
                val dT_dx = (temps[i][j+1] - temps[i][j-1]) / 2.0
                val dT_dy = (temps[i+1][j] - temps[i-1][j]) / 2.0
                val gradientMagnitude = Math.sqrt(dT_dx * dT_dx + dT_dy * dT_dy)

                // Direction of gradient (simplified)
                val direction = Math.toDegrees(Math.atan2(dT_dy, dT_dx))

                if (gradientMagnitude > 0.5) { // Threshold for significant break
                    breaks.add(TemperatureBreak(i, j, gradientMagnitude, direction))
                }
            }
        }

        return breaks
    }

    fun calculateTemperatureBreakScore(
        latIndex: Int,
        lonIndex: Int,
        tempBreaks: List<TemperatureBreak>,
        sensitivity: Double
    ): Double {
        if (tempBreaks.isEmpty()) return 0.0

        var totalScore = 0.0
        var count = 0

        for (tempBreak in tempBreaks) {
            val dx = latIndex - tempBreak.latIndex
            val dy = lonIndex - tempBreak.lonIndex
            val distance = Math.sqrt((dx * dx + dy * dy).toDouble())

            if (distance <= 5.0) { // Within 5 grid cells
                val proximityScore = 1.0 / (1.0 + distance)
                val breakScore = tempBreak.gradientMagnitude / 5.0 // Normalize
                totalScore += proximityScore * breakScore * sensitivity
                count++
            }
        }

        return if (count > 0) (totalScore / count).coerceIn(0.0, 1.0) else 0.0
    }
}
