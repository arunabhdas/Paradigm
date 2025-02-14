package app.paradigmatic.paradigmaticapp.data.remote.api

import app.paradigmatic.paradigmaticapp.config.ApiConfig
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
import app.paradigmatic.paradigmaticapp.domain.PreferencesRepository
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyApiResponse
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyCode
import app.paradigmatic.paradigmaticapp.domain.model.RequestState
import co.touchlab.stately.ensureNeverFrozen
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
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
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

    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        println("Starting getLatestExchangeRates")
        println("Using API Key: $API_KEY")
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

                // Persist timestamp in preferences key-value-pair
                val lastUpdated = apiResponse.meta.lastUpdatedAt
                preferences.saveLastUpdated(lastUpdated)

                // TODO-FIXME-CLEANUP RequestState.Success(data = apiResponse.data.values.toList())
                RequestState.Success(data = availableCurrencies)

            } else {
                RequestState.Error(message = "HTTP Error Code: ${response.status}")
            }
        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())
        }

    }

}