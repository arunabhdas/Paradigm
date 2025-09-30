package app.paradigmatic.paradigmaticapp.data.kotlinble.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.paradigmatic.paradigmaticapp.data.kotlinble.bluetooth.BluetoothStateManager
import app.paradigmatic.paradigmaticapp.data.kotlinble.bluetooth.LocationStateManager
import app.paradigmatic.paradigmaticapp.data.kotlinble.util.BlePermissionNotAvailableReason
import app.paradigmatic.paradigmaticapp.data.kotlinble.util.BlePermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
internal class PermissionViewModel @Inject internal constructor(
    private val bluetoothManager: BluetoothStateManager,
    private val locationManager: LocationStateManager,
) : ViewModel() {
    val bluetoothState = bluetoothManager.bluetoothState()
        .stateIn(
            viewModelScope, SharingStarted.Lazily,
            BlePermissionState.NotAvailable(BlePermissionNotAvailableReason.NOT_AVAILABLE)
        )

    val locationPermission = locationManager.locationState()
        .stateIn(
            viewModelScope, SharingStarted.Lazily,
            BlePermissionState.NotAvailable(BlePermissionNotAvailableReason.NOT_AVAILABLE)
        )

    fun refreshBluetoothPermission() {
        bluetoothManager.refreshPermission()
    }

    fun refreshLocationPermission() {
        locationManager.refreshPermission()
    }

    fun markLocationPermissionRequested() {
        locationManager.markLocationPermissionRequested()
    }

    fun markBluetoothPermissionRequested() {
        bluetoothManager.markBluetoothPermissionRequested()
    }

    fun isBluetoothScanPermissionDeniedForever(context: Context): Boolean {
        return bluetoothManager.isBluetoothScanPermissionDeniedForever(context)
    }

    fun isLocationPermissionDeniedForever(context: Context): Boolean {
        return locationManager.isLocationPermissionDeniedForever(context)
    }
}
