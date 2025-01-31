package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
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
import no.nordicsemi.android.common.core.parseBold

@Composable
internal fun LocationPermissionRequiredView() {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val context = LocalContext.current
    var permissionDenied by remember { mutableStateOf(viewModel.isLocationPermissionDeniedForever(context)) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.markLocationPermissionRequested()
        permissionDenied = viewModel.isLocationPermissionDeniedForever(context)
        viewModel.refreshLocationPermission()
    }

    LocationPermissionRequiredView(
        permissionDenied = permissionDenied,
        onGrantClicked = {
            val requiredPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            launcher.launch(requiredPermissions)
        },
        onOpenSettingsClicked = { openPermissionSettings(context) },
    )
}

@Composable
internal fun LocationPermissionRequiredView(
    permissionDenied: Boolean,
    onGrantClicked: () -> Unit,
    onOpenSettingsClicked: () -> Unit,
) {
    WarningView(
        imageVector = Icons.Default.LocationOff,
        title = stringResource(id = R.string.location_permission_title),
        hint = stringResource(id = R.string.location_permission_info).parseBold(),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (!permissionDenied) {
            Button(onClick = onGrantClicked) {
                Text(text = stringResource(id = R.string.action_grant_permission))
            }
        } else {
            Button(onClick = onOpenSettingsClicked) {
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

@Preview
@Composable
private fun LocationPermissionRequiredView_Preview() {
    Xlr8ProTheme {
        LocationPermissionRequiredView(
            permissionDenied = false,
            onGrantClicked = { },
            onOpenSettingsClicked = { },
        )
    }
}

