package app.paradigmatic.paradigmaticapp.domain

import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyApiRequestState

interface CurrencyApiService {
    suspend fun getLatestExchangeRates(): CurrencyApiRequestState<List<Currency>>
}