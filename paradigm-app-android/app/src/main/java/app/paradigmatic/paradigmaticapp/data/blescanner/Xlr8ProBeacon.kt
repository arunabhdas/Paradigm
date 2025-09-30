package app.paradigmatic.paradigmaticapp.data.blescanner

/*
data class DefaultBeacon (
    val id: Int,
    val uuid: String?,
    val major: Int?,
    val minor: Int?,

)
*/
data class Xlr8ProBeacon(
    val id: Int,
    val name: String,
    val description: String,
    val uuid: String,
)