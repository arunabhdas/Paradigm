package app.paradigmatic.paradigmaticapp.data.beacon


class Beacon(
    macAddress: String?,
) {
    enum class beaconType {
        iBeacon, eddystoneUID, any
    }

    val macAddress = macAddress
    var manufacturer: String? = null
    var type: beaconType = beaconType.any
    var uuid: String? = null
    var major: Int? = null
    var minor: Int? = null
    var namespace: String? = null
    var instance: String? = null
    var rssi: Int? = null
    var txPower: Int? = null
    var proximity: Double? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Beacon) return false

        if (macAddress != other.macAddress) return false

        return true
    }

    override fun hashCode(): Int {
        return macAddress?.hashCode() ?: 0
    }
    companion object {
        fun calculateProximity(txPower: Int, rssi: Int): Double {
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
    }


}
