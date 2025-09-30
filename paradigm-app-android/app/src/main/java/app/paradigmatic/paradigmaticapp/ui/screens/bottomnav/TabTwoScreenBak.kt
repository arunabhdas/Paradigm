package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.ParcelUuid
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.data.beacon.BLEDevice
import app.paradigmatic.paradigmaticapp.data.beacon.BLEDeviceItem
import app.paradigmatic.paradigmaticapp.data.beacon.Beacon
import app.paradigmatic.paradigmaticapp.data.beacon.IBeacon
import app.paradigmatic.paradigmaticapp.data.blescanner.Utils
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.permissions.RequestMultiplePermissions
import app.paradigmatic.paradigmaticapp.ui.components.AboutDialog
import app.paradigmatic.paradigmaticapp.ui.screens.CustomScreenBar
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.TertiaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber
import java.util.Arrays

@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun TabTwoScreenBak(
    navController: NavController,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    var showAboutDialog = remember { mutableStateOf(false) }
    val permissionState = rememberMultiplePermissionsState(
        PermUtils.permissions
    )

    val scope = rememberCoroutineScope()
    val btManager = remember { context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }
    val btAdapter = remember { btManager.adapter }
    val btScanner = remember { btAdapter.bluetoothLeScanner }

    val eddystoneServiceId: ParcelUuid = ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB")
    var beaconSet: HashSet<Beacon> = HashSet()

    // State variables
    var isScanning by remember { mutableStateOf(false) }
    var beacons by remember { mutableStateOf(listOf<Beacon>()) }
    var selectedBeaconType by remember { mutableStateOf(0) }
    var ibeaconState by remember {
        mutableStateOf(
            IBeacon(null, null)
        )
    }



    var bleDeviceList: ArrayList<BLEDevice> = arrayListOf()
    val bleDeviceListState = remember { mutableStateListOf<BLEDevice>() }

    LaunchedEffect(key1 = true) {
        bleDeviceListState.clear()
        bleDeviceListState.clear()
    }


    /**
     * check if our device list already has a scan result whose MAC address is identical to the new incoming ScanResult
     * @param result BLE scan result
     * @return -1 if doesn't exist
     */
    fun checkBleDeviceExists(result: ScanResult): Int {
        val indexQuery = bleDeviceList.indexOfFirst { (it as BLEDevice) .getAddress() == result.device.address }
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
        key1 = true,
    ) {
        Timber.d("- TabTwoScreen")
        // bleDeviceList.clear()
        // bleDeviceListState.clear()
    }

    val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val scanRecord = result.scanRecord
            val beacon = Beacon(
                result.device.address,
            )
            // TODO-FIXME-IMPROVE beacon.manufacturer = result.device.name
            beacon.txPower = result.txPower
            beacon.rssi = result.rssi
            if (scanRecord != null) {

                if (!isIBeacon(scanRecord.bytes)) {
                    val ble = BLEDevice(result)
                    val idx = checkBleDeviceExists(result)
                    if (idx == -1) {
                        bleDeviceList.add(ble)

                    } else {
                        // update
                        bleDeviceList[idx] = ble
                    }
                }

                bleDeviceListState.clear()
                bleDeviceListState.addAll(bleDeviceList)


                val serviceUuids = scanRecord.serviceUuids
                val txPower =
                    app.paradigmatic.paradigmaticapp.ui.screens.getTxPowerLevel(scanRecord.bytes)
                val iBeaconManufactureData = scanRecord.getManufacturerSpecificData(0X004c)
                if (serviceUuids != null && serviceUuids.size > 0 && serviceUuids.contains(
                        eddystoneServiceId
                    )
                ) {
                    val serviceData = scanRecord.getServiceData(eddystoneServiceId)
                    if (serviceData != null && serviceData.size > 18) {
                        val eddystoneUUID =
                            Utils.toHexString(Arrays.copyOfRange(serviceData, 2, 18))
                        val namespace = String(eddystoneUUID.toCharArray().sliceArray(0..19))
                        val instance = String(
                            eddystoneUUID.toCharArray()
                                .sliceArray(20 until eddystoneUUID.toCharArray().size)
                        )
                        beacon.type = Beacon.beaconType.eddystoneUID
                        beacon.namespace = namespace
                        beacon.instance = instance

                        Timber.d( "Namespace:$namespace Instance:$instance")
                    }
                }
                if (iBeaconManufactureData != null && iBeaconManufactureData.size >= 23) {
                    val iBeaconUUID = Utils.toHexString(iBeaconManufactureData.copyOfRange(2, 18))
                    val manufacturer = ""
                    val major = Integer.parseInt(
                        Utils.toHexString(
                            iBeaconManufactureData.copyOfRange(
                                18,
                                20
                            )
                        ), 16
                    )
                    val minor = Integer.parseInt(
                        Utils.toHexString(
                            iBeaconManufactureData.copyOfRange(
                                20,
                                22
                            )
                        ), 16
                    )

                    beacon.manufacturer = manufacturer
                    beacon.type = Beacon.beaconType.iBeacon
                    beacon.uuid = iBeaconUUID
                    beacon.major = major
                    beacon.minor = minor
                    beacon.txPower = txPower
                    beacon.proximity = Beacon.calculateProximity(
                        txPower = txPower,
                        rssi = beacon.rssi!!
                    )
                    ibeaconState = IBeacon(null, null)
                    ibeaconState.setUUID(beacon.uuid.toString())
                    ibeaconState.setRssi(beacon.rssi  ?: -1)
                    ibeaconState.setMajor(beacon.major ?: -1)
                    ibeaconState.setMinor(beacon.minor ?: -1)
                    ibeaconState.setTxPower(beacon.txPower ?: -1)
                    ibeaconState.setProximity(beacon.proximity ?: 0.0)


                    Timber.d( "- " +
                            "iBeaconUUID: ${ibeaconState.getUUID()} " +
                            "major:${ibeaconState.getMajor()} " +
                            "minor:${ibeaconState.getMinor()} " +
                            "rssi : ${beacon.rssi} + " +
                            "proximity: ${beacon.proximity}")

                }
            }
            beaconSet.add(beacon)
        }

        override fun onScanFailed(errorCode: Int) {
            // Handle scan failure, e.g., log an error
            Timber.d( "Scan failed with error code: $errorCode")
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
        contentAlignment = Alignment.Center
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
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Adjust top padding as needed
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    // Add the components that were previously above the LazyColumn here
                    // For example, Image, ClickableText, etc.

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CustomScreenBar(
                            stringResource(R.string.tab_2_name),
                            {
                                PermUtils.openAppSettings(context)
                            },
                            {
                                PermUtils.openAppSettings(context)
                            }
                        )


                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.appicon),
                            contentDescription = stringResource(id = R.string.content_description),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(150.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        RequestMultiplePermissions(
                            permissions = listOf(
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                        Divider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            thickness = 1.dp,
                            color = TertiaryColor
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Button(
                            onClick = {
                                if (!isScanning) {
                                    Toast.makeText(
                                        context,
                                        "Starting Scan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startBluetoothScanning(
                                        context = context,
                                        btScanner = btScanner,
                                        beacons = beacons,
                                        selectedBeaconType = selectedBeaconType,
                                        scope = scope,
                                        scanCallback = leScanCallback
                                    )
                                    isScanning = true
                                } else {
                                    if (isScanning) {
                                        Toast.makeText(
                                            context,
                                            "Stopping Scan",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        stopBluetoothScanning(btScanner, leScanCallback)
                                        isScanning = false
                                    }
                                }

                            }, modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = PrimaryColor
                            )
                        ) {
                            androidx.compose.material.Text(text = if (isScanning) "Stop Scanning" else "Start Scanning",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))

                    }

                }
                // Optional: Placeholder item for when the lists are empty
                items(
                    items = bleDeviceListState,
                    key = { item -> item.getAddress()}
                ) { bleDevice ->
                    BLEDeviceItem(navigator, bleDevice)
                }

                if (bleDeviceListState.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                } else {
                    item {
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }

            }
        }


    }
}


@Preview
@Composable
fun TabTwoScreenBakPreview() {
    TabTwoScreenBak(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}