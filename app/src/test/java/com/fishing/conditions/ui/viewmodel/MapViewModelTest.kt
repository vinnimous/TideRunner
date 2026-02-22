package com.fishing.conditions.ui.viewmodel
import app.cash.turbine.test
import com.fishing.conditions.data.models.FishingSuitability
import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.repository.MarineDataRepository
import com.fishing.conditions.domain.FishingSuitabilityCalculator
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
    private lateinit var suitabilityCalculator: FishingSuitabilityCalculator
    private val testDispatcher = StandardTestDispatcher()
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        suitabilityCalculator = mockk()
        viewModel = MapViewModel(repository, suitabilityCalculator)
    }
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    @DisplayName("Should update selected species")
    fun selectSpeciesUpdatesSpeciesState() = runTest {
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!
        viewModel.selectSpecies(redfish)
        viewModel.selectedSpecies.test {
            assertThat(awaitItem()).isEqualTo(redfish)
        }
    }
    @Test
    @DisplayName("Should load marine conditions on location update")
    fun updateLocationFetchesMarineConditions() = runTest {
        val testConditions = createTestMarineConditions()
        coEvery { repository.getMarineConditions(any(), any()) } returns testConditions
        viewModel.updateLocation(37.7749, -122.4194)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.marineConditions.test {
            assertThat(awaitItem()).isEqualTo(testConditions)
        }
    }
    @Test
    @DisplayName("Should show loading state during data fetch")
    fun updateLocationShowsLoadingState() = runTest {
        val testConditions = createTestMarineConditions()
        coEvery { repository.getMarineConditions(any(), any()) } returns testConditions
        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(MapViewModel.MapUiState.Loading::class.java)
            viewModel.updateLocation(37.7749, -122.4194)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(MapViewModel.MapUiState.Success::class.java)
        }
    }
    @Test
    @DisplayName("Should calculate suitability when species is selected")
    fun updateLocationCalculatesSuitabilityForSelectedSpecies() = runTest {
        val wahoo = FishSpeciesDatabase.getSpeciesById("wahoo")!!
        val testConditions = createTestMarineConditions(waterTemp = 78.8)
        val mockSuitability = FishingSuitability(
            score = 75f,
            rating = FishingSuitability.Rating.EXCELLENT,
            factors = mapOf("Water Temperature" to 25f)
        )
        coEvery { repository.getMarineConditions(any(), any()) } returns testConditions
        coEvery { suitabilityCalculator.calculate(any(), any(), any(), any()) } returns mockSuitability
        viewModel.fishingSuitability.test {
            assertThat(awaitItem()).isNull()
            viewModel.selectSpecies(wahoo)
            viewModel.updateLocation(37.7749, -122.4194)
            testDispatcher.scheduler.advanceUntilIdle()
            val suitability = awaitItem()
            assertThat(suitability).isNotNull()
            assertThat(suitability!!.score).isGreaterThan(0f)
        }
    }
    @Test
    @DisplayName("Should show error state on repository failure")
    fun updateLocationShowsErrorOnFailure() = runTest {
        coEvery { repository.getMarineConditions(any(), any()) } returns null
        viewModel.uiState.test {
            skipItems(1)
            viewModel.updateLocation(37.7749, -122.4194)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(MapViewModel.MapUiState.Error::class.java)
        }
    }
    private fun createTestMarineConditions(waterTemp: Double = 21.0): MarineConditions {
        return MarineConditions(
            latitude = 37.7749, longitude = -122.4194,
            timestamp = System.currentTimeMillis(),
            waterTemperature = waterTemp, waveHeight = 0.5,
            waveDirection = null, wavePeriod = null,
            currentSpeed = null, currentDirection = null,
            windSpeed = 10.0, windDirection = null, windGust = null,
            airTemperature = 20.0, pressure = null, humidity = null,
            cloudCover = null, visibility = null, precipitation = null,
            tideHeight = null, nextHighTide = null, nextLowTide = null,
            currentTidePhase = null, sunrise = null, sunset = null,
            moonrise = null, moonset = null, moonPhase = null,
            moonIllumination = null, majorPeriods = null, minorPeriods = null,
            solunarScore = null, dataSource = "Test", forecastHours = 0
        )
    }
}
