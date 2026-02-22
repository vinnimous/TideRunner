package com.fishing.conditions.data.habitat

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("TemperatureGradientAnalyzer Tests")
class TemperatureGradientAnalyzerTest {

    private val analyzer = TemperatureGradientAnalyzer()

    @Test
    @DisplayName("Should analyze temperature gradient and find breaks")
    fun `analyzeTemperatureGradient finds temperature breaks`() {
        // Given
        val grid = createTestTemperatureGrid()

        // When
        val breaks = analyzer.analyzeTemperatureGradient(grid)

        // Then
        assertThat(breaks).isNotEmpty()
        breaks.forEach { breakPoint ->
            assertThat(breakPoint.gradientMagnitude).isGreaterThan(0.5)
            assertThat(breakPoint.latIndex).isAtLeast(0)
            assertThat(breakPoint.lonIndex).isAtLeast(0)
        }
    }

    @Test
    @DisplayName("Should calculate temperature break score")
    fun `calculateTemperatureBreakScore returns valid score`() {
        // Given
        val grid = createTestTemperatureGrid()
        val breaks = analyzer.analyzeTemperatureGradient(grid)
        val sensitivity = 0.8

        // When
        val score = analyzer.calculateTemperatureBreakScore(2, 2, breaks, sensitivity)

        // Then
        assertThat(score).isAtLeast(0.0)
        assertThat(score).isAtMost(1.0)
    }

    @Test
    @DisplayName("Should return 0 score when no breaks nearby")
    fun `calculateTemperatureBreakScore returns 0 for distant breaks`() {
        // Given
        val breaks = listOf(
            TemperatureGradientAnalyzer.TemperatureBreak(10, 10, 2.0, 45.0) // Far away
        )
        val sensitivity = 0.8

        // When
        val score = analyzer.calculateTemperatureBreakScore(0, 0, breaks, sensitivity)

        // Then
        assertThat(score).isEqualTo(0.0)
    }

    private fun createTestTemperatureGrid(): TemperatureGradientAnalyzer.TemperatureGrid {
        val latitudes = (0..5).map { 40.0 + it * 0.01 }
        val longitudes = (0..5).map { -74.0 + it * 0.01 }
        val temperatures = latitudes.mapIndexed { i, _ ->
            longitudes.mapIndexed { j, _ ->
                // Create large temperature gradient: step change in the middle
                if (j < 3) 18.0 + i * 2.0 else 26.0 + i * 2.0
            }
        }
        return TemperatureGradientAnalyzer.TemperatureGrid(latitudes, longitudes, temperatures)
    }
}
