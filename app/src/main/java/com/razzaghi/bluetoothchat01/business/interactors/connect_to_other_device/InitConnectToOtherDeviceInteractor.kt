package com.razzaghi.bluetoothchat01.business.interactors.connect_to_other_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import kotlin.concurrent.thread

class InitConnectToOtherDeviceInteractor {

    val TAG = "AppDebug InitConnectToOtherDeviceInteractor"

    @SuppressLint("MissingPermission", "LongLogTag")
    fun execute(
        bluetoothDevice: BluetoothDevice,
        secure: Boolean,
        bluetoothAdapter: BluetoothAdapter
    ) : Flow<BluetoothSocket?> = flow{
        Log.i(TAG, "execute: ")
        try {
         //   thread{

           /*  kotlin.runCatching {

             }*/


            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery()

          val  bluetoothSocket = if (secure) {
                bluetoothDevice.createRfcommSocketToServiceRecord(
                    BluetoothConstants.MY_UUID_SECURE
                )
            } else {
                bluetoothDevice.createInsecureRfcommSocketToServiceRecord(
                    BluetoothConstants.MY_UUID_INSECURE
                )
            }

        //}

            emit(bluetoothSocket)

        } catch (e: Exception) {
            Log.i(TAG, "execute e: " + e.message)
            emit(null)
        }


    }


}