package app.paradigmatic.paradigmaticapp.config

import app.paradigmatic.paradigmaticapp.BuildConfig

actual object ApiConfig {

    init {
        println("ApiConfig initialized")
        println("BuildConfig.CURRENCY_API_KEY length: ${BuildConfig.CURRENCY_API_KEY.length}")
    }

    actual val CURRENCY_API_KEY: String
        get() {
            println("Getting CURRENCY_API_KEY from BuildConfig: ${BuildConfig.CURRENCY_API_KEY}")
            return BuildConfig.CURRENCY_API_KEY
        }

    /* TODO-FIXME
    actual val CURRENCY_API_KEY: String
        get() = BuildConfig.CURRENCY_API_KEY
    */
}

