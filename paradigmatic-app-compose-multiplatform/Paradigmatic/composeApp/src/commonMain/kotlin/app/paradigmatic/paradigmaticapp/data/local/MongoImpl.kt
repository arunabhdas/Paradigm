package app.paradigmatic.paradigmaticapp.data.local

import app.paradigmatic.paradigmaticapp.domain.MongoRepository
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyApiRequestState
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.Flow

class MongoImpl: MongoRepository {
    private var realm: Realm? = null

    override fun configureTheRealm() {
        TODO("Not yet implemented")
    }

    override suspend fun insertCurrencyData(currency: Currency) {
        TODO("Not yet implemented")
    }

    override fun readCurrencyData(): Flow<CurrencyApiRequestState<List<Currency>>> {
        TODO("Not yet implemented")
    }
}