package app.paradigmatic.paradigmaticapp.presentation.common

object Constants {
    object Api {
        const val GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com/"
        const val GOOGLE_PLACES_API_KEY = ""
        // TODO-FIXME const val BASE_URL = "https://paradigmatic.app/"
        const val BASE_URL = "https://localhost:5173/"
        // POST endpoint for login
        const val LOGIN_URL = "auth/jwt/create/"
        // POST endpoint for registration
        const val REGISTRATION_URL = "auth/users/"

        const val AUTHORIZATION_BEARER = "Bearer"
    }
}