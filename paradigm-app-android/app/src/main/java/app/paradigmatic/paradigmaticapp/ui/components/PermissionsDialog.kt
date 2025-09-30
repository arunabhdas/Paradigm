package app.paradigmatic.paradigmaticapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PermissionsDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider()
                Text(
                    text = if (isPermanentlyDeclined) {
                        "Grant permission"
                    } else {
                        "Ok"
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                                onOkClick()
                            } else {
                                onOkClick()
                            }
                        }
                        .padding(16.dp)
                )
            }
        },
        title = {
            Text(text = "Permission required")
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined
                )
            )
        },
        modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class CoarseLocationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined coarse location permission. " +
                    "Please go to the app settings to grant it."
        } else {
            "This app needs access to your coarse location so that it may scan for nearby beacons."
        }
    }
}

class FineLocationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined fine location permission. " +
                    "Please go to the app settings to grant it."
        } else {
            "This app needs access to your fine location so that it may scan for nearby beacons."
        }
    }
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined camera permission. " +
                    "Please go to the app settings to grant it."
        } else {
            "This app needs access to camera so that you may use the phone camera to scan QR codes."
        }
    }
}

class BackgroundLocationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined background location permission. " +
                    "Please go to the app settings to grant it."
        } else {
            "This app needs access to your background location so that it may scan for nearby beacons in the background."
        }
    }
}

class BluetoothScanPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined nearby bluetooth connecting permission. " +
                    "Please go to the app settings to grant it."
        } else {
            "This app needs access to connect to nearby beacons permissions so that it may alert you if it finds nearby beacons."
        }
    }
}

class ForegroundServicePermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined foreground service scanning permission. " +
                    "Please go to the app settings to grant it."
        } else {
            "This app needs access to foreground service scanning permissions so that it may alert you if it finds nearby beacons."
        }
    }
}