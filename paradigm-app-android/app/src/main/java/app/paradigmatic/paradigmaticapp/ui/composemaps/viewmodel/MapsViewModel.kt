package app.paradigmatic.paradigmaticapp.ui.composemaps.viewmodel

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskStop
import app.paradigmatic.paradigmaticapp.domain.repository.TaskStopsRepository
import app.paradigmatic.paradigmaticapp.presentation.repo.PlacesRepository
import app.paradigmatic.paradigmaticapp.ui.composemaps.model.MapEvent
import app.paradigmatic.paradigmaticapp.ui.composemaps.model.MapState
import app.paradigmatic.paradigmaticapp.ui.composemaps.model.MapStyle
import android.app.Application
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject@HiltViewModel
class MapsViewModel @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val chargingStationsRepository: PlacesRepository,
    private val taskStopRepository: TaskStopsRepository
): AndroidViewModel(Application())  {
    var state by mutableStateOf(MapState())

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation

    init {
        viewModelScope.launch {
            taskStopRepository.getTaskStops().collectLatest { stops ->
                state = state.copy(
                    taskStops = stops
                )
            }
        }
        fetchLastKnownLocation()
    }

    private fun fetchLastKnownLocation() {
        viewModelScope.launch {
            try {
                val cancellationTokenSource = com.google.android.gms.tasks.CancellationTokenSource()
                fusedLocationProviderClient.getCurrentLocation(
                    com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        _userLocation.value = location
                    }
                }
            } catch (e: SecurityException) {
                // Handle the security exception
            }
        }
    }

    fun onEvent(event: MapEvent, taskStop: TaskStop?) {
        when(event) {
            is MapEvent.ToggleFalloutMap -> {
                state = state.copy(
                    properties = state.properties.copy(
                        mapStyleOptions = if(state.isFalloutMap) {
                            null
                        } else {
                            MapStyleOptions(MapStyle.json)
                        },
                    ),
                    isFalloutMap = !state.isFalloutMap
                )
            }

            is MapEvent.OnMapLongClick -> {
                viewModelScope.launch {
                    taskStopRepository.insertTaskStop(TaskStop(
                            title = taskStop?.title ?: "",
                            description = taskStop?.description ?: "",
                            formattedAddressOrigin = taskStop?.formattedAddressOrigin ?: "",
                            latOrigin = event.latLng.latitude,
                            lngOrigin = event.latLng.longitude,
                            formattedAddressDestination = taskStop?.formattedAddressDestination ?: "",
                            latDestination =  event.latLng.latitude,
                            lngDestination = event.latLng.longitude,
                        )
                    )
                }
            }

            is MapEvent.OnInfoWindowLongClick -> {
                viewModelScope.launch {
                    Timber.d("InfoWindowLongClick")
                    taskStopRepository.deleteTaskStop(event.taskStop)
                }
            }

            else -> {
                Timber.d("Other map event handler")
            }
        }
    }
}

