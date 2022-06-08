package com.razzaghi.bluetoothchat01.business.datasource.connect_from_other_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import java.io.IOException

@SuppressLint("MissingPermission", "LongLogTag")
class ConnectFromOtherDevice {

    private val TAG = "AppDebug ConnectFromOtherDevice"
    private lateinit var bluetoothServerSocket: BluetoothServerSocket


    private val _isConnectFromDevice: MutableLiveData<ConnectionState> =
        MutableLiveData(ConnectionState.None)
    val isConnectFromDevice get() = _isConnectFromDevice

    /**
    TODO()
    // If a connection was accepted
    if (bluetoothServerSocket != null) {
    connectionAcceptCallback()
    }
     * */


    fun init(bluetoothAdapter: BluetoothAdapter, secure: Boolean) {
        Log.i(TAG, "init bluetoothAdapter: " + bluetoothAdapter)
        Log.i(TAG, "init secure: " + secure)
        try {

            bluetoothServerSocket =
                if (secure) {
                    bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                        BluetoothConstants.NAME_SECURE,
                        BluetoothConstants.MY_UUID_SECURE
                    )
                } else {
                    bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                        BluetoothConstants.NAME_INSECURE, BluetoothConstants.MY_UUID_INSECURE
                    )
                }

            _isConnectFromDevice.value = ConnectionState.Inited
        } catch (e: Exception) {
            Log.i(TAG, "init e: " + e)
            _isConnectFromDevice.value = ConnectionState.Failed
        }

    }


    fun accept() {
        try {
            bluetoothServerSocket.accept()
            _isConnectFromDevice.value = ConnectionState.Connected
            Log.i(TAG, "accept: ")
        } catch (e: Exception) {
            Log.i(TAG, "accept e: " + e.message)
            _isConnectFromDevice.value = ConnectionState.Failed
        }
    }

    fun getServerSocket(): BluetoothServerSocket {
        return bluetoothServerSocket
    }

    fun close() {
        try {
            bluetoothServerSocket.close()
            _isConnectFromDevice.value = ConnectionState.Closed
            Log.i(TAG, "close: ")
        } catch (e: Exception) {
            Log.i(TAG, "close e: " + e.message)
            _isConnectFromDevice.value = ConnectionState.Failed
        }
    }

}