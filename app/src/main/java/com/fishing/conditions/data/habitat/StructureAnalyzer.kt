package com.fishing.conditions.data.habitat

import com.fishing.conditions.data.habitat.BathymetryRepository.BathymetryGrid
import kotlin.math.sqrt

class StructureAnalyzer {

    data class StructureData(
        val slopeGrid: List<List<Double>>,
        val structureEdges: List<Pair<Int, Int>>, // indices of high-slope cells
        val maxSlope: Double,
        val minSlope: Double
    )

    fun analyzeStructure(grid: BathymetryGrid): StructureData {
        val depths = grid.depths
        val rows = depths.size
        val cols = depths[0].size

        val slopeGrid = MutableList(rows) { MutableList(cols) { 0.0 } }

        var maxSlope = 0.0
        var minSlope = Double.MAX_VALUE

        for (i in 1 until rows - 1) {
            for (j in 1 until cols - 1) {
                // Calculate slope using central difference
                val dz_dx = (depths[i][j+1] - depths[i][j-1]) / (2 * 0.01) // lon step ~0.01 degrees
                val dz_dy = (depths[i+1][j] - depths[i-1][j]) / (2 * 0.01) // lat step ~0.01 degrees
                val slope = sqrt(dz_dx * dz_dx + dz_dy * dz_dy)

                slopeGrid[i][j] = slope
                maxSlope = maxOf(maxSlope, slope)
                minSlope = minOf(minSlope, slope)
            }
        }

        // Identify structure edges (high slope areas)
        val structureEdges = mutableListOf<Pair<Int, Int>>()
        val slopeThreshold = (maxSlope - minSlope) * 0.7 + minSlope // Top 30% slopes

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (slopeGrid[i][j] >= slopeThreshold) {
                    structureEdges.add(Pair(i, j))
                }
            }
        }

        return StructureData(
            slopeGrid = slopeGrid,
            structureEdges = structureEdges,
            maxSlope = maxSlope,
            minSlope = minSlope
        )
    }

    fun calculateDistanceToStructure(latIndex: Int, lonIndex: Int, structureEdges: List<Pair<Int, Int>>): Double {
        if (structureEdges.isEmpty()) return Double.MAX_VALUE

        var minDistance = Double.MAX_VALUE
        for ((edgeLat, edgeLon) in structureEdges) {
            val distance = sqrt(
                ((latIndex - edgeLat).toDouble() * (latIndex - edgeLat)).toDouble() +
                ((lonIndex - edgeLon).toDouble() * (lonIndex - edgeLon)).toDouble()
            )
            minDistance = minOf(minDistance, distance)
        }
        return minDistance
    }

    fun normalizeSlope(slope: Double, minSlope: Double, maxSlope: Double): Double {
        if (maxSlope == minSlope) return 0.5
        return (slope - minSlope) / (maxSlope - minSlope)
    }
}
