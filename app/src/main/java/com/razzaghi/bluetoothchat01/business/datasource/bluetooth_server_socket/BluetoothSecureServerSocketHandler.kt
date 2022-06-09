package com.razzaghi.bluetoothchat01.business.datasource.bluetooth_server_socket

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MY_UUID_INSECURE
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MY_UUID_SECURE
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.NAME_INSECURE
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.NAME_SECURE
import java.io.IOException

@SuppressLint("MissingPermission", "LongLogTag")
class BluetoothSecureServerSocketHandler(
    private val bluetoothAdapter: BluetoothAdapter,
    private val secure:Boolean,
) {
    private var bluetoothServerSocket: BluetoothServerSocket? = null

    private val TAG = "AppDebug BluetoothInSecureServerSocketHandler"

    init {
        bluetoothServerSocket =
            if (secure) {
                bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                    NAME_SECURE,
                    MY_UUID_SECURE
                )
            } else {
                bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    NAME_INSECURE, MY_UUID_INSECURE
                )
            }
    }

    fun getServerSocket(): BluetoothServerSocket? {
        return bluetoothServerSocket
    }

    fun closeServerSocket() {
        bluetoothServerSocket?.close()
    }

    // Listen to the server socket if we're not connected
    fun run(connectionAcceptCallback:()->Unit) {
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            bluetoothServerSocket?.accept()
        } catch (e: IOException) {
            Log.e(TAG, "e: " + e)
        }

        // If a connection was accepted
        if (bluetoothServerSocket != null) {
            connectionAcceptCallback()
        }
    }
}