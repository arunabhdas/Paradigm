package app.paradigmatic.paradigmaticapp.ui.ibeacon

object ConversionUtils {

    /**
     * convert bytes data to hex string
     * https://stackoverflow.com/a/9855338
     * @param bytes input bytes
     * @return hex string
     */
    fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v: Int = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray.get(v ushr 4)
            hexChars[j * 2 + 1] = hexArray.get(v and 0x0F)
        }
        return String(hexChars)
    }
}