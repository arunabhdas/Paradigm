package app.paradigmatic.paradigmaticapp.data.local

import app.paradigmatic.paradigmaticapp.domain.MongoRepository
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.RequestState
import kotlinx.coroutines.flow.Flow

class MongoImpl: MongoRepository {
    override fun configureTheRealm() {
        TODO("Not yet implemented")
    }

    override suspend fun insertCurrencyData(currency: Currency) {
        TODO("Not yet implemented")
    }

    override fun readCurrencyData(): Flow<RequestState<List<Currency>>> {
        TODO("Not yet implemented")
    }
}