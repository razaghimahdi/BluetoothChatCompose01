package com.razzaghi.bluetoothchat01.business.interactors.transfer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import java.lang.Exception

class InitTransferMessagesFromDevicesInteractor {

    val TAG = "AppDebug initTransferMessagesFromDevicesInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(
        bluetoothSocket: BluetoothSocket,
    ) : Flow<BluetoothSocket?> = flow{
        Log.i(TAG, "execute: ")
        try {

            emit(bluetoothSocket)
        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(null)
        }
    }
}