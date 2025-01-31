package app.paradigmatic.paradigmaticapp.data.kotlinble.repository

import android.annotation.SuppressLint
import dagger.hilt.android.scopes.ViewModelScoped
import no.nordicsemi.android.kotlin.ble.scanner.BleScanner
import javax.inject.Inject

@ViewModelScoped
class ScannerRepository @Inject internal constructor(
    private val nordicScanner: BleScanner
) {

    @SuppressLint("MissingPermission")
    fun getScannerState() = nordicScanner.scan()
}
