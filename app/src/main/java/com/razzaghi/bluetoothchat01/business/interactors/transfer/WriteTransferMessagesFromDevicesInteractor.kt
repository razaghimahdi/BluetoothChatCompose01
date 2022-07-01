package com.razzaghi.bluetoothchat01.business.interactors.transfer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.core.DataState
import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import com.razzaghi.bluetoothchat01.business.domain.Message
import com.razzaghi.bluetoothchat01.business.util.SocketTools.toCustomString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception

class WriteTransferMessagesFromDevicesInteractor {

    val TAG = "AppDebug WriteTransferMessagesFromDevicesInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(
        buffer: ByteArray,
        outputStream: OutputStream,
    ): Flow<DataState<Message>> = flow {
        Log.i(TAG, "execute: ")
        try {

            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            outputStream.write(buffer)

            val msg = buffer.toCustomString()
            val milliSecondsTime = System.currentTimeMillis()
            val message = Message(message = msg ?: "", time = milliSecondsTime, type = BluetoothConstants.MESSAGE_TYPE_SENT)

            emit(DataState.Data(ConnectionState.Connected, message))

        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(DataState.Data(ConnectionState.Failed))
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}