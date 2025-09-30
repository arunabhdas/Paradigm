package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.ParcelUuid
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.data.beacon.BLEDevice
import app.paradigmatic.paradigmaticapp.data.beacon.IBeacon
import app.paradigmatic.paradigmaticapp.data.beacon.Beacon
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.services.BeaconProToolsService
import app.paradigmatic.paradigmaticapp.ui.components.AboutDialog
import app.paradigmatic.paradigmaticapp.ui.screens.CustomScreenBar
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.SecondaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.TertiaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun TabFourScreenBak(
    navController: NavController,
    navigator: DestinationsNavigator

) {
    val context = LocalContext.current
    var showAboutDialog = remember { mutableStateOf(false) }

    val gradientColors = listOf(
        PrimaryColor,
        SecondaryColor
    )
    var showExpandedText by remember {
        mutableStateOf(false)
    }

    val permissionState = rememberMultiplePermissionsState(
        PermUtils.permissions
    )
    val scope = rememberCoroutineScope()
    val btManager =
        remember { context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }
    val btAdapter = remember { btManager.adapter }
    val btScanner = remember { btAdapter.bluetoothLeScanner }

    val eddystoneServiceId: ParcelUuid =
        ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB")
    var beaconSet: HashSet<Beacon> = HashSet()

    // State variables
    var isScanning by remember { mutableStateOf(false) }
    var isServiceScanning by remember { mutableStateOf(false) }
    var beacons by remember { mutableStateOf(listOf<Beacon>()) }
    var selectedBeaconType by remember { mutableStateOf(0) }
    var ibeaconState by remember {
        mutableStateOf(
            IBeacon(null, null)
        )
    }

    var iBeaconDeviceList: ArrayList<IBeacon> = arrayListOf()
    var bleDeviceList: ArrayList<BLEDevice> = arrayListOf()
    val iBeaconDeviceListState = remember { mutableStateListOf<IBeacon>() }
    val bleDeviceListState = remember { mutableStateListOf<BLEDevice>() }



    LaunchedEffect(key1 = bleDeviceList) {
        iBeaconDeviceListState.clear()
        iBeaconDeviceListState.addAll(iBeaconDeviceList)
        bleDeviceListState.clear()
        bleDeviceListState.addAll(bleDeviceList)
    }

    /**
     * check if our device list already has a scan result whose MAC address is identical to the new incoming ScanResult
     * @param result BLE scan result
     * @return -1 if doesn't exist
     */
    fun checkIBeaconDeviceExists(result: ScanResult): Int {
        val indexQuery =
            iBeaconDeviceList.indexOfFirst { (it as BLEDevice).getAddress() == result.device.address }
        return indexQuery
    }

    /**
     * check if our device list already has a scan result whose MAC address is identical to the new incoming ScanResult
     * @param result BLE scan result
     * @return -1 if doesn't exist
     */
    fun checkBleDeviceExists(result: ScanResult): Int {
        val indexQuery =
            bleDeviceList.indexOfFirst { (it as BLEDevice).getAddress() == result.device.address }
        return indexQuery
    }

    /**
     * Check if packet is from an iBeacon
     * @param packetData packet data which app captured
     * @return true if packet is from iBeacon, otherwise false
     */
    fun isIBeacon(packetData: ByteArray): Boolean {
        var startByte = 2
        while (startByte <= 5) {
            if (packetData[startByte + 2].toInt() and 0xff == 0x02 && packetData[startByte + 3].toInt() and 0xff == 0x15) {
                // debug result: startByte = 5
                return true
            }
            startByte++
        }
        return false
    }

    LaunchedEffect(
        key1 = permissionState.allPermissionsGranted,
    ) {
        Timber.d("- ScannerScreen")

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
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (showAboutDialog.value) {
                AboutDialog(
                    title = stringResource(id = R.string.app_about_title),
                    description = stringResource(id = R.string.app_about_description),
                    navigator = navigator,
                    onDismiss = {},
                    onBottomButtonTapped = {
                        showAboutDialog.value = false
                    }
                )

            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Adjust top padding as needed
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CustomScreenBar(
                            stringResource(R.string.tab_4_name),
                            {
                                PermUtils.openAppSettings(context)
                            },
                            {
                                PermUtils.openAppSettings(context)
                            }
                        )
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                thickness = 1.dp,
                                color = TertiaryColor
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                painter = painterResource(id = R.drawable.appicon),
                                contentDescription = stringResource(id = R.string.content_description),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .size(150.dp)
                            )

                            Spacer(modifier = Modifier.padding(4.dp))
                            Button(
                                onClick = {
                                    val locationManager =
                                        context.getSystemService(ComponentActivity.LOCATION_SERVICE) as LocationManager
                                    if (!isLocationEnabled(locationManager) && Build.VERSION.SDK_INT <= 30) {
                                        Toast.makeText(
                                            context,
                                            "Location should be enabled since Location services are needed on some devices for correctly locating other Bluetooth devices",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        if (PermUtils.hasBluetoothPermissions(context)) {
                                            if (!isServiceScanning) {
                                                Toast.makeText(
                                                    context,
                                                    "Service Started",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                Intent(
                                                    context,
                                                    BeaconProToolsService::class.java
                                                ).also {
                                                    it.action =
                                                        BeaconProToolsService.Actions.START.toString()
                                                    context.startService(it)
                                                }
                                                isServiceScanning = true
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Please enable permissions",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    }

                                }, modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = PrimaryColor
                                )
                            ) {
                                Text(
                                    text = "Start Service",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.padding(10.dp))
                            Button(
                                onClick = {
                                    if (isServiceScanning) {
                                        Toast.makeText(
                                            context,
                                            "Stop Service",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Intent(context, BeaconProToolsService::class.java).also {
                                            it.action =
                                                BeaconProToolsService.Actions.STOP.toString()
                                            context.startService(it)
                                        }

                                        isServiceScanning = false
                                    }

                                }, modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = PrimaryColor
                                )
                            ) {
                                Text(
                                    text = "Stop Service",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                thickness = 1.dp,
                                color = TertiaryColor
                            )
                            Spacer(modifier = Modifier.height(180.dp))
                        }
                    }
            }

        }

    }

    /**
     * Take the user to location settings to enable location services
     *
     * @param activity
     */
    fun enableLocation(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }


    @Composable
    fun BeaconTypeSpinner(selectedType: Int, onTypeSelected: (Int) -> Unit) {
        // Implement spinner logic using DropdownMenu or similar approach
    }




}

@Preview
@Composable
fun TabFourScreenBakPreview() {
    TabFourScreenBak(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}

