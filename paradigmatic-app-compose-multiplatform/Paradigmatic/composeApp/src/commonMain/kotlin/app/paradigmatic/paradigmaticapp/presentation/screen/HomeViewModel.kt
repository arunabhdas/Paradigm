package app.paradigmatic.paradigmaticapp.presentation.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
import app.paradigmatic.paradigmaticapp.domain.PreferencesRepository
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.RateStatus
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyApiRequestState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

sealed class HomeUiEvent {
    data object RefreshRates: HomeUiEvent()
}

class HomeViewModel(
    private val preferences: PreferencesRepository,
    private val api: CurrencyApiService
): ScreenModel {
    private var _rateStatus: MutableState<RateStatus> = mutableStateOf(RateStatus.Idle)
    val rateStatus: State<RateStatus> = _rateStatus

    private var _sourceCurrency: MutableState<CurrencyApiRequestState<Currency>> = mutableStateOf(CurrencyApiRequestState.Idle)
    val sourceCurrency: State<CurrencyApiRequestState<Currency>> = _sourceCurrency

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
            api.getLatestExchangeRates()
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