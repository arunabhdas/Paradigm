package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import app.paradigmatic.paradigmaticapp.data.kotlinble.viewmodel.PermissionViewModel
import app.paradigmatic.paradigmaticapp.ui.theme.Xlr8ProTheme
import app.paradigmatic.paradigmaticapp.R

@RequiresApi(Build.VERSION_CODES.S)
@Composable
internal fun BluetoothPermissionRequiredView() {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val context = LocalContext.current
    var permissionDenied by remember { mutableStateOf(viewModel.isBluetoothScanPermissionDeniedForever(context)) }

    WarningView(
        imageVector = Icons.Default.BluetoothDisabled,
        title = stringResource(id = R.string.bluetooth_permission_title),
        hint = stringResource(id = R.string.bluetooth_permission_info),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val requiredPermissions = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADVERTISE,
        )

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.markBluetoothPermissionRequested()
            permissionDenied = viewModel.isBluetoothScanPermissionDeniedForever(context)
            viewModel.refreshBluetoothPermission()
        }

        if (!permissionDenied) {
            Button(onClick = { launcher.launch(requiredPermissions) }) {
                Text(text = stringResource(id = R.string.action_grant_permission))
            }
        } else {
            Button(onClick = { openPermissionSettings(context) }) {
                Text(text = stringResource(id = R.string.action_settings))
            }
        }
    }
}

private fun openPermissionSettings(context: Context) {
    ContextCompat.startActivity(
        context,
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ),
        null
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
private fun BluetoothPermissionRequiredView_Preview() {
    Xlr8ProTheme {
        BluetoothPermissionRequiredView()
    }
}
