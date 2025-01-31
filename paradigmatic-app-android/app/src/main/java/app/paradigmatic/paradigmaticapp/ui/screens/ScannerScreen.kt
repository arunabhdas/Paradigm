package app.paradigmatic.paradigmaticapp.ui.screens

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.os.ParcelUuid
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.data.beacon.Beacon
import app.paradigmatic.paradigmaticapp.data.blescanner.Utils
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.services.BeaconProToolsService
import app.paradigmatic.paradigmaticapp.data.beacon.BLEDevice
import app.paradigmatic.paradigmaticapp.data.beacon.IBeacon
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.SecondaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.TransparentColor
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import androidx.compose.foundation.lazy.items
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import java.util.Arrays

@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun ScannerScreen(
    // TODO-FIXME-DEPRECATE navController: NavController
    navigator: DestinationsNavigator
) {
    val gradientColors = listOf(
        PrimaryColor,
        SecondaryColor
    )
    var showExpandedText by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

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
    var isServiceScanning by remember { mutableStateOf(false) }
    var beacons by remember { mutableStateOf(listOf<Beacon>()) }
    var selectedBeaconType by remember { mutableStateOf(0) }
    var ibeaconState by rememberSaveable { mutableStateOf("iBeacon Info") }

    var iBeaconDeviceList: ArrayList<IBeacon> = arrayListOf()
    var bleDeviceList: ArrayList<BLEDevice> = arrayListOf()
    val bleDeviceListState = remember { mutableStateListOf<BLEDevice>() }

    LaunchedEffect(key1 = bleDeviceList) {
        bleDeviceListState.clear()
        bleDeviceListState.addAll(bleDeviceList)
    }

    /**
     * check if our device list already has a scan result whose MAC address is identical to the new incoming ScanResult
     * @param result BLE scan result
     * @return -1 if doesn't exist
     */
    fun checkIBeaconDeviceExists(result: ScanResult): Int {
        val indexQuery = iBeaconDeviceList.indexOfFirst { (it as BLEDevice) .getAddress() == result.device.address }
        return indexQuery
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
        key1 = permissionState.allPermissionsGranted,
    ) {
            Timber.d("- ScannerScreen")

    }



    val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val scanRecord = result.scanRecord
            val beacon = Beacon(
                result.device.address,
            )
            // TODO-FIXME-IMPROVE beacon.manufacturer = result.device.name
            beacon.rssi = result.rssi
            if (scanRecord != null) {

                if (isIBeacon(scanRecord.bytes)) {
                    val iBeacon = IBeacon(result, scanRecord.bytes)
                    val idx = checkIBeaconDeviceExists(result)
                    Timber.d(iBeacon.toString())
                    if (idx == -1) {
                        iBeaconDeviceList.add(iBeacon)
                    } else {
                        // update
                        iBeaconDeviceList[idx] = iBeacon
                    }
                    Timber.d("x----------------")
                    Timber.d(iBeaconDeviceList.size.toString())
                    Timber.d("x----------------")
                } else {
                    val ble = BLEDevice(result)
                    val idx = checkBleDeviceExists(result)
                    if (idx == -1) {
                        bleDeviceList.add(ble)

                        Timber.d("y----------------")
                        Timber.d(bleDeviceList.size.toString())
                        Timber.d("y----------------")
                    } else {
                        // update
                        bleDeviceList[idx] = ble
                    }
                }

                bleDeviceListState.clear()
                bleDeviceListState.addAll(bleDeviceList)


                val serviceUuids = scanRecord.serviceUuids
                val txPower = getTxPowerLevel(scanRecord.bytes)
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


                    beacon.type = Beacon.beaconType.iBeacon
                    beacon.uuid = iBeaconUUID
                    beacon.major = major
                    beacon.minor = minor
                    beacon.txPower = txPower
                    beacon.proximity = calculateProximity(
                        txPower = txPower,
                        rssi = beacon.rssi!!
                    )
                    ibeaconState = "iBeaconUUID:$iBeaconUUID major:$major minor:$minor rssi : ${beacon.rssi}"
                    // Timber.d( "- iBeaconUUID:$iBeaconUUID major:$major minor:$minor rssi : ${beacon.rssi} : ${beacon.txPower}")

                    /*
                    Timber.d( "- " +
                            "iBeaconUUID: $iBeaconUUID " +
                            "major:$major " +
                            "minor:$minor " +
                            "rssi : ${beacon.rssi} + " +
                            "proximity: ${beacon.proximity}")
                  */

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
                    colors = gradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = createGradientEffect(
                        colors = gradientColors,
                        isVertical = true
                    )
                )
                .padding(start = 16.dp, end = 16.dp) // Add left and right padding here
        ) {
            item {
                // Add the components that were previously above the LazyColumn here
                // For example, Image, ClickableText, etc.
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Image(
                        painter = painterResource(id = R.drawable.appicon),
                        contentDescription = stringResource(id = R.string.content_description),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ClickableText(
                        text = AnnotatedString(stringResource(id = R.string.app_name)),
                        style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                            showExpandedText = !showExpandedText
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedVisibility(visible = showExpandedText) {
                        Text(
                            text = "Powered by App Liaison Inc.",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = ibeaconState,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = {
                            if (!isScanning) {
                                Toast.makeText(
                                    context,
                                    "Starting Scan",
                                    Toast.LENGTH_LONG
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
                                        Toast.LENGTH_LONG
                                    ).show()
                                    stopBluetoothScanning(btScanner, leScanCallback)
                                    isScanning = false
                                }
                            }

                        }, modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(if (isScanning) "Stop Scanning" else "Start Scanning")
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                }


            }

            /*
            itemsIndexed(xlr8ProBeacons) { _, item ->
                Xlr8ProBeaconItem(navigator, item)
            }
            */

            item {
                // Add the components that were previously below the LazyColumn here
                // For example, Buttons

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(
                        onClick = {
                            if (!isServiceScanning) {
                                Toast.makeText(
                                    context,
                                    "Start Service",
                                    Toast.LENGTH_LONG
                                ).show()
                                Intent(context, BeaconProToolsService::class.java).also {
                                    it.action = BeaconProToolsService.Actions.START.toString()
                                    context.startService(it)
                                }
                                isServiceScanning = true
                            }

                        }, modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(text = "Start Service", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(
                        onClick = {
                            if (isServiceScanning) {
                                Toast.makeText(
                                    context,
                                    "Stop Service",
                                    Toast.LENGTH_LONG
                                ).show()
                                Intent(context, BeaconProToolsService::class.java).also {
                                    it.action = BeaconProToolsService.Actions.STOP.toString()
                                    context.startService(it)
                                }

                                isServiceScanning = false
                            }

                        }, modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(text = "Stop Service", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                                  navigator.popBackStack()
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .background(TransparentColor),
                        colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(text = "Back")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }


        }
    }


}



@Composable
fun BeaconTypeSpinner(selectedType: Int, onTypeSelected: (Int) -> Unit) {
    // Implement spinner logic using DropdownMenu or similar approach
}

@Composable
fun BeaconsList(beacons: List<Beacon>) {
    LazyColumn {
        items(beacons) { beacon ->
            // Render each beacon item
        }
    }
}

@SuppressLint("MissingPermission")
fun startBluetoothScanning(
    context: Context,
    btScanner: BluetoothLeScanner?,
    beacons: List<Beacon>,
    selectedBeaconType: Int,
    scope: CoroutineScope,
    scanCallback: ScanCallback
) {
    val filters = mutableListOf<ScanFilter>()
    val settings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    // Define the Eddystone Service UUID to filter
    val eddystoneServiceId = ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB")

    // Add filters based on selected beacon type
    when (selectedBeaconType) {
        // Example for Eddystone; adjust as needed for other types
        1 -> filters.add(ScanFilter.Builder().setServiceUuid(eddystoneServiceId).build())
        // Add other cases as necessary
    }

    if (!PermUtils.hasBluetoothPermissions(context)) {
        // Handle missing permissions
        Timber.d("- Missing Bluetooth Permissions")
        return
    }

    // 2. Check Bluetooth Availability
    val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val btAdapter = btManager.adapter
    if (btAdapter == null || !btAdapter.isEnabled) {
        // Handle Bluetooth not available or disabled
        Timber.d("- Bluetooth Adapter is null or not enabled")
        return
    }

    // 3. Requirements Checker (Implement this based on your app's specific needs)
    // This could include checking for specific hardware capabilities, etc.

    // 4. Reduce Callback Registrations
    // (Implement if you have additional features that register more callbacks)


    btScanner?.startScan(scanCallback)
}

@SuppressLint("MissingPermission")
fun stopBluetoothScanning(
    btScanner: BluetoothLeScanner?,
    scanCallback: ScanCallback
    ) {
    Timber.d("- stopBluetoothScanning stop beacon scan")
    // Implement logic to stop Bluetooth scanning
    btScanner?.stopScan(scanCallback)
}

private fun processScanResult(result: ScanResult): Beacon {
    // Process the ScanResult and convert it to your Beacon model
    // This includes parsing the advertisement data and extracting beacon information

    val beacon = Beacon(
        result.device.address,
    )
    // TODO-FIXME-PERMISSION-CHECK beacon.manufacturer = result.device.name
    beacon.rssi = result.rssi

    // Additional processing based on your Beacon model and BLE advertisement data
    // ...

    return beacon
}

fun getTxPowerLevel(scanRecord: ByteArray): Int {
    // Check for BLE 4.0 TX power
    val pos: Int = findCodeInBuffer(scanRecord, BeaconProToolsService.AssignedNumbers.TXPOWER)
    return if (pos > 0) {
        Integer.valueOf(scanRecord[pos].toInt())
    } else 1
}

private fun findCodeInBuffer(buffer: ByteArray, code: Byte): Int {
    val length = buffer.size
    var i = 0
    while (i < length - 2) {
        val len = buffer[i].toInt()
        if (len < 0) {
            return -1
        }
        if (i + len >= length) {
            return -1
        }
        val tcode = buffer[i + 1]
        if (tcode == code) {
            return i + 2
        }
        i += len + 1
    }
    return -1
}

private fun calculateProximity(txPower: Int, rssi: Int): Double {
    if (rssi == 0) {
        return -1.0 // if we cannot determine accuracy, return -1.
    }
    val ratio = rssi * 1.0 / txPower
    return if (ratio < 1.0) {
        Math.pow(ratio, 10.0)
    } else {
        0.89976 * Math.pow(ratio, 7.7095) + 0.111
    }
}

fun parseTxPower(iBeaconManufactureData: ByteArray): Int {
    // Extract the txPower value (8th byte following the iBeacon UUID, Major, and Minor)
    val txPowerByte = iBeaconManufactureData[23]

    // Convert the byte to an integer
    var txPower = txPowerByte.toInt()

    // Convert to a negative integer if txPowerByte is greater than 127 (two's complement)
    if (txPower > 127) {
        txPower = txPower - 256
    }

    return txPower
}


private fun calculateDistance(txPower: Int, rssi: Double): Double {
    if (rssi == 0.0) {
        return -1.0 // if we cannot determine distance, return -1.
    }

    val ratio = rssi / txPower
    return if (ratio < 1.0) {
        Math.pow(ratio, 10.0)
    } else {
        0.89976 * Math.pow(ratio, 7.7095) + 0.111
    }
}



@Composable
@Preview
fun ScannerScreenPreview() {
    // TODO-FIXME-CLEANUP LandingScreen(navigator = MockDestinationsNavigator())
    ScannerScreen(
        // navController = rememberNavController()
        navigator = MockDestinationsNavigator()
    )
}
