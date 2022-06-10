package com.razzaghi.bluetoothchat01.business.datasource.connect_to_other_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


@SuppressLint("MissingPermission", "LongLogTag")
// @Singleton
class ConnectToOtherDevice {

    private val TAG = "AppDebug ConnectToOtherDevice"


    private val _isConnectToDevice: MutableLiveData<ConnectionState> =
        MutableLiveData(ConnectionState.None)
    val isConnectToDevice get() = _isConnectToDevice


    /**
     * if isConnectToDevice == true then make device to get messages and cancelDiscovery from bluetooth adapter TODO()
     *
     * */

    private lateinit var bluetoothSocket: BluetoothSocket


    suspend fun init(
        bluetoothDevice: BluetoothDevice,
        secure: Boolean,
        bluetoothAdapter: BluetoothAdapter
    ) { // bluetoothDevice comes from UI
        Log.i(TAG, "initBluetoothSocket bluetoothDevice: " + bluetoothDevice)
        Log.i(TAG, "initBluetoothSocket secure: " + secure)

        // Always cancel discovery because it will slow down a connection
        bluetoothAdapter.cancelDiscovery()

        try {
            bluetoothSocket = if (secure) {
                bluetoothDevice.createRfcommSocketToServiceRecord(
                    BluetoothConstants.MY_UUID_SECURE
                )
            } else {
                bluetoothDevice.createInsecureRfcommSocketToServiceRecord(
                    BluetoothConstants.MY_UUID_INSECURE
                )
            }
            withContext(Dispatchers.Main){
                _isConnectToDevice.value = ConnectionState.Inited
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _isConnectToDevice.value = ConnectionState.Failed
            }
            Log.i(TAG, "init e: " + e.message)
        }
    }


    suspend fun connect() {
        Log.i(TAG, "connect: ")
        try {
            bluetoothSocket.connect()

            withContext(Dispatchers.Main) {
                _isConnectToDevice.value = ConnectionState.Connected
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _isConnectToDevice.value = ConnectionState.Failed
            }
            Log.i(TAG, "connect e: " + e.message)

        }
    }

    suspend fun close() {
        Log.i(TAG, "closeSocket: ")
        try {
            bluetoothSocket.close()
            withContext(Dispatchers.Main) {
                _isConnectToDevice.value = ConnectionState.Closed
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _isConnectToDevice.value = ConnectionState.Failed
            }
            Log.i(TAG, "close e: " + e.message)
        }
    }

    fun getSocket(): BluetoothSocket {
        Log.i(TAG, "getSocket: ")
        return bluetoothSocket
    }

    fun currentStateIsNotFailed() = isConnectToDevice.value != ConnectionState.Failed

    fun currentStateIsConnected() = isConnectToDevice.value == ConnectionState.Connected

}