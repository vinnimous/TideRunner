package com.fishing.conditions.data.habitat

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("BathymetryRepository Tests")
class BathymetryRepositoryTest {

    private val repository = BathymetryRepository()

    @Test
    @DisplayName("Should generate bathymetry grid for given bounds")
    fun `getBathymetryGrid generates valid grid`() {
        // Given
        val latMin = 40.0
        val latMax = 40.1
        val lonMin = -74.0
        val lonMax = -73.9

        // When
        val grid = repository.getBathymetryGrid(latMin, latMax, lonMin, lonMax)

        // Then
        assertThat(grid.latitudes).isNotEmpty()
        assertThat(grid.longitudes).isNotEmpty()
        assertThat(grid.depths).hasSize(grid.latitudes.size)
        assertThat(grid.depths[0]).hasSize(grid.longitudes.size)

        // Depths should be reasonable (5-125m)
        grid.depths.flatten().forEach { depth ->
            assertThat(depth).isAtLeast(5.0)
            assertThat(depth).isAtMost(125.0)
        }
    }

    @Test
    @DisplayName("Should return depth at specific location")
    fun `getDepthAt returns valid depth for location`() {
        // Given
        val grid = repository.getBathymetryGrid(40.0, 40.1, -74.0, -73.9)

        // When
        val depth = repository.getDepthAt(40.05, -73.95, grid)

        // Then
        assertThat(depth).isNotNull()
        assertThat(depth!!).isAtLeast(5.0)
        assertThat(depth).isAtMost(125.0)
    }

    @Test
    @DisplayName("Should return null for out of bounds location")
    fun `getDepthAt returns null for out of bounds`() {
        // Given
        val grid = repository.getBathymetryGrid(40.0, 40.1, -74.0, -73.9)

        // When
        val depth = repository.getDepthAt(50.0, -80.0, grid) // Way outside bounds

        // Then
        assertThat(depth).isNull()
    }
}
