package com.fishing.conditions.ui.viewmodel

import app.cash.turbine.test
import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.repository.MarineDataRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("MapViewModel Tests")
class MapViewModelTest {

    private lateinit var viewModel: MapViewModel
    private lateinit var repository: MarineDataRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = MapViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("Should update selected species")
    fun `selectSpecies updates species state`() = runTest {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!

        // When
        viewModel.selectSpecies(redfish)

        // Then
        viewModel.selectedSpecies.test {
            assertThat(awaitItem()).isEqualTo(redfish)
        }
    }

    @Test
    @DisplayName("Should load marine conditions on location update")
    fun `updateLocation fetches marine conditions`() = runTest {
        // Given
        val testConditions = createTestMarineConditions()
        coEvery {
            repository.getMarineConditions(any(), any())
        } returns testConditions

        // When
        viewModel.updateLocation(37.7749, -122.4194)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.marineConditions.test {
            assertThat(awaitItem()).isEqualTo(testConditions)
        }
    }

    @Test
    @DisplayName("Should show loading state during data fetch")
    fun `updateLocation shows loading state`() = runTest {
        // Given
        val testConditions = createTestMarineConditions()
        coEvery {
            repository.getMarineConditions(any(), any())
        } returns testConditions

        // When/Then
        viewModel.uiState.test {
            // Initial state is Loading
            assertThat(awaitItem()).isInstanceOf(MapViewModel.MapUiState.Loading::class.java)

            viewModel.updateLocation(37.7749, -122.4194)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should transition to Success
            assertThat(awaitItem()).isInstanceOf(MapViewModel.MapUiState.Success::class.java)
        }
    }

    @Test
    @DisplayName("Should calculate suitability when species is selected")
    fun `updateLocation calculates suitability for selected species`() = runTest {
        // Given
        val wahoo = FishSpeciesDatabase.getSpeciesById("wahoo")!!
        val testConditions = createTestMarineConditions(waterTemp = 26.0) // Optimal for wahoo

        coEvery {
            repository.getMarineConditions(any(), any())
        } returns testConditions

        // When
        viewModel.selectSpecies(wahoo)
        viewModel.updateLocation(37.7749, -122.4194)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.fishingSuitability.test {
            val suitability = awaitItem()
            assertThat(suitability).isNotNull()
            assertThat(suitability?.score).isGreaterThan(0f)
        }
    }

    @Test
    @DisplayName("Should show error state on repository failure")
    fun `updateLocation shows error on failure`() = runTest {
        // Given
        coEvery {
            repository.getMarineConditions(any(), any())
        } returns null

        // When/Then
        viewModel.uiState.test {
            skipItems(1) // Skip initial Loading state

            viewModel.updateLocation(37.7749, -122.4194)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = awaitItem()
            assertThat(state).isInstanceOf(MapViewModel.MapUiState.Error::class.java)
        }
    }

    private fun createTestMarineConditions(
        waterTemp: Double = 21.0
    ): MarineConditions {
        return MarineConditions(
            latitude = 37.7749,
            longitude = -122.4194,
            timestamp = System.currentTimeMillis(),
            waterTemperature = waterTemp,
            waveHeight = 0.5,
            waveDirection = null,
            wavePeriod = null,
            currentSpeed = null,
            currentDirection = null,
            windSpeed = 10.0,
            windDirection = null,
            windGust = null,
            airTemperature = 20.0,
            pressure = null,
            humidity = null,
            cloudCover = null,
            visibility = null,
            precipitation = null,
            tideHeight = null,
            nextHighTide = null,
            nextLowTide = null,
            currentTidePhase = null,
            sunrise = null,
            sunset = null,
            moonrise = null,
            moonset = null,
            moonPhase = null,
            moonIllumination = null,
            majorPeriods = null,
            minorPeriods = null,
            solunarScore = null,
            dataSource = "Test",
            forecastHours = 0
        )
    }
}
