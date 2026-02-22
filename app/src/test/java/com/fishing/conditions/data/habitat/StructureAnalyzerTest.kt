package com.fishing.conditions.data.habitat

import com.fishing.conditions.data.habitat.BathymetryRepository.BathymetryGrid
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("StructureAnalyzer Tests")
class StructureAnalyzerTest {

    private val analyzer = StructureAnalyzer()

    @Test
    @DisplayName("Should analyze structure and identify edges")
    fun `analyzeStructure identifies structure edges`() {
        // Given
        val grid = createTestGrid()

        // When
        val structureData = analyzer.analyzeStructure(grid)

        // Then
        assertThat(structureData.slopeGrid).hasSize(grid.depths.size)
        assertThat(structureData.slopeGrid[0]).hasSize(grid.depths[0].size)
        assertThat(structureData.maxSlope).isAtLeast(structureData.minSlope)
        assertThat(structureData.structureEdges).isNotEmpty()
    }

    @Test
    @DisplayName("Should normalize slope values")
    fun `normalizeSlope returns value between 0 and 1`() {
        // Given
        val slope = 5.0
        val minSlope = 0.0
        val maxSlope = 10.0

        // When
        val normalized = analyzer.normalizeSlope(slope, minSlope, maxSlope)

        // Then
        assertThat(normalized).isAtLeast(0.0)
        assertThat(normalized).isAtMost(1.0)
        assertThat(normalized).isEqualTo(0.5)
    }

    @Test
    @DisplayName("Should calculate distance to nearest structure")
    fun `calculateDistanceToStructure returns valid distance`() {
        // Given
        val grid = createTestGrid()
        val structureData = analyzer.analyzeStructure(grid)
        val latIndex = 5
        val lonIndex = 5

        // When
        val distance = analyzer.calculateDistanceToStructure(latIndex, lonIndex, structureData.structureEdges)

        // Then
        assertThat(distance).isAtLeast(0.0)
    }

    private fun createTestGrid(): BathymetryGrid {
        val latitudes = (0..10).map { 40.0 + it * 0.01 }
        val longitudes = (0..10).map { -74.0 + it * 0.01 }
        val depths = latitudes.map { lat ->
            longitudes.map { lon ->
                // Create some structure variation
                20.0 + Math.sin(lat * 10) * 5.0 + Math.cos(lon * 10) * 3.0
            }
        }
        return BathymetryGrid(latitudes, longitudes, depths)
    }
}
