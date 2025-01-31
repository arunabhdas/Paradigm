package app.paradigmatic.paradigmaticapp.data.gattble.model

data class PropzProBeaconResult(
    val proximity: Float,
    val timestamp: Float,
    val rssi: Float,
    val region: String,
    val accuracy: Float,
    val major: Float,
    val uuid: String,
    val minor: Float,
    val connectionState: ConnectionState
)
