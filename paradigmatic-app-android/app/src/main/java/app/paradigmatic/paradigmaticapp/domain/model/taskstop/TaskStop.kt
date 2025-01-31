package app.paradigmatic.paradigmaticapp.domain.model.taskstop


data class TaskStop (
    val title: String,
    val description: String,
    val formattedAddressOrigin: String,
    val latOrigin: Double,
    val lngOrigin: Double,
    var formattedAddressDestination: String,
    val latDestination: Double,
    val lngDestination: Double,
    val id: Int? = null
)