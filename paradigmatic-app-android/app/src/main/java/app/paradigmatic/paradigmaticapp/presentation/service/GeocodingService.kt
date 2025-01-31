package app.paradigmatic.paradigmaticapp.presentation.service

import app.paradigmatic.paradigmaticapp.presentation.data.model.GeocodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface GeocodingApiService {
    @GET("maps/api/geocode/json")
    suspend fun geocodeAddress(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Response<GeocodeResponse> // Define GeocodeResponse based on the expected JSON structure

    @GET("maps/api/geocode/json")
    suspend fun reverseGeocodeLatLng(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): Response<GeocodeResponse> // GeocodeResponse is a data class representing the API response
}

/* TODO-FIXME-CLEANUP
class GeocodingService @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun geocodeAddress(address: String): Pair<Double, Double>? {
        val apiKey = getGoogleMapsApiKey()
        val response = apiService.geocodeAddress(address, apiKey)
        if (response.isSuccessful) {
            val body = response.body()
            // Assuming GeocodeResponse and its parsing is correctly implemented
            val location = body?.results?.firstOrNull()?.geometry?.location
            return Pair(location?.lat ?: 0.0, location?.lng ?: 0.0)
        }
        return null
    }

    private fun getGoogleMapsApiKey(): String {
        val metaData = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        ).metaData
        return metaData.getString("com.google.android.geo.API_KEY") ?: ""
    }
}

*/