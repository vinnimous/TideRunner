package com.fishing.conditions.data.habitat

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.sequences.generateSequence

@Singleton
class BathymetryRepository @Inject constructor() {

    // Simplified bathymetry grid for demonstration
    // In a real app, this would load from NOAA/GEBCO data
    data class BathymetryGrid(
        val latitudes: List<Double>,
        val longitudes: List<Double>,
        val depths: List<List<Double>> // depths[latIndex][lonIndex]
    )

    // Mock data for Atlantic coast (simplified)
    fun getBathymetryGrid(latMin: Double, latMax: Double, lonMin: Double, lonMax: Double): BathymetryGrid {
        // Generate a simple grid with varying depths
        val latStep = 0.01 // ~1km
        val lonStep = 0.01
        val latitudes = mutableListOf<Double>()
        var lat = latMin
        while (lat <= latMax) {
            latitudes.add(lat)
            lat += latStep
        }

        val longitudes = mutableListOf<Double>()
        var lon: Double = lonMin
        while (lon <= lonMax) {
            longitudes.add(lon)
            lon += lonStep
        }

        val depths = latitudes.map { lat ->
            longitudes.map { lon ->
                // Simple depth model: deeper offshore, shallower inshore
                val distanceFromCoast = (lon - lonMin) / (lonMax - lonMin) // 0 = coast, 1 = offshore
                5.0 + distanceFromCoast * 100.0 + Math.random() * 20.0 // 5-125m with noise
            }
        }

        return BathymetryGrid(latitudes, longitudes, depths)
    }

    fun getDepthAt(lat: Double, lon: Double, grid: BathymetryGrid): Double? {
        val latIndex = grid.latitudes.indexOfFirst { it >= lat }.takeIf { it >= 0 } ?: return null
        val lonIndex = grid.longitudes.indexOfFirst { it >= lon }.takeIf { it >= 0 } ?: return null
        return grid.depths.getOrNull(latIndex)?.getOrNull(lonIndex)
    }
}
