package com.fishing.conditions.data.habitat

import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species
import javax.inject.Singleton

@Singleton
class HabitatSuitabilityEngine(
    private val bathymetryRepository: BathymetryRepository,
    private val structureAnalyzer: StructureAnalyzer,
    private val currentInteractionModel: CurrentInteractionModel,
    private val temperatureGradientAnalyzer: TemperatureGradientAnalyzer
) {

    data class HabitatSuitabilityIndex(
        val hsi: Double,
        val components: Map<String, Double>,
        val rating: Rating
    ) {
        enum class Rating {
            POOR, FAIR, GOOD, EXCELLENT
        }
    }

    suspend fun calculateHSI(
        latitude: Double,
        longitude: Double,
        conditions: MarineConditions,
        species: Species
    ): HabitatSuitabilityIndex {
        // Get bathymetry grid for the area
        val grid = bathymetryRepository.getBathymetryGrid(
            latitude - 0.1, latitude + 0.1,
            longitude - 0.1, longitude + 0.1
        )

        // Analyze structure
        val structureData = structureAnalyzer.analyzeStructure(grid)

        // Find grid indices for the location
        val latIndex = grid.latitudes.indexOfFirst { it >= latitude }.takeIf { it >= 0 } ?: 0
        val lonIndex = grid.longitudes.indexOfFirst { it >= longitude }.takeIf { it >= 0 } ?: 0

        // Component scores
        val structureScore = calculateStructureScore(latIndex, lonIndex, structureData, species)
        val temperatureGradientScore = calculateTemperatureGradientScore(conditions, species)
        val currentInteractionScore = calculateCurrentInteractionScore(
            latIndex, lonIndex, conditions, grid, structureData, species
        )
        val depthMatchScore = calculateDepthMatchScore(latitude, longitude, grid, species)
        val environmentalScore = calculateEnvironmentalScore(conditions, species)
        val solunarScore = calculateSolunarScore(conditions, species)

        // Species-dependent weights
        val weights = getSpeciesWeights(species)

        val hsi = weights.structure * structureScore +
                  weights.temperature * temperatureGradientScore +
                  weights.current * currentInteractionScore +
                  weights.depth * depthMatchScore +
                  weights.environmental * environmentalScore +
                  weights.solunar * solunarScore

        val components = mapOf(
            "structure" to structureScore,
            "temperatureGradient" to temperatureGradientScore,
            "currentInteraction" to currentInteractionScore,
            "depthMatch" to depthMatchScore,
            "environmental" to environmentalScore,
            "solunar" to solunarScore
        )

        val rating = when {
            hsi >= 0.7 -> HabitatSuitabilityIndex.Rating.EXCELLENT // Reduced from 0.8
            hsi >= 0.5 -> HabitatSuitabilityIndex.Rating.GOOD // Reduced from 0.6
            hsi >= 0.3 -> HabitatSuitabilityIndex.Rating.FAIR // Reduced from 0.4
            else -> HabitatSuitabilityIndex.Rating.POOR
        }

        return HabitatSuitabilityIndex(hsi, components, rating)
    }

    private fun calculateStructureScore(
        latIndex: Int,
        lonIndex: Int,
        structureData: StructureAnalyzer.StructureData,
        species: Species
    ): Double {
        val slope = structureData.slopeGrid.getOrNull(latIndex)?.getOrNull(lonIndex) ?: 0.0
        val normalizedSlope = structureAnalyzer.normalizeSlope(
            slope,
            structureData.minSlope,
            structureData.maxSlope
        )

        val distanceToStructure = structureAnalyzer.calculateDistanceToStructure(
            latIndex, lonIndex, structureData.structureEdges
        )

        val slopeScore = when {
            slope in species.habitatPreferences.preferredSlope.min..species.habitatPreferences.preferredSlope.max -> 0.7 // Reduced from 1.0
            slope < species.habitatPreferences.preferredSlope.min -> (slope / species.habitatPreferences.preferredSlope.min) * 0.5
            else -> (species.habitatPreferences.preferredSlope.max / slope) * 0.5
        }.coerceIn(0.0, 0.7)

        val proximityScore = if (distanceToStructure <= 10.0) { // Within 10 grid cells
            species.habitatPreferences.structureProximityWeight * 0.6 // Reduced multiplier
        } else {
            (1.0 - species.habitatPreferences.structureProximityWeight) * 0.4 // Reduced base
        }

        return (slopeScore + proximityScore) / 2.0
    }

    private fun calculateTemperatureGradientScore(
        conditions: MarineConditions,
        species: Species
    ): Double {
        // Use real water temperature (°F) vs species preferred range (also °F now)
        val tempF = conditions.waterTemperature ?: return species.habitatPreferences.temperatureBreakSensitivity * 0.5
        val inRange = tempF >= species.preferredWaterTemp.min && tempF <= species.preferredWaterTemp.max
        val sigma = (species.preferredWaterTemp.max - species.preferredWaterTemp.min) / 3.0
        val gaussianScore = gaussianWeight(tempF, species.preferredWaterTemp.optimal, sigma)
        // Weight by the species' temperature break sensitivity
        return gaussianScore * species.habitatPreferences.temperatureBreakSensitivity
            .coerceIn(0.0, 1.0)
    }

    private fun calculateCurrentInteractionScore(
        latIndex: Int,
        lonIndex: Int,
        conditions: MarineConditions,
        grid: BathymetryRepository.BathymetryGrid,
        structureData: StructureAnalyzer.StructureData,
        species: Species
    ): Double {
        // Use real current speed from conditions; fall back to species preferred midpoint
        val currentSpeedKnots = conditions.currentSpeed ?: run {
            val pref = species.preferredCurrentSpeed ?: species.habitatPreferences.preferredCurrentSpeed
            pref.optimal
        }
        // Direction unknown without vector data — use 90° as neutral eastward assumption
        val current = CurrentInteractionModel.CurrentVector(currentSpeedKnots, 90.0)

        val interaction = currentInteractionModel.calculateCurrentInteraction(
            latIndex, lonIndex, current, grid, structureData, species.habitatPreferences.preferredCurrentSpeed
        )

        return interaction.score
    }

    private fun calculateDepthMatchScore(
        latitude: Double,
        longitude: Double,
        grid: BathymetryRepository.BathymetryGrid,
        species: Species
    ): Double {
        val depth = bathymetryRepository.getDepthAt(latitude, longitude, grid) ?: return 0.0

        return when {
            depth in species.preferredDepth.min..species.preferredDepth.max -> 0.8 // Reduced from 1.0
            depth < species.preferredDepth.min -> (depth / species.preferredDepth.min) * 0.4
            else -> (species.preferredDepth.max / depth) * 0.4
        }.coerceIn(0.0, 0.8)
    }

    private fun calculateEnvironmentalScore(
        conditions: MarineConditions,
        species: Species
    ): Double {
        // Temperature: conditions.waterTemperature is °F; species.preferredWaterTemp is now also °F
        val tempScore = conditions.waterTemperature?.let { tempF ->
            val sigma = (species.preferredWaterTemp.max - species.preferredWaterTemp.min) / 3.0
            when {
                tempF in species.preferredWaterTemp.min..species.preferredWaterTemp.max ->
                    gaussianWeight(tempF, species.preferredWaterTemp.optimal, sigma)
                else -> 0.2
            }
        } ?: 0.3

        val windScore = conditions.windSpeed?.let { wind ->
            when {
                wind <= species.preferredWindSpeed.max -> {
                    val sigma = (species.preferredWindSpeed.max - species.preferredWindSpeed.min) / 3.0
                    gaussianWeight(wind, (species.preferredWindSpeed.min + species.preferredWindSpeed.max) / 2.0, sigma)
                }
                else -> 0.1
            }
        } ?: 0.3

        return (tempScore + windScore) / 2.0
    }

    private fun calculateSolunarScore(
        conditions: MarineConditions,
        species: Species
    ): Double {
        // Use real solunar score from API (1–4: poor/fair/good/excellent); Gaussian peak at 4
        val solunar = conditions.solunarScore?.toDouble() ?: return 0.4
        return gaussianWeight(solunar, 4.0, 1.0).coerceIn(0.1, 1.0)
    }

    private fun gaussianWeight(x: Double, mean: Double, sigma: Double): Double {
        if (sigma == 0.0) return if (x == mean) 1.0 else 0.0
        return kotlin.math.exp(-((x - mean) * (x - mean)) / (2 * sigma * sigma))
    }

    private data class Weights(
        val structure: Double,
        val temperature: Double,
        val current: Double,
        val depth: Double,
        val environmental: Double,
        val solunar: Double
    )

    private fun getSpeciesWeights(species: Species): Weights {
        return when (species.category) {
            Species.FishCategory.SALTWATER_INSHORE -> Weights(
                structure = 0.3, temperature = 0.1, current = 0.2,
                depth = 0.2, environmental = 0.1, solunar = 0.1
            )
            Species.FishCategory.SALTWATER_OFFSHORE -> Weights(
                structure = 0.2, temperature = 0.2, current = 0.3,
                depth = 0.1, environmental = 0.1, solunar = 0.1
            )
            else -> Weights(
                structure = 0.2, temperature = 0.1, current = 0.1,
                depth = 0.3, environmental = 0.2, solunar = 0.1
            )
        }
    }
}
