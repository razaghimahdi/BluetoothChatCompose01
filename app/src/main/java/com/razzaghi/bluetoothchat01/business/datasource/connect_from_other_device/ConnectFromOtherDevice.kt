package com.razzaghi.bluetoothchat01.business.datasource.connect_from_other_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import java.io.IOException

@SuppressLint("MissingPermission", "LongLogTag")
class ConnectFromOtherDevice {

    private val TAG = "AppDebug ConnectFromOtherDevice"
    private lateinit var bluetoothSecureServerSocket: BluetoothServerSocket
    private lateinit var bluetoothInSecureServerSocket: BluetoothServerSocket
    private lateinit var bluetoothSecureSocket: BluetoothSocket
    private lateinit var bluetoothInSecureSocket: BluetoothSocket


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


    fun init(bluetoothAdapter: BluetoothAdapter) {
        Log.i(TAG, "init bluetoothAdapter: " + bluetoothAdapter)
        try {

            bluetoothSecureServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                BluetoothConstants.NAME_SECURE,
                BluetoothConstants.MY_UUID_SECURE
            )
            bluetoothInSecureServerSocket =
                bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    BluetoothConstants.NAME_INSECURE, BluetoothConstants.MY_UUID_INSECURE
                )

            _isConnectFromDevice.value = ConnectionState.Inited
        } catch (e: Exception) {
            Log.i(TAG, "init e: " + e)
            _isConnectFromDevice.value = ConnectionState.Failed
        }

    }


    fun accept() {
        try {
            bluetoothSecureSocket = bluetoothSecureServerSocket.accept()
            bluetoothInSecureSocket = bluetoothInSecureServerSocket.accept()
            _isConnectFromDevice.value = ConnectionState.Connected
            Log.i(TAG, "accept: ")
        } catch (e: Exception) {
            Log.i(TAG, "accept e: " + e.message)
            _isConnectFromDevice.value = ConnectionState.Failed
            close()
        }
    }

    fun getSecureServerSocket(): BluetoothServerSocket {
        return bluetoothSecureServerSocket
    }

    fun getInSecureServerSocket(): BluetoothServerSocket {
        return bluetoothInSecureServerSocket
    }

    fun getSecureSocket(): BluetoothSocket {
        Log.i(TAG, "getSecureSocket: ")
        return bluetoothSecureSocket
    }
    fun getInSecureSocket(): BluetoothSocket {
        Log.i(TAG, "getInSecureSocket: ")
        return bluetoothInSecureSocket
    }

    fun close() {
        try {
            bluetoothSecureServerSocket.close()
            bluetoothInSecureServerSocket.close()
            _isConnectFromDevice.value = ConnectionState.Closed
            Log.i(TAG, "close: ")
        } catch (e: Exception) {
            Log.i(TAG, "close e: " + e.message)
            _isConnectFromDevice.value = ConnectionState.Failed
        }
    }

    fun currentStateIsNotFailed() = isConnectFromDevice.value != ConnectionState.Failed
    fun currentStateIsConnected() = isConnectFromDevice.value == ConnectionState.Connected

}