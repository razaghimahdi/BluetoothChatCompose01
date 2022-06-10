package com.razzaghi.bluetoothchat01.business.interactors.connect_from_other_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class CloseFromOtherDeviceInteractor {



    val TAG = "AppDebug CloseFromOtherDeviceInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(bluetoothSecureSocket: BluetoothServerSocket) : Flow<ConnectionState> = flow{
        Log.i(TAG, "execute: ")
        try {

            bluetoothSecureSocket.accept()
            emit(ConnectionState.Connected)

        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(ConnectionState.Failed)
        }
    }



}