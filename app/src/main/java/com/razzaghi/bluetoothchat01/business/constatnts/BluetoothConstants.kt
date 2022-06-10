package com.razzaghi.bluetoothchat01.business.constatnts

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

object BluetoothConstants {

    // Unique UUID for this application
    val MY_UUID_SECURE = UUID.fromString("29621b37-e817-485a-a258-52da5261421a")
    val MY_UUID_INSECURE = UUID.fromString("d620cd2b-e0a4-435b-b02e-40324d57195b")


    // Name for the SDP record when creating server socket
    const val NAME_SECURE = "BluetoothChatSecure"
    const val NAME_INSECURE = "BluetoothChatInsecure"

    const val BLUETOOTH_SOCKET_TYPE_SECURE = "Secure"
    const val BLUETOOTH_SOCKET_TYPE_INSECURE = "Insecure"

      val DEFAULT_OUTPUT_STREAM =  ByteArrayOutputStream(1024)
      val DEFAULT_INPUT_STREAM =  ByteArrayInputStream(ByteArray(1024))



}