package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import app.paradigmatic.paradigmaticapp.ui.theme.Xlr8ProTheme
import app.paradigmatic.paradigmaticapp.R

@Composable
internal fun BluetoothDisabledView() {
    WarningView(
        imageVector = Icons.Default.BluetoothDisabled,
        title = stringResource(id = R.string.bluetooth_disabled_title),
        hint = stringResource(id = R.string.bluetooth_disabled_info),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val context = LocalContext.current
        Button(onClick = { enableBluetooth(context) }) {
            Text(text = stringResource(id = R.string.action_enable))
        }
    }
}

@SuppressLint("MissingPermission")
private fun enableBluetooth(context: Context) {
    context.startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
}

@Preview
@Composable
private fun BluetoothDisabledView_Preview() {
    Xlr8ProTheme {
        BluetoothDisabledView()
    }
}
