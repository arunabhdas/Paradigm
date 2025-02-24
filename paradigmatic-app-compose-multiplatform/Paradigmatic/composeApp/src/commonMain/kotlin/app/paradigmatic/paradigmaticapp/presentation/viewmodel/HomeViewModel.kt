package app.paradigmatic.paradigmaticapp.presentation.viewmodel

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

sealed class HomeUiEvent {
    data object RefreshRates: HomeUiEvent()
    data object SwitchCurrencies: HomeUiEvent()
    data class SaveSourceCurrencyCode(val code: String): HomeUiEvent()
    data class SaveTargetCurrencyCode(val code: String): HomeUiEvent()

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

    private var _targetCurrency: MutableState<CurrencyApiRequestState<Currency>> = mutableStateOf(CurrencyApiRequestState.Idle)
    val targetCurrency: State<CurrencyApiRequestState<Currency>> = _targetCurrency

    private var _sourceCurrencyDisplay: MutableState<CurrencyApiRequestState<Currency>> = mutableStateOf(CurrencyApiRequestState.Idle)
    val sourceCurrencyDisplay: State<CurrencyApiRequestState<Currency>> = _sourceCurrencyDisplay

    private var _targetCurrencyDisplay: MutableState<CurrencyApiRequestState<Currency>> = mutableStateOf(CurrencyApiRequestState.Idle)
    val targetCurrencyDisplay: State<CurrencyApiRequestState<Currency>> = _targetCurrencyDisplay

    private var _allCurrencies = mutableStateListOf<Currency>()
    val allCurrencies: List<Currency> = _allCurrencies



    init {
        screenModelScope.launch {
            fetchNewRates()
            readSourceCurrency()
            readTargetCurrency()
        }
    }

    fun sendEvent(event: HomeUiEvent) {
        when(event) {
            HomeUiEvent.RefreshRates -> {
                screenModelScope.launch {
                    fetchNewRates()
                }
            }
            HomeUiEvent.SwitchCurrencies -> {
                println("SwitchCurrencies event was sent to HomeViewModel")
                screenModelScope.launch {
                    switchCurrencies()
                }
            }

            is HomeUiEvent.SaveSourceCurrencyCode -> {
               println("Saving source currency code ${event.code} ")
               saveSourceCurrencyCode(event.code)
            }
            is HomeUiEvent.SaveTargetCurrencyCode -> {
                println("Saving target currency code ${event.code} ")
                saveTargetCurrencyCode(event.code)
            }
        }

    }

    private fun readSourceCurrency() {
        screenModelScope.launch(Dispatchers.Main) {
           preferences.readSourceCurrencyCode().collectLatest { currencyCode ->
               val selectedCurrency = _allCurrencies.find { it.code == currencyCode.name }
               if (selectedCurrency != null) {
                   _sourceCurrency.value = CurrencyApiRequestState.Success(data = selectedCurrency)
                   _sourceCurrencyDisplay.value = CurrencyApiRequestState.Success(data = selectedCurrency)
               } else {
                   _sourceCurrency.value = CurrencyApiRequestState.Error(message = "Couldn't find the selected currency")
                   _sourceCurrencyDisplay.value = CurrencyApiRequestState.Error(message = "Couldn't find the selected currency")
               }
           }
        }
    }

    private fun readTargetCurrency() {
        screenModelScope.launch(Dispatchers.Main) {
            preferences.readTargetCurrencyCode().collectLatest { currencyCode ->
                val selectedCurrency = _allCurrencies.find { it.code == currencyCode.name }
                if (selectedCurrency != null) {
                    _targetCurrency.value = CurrencyApiRequestState.Success(data = selectedCurrency)
                    _targetCurrencyDisplay.value = CurrencyApiRequestState.Success(data = selectedCurrency)
                } else {
                    _targetCurrency.value = CurrencyApiRequestState.Error(message = "Couldn't find the selected currency")
                    _targetCurrencyDisplay.value = CurrencyApiRequestState.Error(message = "Couldn't find the selected currency")
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
                    _allCurrencies.clear()
                    _allCurrencies.addAll(localCache.getSuccessData())
                    if (!preferences.isDataFresh(Clock.System.now().toEpochMilliseconds())) {
                        println("HomeViewModel: DATA NOT FRESH")
                        cacheTheData()
                    } else {
                        println("HomeViewModel: DATA IS FRESH")
                    }
                } else {
                    println("HomeViewModel: DATABASE NEEDS DATA")
                    cacheTheData()
                }
            } else if (localCache.isError()){
                println("HomeViewModel: ERROR READING LOCAL DATABASE ${localCache.getErrorMessage()}")
            }
            getRateStatus()
            println("The RateStatus is ${_rateStatus.value}")
        } catch (e: Exception) {
           println(e.message)
        }
    }

    private fun switchCurrencies() {
        val source = _sourceCurrency.value
        val target = _targetCurrency.value
        _sourceCurrency.value = target
        _targetCurrency.value = source
    }

    private fun saveSourceCurrencyCode(code: String) {
        screenModelScope.launch(Dispatchers.IO) {
           preferences.saveSourceCurrencyCode(code)
        }
    }

    private fun saveTargetCurrencyCode(code: String) {
        screenModelScope.launch(Dispatchers.IO) {
            preferences.saveTargetCurrencyCode(code)
        }
    }

    private suspend fun cacheTheData() {
        val fetchedData = api.getLatestExchangeRates()
        if (fetchedData.isSuccess()) {
            mongoDb.cleanUp()
            fetchedData.getSuccessData().forEach {
                println("HomeViewModel: ADDING ${it.code}")
                mongoDb.insertCurrencyData(it)
            }
            println("HomeViewModel: UPDATING _allCurrencies")
            _allCurrencies.clear()
            _allCurrencies.addAll(fetchedData.getSuccessData())
        } else if (fetchedData.isError()){
            println("HomeViewModel: FETCHING FAILED ${fetchedData.getErrorMessage()}")
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