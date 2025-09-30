package app.paradigmatic.paradigmaticapp.data.kotlinble.model

import no.nordicsemi.android.kotlin.ble.core.scanner.BleScanResults

sealed class ScanningState {

    object Loading : ScanningState()

    data class Error(val errorCode: Int) : ScanningState()

    data class DevicesDiscovered(val devices: List<BleScanResults>) : ScanningState() {
        val bonded: List<BleScanResults> = devices.filter { it.device.isBonded }

        val notBonded: List<BleScanResults> = devices.filter { !it.device.isBonded }

        fun size(): Int = bonded.size + notBonded.size

        fun isEmpty(): Boolean = devices.isEmpty()
    }

    fun isRunning(): Boolean {
        return this is Loading || this is DevicesDiscovered
    }
}
