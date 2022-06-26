package com.razzaghi.bluetoothchat01.presentation.bluetooth_manager

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.business.domain.BluetoothConnectionState
import com.razzaghi.bluetoothchat01.presentation.screen.main.state.MainEvents

sealed class BluetoothManagerEvent {

    data class UpdateProgressBarState(val value: ProgressBarState) : BluetoothManagerEvent()

    data class ConnectToOtherDevice(val bluetoothDevice: BluetoothDevice, val secure: Boolean) :
        BluetoothManagerEvent()


    data class ConnectFromOtherDevice(val bluetoothAdapter: BluetoothAdapter ) :
        BluetoothManagerEvent()

    data class ReadFromTransferring(val bluetoothSocket: BluetoothSocket) : BluetoothManagerEvent()

    data class WriteFromTransferring(val message: ByteArray ) : BluetoothManagerEvent()

    data class CloseTransferring(val bluetoothSocket: BluetoothSocket) : BluetoothManagerEvent()

    object OnRemoveHeadFromQueue : BluetoothManagerEvent()

    object Start : BluetoothManagerEvent()

}
