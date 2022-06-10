package com.razzaghi.bluetoothchat01.business.interactors.connect_to_other_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class CloseToOtherDeviceInteractor {

    val TAG = "AppDebug CloseToOtherDeviceInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(bluetoothSocket: BluetoothSocket) : Flow<ConnectionState> = flow{
        Log.i(TAG, "connect: ")
        try {
            bluetoothSocket.close()

            emit(ConnectionState.Closed)

        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(ConnectionState.Failed)
        }
    }


}