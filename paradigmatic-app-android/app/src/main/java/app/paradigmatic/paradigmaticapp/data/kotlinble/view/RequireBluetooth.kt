package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.paradigmatic.paradigmaticapp.data.kotlinble.util.BlePermissionNotAvailableReason
import app.paradigmatic.paradigmaticapp.data.kotlinble.util.BlePermissionState
import app.paradigmatic.paradigmaticapp.data.kotlinble.viewmodel.PermissionViewModel

@Composable
fun RequireBluetooth(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutBluetooth: @Composable (BlePermissionNotAvailableReason) -> Unit = {
        NoBluetoothView(reason = it)
    },
    content: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val state by viewModel.bluetoothState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onChanged(state is BlePermissionState.Available)
    }

    when (val s = state) {
        BlePermissionState.Available -> content()
        is BlePermissionState.NotAvailable -> contentWithoutBluetooth(s.reason)
    }
}

@Composable
private fun NoBluetoothView(
    reason: BlePermissionNotAvailableReason,
) {
    when (reason) {
        BlePermissionNotAvailableReason.NOT_AVAILABLE -> BluetoothNotAvailableView()
        BlePermissionNotAvailableReason.PERMISSION_REQUIRED ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                BluetoothPermissionRequiredView()
            }

        BlePermissionNotAvailableReason.DISABLED -> BluetoothDisabledView()
    }
}
