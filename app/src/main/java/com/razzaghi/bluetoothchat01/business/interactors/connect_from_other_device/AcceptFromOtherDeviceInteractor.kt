package com.razzaghi.bluetoothchat01.business.interactors.connect_from_other_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.core.DataState
import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class AcceptFromOtherDeviceInteractor {



    val TAG = "AppDebug AcceptFromOtherDeviceInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(bluetoothServerSocket: BluetoothServerSocket) : Flow<DataState<BluetoothSocket>> = flow{
        Log.i(TAG, "execute: ")
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            val bluetoothSocket = bluetoothServerSocket.accept()

            emit(DataState.Data(ConnectionState.Connected, bluetoothSocket))

        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(DataState.Data(ConnectionState.Failed))
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }



}