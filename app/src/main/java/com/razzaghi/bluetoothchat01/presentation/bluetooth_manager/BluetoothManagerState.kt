package com.razzaghi.bluetoothchat01.presentation.bluetooth_manager

import com.razzaghi.bluetoothchat01.business.domain.BluetoothConnectionState
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState

data class BluetoothManagerState(
    val bluetoothConnectionState: BluetoothConnectionState = BluetoothConnectionState.None,
    val isMessageTransferring: ConnectionState = ConnectionState.None,
    val isConnectToDevice: ConnectionState = ConnectionState.None,
    val isConnectFromDevice: ConnectionState = ConnectionState.None,
    val messagesList: ArrayList<String> = arrayListOf(),
)
