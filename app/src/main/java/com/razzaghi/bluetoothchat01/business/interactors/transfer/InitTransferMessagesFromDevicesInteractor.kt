package com.razzaghi.bluetoothchat01.business.interactors.transfer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.core.DataState
import com.razzaghi.bluetoothchat01.business.core.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class InitTransferMessagesFromDevicesInteractor {

    val TAG = "AppDebug initTransferMessagesFromDevicesInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(
        bluetoothSocket: BluetoothSocket,
    ) : Flow<DataState<BluetoothSocket>> = flow{
        Log.i(TAG, "execute: ")
        try {
           // emit(DataState.Loading(progressBarState = ProgressBarState.Loading)) no loading need here

            emit(DataState.Data(ConnectionState.Inited, bluetoothSocket))
        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(DataState.Data(ConnectionState.Failed))
        }/*finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }*/
    }
}