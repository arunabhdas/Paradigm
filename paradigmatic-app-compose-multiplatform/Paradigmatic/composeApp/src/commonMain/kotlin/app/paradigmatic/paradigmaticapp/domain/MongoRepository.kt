package app.paradigmatic.paradigmaticapp.domain

import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyApiRequestState
import kotlinx.coroutines.flow.Flow

interface MongoRepository {
    fun configureTheRealm()
    suspend fun insertCurrencyData(currency: Currency)
    fun readCurrencyData(): Flow<CurrencyApiRequestState<List<Currency>>>
    suspend fun cleanUp()
}