package com.fishing.conditions.domain

import com.fishing.conditions.data.habitat.HabitatSuitabilityEngine
import com.fishing.conditions.data.models.FishingSuitability
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species

class FishingSuitabilityCalculator(
    private val habitatEngine: HabitatSuitabilityEngine
) {

    suspend fun calculate(
        conditions: MarineConditions,
        species: Species,
        latitude: Double,
        longitude: Double
    ): FishingSuitability {
        // ── FSI: rich Gaussian-weighted environmental score (0–100) ──────────
        // This is the primary score — uses real API data for temp (°F), wind,
        // waves, moon, tide, and solunar. Always available.
        val fsiResult = conditions.getFishingSuitability(species)

        // ── HSI: habitat/structure/depth score (0–1) ─────────────────────────
        // Adds bathymetry, depth match, and current context when available.
        // Falls back gracefully if the engine throws.
        val hsiResult = try {
            habitatEngine.calculateHSI(latitude, longitude, conditions, species)
        } catch (_: Exception) {
            null
        }

        // ── Option C: Blend FSI 60% + HSI 40% ────────────────────────────────
        // If HSI is unavailable, FSI alone drives the score (no penalty).
        // Both are normalised to 0-100 before blending.
        val finalScore: Float = if (hsiResult != null) {
            val hsiAs100 = (hsiResult.hsi * 100.0).toFloat().coerceIn(0f, 100f)
            (fsiResult.score * 0.60f + hsiAs100 * 0.40f).coerceIn(0f, 100f)
        } else {
            fsiResult.score
        }

        val finalRating = when {
            finalScore >= 70f -> FishingSuitability.Rating.EXCELLENT
            finalScore >= 50f -> FishingSuitability.Rating.GOOD
            finalScore >= 30f -> FishingSuitability.Rating.FAIR
            else -> FishingSuitability.Rating.POOR
        }

        // Expose HSI sub-components so the UI explanation dialog can show them
        val hsiComponents = hsiResult?.components?.mapValues { it.value.toFloat() }

        return FishingSuitability(
            score = finalScore,
            rating = finalRating,
            factors = fsiResult.factors,   // the named environmental factors (°F-based)
            hsi = hsiResult?.hsi?.toFloat(),
            hsiComponents = hsiComponents
        )
    }
}
