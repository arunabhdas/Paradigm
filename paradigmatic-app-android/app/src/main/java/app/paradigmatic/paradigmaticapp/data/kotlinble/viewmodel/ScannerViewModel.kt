package app.paradigmatic.paradigmaticapp.data.kotlinble.viewmodel

import android.os.ParcelUuid
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.paradigmatic.paradigmaticapp.data.kotlinble.model.ScanningState
import app.paradigmatic.paradigmaticapp.data.kotlinble.repository.DevicesScanFilter
import app.paradigmatic.paradigmaticapp.data.kotlinble.repository.ScannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import no.nordicsemi.android.kotlin.ble.core.scanner.BleScanResults
import no.nordicsemi.android.kotlin.ble.scanner.aggregator.BleScanResultAggregator
import no.nordicsemi.android.kotlin.ble.scanner.errors.ScanFailedError
import no.nordicsemi.android.kotlin.ble.scanner.errors.ScanningFailedException
import javax.inject.Inject


// private const val FILTER_RSSI = -50 // [dBm]
private const val FILTER_RSSI = -80 // [dBm]

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository,
) : ViewModel() {
    private var uuid: ParcelUuid? = null

    val filterConfig = MutableStateFlow(
        DevicesScanFilter(
            filterUuidRequired = true,
            filterNearbyOnly = false,
            filterWithNames = true
        )
    )

    private var currentJob: Job? = null

    private val _state = MutableStateFlow<ScanningState>(ScanningState.Loading)
    val state = _state.asStateFlow()

    init {
        relaunchScanning()
    }

    private fun relaunchScanning() {
        currentJob?.cancel()
        val aggregator = BleScanResultAggregator()
        currentJob = scannerRepository.getScannerState()
            .map { aggregator.aggregate(it) }
            .filter { it.isNotEmpty() }
            .combine(filterConfig) { result, config  ->
                result.applyFilters(config)
            }
            .onStart { _state.value = ScanningState.Loading }
            .cancellable()
            .onEach {
                _state.value = ScanningState.DevicesDiscovered(it)
            }
            .catch { e ->
                _state.value = (e as? ScanningFailedException)?.let {
                    ScanningState.Error(it.errorCode.value)
                } ?: ScanningState.Error(ScanFailedError.UNKNOWN.value)
            }
            .launchIn(viewModelScope)
    }

    // This can't be observed in View Model Scope, as it can exist even when the
    // scanner is not visible. Scanner state stops scanning when it is not observed.
    // .stateIn(viewModelScope, SharingStarted.Lazily, ScanningState.Loading)
    private fun List<BleScanResults>.applyFilters(config: DevicesScanFilter) =
        filter {
            uuid == null ||
                    config.filterUuidRequired == false ||
                    it.lastScanResult?.scanRecord?.serviceUuids?.contains(uuid) == true
        }
            .filter { !config.filterNearbyOnly || it.highestRssi >= FILTER_RSSI }
            .filter { !config.filterWithNames || it.device.hasName || it.advertisedName?.isNotEmpty() == true }

    fun setFilterUuid(uuid: ParcelUuid?) {
        this.uuid = uuid
        if (uuid == null) {
            filterConfig.value = filterConfig.value.copy(filterUuidRequired = null)
        }
    }

    fun setFilter(config: DevicesScanFilter) {
        this.filterConfig.value = config
    }

    fun refresh() {
        relaunchScanning()
    }
}
