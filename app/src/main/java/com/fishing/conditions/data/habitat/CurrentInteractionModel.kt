package com.fishing.conditions.data.habitat

import com.fishing.conditions.data.habitat.BathymetryRepository.BathymetryGrid
import com.fishing.conditions.data.habitat.StructureAnalyzer.StructureData
import kotlin.math.abs
import kotlin.math.sqrt

class CurrentInteractionModel {

    data class CurrentVector(val speed: Double, val direction: Double) // direction in degrees

    data class CurrentInteractionScore(
        val score: Double,
        val isUpCurrent: Boolean,
        val speedMatch: Double
    )

    fun calculateCurrentInteraction(
        latIndex: Int,
        lonIndex: Int,
        current: CurrentVector,
        grid: BathymetryGrid,
        structureData: StructureData,
        speciesCurrentPrefs: com.fishing.conditions.data.models.Species.CurrentRange
    ): CurrentInteractionScore {
        val slope = structureData.slopeGrid.getOrNull(latIndex)?.getOrNull(lonIndex) ?: 0.0

        // Determine if structure faces into current
        // Simplified: assume slope direction based on depth gradient
        val depth = grid.depths.getOrNull(latIndex)?.getOrNull(lonIndex) ?: 0.0
        val neighbors = getNeighbors(latIndex, lonIndex, grid)
        val avgNeighborDepth = neighbors.map { (i, j) -> grid.depths.getOrNull(i)?.getOrNull(j) ?: 0.0 }.average()

        val slopeDirection = if (depth > avgNeighborDepth) "down" else "up" // Simplified

        // Current direction: 0 = north, 90 = east, etc.
        val isUpCurrent = when (slopeDirection) {
            "up" -> current.direction in 0.0..180.0 // Current from north/south
            "down" -> current.direction in 180.0..360.0 // Current from south/north
            else -> false
        }

        // Speed match
        val speedScore = when {
            current.speed in speciesCurrentPrefs.min..speciesCurrentPrefs.max -> 1.0
            current.speed < speciesCurrentPrefs.min -> current.speed / speciesCurrentPrefs.min
            else -> speciesCurrentPrefs.max / current.speed
        }.coerceIn(0.0, 1.0)

        // Overall score: higher if up-current and speed matches
        val score = if (isUpCurrent) speedScore else speedScore * 0.5

        return CurrentInteractionScore(
            score = score,
            isUpCurrent = isUpCurrent,
            speedMatch = speedScore
        )
    }

    private fun getNeighbors(latIndex: Int, lonIndex: Int, grid: BathymetryGrid): List<Pair<Int, Int>> {
        val neighbors = mutableListOf<Pair<Int, Int>>()
        for (di in -1..1) {
            for (dj in -1..1) {
                if (di == 0 && dj == 0) continue
                val ni = latIndex + di
                val nj = lonIndex + dj
                if (ni in grid.depths.indices && nj in grid.depths[0].indices) {
                    neighbors.add(Pair(ni, nj))
                }
            }
        }
        return neighbors
    }
}
