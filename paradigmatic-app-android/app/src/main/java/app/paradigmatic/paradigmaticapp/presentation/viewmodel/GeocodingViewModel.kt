package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import app.paradigmatic.paradigmaticapp.BuildConfig
import app.paradigmatic.paradigmaticapp.presentation.repo.GeocodingRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class GeocodingViewModel @Inject constructor(
    private val geocodingRepository: GeocodingRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _geocodeUiState = MutableStateFlow<GeocodeUiState>(GeocodeUiState.Idle)
    val geocodeUiState: StateFlow<GeocodeUiState> = _geocodeUiState.asStateFlow()

    // You might want to hide or encrypt your API key rather than hard-coding it here.
    val googleMapsApiKey: String = BuildConfig.GOOGLE_MAPS_API_KEY

    fun fetchAddressFromLatLng(lat: Double, lng: Double) {
        _geocodeUiState.value = GeocodeUiState.Loading

        viewModelScope.launch {
            try {
                val latLngString = "$lat,$lng"
                val result = geocodingRepository.fetchAddressFromLatlng(latLngString, googleMapsApiKey)
                result.onSuccess { address ->
                    Timber.d("Geocoding success: $address")
                    _geocodeUiState.value = GeocodeUiState.Success(address)
                }.onFailure { throwable ->
                    Timber.e("Geocoding error", throwable)
                    _geocodeUiState.value = GeocodeUiState.Error(throwable.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                Timber.e("Geocoding exception", e)
                _geocodeUiState.value = GeocodeUiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class GeocodeUiState {
    object Loading : GeocodeUiState()
    data class Success(val address: String) : GeocodeUiState()
    data class Error(val exception: String) : GeocodeUiState()
    object Idle : GeocodeUiState()
}
