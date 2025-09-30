package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import app.paradigmatic.paradigmaticapp.BuildConfig
import app.paradigmatic.paradigmaticapp.presentation.data.model.FetchRoutesResponseItem
import app.paradigmatic.paradigmaticapp.presentation.repo.GeocodingRepository
import app.paradigmatic.paradigmaticapp.presentation.repo.RoutesRepository
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
open class RoutesViewModel @Inject constructor(
    private val routesRepository: RoutesRepository,
    private val geocodingRepository: GeocodingRepository,
) : AndroidViewModel(Application()) {
    private val _routeUiState = MutableStateFlow<FetchRoutesUiState>(FetchRoutesUiState.Idle)
    val routeUiState: StateFlow<FetchRoutesUiState> = _routeUiState.asStateFlow()

    val googleMapsApiKey: String = BuildConfig.GOOGLE_MAPS_API_KEY
    fun fetchRoutes(
        authorization: String,
        userId: Int

    ) {
        _routeUiState.value = FetchRoutesUiState.Loading

        viewModelScope.launch {
            try {
                val result = routesRepository.fetchRoutes(
                    authorization = authorization,
                    userId = userId
                )
                result.onSuccess { response ->
                    Timber.d("RoutesViewModel Fetch success: $response")
                    _routeUiState.value = FetchRoutesUiState.Success(response)
                }.onFailure { throwable ->
                    Timber.e("RoutesViewModel Fetch error ${throwable}")
                    _routeUiState.value = FetchRoutesUiState.Error(throwable.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                Timber.e("RoutesViewModel Fetch exception", e)
                _routeUiState.value = FetchRoutesUiState.Error(e.message ?: "An error occurred")
            }
        }
    }


    fun fetchRoutesWithStopsAndLatLng(
        authorization: String,
        userId: Int

    ) {
        _routeUiState.value = FetchRoutesUiState.Loading

        viewModelScope.launch {
            try {
                val result = routesRepository.fetchRoutes(
                    authorization = authorization,
                    userId = userId
                )
                result.onSuccess { routes ->
                    Timber.d("RoutesViewModel Fetch success: $routes[0]")
                    routes[0].stops.forEach {stop ->
                        // Assume destinationAddress is a string that can be geocoded
                        val stopDestinationAddressFull = stop.destinationAddressFull ?: ""
                        val geocodeResult = geocodingRepository.fetchLatLngFromAddress(
                            address = stopDestinationAddressFull,
                            apiKey = googleMapsApiKey
                        )

                        geocodeResult.onSuccess { location ->
                            stop.destinationAddressLat = location.lat
                            stop.destinationAddressLng = location.lng
                        }.onFailure {
                            // Handle geocoding failure, e.g., log or set default values
                            Timber.e("Geocoding failed for stop address: $stopDestinationAddressFull")
                        }
                    }
                    _routeUiState.value = FetchRoutesUiState.Success(routes)
                }.onFailure { throwable ->
                    Timber.e("RoutesViewModel Fetch error ${throwable}")
                    _routeUiState.value = FetchRoutesUiState.Error(throwable.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                Timber.e("RoutesViewModel Fetch exception", e)
                _routeUiState.value = FetchRoutesUiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}



sealed class FetchRoutesUiState {
    object Loading : FetchRoutesUiState()
    data class Success(val fetchRoutesResponseList: List<FetchRoutesResponseItem>) : FetchRoutesUiState()
    data class Error(val exception: String) : FetchRoutesUiState()
    object Idle : FetchRoutesUiState() // Added to represent the initial state
}
