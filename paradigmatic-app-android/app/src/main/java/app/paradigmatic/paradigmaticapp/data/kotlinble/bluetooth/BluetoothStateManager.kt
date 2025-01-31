package app.paradigmatic.paradigmaticapp.data.kotlinble.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import app.paradigmatic.paradigmaticapp.data.kotlinble.util.BlePermissionNotAvailableReason
import app.paradigmatic.paradigmaticapp.data.kotlinble.util.BlePermissionState
import app.paradigmatic.paradigmaticapp.data.kotlinble.util.LocalDataProvider
import app.paradigmatic.paradigmaticapp.data.kotlinble.util.PermissionUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val REFRESH_PERMISSIONS = "app.xlr8pro.xlr8proapp.android.common.permission.REFRESH_BLUETOOTH_PERMISSIONS"

@Singleton
class BluetoothStateManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataProvider = LocalDataProvider(context)
    private val utils = PermissionUtils(context, dataProvider)

    fun bluetoothState() = callbackFlow {
        trySend(getBluetoothPermissionState())

        val bluetoothStateChangeHandler = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                trySend(getBluetoothPermissionState())
            }
        }
        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(REFRESH_PERMISSIONS)
        }
        ContextCompat.registerReceiver(context, bluetoothStateChangeHandler, filter, ContextCompat.RECEIVER_EXPORTED)
        awaitClose {
            context.unregisterReceiver(bluetoothStateChangeHandler)
        }
    }

    fun refreshPermission() {
        val intent = Intent(REFRESH_PERMISSIONS)
        context.sendBroadcast(intent)
    }

    fun markBluetoothPermissionRequested() {
        dataProvider.bluetoothPermissionRequested = true
    }

    fun isBluetoothScanPermissionDeniedForever(context: Context): Boolean {
        return utils.isBluetoothScanPermissionDeniedForever(context)
    }

    private fun getBluetoothPermissionState() = when {
        !utils.isBluetoothAvailable -> BlePermissionState.NotAvailable(
            BlePermissionNotAvailableReason.NOT_AVAILABLE)
        !utils.areNecessaryBluetoothPermissionsGranted -> BlePermissionState.NotAvailable(
            BlePermissionNotAvailableReason.PERMISSION_REQUIRED)
        !utils.isBleEnabled -> BlePermissionState.NotAvailable(
            BlePermissionNotAvailableReason.DISABLED)
        else -> BlePermissionState.Available
    }
}
