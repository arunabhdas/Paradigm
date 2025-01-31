package app.paradigmatic.paradigmaticapp.presentation.repo

import app.paradigmatic.paradigmaticapp.presentation.service.PlacesApiService
import app.paradigmatic.paradigmaticapp.ui.chargingstations.model.ChargingStation
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.scopes.ViewModelScoped
import timber.log.Timber
import javax.inject.Inject

@ViewModelScoped
class PlacesRepository @Inject constructor(
    private val chargingStationsApiService: PlacesApiService,
    private val preferencesDataStore: DataStore<Preferences>
) {
    suspend fun fetchPlaces(
        radius: Int,
        locationString: String,
        type: String
    ): Result<List<ChargingStation>> {
        return try {

            val response = chargingStationsApiService.fetchPlaces(
                radius = radius,
                locationString = locationString,
                type = type
            )
            if (response.isSuccessful && response.body() != null) {
                val chargingStations = response.body()!!.chargingStations
                Timber.d("--------------" + chargingStations.size)
                Result.success(chargingStations)
            } else {
                Timber.e("RoutesRepository Error fetching routes: ${response.errorBody()?.string()}")
                Result.failure(Exception("Error"))
            }
        } catch (e: Exception) {
            Timber.e("ChargingStationsRepository Exception fetching chargingStations: $e" )
            Result.failure(e)
        }
    }

}