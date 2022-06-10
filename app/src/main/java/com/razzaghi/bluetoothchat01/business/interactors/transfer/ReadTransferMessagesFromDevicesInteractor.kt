package com.razzaghi.bluetoothchat01.business.interactors.transfer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import com.razzaghi.bluetoothchat01.business.util.SocketTools.toCustomString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception

class ReadTransferMessagesFromDevicesInteractor {

    val TAG = "AppDebug ReadTransferMessagesFromDevicesInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(
        inputStream: InputStream,
    ) : Flow<String> = flow{
        Log.i(TAG, "execute: ")
        val buffer = ByteArray(1024)
        val bytes: Int
        try {
            bytes = inputStream.read(buffer) ?: 0

            emit(inputStream.bufferedReader().use { it.readText() })// defaults to UTF-8
        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(ConnectionState.Failed.toString())
        }
    }
}