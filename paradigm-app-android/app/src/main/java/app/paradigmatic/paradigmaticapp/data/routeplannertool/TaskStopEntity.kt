package app.paradigmatic.paradigmaticapp.data.routeplannertool

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskStopEntity (
    val title: String,
    val description: String,
    val formattedAddressOrigin: String,
    val latOrigin: Double,
    val lngOrigin: Double,
    val formattedAddressDestination: String,
    val latDestination: Double,
    val lngDestination: Double,
    @PrimaryKey val id: Int? = null
)