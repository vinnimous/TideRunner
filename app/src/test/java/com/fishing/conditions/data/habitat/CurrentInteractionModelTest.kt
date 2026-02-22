package com.fishing.conditions.data.habitat

import com.fishing.conditions.data.models.Species
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("CurrentInteractionModel Tests")
class CurrentInteractionModelTest {

    private val model = CurrentInteractionModel()

    @Test
    @DisplayName("Should calculate current interaction score")
    fun `calculateCurrentInteraction returns valid score`() {
        // Given
        val grid = createTestGrid()
        val structureData = StructureAnalyzer().analyzeStructure(grid)
        val current = CurrentInteractionModel.CurrentVector(1.5, 90.0)
        val currentPrefs = Species.CurrentRange(0.5,3.0)

        // When
        val result = model.calculateCurrentInteraction(5, 5, current, grid, structureData, currentPrefs)

        // Then
        assertThat(result.score).isAtLeast(0.0)
        assertThat(result.score).isAtMost(1.0)
        assertThat(result.speedMatch).isAtLeast(0.0)
        assertThat(result.speedMatch).isAtMost(1.0)
    }

    @Test
    @DisplayName("Should score higher for optimal current speed")
    fun `optimal current speed gets high score`() {
        // Given
        val grid = createTestGrid()
        val structureData = StructureAnalyzer().analyzeStructure(grid)
        val optimalCurrent = CurrentInteractionModel.CurrentVector(1.5, 90.0) // Within 0.5-3.0 range
        val currentPrefs = Species.CurrentRange(0.5, 3.0)

        // When
        val result = model.calculateCurrentInteraction(5, 5, optimalCurrent, grid, structureData, currentPrefs)

        // Then
        assertThat(result.speedMatch).isEqualTo(1.0)
    }

    @Test
    @DisplayName("Should score lower for poor current speed")
    fun `poor current speed gets low score`() {
        // Given
        val grid = createTestGrid()
        val structureData = StructureAnalyzer().analyzeStructure(grid)
        val poorCurrent = CurrentInteractionModel.CurrentVector(5.0, 90.0) // Above max 3.0
        val currentPrefs = Species.CurrentRange(0.5, 3.0)

        // When
        val result = model.calculateCurrentInteraction(5, 5, poorCurrent, grid, structureData, currentPrefs)

        // Then
        assertThat(result.speedMatch).isLessThan(1.0)
    }

    private fun createTestGrid(): BathymetryRepository.BathymetryGrid {
        val latitudes = (0..10).map { 40.0 + it * 0.01 }
        val longitudes = (0..10).map { -74.0 + it * 0.01 }
        val depths = latitudes.map { lat ->
            longitudes.map { lon ->
                20.0 + Math.sin(lat * 10) * 5.0
            }
        }
        return BathymetryRepository.BathymetryGrid(latitudes, longitudes, depths)
    }
}
