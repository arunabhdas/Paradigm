package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import app.paradigmatic.paradigmaticapp.presentation.repo.PlacesRepository
import app.paradigmatic.paradigmaticapp.ui.chargingstations.model.ChargingStation
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class PlacesViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : AndroidViewModel(Application()) {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _chargingStations = MutableStateFlow<List<ChargingStation>>(emptyList())
    val chargingStations: StateFlow<List<ChargingStation>> = _chargingStations

    private val _gasStations = MutableStateFlow<List<ChargingStation>>(emptyList())
    val gasStations: StateFlow<List<ChargingStation>> = _gasStations

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchChargingStations(radius: Int, locationString: String) {
        viewModelScope.launch {
            _isLoading.value = true
            placesRepository.fetchPlaces(
                radius = radius,
                locationString = locationString,
                type = "charging_station"
            ).let { result ->
                if (result.isSuccess) {
                    _chargingStations.value = result.getOrDefault(emptyList())
                    _error.value = null
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Unknown error"
                }
                _isLoading.value = false
            }
            _isLoading.value = false
        }
    }

    fun fetchGasStations(radius: Int, locationString: String) {
        viewModelScope.launch {
            _isLoading.value = true
            placesRepository.fetchPlaces(
                radius = radius,
                locationString = locationString,
                type = "gas_station"
            ).let { result ->
                if (result.isSuccess) {
                    _gasStations.value = result.getOrDefault(emptyList())
                    _error.value = null
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Unknown error"
                }
                _isLoading.value = false
            }
            _isLoading.value = false
        }
    }

    companion object {
        const val DEFAULT_RADIUS = 50000
    }
}
