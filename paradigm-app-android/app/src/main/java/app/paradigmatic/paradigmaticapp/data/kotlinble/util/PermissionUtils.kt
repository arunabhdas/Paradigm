package app.paradigmatic.paradigmaticapp.data.kotlinble.util


import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat

@Suppress("MemberVisibilityCanBePrivate")
internal class PermissionUtils(
    private val context: Context,
    private val dataProvider: LocalDataProvider,
) {
    val isBleEnabled: Boolean
        get() = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
            .adapter
            .isEnabled

    val isLocationEnabled: Boolean
        get() = if (dataProvider.isMarshmallowOrAbove) {
            val lm = context.getSystemService(LocationManager::class.java)
            LocationManagerCompat.isLocationEnabled(lm)
        } else true

    val isBluetoothAvailable: Boolean
        get() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    val isLocationPermissionRequired: Boolean
        get() = dataProvider.isMarshmallowOrAbove && !dataProvider.isSOrAbove

    val isBluetoothScanPermissionGranted: Boolean
        get() = !dataProvider.isSOrAbove ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED

    val isBluetoothConnectPermissionGranted: Boolean
        get() = !dataProvider.isSOrAbove ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED

    val isLocationPermissionGranted: Boolean
        get() = !isLocationPermissionRequired ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    val areNecessaryBluetoothPermissionsGranted: Boolean
        get() = isBluetoothScanPermissionGranted && isBluetoothConnectPermissionGranted

    fun markBluetoothPermissionRequested() {
        dataProvider.bluetoothPermissionRequested = true
    }

    fun markLocationPermissionRequested() {
        dataProvider.locationPermissionRequested = true
    }

    fun isBluetoothScanPermissionDeniedForever(context: Context): Boolean {
        return dataProvider.isSOrAbove &&
                !isBluetoothScanPermissionGranted && // Bluetooth Scan permission must be denied
                dataProvider.bluetoothPermissionRequested && // Permission must have been requested before
                !context.findActivity()
                    .shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_SCAN)
    }

    fun isLocationPermissionDeniedForever(context: Context): Boolean {
        return dataProvider.isMarshmallowOrAbove &&
                !isLocationPermissionGranted // Location permission must be denied
                && dataProvider.locationPermissionRequested // Permission must have been requested before
                && !context.findActivity()
            .shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * Finds the activity from the given context.
     *
     * https://github.com/google/accompanist/blob/6611ebda55eb2948eca9e1c89c2519e80300855a/permissions/src/main/java/com/google/accompanist/permissions/PermissionsUtil.kt#L99
     *
     * @throws IllegalStateException if no activity was found.
     * @return the activity.
     */
    fun Context.findActivity(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("no activity")
    }
}
