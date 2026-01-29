package com.fishing.conditions.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fishing.conditions.data.models.FishingSuitability
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species
import com.fishing.conditions.data.repository.MarineDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: MarineDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val _selectedSpecies = MutableStateFlow<Species?>(null)
    val selectedSpecies: StateFlow<Species?> = _selectedSpecies.asStateFlow()

    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation.asStateFlow()

    private val _marineConditions = MutableStateFlow<MarineConditions?>(null)
    val marineConditions: StateFlow<MarineConditions?> = _marineConditions.asStateFlow()

    private val _fishingSuitability = MutableStateFlow<FishingSuitability?>(null)
    val fishingSuitability: StateFlow<FishingSuitability?> = _fishingSuitability.asStateFlow()

    fun selectSpecies(species: Species) {
        _selectedSpecies.value = species
        // Recalculate suitability if we have conditions
        _marineConditions.value?.let { conditions ->
            _fishingSuitability.value = conditions.getFishingSuitability(species)
        }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        _currentLocation.value = Pair(latitude, longitude)
        loadMarineConditions(latitude, longitude)
    }

    private fun loadMarineConditions(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                _uiState.value = MapUiState.Loading
                val conditions = repository.getMarineConditions(latitude, longitude)

                if (conditions != null) {
                    _marineConditions.value = conditions

                    // Calculate suitability if species is selected
                    _selectedSpecies.value?.let { species ->
                        _fishingSuitability.value = conditions.getFishingSuitability(species)
                    }

                    _uiState.value = MapUiState.Success(conditions)
                } else {
                    _uiState.value = MapUiState.Error("No data available for this location")
                }
            } catch (e: Exception) {
                _uiState.value = MapUiState.Error(e.message ?: "Failed to load marine conditions")
            }
        }
    }

    fun refreshData() {
        _currentLocation.value?.let { (lat, lon) ->
            loadMarineConditions(lat, lon)
        }
    }

    sealed class MapUiState {
        object Loading : MapUiState()
        data class Success(val conditions: MarineConditions) : MapUiState()
        data class Error(val message: String) : MapUiState()
    }
}
