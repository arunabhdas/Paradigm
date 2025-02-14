package app.paradigmatic.paradigmaticapp.data.remote.api
import app.paradigmatic.paradigmaticapp.config.ApiConfig
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService

class CurrencyApiServiceImpl : CurrencyApiService {

    companion object {
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
        private val API_KEY = ApiConfig.CURRENCY_API_KEY
    }

}