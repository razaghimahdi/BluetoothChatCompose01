package com.razzaghi.bluetoothchat01.business.interactors.connect_to_other_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.core.DataState
import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.business.core.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class ConnectToOtherDeviceInteractor {

    val TAG = "AppDebug ConnectToOtherDeviceInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(bluetoothSocket: BluetoothSocket): Flow<DataState<ConnectionState>> = flow {
        Log.i(TAG, "connect: ")
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))
            bluetoothSocket.connect()

            emit(DataState.Data(ConnectionState.Connected))

        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(DataState.Data(ConnectionState.Failed))
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }


}