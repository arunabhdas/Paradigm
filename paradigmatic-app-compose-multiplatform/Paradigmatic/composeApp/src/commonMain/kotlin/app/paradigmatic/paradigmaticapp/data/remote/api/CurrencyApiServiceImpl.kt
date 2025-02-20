package app.paradigmatic.paradigmaticapp.data.remote.api

import app.paradigmatic.paradigmaticapp.config.ApiConfig
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
import app.paradigmatic.paradigmaticapp.domain.PreferencesRepository
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyApiResponse
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyCode
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyApiRequestState
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyDto
import app.paradigmatic.paradigmaticapp.domain.model.toCurrency
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl(
    private val preferences: PreferencesRepository
) : CurrencyApiService {

    companion object {
        // TODO-FIXME-CLEANUP-TEMP const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
        const val ENDPOINT = "https://chatoffside.onrender.com/currencyapi/latest"
        private val API_KEY = ApiConfig.CURRENCY_API_KEY
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }

        install(DefaultRequest) {
            headers {
                append("apikey", API_KEY)
            }
        }
    }

    override suspend fun getLatestExchangeRates(): CurrencyApiRequestState<List<Currency>> {
        println("Starting getLatestExchangeRates")
        println("Using API Key")
        println("Endpoint: $ENDPOINT")
        return try {
            val response = httpClient.get(ENDPOINT)
            if (response.status.value == 200) {
                println("API RESPONSE : ${response.body<String>()}")
                val apiResponse = Json.decodeFromString<CurrencyApiResponse>(response.body())

                val availableCurrencyCodes = apiResponse.data.keys
                    .filter {
                        CurrencyCode.entries
                            .map { code -> code.name }
                            .toSet()
                            .contains(it)
                    }

                val availableCurrencies = apiResponse.data.values
                    .filter { currency ->
                        availableCurrencyCodes.contains(currency.code)
                    }
                    .map {
                       it.toCurrency()
                    }

                // Persist timestamp in preferences key-value-pair
                val lastUpdated = apiResponse.meta.lastUpdatedAt
                preferences.saveLastUpdated(lastUpdated)

                // TODO-FIXME-CLEANUP RequestState.Success(data = apiResponse.data.values.toList())
                CurrencyApiRequestState.Success(data = availableCurrencies)

            } else {
                CurrencyApiRequestState.Error(message = "HTTP Error Code: ${response.status}")
            }
        } catch (e: Exception) {
            CurrencyApiRequestState.Error(message = e.message.toString())
        }

    }

}