package com.razzaghi.bluetoothchat01.presentation.bluetooth_manager

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket

sealed class BluetoothManagerEvent {

    data class ConnectToOtherDevice(val bluetoothDevice: BluetoothDevice, val secure: Boolean) :
        BluetoothManagerEvent()

    data class ConnectFromOtherDevice(val bluetoothAdapter: BluetoothAdapter ) :
        BluetoothManagerEvent()

    data class ReadFromTransferring(val bluetoothSocket: BluetoothSocket) : BluetoothManagerEvent()

    data class WriteFromTransferring(val message: ByteArray) : BluetoothManagerEvent()


    object Start : BluetoothManagerEvent()

}
