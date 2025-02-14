package app.paradigmatic.paradigmaticapp.config

import app.paradigmatic.paradigmaticapp.BuildConfig

actual object ApiConfig {
    actual val CURRENCY_API_KEY: String
        get() = BuildConfig.CURRENCY_API_KEY
}