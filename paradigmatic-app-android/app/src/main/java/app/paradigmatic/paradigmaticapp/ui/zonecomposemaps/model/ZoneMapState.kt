package app.paradigmatic.paradigmaticapp.ui.zonecomposemaps.model

import app.paradigmatic.paradigmaticapp.ui.zonecomposemaps.clusters.ZoneClusterItem
import android.location.Location

data class ZoneMapState(
    val lastKnownLocation: Location?,
    val clusterItems: List<ZoneClusterItem>,
)