package app.paradigmatic.paradigmaticapp.ui.screens.connect

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.ui.ibeacon.ScanService
import app.paradigmatic.paradigmaticapp.ui.navigation.Screen
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.TransparentColor
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun ConnectScreen(
    navigator: DestinationsNavigator,
    connectViewModel: ConnectViewModel = hiltViewModel(),

    ) {
    val context = LocalContext.current
    var scanService: ScanService
    var deviceList: ArrayList<Any>

    val permissionState = rememberMultiplePermissionsState(
        PermUtils.permissions
    )
    val lifeCycleOwner = LocalLifecycleOwner.current


    DisposableEffect(
        key1 = lifeCycleOwner,
        effect = {
            deviceList = ArrayList()
            val observer = LifecycleEventObserver{_, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchMultiplePermissionRequest(

                    )

                    if (
                        permissionState.allPermissionsGranted
                    ) {
                        Timber.d("- All permissions granted")
                        scanService = ScanService(context, deviceList)
                        scanService.initScanner()
                        // start scanning BLE device
                        if (scanService.isScanning()) {
                            Timber.d("- isScanning")
                            scanService.stopBLEScan()
                        } else {
                            scanService.startBLEScan()
                            Timber.d("- startBLEScan called")
                        }
                    }
                }
                if (event == Lifecycle.Event.ON_STOP) {
                    if (
                        permissionState.allPermissionsGranted
                    ) {
                        Timber.d("- Lifecycle stop")
                    }
                }
            }

            lifeCycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifeCycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    LaunchedEffect(key1 = "ConnectionScreenAppeared") {
        Timber.d( "route: ${Screen.ConnectScreen.route}")

        if (!PermUtils.hasBluetoothPermissions(context)) {
            // Handle missing permissions
            Timber.d("- Missing Bluetooth Permissions")
            Toast.makeText(
                context,
                "Missing Bluetooth Permissions",
                Toast.LENGTH_SHORT
            ).show()
            return@LaunchedEffect
        }

        // 2. Check Bluetooth Availability
        val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        if (btAdapter == null || !btAdapter.isEnabled) {
            // Handle Bluetooth not available or disabled
            Timber.d("- Bluetooth Adapter is null or not enabled")
            return@LaunchedEffect
        }

        if (permissionState.allPermissionsGranted) {

            Timber.d("- Permissions checks seemed to be passed")
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
       Column(
           verticalArrangement = Arrangement.Center,
           modifier = Modifier
               .fillMaxSize()
               .padding(horizontal = 0.dp)
       ) {
           Button(
               onClick = {
                   Timber.d( "- Beacon Scan Started")

                   Toast.makeText(
                       context,
                       "Scan Started",
                       Toast.LENGTH_LONG
                   ).show()

                   connectViewModel.startScan()

               },
               modifier = Modifier
                   .align(Alignment.CenterHorizontally)
                   .background(TransparentColor),
               colors = ButtonDefaults.buttonColors(
                   containerColor = MaterialTheme.colorScheme.tertiary
               )
           ) {
               Text(text = "Scan")
           }

           // Replace RecyclerView with LazyColumn for list
           DeviceList(listOf(connectViewModel.deviceList))
       }

    }

}


private fun isPermissionGranted(context: Context): Boolean {
    Timber.d( ":-) - @isPermissionGranted: checking bluetooth")
    // necessary permissions on Android <12
    val BLE_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // necessary permissions on Android >=12
    val ANDROID_12_BLE_PERMISSIONS = arrayOf(

        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if ((ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            Timber.d( "@isPermissionGranted: requesting Bluetooth on Android >= 12")
            ActivityCompat.requestPermissions(context as Activity, ANDROID_12_BLE_PERMISSIONS, 2)
            return false
        }
    } else {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.d("@isPermissionGranted: requesting Location on Android < 12")
            ActivityCompat.requestPermissions(context as Activity, BLE_PERMISSIONS, 3)
            return false
        }
    }
    Timber.d("@isPermissionGranted Bluetooth permission is ON")
    return true
}


