package com.razzaghi.bluetoothchat01.presentation.bluetooth_manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.razzaghi.bluetoothchat01.business.core.Queue
import com.razzaghi.bluetoothchat01.business.datasource.connect_from_other_device.ConnectFromOtherDevice
import com.razzaghi.bluetoothchat01.business.datasource.connect_to_other_device.ConnectToOtherDevice
import com.razzaghi.bluetoothchat01.business.datasource.transfer.TransferMessagesFromDevices
import com.razzaghi.bluetoothchat01.business.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("LongLogTag", "MissingPermission")
class ChatBluetoothManager02(
    val bluetoothAdapter: BluetoothAdapter,
) {

    private val TAG = "AppDebug ChatBluetoothImpl"

    private var transferMessagesFromDevices: TransferMessagesFromDevices? = null
    private var connectToOtherDevice: ConnectToOtherDevice? = null
    private var connectFromOtherDevice: ConnectFromOtherDevice? = null

    private val _state: MutableState<BluetoothManagerState> =
        mutableStateOf(BluetoothManagerState())
    val state get() = _state.value

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

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
            is BluetoothManagerEvent.OnRemoveHeadFromQueue -> {
                removeHeadMessage()
            }
        }
    }


    init {

    }

    private fun updateBluetoothConnectionState(bluetoothConnectionState: BluetoothConnectionState) {
        _state.value = _state.value.copy(bluetoothConnectionState = bluetoothConnectionState)
    }

    private fun writeFromTransferring(message: ByteArray) {
        transferMessagesFromDevices?.apply {
            if (this.currentStateIsNotFailed()) {
                updateBluetoothConnectionState(BluetoothConnectionState.Connected)
                addedToMessageList(this.write(message))
            } else {
                updateBluetoothConnectionState(BluetoothConnectionState.None)
                onTriggerEvent(BluetoothManagerEvent.Start)
                appendToMessageQueue(FAILED_Write_messages_DIALOG)
            }
        }
    }

    private fun readFromTransferring(bluetoothSocket: BluetoothSocket) {
        transferMessagesFromDevices = TransferMessagesFromDevices()
        transferMessagesFromDevices?.apply {
            if (this.isMessageTransferring.value == ConnectionState.Connected) {
                this.close()
            }
            this.init(bluetoothSocket = bluetoothSocket)
            if (this.currentStateIsNotFailed()) {
                updateBluetoothConnectionState(BluetoothConnectionState.Connected)
                addedToMessageList(this.read())
            }
            if (!this.currentStateIsNotFailed()) {
                updateBluetoothConnectionState(BluetoothConnectionState.None)
                onTriggerEvent(BluetoothManagerEvent.Start)
                appendToMessageQueue(FAILED_Read_messages_DIALOG)
            }
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

            if (!this.currentStateIsNotFailed()) {
                appendToMessageQueue(FAILED_From_Connect_DIALOG)
            }
        }

    }

    private fun makeConnectToOtherDevice(bluetoothDevice: BluetoothDevice, secure: Boolean) {
        coroutineScope.launch {

            connectToOtherDevice = ConnectToOtherDevice()
            connectToOtherDevice?.apply {
                if (this.currentStateIsConnected()) {
                    this.close()
                }
                this.init(
                    bluetoothDevice = bluetoothDevice,
                    secure = secure,
                    bluetoothAdapter = bluetoothAdapter
                )
                if (this.currentStateIsNotFailed()) this.connect()
                if (this.currentStateIsConnected()) {
                    onTriggerEvent(BluetoothManagerEvent.ReadFromTransferring(bluetoothSocket = this.getSocket()))
                }
                if (!this.currentStateIsNotFailed()) {
                    appendToMessageQueue(FAILED_To_Connect_DIALOG)
                }

            }

        }
    }

    private fun addedToMessageList(message: String?) {
        val currentList = _state.value?.messagesList ?: arrayListOf()
        Log.i(TAG, "addedToMessageList message  : " + message)
        if (message != null) {
            currentList.add(message)
        } else {
            currentList.add("Unknown Message")
        }
        _state.value = _state.value.copy(messagesList = currentList)
    }

    private fun start() {
        onTriggerEvent(BluetoothManagerEvent.ConnectFromOtherDevice(bluetoothAdapter = bluetoothAdapter))
    }


    private fun appendToMessageQueue(uiComponent: Dialog) {
        val queue = _state.value.errorQueue
        queue.add(uiComponent)
        _state.value = _state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
        _state.value = _state.value.copy(errorQueue = queue)
    }

    private fun removeHeadMessage() {
        try {
            val queue = _state.value.errorQueue
            queue.remove() // can throw exception if empty
            _state.value =
                _state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
            _state.value = _state.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            Log.i(TAG, "removeHeadMessage: Nothing to remove from DialogQueue")
        }
    }
}