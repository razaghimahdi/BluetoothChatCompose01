package com.razzaghi.bluetoothchat01.business.datasource.bluetooth_server_socket

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MY_UUID_INSECURE
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MY_UUID_SECURE
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.NAME_INSECURE
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.NAME_SECURE

@SuppressLint("MissingPermission")
class BluetoothSecureServerSocketHandler(
    private val bluetoothAdapter: BluetoothAdapter,
) {
    private var bluetoothServerSocket: BluetoothServerSocket? = null

    init {
        bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(
            NAME_SECURE,
            MY_UUID_SECURE
        )
    }

    fun getServerSocket(): BluetoothServerSocket? {
        return bluetoothServerSocket
    }

    fun closeServerSocket() {
        bluetoothServerSocket?.close()
    }

    fun accept() {
        bluetoothServerSocket?.accept()
    }
}