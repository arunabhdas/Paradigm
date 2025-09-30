package app.paradigmatic.paradigmaticapp.data.blescanner

import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.util.Arrays


class AdRecord(length: Int, type: Int, data: ByteArray?) {
    init {
        var decodedRecord = ""
        try {
            decodedRecord = String(data!!, charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        Timber.d(
            "Length: " + length + " Type : " + type + " Data : " + data?.let { ByteArrayToString(it) }
        )
    }

    companion object {
        fun parseScanRecord(scanRecord: ByteArray): List<AdRecord> {
            val records: MutableList<AdRecord> = ArrayList()
            var index = 0
            while (index < scanRecord.size) {
                val length = scanRecord[index++].toInt()
                //Done once we run out of records
                if (length == 0) break
                val type = scanRecord[index].toInt()
                //Done if our record isn't a valid type
                if (type == 0) break
                val data = Arrays.copyOfRange(scanRecord, index + 1, index + length)
                records.add(AdRecord(length, type, data))
                //Advance
                index += length
            }
            return records
        }
    }

    fun ByteArrayToString(ba: ByteArray): String {
        val hex = StringBuilder(ba.size * 2)
        for (b in ba) hex.append("$b ")
        return hex.toString()
    }

}