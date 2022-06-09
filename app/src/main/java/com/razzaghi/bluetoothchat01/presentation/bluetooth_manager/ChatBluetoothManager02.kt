package com.razzaghi.bluetoothchat01.presentation.bluetooth_manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.razzaghi.bluetoothchat01.business.datasource.connect_from_other_device.ConnectFromOtherDevice
import com.razzaghi.bluetoothchat01.business.datasource.connect_to_other_device.ConnectToOtherDevice
import com.razzaghi.bluetoothchat01.business.datasource.transfer.TransferMessagesFromDevices
import com.razzaghi.bluetoothchat01.business.domain.BluetoothConnectionState
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState

@SuppressLint("LongLogTag", "MissingPermission")
class ChatBluetoothManager02(
    private val bluetoothAdapter: BluetoothAdapter,
) {

    private val TAG = "AppDebug ChatBluetoothImpl"

    private var transferMessagesFromDevices: TransferMessagesFromDevices? = null
    private var connectToOtherDevice: ConnectToOtherDevice? = null
    private var connectFromOtherDevice: ConnectFromOtherDevice? = null

    private val state: MutableLiveData<BluetoothManagerState> =
        MutableLiveData(BluetoothManagerState())


    fun onTriggerEvent(event: BluetoothManagerEvent) {
        when (event) {
            is BluetoothManagerEvent.ReadFromTransferring -> {
                readFromTransferring(
                    bluetoothSocket = event.bluetoothSocket,
                )
            }
            is BluetoothManagerEvent.WriteFromTransferring -> {
                writeFromTransferring(
                    message = event.message,
                )
            }
            is BluetoothManagerEvent.ConnectToOtherDevice -> {
                makeConnectToOtherDevice(
                    bluetoothDevice = event.bluetoothDevice,
                    secure = event.secure
                )
            }
            is BluetoothManagerEvent.ConnectFromOtherDevice -> {
                makeConnectFromOtherDevice(
                    bluetoothAdapter = event.bluetoothAdapter,
                )
            }
            is BluetoothManagerEvent.Start -> {
                start()
            }
        }
    }


    init {

    }

    private fun writeFromTransferring(message: ByteArray) {
        transferMessagesFromDevices?.apply {
            if (this.currentStateIsNotFailed()) addedToMessageList(this.write(message))
        }
    }

    private fun readFromTransferring(bluetoothSocket: BluetoothSocket) {
        transferMessagesFromDevices = TransferMessagesFromDevices()
        transferMessagesFromDevices?.apply {
            if (this.isMessageTransferring.value == ConnectionState.Connected) {
                this.close()
            }
            this.init(bluetoothSocket = bluetoothSocket)
            if (this.currentStateIsNotFailed()) addedToMessageList(this.read())
            checkIfReadingMessagesFromSocketFailed(value = this.isMessageTransferring.value == ConnectionState.Failed)
        }
    }

    private fun checkIfReadingMessagesFromSocketFailed(value: Boolean) {
        if (value) {
            onTriggerEvent(BluetoothManagerEvent.Start)
        }
    }

    private fun makeConnectFromOtherDevice(bluetoothAdapter: BluetoothAdapter) {
        connectFromOtherDevice = ConnectFromOtherDevice()
        connectFromOtherDevice?.apply {
            if (this.isConnectFromDevice.value == ConnectionState.Connected) {
                this.close()
            }
            this.init(bluetoothAdapter = bluetoothAdapter)
            if (this.currentStateIsNotFailed()) this.accept()
            if (this.currentStateIsConnected()) {
                onTriggerEvent(BluetoothManagerEvent.ReadFromTransferring(bluetoothSocket = this.getSecureSocket()))
                onTriggerEvent(BluetoothManagerEvent.ReadFromTransferring(bluetoothSocket = this.getInSecureSocket()))
            }
        }

    }

    private fun makeConnectToOtherDevice(bluetoothDevice: BluetoothDevice, secure: Boolean) {
        connectToOtherDevice = ConnectToOtherDevice()
        connectToOtherDevice?.apply {
            this.init(
                bluetoothDevice = bluetoothDevice,
                secure = secure,
                bluetoothAdapter = bluetoothAdapter
            )
            if (this.currentStateIsNotFailed()) this.connect()
            if (this.currentStateIsConnected()) {
                onTriggerEvent(BluetoothManagerEvent.ReadFromTransferring(bluetoothSocket = this.getSocket()))
            }


        }

    }


    private fun addedToMessageList(message: String?) {
        val currentList = state.value?.messagesList ?: arrayListOf()
        Log.i(TAG, "addedToMessageList message  : " + message)
        if (message != null) {
            currentList.add(message)
        } else {
            currentList.add("Unknown Message")
        }
        state.value = state.value?.copy(messagesList = currentList)
    }

    private fun start() {
        onTriggerEvent(BluetoothManagerEvent.ConnectFromOtherDevice(bluetoothAdapter = bluetoothAdapter))
    }


}