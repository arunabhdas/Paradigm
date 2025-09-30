package app.paradigmatic.paradigmaticapp.data.beacon

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult

@SuppressLint("MissingPermission")
open class BLEDevice(scanResult: ScanResult?) {

    /**
     * The measured signal strength of the Bluetooth packet
     */
    private var rssi: Int = 0

    /**
     * The transmitted signal strength of the Bluetooth packet at 1m
     */
    private var txPower: Int = 0

    /**
     * Device mac address
     */
    private var address: String = ""

    /**
     * Device friendly name
     */
    private var name: String = ""


    init {
        if (scanResult?.device?.name != null) {
            name = scanResult.device.name
        }
        address = scanResult?.device?.address.toString()
        rssi = scanResult?.rssi ?: 0
    }

    fun getAddress(): String {
        return address
    }

    fun setAddress(address: String) {
        this.address = address
    }

    fun getRssi(): Int {
        return rssi
    }

    fun setRssi(rssi: Int) {
        this.rssi = rssi
    }

    fun getTxPower(): Int {
        return txPower
    }

    fun setTxPower(txPower: Int) {
        this.txPower = txPower
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }
}