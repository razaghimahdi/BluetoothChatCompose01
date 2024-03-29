package com.razzaghi.bluetoothchat01.business.interactors.transfer

import android.annotation.SuppressLint
import android.util.Log
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.core.DataState
import com.razzaghi.bluetoothchat01.business.core.ConnectionState
import com.razzaghi.bluetoothchat01.business.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import java.lang.Exception

class ReadTransferMessagesFromDevicesInteractor {

    val TAG = "AppDebug ReadTransferMessagesFromDevicesInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(
        inputStream: InputStream,
    ): Flow<DataState<Message>> = flow {
        Log.i(TAG, "execute: ")
        try {

            // emit(DataState.Loading(progressBarState = ProgressBarState.Loading)) no need loading here

            val buffer = ByteArray(1024)
            val bytes: Int

            bytes = inputStream.read(buffer) ?: 0

            val readMessage = String(buffer, 0, bytes)

            val milliSecondsTime = System.currentTimeMillis()
            val message = Message(message = readMessage ?: "", time = milliSecondsTime, type = BluetoothConstants.MESSAGE_TYPE_RECEIVED)


            emit(DataState.Data(ConnectionState.Connected, message))
        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(DataState.Data(ConnectionState.Failed))
        } /*finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }*/
    }
}