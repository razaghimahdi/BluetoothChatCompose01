package com.razzaghi.bluetoothchat01.presentation.bluetooth_manager

import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.DEFAULT_INPUT_STREAM
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.DEFAULT_OUTPUT_STREAM
import com.razzaghi.bluetoothchat01.business.core.Queue
import com.razzaghi.bluetoothchat01.business.domain.BluetoothConnectionState
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import com.razzaghi.bluetoothchat01.business.domain.Dialog
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

data class BluetoothManagerState(
    val bluetoothConnectionState: BluetoothConnectionState = BluetoothConnectionState.None,
    val messagesList: ArrayList<String> = arrayListOf(),
    val errorQueue: Queue<Dialog> = Queue(mutableListOf()),
    val outputStream: OutputStream = DEFAULT_OUTPUT_STREAM,
    val inputStream: InputStream = DEFAULT_INPUT_STREAM,
)
