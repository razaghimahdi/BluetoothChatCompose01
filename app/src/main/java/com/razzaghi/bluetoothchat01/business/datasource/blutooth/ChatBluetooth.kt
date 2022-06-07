package com.razzaghi.bluetoothchat01.business.datasource.blutooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.lifecycle.MutableLiveData
import com.razzaghi.bluetoothchat01.business.domain.BluetoothState

interface ChatBluetooth {


    suspend fun start()

    /**
     * This runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    suspend fun connectToBluetoothSocket(device: BluetoothDevice, secure: Boolean)

    /**
     * This runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    suspend fun connectToBluetoothServerSocket(adapter: BluetoothAdapter, secure: Boolean)



    suspend fun TransferMessage(socket: BluetoothSocket?, device: BluetoothDevice?, socketType: String)

    suspend fun stop()


    suspend fun connectionFailed()

    suspend fun connectionLost()

}