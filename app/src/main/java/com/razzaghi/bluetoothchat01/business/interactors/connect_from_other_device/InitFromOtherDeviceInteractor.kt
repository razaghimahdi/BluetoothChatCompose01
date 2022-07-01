package com.razzaghi.bluetoothchat01.business.interactors.connect_from_other_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.core.DataState
import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class InitFromOtherDeviceInteractor {


    val TAG = "AppDebug InitFromOtherDeviceInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(
        bluetoothAdapter: BluetoothAdapter,
        secure: Boolean
    ): Flow<DataState<BluetoothServerSocket>> =
        flow {
            Log.i(TAG, "execute: ")
            try {
                emit(DataState.Loading(progressBarState = ProgressBarState.Loading))


                val bluetoothSecureServerSocket =
                    if (secure) bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                        BluetoothConstants.NAME_SECURE,
                        BluetoothConstants.MY_UUID_SECURE
                    ) else bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                        BluetoothConstants.NAME_INSECURE,
                        BluetoothConstants.MY_UUID_INSECURE
                    )


                emit(DataState.Data(ConnectionState.Inited, bluetoothSecureServerSocket))
            } catch (e: Exception) {
                Log.i(TAG, "execute e: " + e.message)
                emit(DataState.Data(ConnectionState.Failed))
            } finally {
                emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
            }
        }



}