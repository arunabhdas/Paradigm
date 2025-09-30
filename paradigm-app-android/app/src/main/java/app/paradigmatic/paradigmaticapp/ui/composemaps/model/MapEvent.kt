package app.paradigmatic.paradigmaticapp.ui.composemaps.model

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskStop
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    object ToggleFalloutMap : MapEvent()
    data class onMapClick(val latLng: LatLng): MapEvent()
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
    data class OnInfoWindowLongClick(val taskStop: TaskStop): MapEvent()

}
