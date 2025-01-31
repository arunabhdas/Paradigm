package app.paradigmatic.paradigmaticapp.ui.composemaps.model

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskStop
import android.location.Location
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions(MapStyle.json),
        isMyLocationEnabled = true,
        isIndoorEnabled = true,
        isTrafficEnabled = true
    ),
    val taskStops: List<TaskStop> = emptyList(),
    val isFalloutMap: Boolean = false,
    val lastKnownLocation: Location? = null
)
