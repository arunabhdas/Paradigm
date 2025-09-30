package app.paradigmatic.paradigmaticapp.ui.ibeacon

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import app.paradigmatic.paradigmaticapp.data.beacon.BLEDevice
import app.paradigmatic.paradigmaticapp.data.beacon.IBeacon
import timber.log.Timber


class ScanService {

    private val bluetoothManager: BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter
    private val builder: ScanSettings.Builder
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private val TAG = "ScanService"

    // Scan service flag
    private var isScanning = false

    private lateinit var deviceList: ArrayList<Any>

    constructor(context: Context, deviceList: ArrayList<Any>) {
        this.deviceList = deviceList

        builder = ScanSettings.Builder()
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        Timber.d("- set scan mode to low latency")
        bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Timber.e(":-) Device doesn't support Bluetooth")
            throw Exception("Device doesn't support Bluetooth")
        }
        if (isBluetoothEnabled()) {
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
    }

    fun initScanner() {
        if (!this::bluetoothLeScanner.isInitialized) {
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
    }

    /**
     * Determine whether bluetooth is enabled or not
     *
     * @return true if bluetooth is enabled, false otherwise
     */
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    /**
     * Start BLE scan using bluetoothLeScanner
     * if app is not scanning, start scan by calling startScan and passing a callback method
     * else stop scan
     * @return {none}
     */
    fun startBLEScan() {
        if (isScanning)
            return
        Timber.d("@startBLEScan start beacon scan")
        isScanning = true
        try {
            bluetoothLeScanner.startScan(null, builder.build(), leScanCallback)
        } catch (e: SecurityException) {
            Timber.e("@startScan SecurityException: " + e.message)
        }
    }

    fun stopBLEScan() {
        if (!isScanning)
            return
        Timber.d( "@stopBLEScan stop beacon scan")
        isScanning = false
        bluetoothLeScanner.stopScan(leScanCallback)
    }

    /**
     * callback method when app detects BLE device
     */
    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if (result != null) {
                val scanRecord = result.scanRecord
                // TODO-FIXME-CLEANUP Log.e(TAG, "@result: " + result.device.uuids.size)
                super.onScanResult(callbackType, result)
                try {
                    if (scanRecord != null) {
                        if (isIBeacon(scanRecord.bytes)) {
                            val iBeacon = IBeacon(result, scanRecord.bytes)
                            val idx = checkDeviceExists(result)
                            Log.e(TAG, iBeacon.toString())
                            if (idx == -1) {
                                deviceList.add(iBeacon)
                            } else {
                                // update
                                deviceList[idx] = iBeacon
                            }
                        } else {
                            val ble = BLEDevice(result)
                            val idx = checkDeviceExists(result)
                            if (idx == -1) {
                                deviceList.add(ble)
                                Timber.d("---------" + deviceList.size)
                            } else {
                                // update
                                deviceList[idx] = ble
                            }
                        }
                    }
                } catch (e: SecurityException) {
                    Timber.e("@startScan SecurityException: " + e.message)
                }
            }
            return
        }
    }

    /**
     * check if our device list already has a scan result whose MAC address is identical to the new incoming ScanResult
     * @param result BLE scan result
     * @return -1 if doesn't exist
     */
    fun checkDeviceExists(result: ScanResult): Int {
        val indexQuery = deviceList.indexOfFirst { (it as BLEDevice) .getAddress() == result.device.address }
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

    fun isScanning(): Boolean {
        return isScanning
    }

}