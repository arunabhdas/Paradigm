package app.paradigmatic.paradigmaticapp.ui.routetaskmanager.data

data class Address(
    val addressLine1: String,
    val addressLine2: String?,
    val unitNumber: String?,
    val city: String,
    val stateProvince: String,
    val country: String,
    val postalCode: String,
    val lat: Double,
    val lng: Double,
    val specialInstructions: String
)
