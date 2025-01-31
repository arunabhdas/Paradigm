package app.paradigmatic.paradigmaticapp.presentation.data.model

data class LoginResponseSupabase (
    val accessToken: String,
    val refreshToken: String
)