package com.razzaghi.bluetoothchat01.business.datasource.bluetooth_socket

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MY_UUID_INSECURE
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MY_UUID_SECURE

@SuppressLint("MissingPermission")
class BluetoothSocketSetupHandler(
    private val bluetoothDevice: BluetoothDevice,
    private val secure: Boolean,
) {
    private var bluetoothSocket: BluetoothSocket? = null

      init {
        bluetoothSocket = if (secure) {
            bluetoothDevice.createRfcommSocketToServiceRecord(
                MY_UUID_SECURE
            )
        } else {
            bluetoothDevice.createInsecureRfcommSocketToServiceRecord(
                MY_UUID_INSECURE
            )
        }
    }

    fun getSocket(): BluetoothSocket? {
        return bluetoothSocket
    }

    fun closeSocket() {
        bluetoothSocket?.close()
    }

    fun connect() {
        bluetoothSocket?.connect()
    }
}