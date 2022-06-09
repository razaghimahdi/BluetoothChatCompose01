package com.razzaghi.bluetoothchat01.business.util

import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.BLUETOOTH_SOCKET_TYPE_INSECURE
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.BLUETOOTH_SOCKET_TYPE_SECURE
import java.nio.charset.Charset

object SocketTools {



    fun Boolean.getSocketType():String = if (this) BLUETOOTH_SOCKET_TYPE_SECURE else BLUETOOTH_SOCKET_TYPE_INSECURE

    fun ByteArray.toCustomString()= this.toString(Charset.defaultCharset())

}