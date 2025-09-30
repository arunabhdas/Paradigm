package app.paradigmatic.paradigmaticapp.presentation.service

import app.paradigmatic.paradigmaticapp.presentation.common.Constants
import app.paradigmatic.paradigmaticapp.presentation.data.model.FetchRoutesResponseItem
import app.paradigmatic.paradigmaticapp.presentation.data.model.LoginRequest
import app.paradigmatic.paradigmaticapp.presentation.data.model.LoginResponse
import app.paradigmatic.paradigmaticapp.presentation.data.model.RegisterRequest
import app.paradigmatic.paradigmaticapp.presentation.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST(Constants.Api.LOGIN_URL)
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST(Constants.Api.REGISTRATION_URL)
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @GET("api/routes")
    suspend fun fetchRoutes(
        @Header("Authorization") authorization: String,
        @Query("route_assigned_to_user")
        userId: Int
    ): Response<List<FetchRoutesResponseItem>>
}