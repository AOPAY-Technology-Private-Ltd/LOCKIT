package com.bosandroidapp.aopaykit.utils

import android.content.Context
import android.provider.Settings
import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val NTP_SERVER = "time.google.com"
private const val NTP_PORT = 123
private const val NTP_EPOCH_OFFSET = 2208988800L // seconds between 1900 and 1970


fun getNetworkTime(context: Context?): Pair<Date?, Boolean> {
    if(isAutomaticTimeEnabled(context)){
        return Pair(Calendar.getInstance().time, true)
    }
    else {
        return try {
            val buffer = ByteArray(48)
            buffer[0] = 0b00100011 // NTP mode 3 (client), version 4

            val address = InetAddress.getByName(NTP_SERVER)
            val packet = DatagramPacket(buffer, buffer.size, address, NTP_PORT)
            val socket = DatagramSocket()
            socket.soTimeout = 5000
            socket.send(packet)


            val response = DatagramPacket(buffer, buffer.size)
            socket.receive(response)

            // Read 64-bit transmit timestamp (seconds + fraction)
            val seconds = ((buffer[40].toLong() and 0xFF) shl 24) or
                    ((buffer[41].toLong() and 0xFF) shl 16) or
                    ((buffer[42].toLong() and 0xFF) shl 8) or
                    (buffer[43].toLong() and 0xFF)

            val fraction = ((buffer[44].toLong() and 0xFF) shl 24) or
                    ((buffer[45].toLong() and 0xFF) shl 16) or
                    ((buffer[46].toLong() and 0xFF) shl 8) or
                    (buffer[47].toLong() and 0xFF)

            // Convert to milliseconds since Unix epoch
            val ms = ((seconds - NTP_EPOCH_OFFSET) * 1000L) +
                    ((fraction * 1000L) ushr 32)
            Log.d("SntpClient", "${Date(ms)}")
            Pair(Date(ms), true)
        }
        catch (e: Exception) {
            Pair(Calendar.getInstance().time, false)
        }
    }

}


fun isAutomaticTimeEnabled(context: Context?): Boolean {
    return try {
        Settings.Global.getInt(context?.contentResolver, Settings.Global.AUTO_TIME) == 1
    } catch (e: Exception) {
        false
    }
}


fun Date.formatDate(): String {
    val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    return sdf.format(this).replace("-", "/")
}






