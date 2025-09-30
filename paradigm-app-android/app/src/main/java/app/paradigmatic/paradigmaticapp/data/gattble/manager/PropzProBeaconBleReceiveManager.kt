package app.paradigmatic.paradigmaticapp.data.gattble.manager

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import app.paradigmatic.paradigmaticapp.data.gattble.CCCD_DESCRIPTOR_UUID
import app.paradigmatic.paradigmaticapp.data.gattble.isIndicatable
import app.paradigmatic.paradigmaticapp.data.gattble.isNotifiable
import app.paradigmatic.paradigmaticapp.data.gattble.model.ConnectionState
import app.paradigmatic.paradigmaticapp.data.gattble.model.PropzProBeaconResult
import app.paradigmatic.paradigmaticapp.data.gattble.printGattTable
import app.paradigmatic.paradigmaticapp.data.gattble.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

// @SuppressLint("MissingPermission")
class PropzProBeaconBleReceiveManager @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context
): PropzProBeaconReceiveManager {

    private val DEVICE_NAME = "iBeacon"
    private val BEACON_SERVICE_UIID = "0000aa20-0000-1000-8000-00805f9b34fb"
    private val BEACON_CHARACTERISTICS_UUID = "0000aa21-0000-1000-8000-00805f9b34fb"

    override val data: MutableSharedFlow<Resource<PropzProBeaconResult>> = MutableSharedFlow()

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var gatt: BluetoothGatt? = null

    private var isScanning = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private var currentConnectionAttempt = 1

    private var MAXIMUM_CONNECTION_ATTEMPTS = 5


    private val scanCallback = object: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Timber.d("- scanCallback - onScanResult")
            if (result.device.name == DEVICE_NAME) {
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Connecting to device"))
                }
            }

            if (isScanning) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                Timber.d("- Connnect Gatt will be called")
                result.device.connectGatt(
                    context,
                    false,
                    gattCallback
                )
            }

        }
    }

    private val gattCallback = object: BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            Timber.d("- onServicesDiscovered")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    coroutineScope.launch {
                        data.emit(
                            Resource.Loading(
                                message = "Discovering Services"
                            )
                        )
                    }
                    gatt.discoverServices()
                    this@PropzProBeaconBleReceiveManager.gatt = gatt
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    coroutineScope.launch {
                        data.emit(
                            Resource.Success(
                            data = PropzProBeaconResult(
                                0f,
                                0f,
                                0f,
                                "Zone1",
                                0f,
                                0f,
                                "",
                                0f,
                                ConnectionState.Connected
                            )
                        ))
                    }
                    gatt.close()
                }
            } else {
                gatt.close()
                currentConnectionAttempt+=1

                coroutineScope.launch {
                    data.emit(
                        Resource.Loading(
                            message = "Attempting to connect $currentConnectionAttempt/$MAXIMUM_CONNECTION_ATTEMPTS"
                        )
                    )
                }

                if (currentConnectionAttempt <= MAXIMUM_CONNECTION_ATTEMPTS) {
                    startReceiving()
                } else {
                    coroutineScope.launch {
                        data.emit(
                            Resource.Error(
                            errorMessage = "Could not connect to ble device"
                        ))
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Timber.d("- onServicesDiscovered")
            with(gatt) {
                printGattTable()
                coroutineScope.launch {
                    data.emit(
                        Resource.Loading(
                        message = "Adjusting MTU space"
                    ))
                }
                gatt.requestMtu(517)
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            val characteristic = findCharacteristics(
                BEACON_SERVICE_UIID,
                BEACON_CHARACTERISTICS_UUID
            )

            if (characteristic == null) {
                coroutineScope.launch {
                    data.emit(
                        Resource.Error(
                        errorMessage = "Could not find beacon publisher"
                    ))
                }
                return
            }
            enableNotification(characteristic)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            Timber.d("- onCharacteristicChanged")
            with(characteristic) {
                when(uuid) {
                    UUID.fromString(BEACON_CHARACTERISTICS_UUID) -> {
                        // XX XX XX XX XX XX
                        // 23 45 12 00 12 34
                        val multiplicator = if (value.first().toInt() > 0) -1 else 1
                        val temperature = value[1].toInt() + value[2].toInt() / 10f
                        val humidity = value[4].toInt() + value[5].toInt() / 10f
                        val tempHumidityResult = PropzProBeaconResult(
                            multiplicator * temperature,
                            humidity,
                            humidity,
                            "humidity",
                            humidity,
                            humidity,
                            "humidity",
                            humidity,
                            ConnectionState.Connected
                        )
                        coroutineScope.launch {
                            data.emit(
                                Resource.Success(data = tempHumidityResult)
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun enableNotification(characteristic: BluetoothGattCharacteristic) {
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        val payload = when {
            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            else -> return
        }

        characteristic.getDescriptor(cccdUuid)?.let {cccdDescriptor ->
            if (gatt?.setCharacteristicNotification(characteristic, true) == false) {
                Timber.d( "set characteristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, payload)
        }
    }

    private fun writeDescription(
        descriptor: BluetoothGattDescriptor,
        payload: ByteArray
    ) {
        gatt?.let {gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Not connected to a BLE device")
    }

    private fun findCharacteristics(
        serviceUUID: String,
        charactericsUUID: String
    ): BluetoothGattCharacteristic? {
        return gatt?.services?.find {service ->
            service.uuid.toString() == serviceUUID
        }?.characteristics?.find { characteristics ->
            characteristics.uuid.toString() == charactericsUUID
        }
    }

    override fun startReceiving() {
        coroutineScope.launch {
            Timber.d("- Scanning for iBeacons")
            data.emit(
                Resource.Loading(
                message = "Scanning for iBeacons"
            ))
        }
        isScanning = true
        bleScanner.startScan(null, scanSettings, scanCallback)

    }

    override fun reconnect() {
        gatt?.connect()
    }

    override fun disconnect() {
        gatt?.disconnect()
    }


    override fun closeConnection() {
        bleScanner.stopScan(scanCallback)
        val characteristic = findCharacteristics(
            BEACON_SERVICE_UIID,
            BEACON_CHARACTERISTICS_UUID
        )
        if (characteristic != null) {
            disconnectCharacteristic(characteristic)
        }
        gatt?.close()
    }

    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic) {
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        Timber.d("Set characteristics notification failed")
        characteristic.getDescriptor(cccdUuid)?.let {cccdDescriptor ->
            if (gatt?.setCharacteristicNotification(characteristic, false) == false) {
                Timber.d("Set characteristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        }
    }

}