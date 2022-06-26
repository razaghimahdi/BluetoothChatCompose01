package com.razzaghi.bluetoothchat01.business.util

import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MESSAGE_TYPE_RECEIVED
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MESSAGE_TYPE_SENT
import java.text.SimpleDateFormat
import java.util.*

object MessageTools {

    fun Int.convertToString():String{
        if (this == MESSAGE_TYPE_SENT) return "You"
        if (this == MESSAGE_TYPE_RECEIVED) return "User"
        return "Unknown"
    }



    fun Long.convertToString(): String {
        val df: SimpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val resultdate = Date(this)

        return df.format(resultdate)
    }


}