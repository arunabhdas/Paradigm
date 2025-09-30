package app.paradigmatic.paradigmaticapp.presentation.repo

import app.paradigmatic.paradigmaticapp.presentation.data.model.Location
import app.paradigmatic.paradigmaticapp.presentation.service.GeocodingApiService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class GeocodingRepository @Inject constructor(
    private val geocodingApiService: GeocodingApiService
) {
    suspend fun fetchLatLngFromAddress(
        address: String,
        apiKey: String
    ): Result<Location> {
        return try {
            val response = geocodingApiService.geocodeAddress(address, apiKey)
            if (response.isSuccessful && response.body() != null) {
                val location = response.body()!!.results.firstOrNull()?.geometry?.location
                if (location != null) {
                    Result.success(location)
                } else {
                    Result.failure(Exception("No location found for the address"))
                }
            } else {
                Result.failure(Exception("API call failed: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Geocoding request failed", e))
        }
    }

    suspend fun fetchAddressFromLatlng(
        latlng: String,
        apiKey: String
    ): Result<String> {
        return try {
            val response = geocodingApiService.reverseGeocodeLatLng(latlng, apiKey)
            if (response.isSuccessful && response.body() != null) {
                val address = response.body()?.results?.firstOrNull()?.formattedAddress
                    if (address != null) {
                        Result.success(address)
                    } else {
                        Result.failure(Exception("No location found for the address"))
                    }
            } else {
                Result.failure(Exception("API call failed: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Geocoding request failed", e))
        }
    }
}