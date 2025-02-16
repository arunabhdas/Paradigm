package app.paradigmatic.paradigmaticapp.presentation.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
import app.paradigmatic.paradigmaticapp.domain.MongoRepository
import app.paradigmatic.paradigmaticapp.domain.PreferencesRepository
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.RateStatus
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyApiRequestState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

sealed class HomeUiEvent {
    data object RefreshRates: HomeUiEvent()
}

class HomeViewModel(
    private val preferences: PreferencesRepository,
    private val mongoDb: MongoRepository,
    private val api: CurrencyApiService
): ScreenModel {
    private var _rateStatus: MutableState<RateStatus> = mutableStateOf(RateStatus.Idle)
    val rateStatus: State<RateStatus> = _rateStatus

    private var _sourceCurrency: MutableState<CurrencyApiRequestState<Currency>> = mutableStateOf(CurrencyApiRequestState.Idle)
    val sourceCurrency: State<CurrencyApiRequestState<Currency>> = _sourceCurrency

    private var _allCurrencies = mutableStateListOf<Currency>()
    val allCurrencies: List<Currency> = _allCurrencies

    private var _targetCurrency: MutableState<CurrencyApiRequestState<Currency>> = mutableStateOf(CurrencyApiRequestState.Idle)
    val targetCurrency: State<CurrencyApiRequestState<Currency>> = _targetCurrency

    init {
        screenModelScope.launch {
            fetchNewRates()
        }
    }

    fun sendEvent(event: HomeUiEvent) {
        when(event) {
            HomeUiEvent.RefreshRates -> {
                screenModelScope.launch {
                    fetchNewRates()
                }
            }
        }
    }

    private suspend fun fetchNewRates() {
        try {
            val localCache = mongoDb.readCurrencyData().first()
            if (localCache.isSuccess()) {
                if (localCache.getSuccessData().isNotEmpty()) {
                    println("HomeViewModel: DATABASE IS FULL")
                    _allCurrencies.addAll(localCache.getSuccessData())
                    if (!preferences.isDataFresh(Clock.System.now().toEpochMilliseconds())) {
                        println("HomeViewModel: DATA NOT FRESH")
                        val fetchedData = api.getLatestExchangeRates()
                        if (fetchedData.isSuccess()) {
                            fetchedData.getSuccessData().forEach {
                                println("HomeViewModel: ADDING ${it.code}")
                                mongoDb.insertCurrencyData(it)
                            }
                            println("HomeViewModel: UPDATING _allCurrencies")
                            _allCurrencies.addAll(fetchedData.getSuccessData())
                        } else if (fetchedData.isError()){
                            println("HomeViewModel: FETCHING FAILED ${fetchedData.getErrorMessage()}")
                        }
                    } else {
                        println("HomeViewModel: DATA IS FRESH")
                    }
                } else {
                    println("HomeViewModel: DATABASE NEEDS DATA")
                    val fetchedData = api.getLatestExchangeRates()
                    if (fetchedData.isSuccess()) {
                        fetchedData.getSuccessData().forEach {
                            println("HomeViewModel: ADDING ${it.code}")
                            mongoDb.insertCurrencyData(it)
                        }
                        println("HomeViewModel: UPDATING _allCurrencies")
                        _allCurrencies.addAll(fetchedData.getSuccessData())
                    } else if (fetchedData.isError()){
                        println("HomeViewModel: FETCHING FAILED ${fetchedData.getErrorMessage()}")
                    }
                }
            } else if (localCache.isError()){
                println("HomeViewModel: ERROR READING LOCAL DATABASE ${localCache.getErrorMessage()}")
            }
            // TODO-FIXME-CLEANUP api.getLatestExchangeRates()
            getRateStatus()
            println("The RateStatus is ${_rateStatus.value}")
        } catch (e: Exception) {
           println(e.message)
        }
    }

    private suspend fun getRateStatus() {
        _rateStatus.value = if (preferences.isDataFresh(
            currentTimestamp = Clock.System.now().toEpochMilliseconds()
            )
        ) RateStatus.Fresh
        else RateStatus.Stale
    }
}