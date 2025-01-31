package app.paradigmatic.paradigmaticapp.presentation.service

import app.paradigmatic.paradigmaticapp.ui.chargingstations.model.StationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("maps/api/place/nearbysearch/json?key=")
    suspend fun fetchPlaces(
        @Query("radius")
        radius: Int,
        @Query("location")
        locationString: String,
        @Query("type")
        type: String

    ): Response<StationsResponse>

}