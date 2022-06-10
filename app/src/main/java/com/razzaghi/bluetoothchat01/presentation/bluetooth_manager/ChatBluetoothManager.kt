package com.razzaghi.bluetoothchat01.presentation.bluetooth_manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.razzaghi.bluetoothchat01.business.core.Queue
import com.razzaghi.bluetoothchat01.business.datasource.connect_from_other_device.ConnectFromOtherDevice
import com.razzaghi.bluetoothchat01.business.datasource.connect_to_other_device.ConnectToOtherDevice
import com.razzaghi.bluetoothchat01.business.datasource.transfer.TransferMessagesFromDevices
import com.razzaghi.bluetoothchat01.business.domain.*
import com.razzaghi.bluetoothchat01.business.interactors.connect_from_other_device.AcceptFromOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_from_other_device.CloseFromOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_from_other_device.InitFromOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_to_other_device.CloseToOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_to_other_device.ConnectToOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_to_other_device.InitConnectToOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.transfer.CloseTransferMessagesFromDevicesInteractor
import com.razzaghi.bluetoothchat01.business.interactors.transfer.InitTransferMessagesFromDevicesInteractor
import com.razzaghi.bluetoothchat01.business.interactors.transfer.ReadTransferMessagesFromDevicesInteractor
import com.razzaghi.bluetoothchat01.business.interactors.transfer.WriteTransferMessagesFromDevicesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("LongLogTag", "MissingPermission")
class ChatBluetoothManager (
    val bluetoothAdapter: BluetoothAdapter,
    val closeTransferMessagesFromDevicesInteractor: CloseTransferMessagesFromDevicesInteractor,
    val initTransferMessagesFromDevicesInteractor: InitTransferMessagesFromDevicesInteractor,
    val readTransferMessagesFromDevicesInteractor: ReadTransferMessagesFromDevicesInteractor,
    val writeTransferMessagesFromDevicesInteractor: WriteTransferMessagesFromDevicesInteractor,
    val closeToOtherDeviceInteractor: CloseToOtherDeviceInteractor,
    val connectToOtherDeviceInteractor: ConnectToOtherDeviceInteractor,
    val initConnectToOtherDeviceInteractor: InitConnectToOtherDeviceInteractor,
    val acceptFromOtherDeviceInteractor: AcceptFromOtherDeviceInteractor,
    val closeFromOtherDeviceInteractor: CloseFromOtherDeviceInteractor,
    val initFromOtherDeviceInteractor: InitFromOtherDeviceInteractor,
) {

    private val TAG = "AppDebug ChatBluetoothImpl"

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

        writeTransferMessagesFromDevicesInteractor.execute(message, state.outputStream).onEach {
            if (it != ConnectionState.Failed.toString()) {
                updateBluetoothConnectionState(BluetoothConnectionState.Connected)
                addedToMessageList(it)
            } else {
                TransferringMessageHasBeenFailed(FAILED_Write_messages_DIALOG)
            }
        }.launchIn(coroutineScope)

    }

    private fun readFromTransferring(bluetoothSocket: BluetoothSocket) {
        initTransferMessagesFromDevices(bluetoothSocket = bluetoothSocket) {
            readTransferMessagesFromDevicesInteractor.execute(state.inputStream).onEach {
                if (it != ConnectionState.Failed.toString()) {
                    updateBluetoothConnectionState(BluetoothConnectionState.Connected)
                    addedToMessageList(it)
                } else {
                    TransferringMessageHasBeenFailed(FAILED_Read_messages_DIALOG)
                }
            }.launchIn(coroutineScope)
        }
    }

    private fun initTransferMessagesFromDevices(
        bluetoothSocket: BluetoothSocket,
        onFinish: () -> Unit
    ) {
        initTransferMessagesFromDevicesInteractor.execute(bluetoothSocket).onEach {
            if (it != null) {
                updateOutputStream(outputStream = bluetoothSocket.outputStream)
                updateInputStream(inputStream = bluetoothSocket.inputStream)
                onFinish()
            } else {
                TransferringMessageHasBeenFailed(FAILED_to_init_transferring_DIALOG)
            }
        }.launchIn(coroutineScope)
    }

    private fun TransferringMessageHasBeenFailed(dialog: Dialog) {
        updateBluetoothConnectionState(BluetoothConnectionState.None)
        onTriggerEvent(BluetoothManagerEvent.Start)
        appendToMessageQueue(dialog)
    }

    private fun updateInputStream(inputStream: InputStream?) {
        inputStream?.let {
            _state.value = _state.value.copy(inputStream = inputStream)
        }
    }

    private fun updateOutputStream(outputStream: OutputStream?) {
        outputStream?.let {
            _state.value = _state.value.copy(outputStream = outputStream)
        }
    }

    private fun makeConnectFromOtherDevice(bluetoothAdapter: BluetoothAdapter) {


        initConnectFromOtherDevice(bluetoothAdapter = bluetoothAdapter, secure = true) {
            acceptFromOtherDeviceInteractor.execute(it).onEach {
                if (it != null) {
                    onTriggerEvent(BluetoothManagerEvent.ReadFromTransferring(bluetoothSocket = it))
                } else {
                    appendToMessageQueue(FAILED_From_Connect_DIALOG)
                }

            }.launchIn(coroutineScope)
        }

        initConnectFromOtherDevice(bluetoothAdapter = bluetoothAdapter, secure = false) {
            acceptFromOtherDeviceInteractor.execute(it).onEach {
                if (it != null) {
                    onTriggerEvent(BluetoothManagerEvent.ReadFromTransferring(bluetoothSocket = it))
                } else {
                    appendToMessageQueue(FAILED_From_Connect_DIALOG)
                }

            }.launchIn(coroutineScope)
        }


    }

    private fun initConnectFromOtherDevice(
        bluetoothAdapter: BluetoothAdapter,
        secure: Boolean,
        onFinish: (BluetoothServerSocket) -> Unit
    ) {
        initFromOtherDeviceInteractor.execute(bluetoothAdapter, secure).onEach {
            if (it != null) {
                //  updateBluetoothServerSocket(bluetoothServerSocket = it)
                onFinish(it)
            } else {
                appendToMessageQueue(FAILED_From_Init_Connect_DIALOG)
            }
        }.launchIn(coroutineScope)
    }

    private fun makeConnectToOtherDevice(bluetoothDevice: BluetoothDevice, secure: Boolean) {

        initConnectToOtherDevice(
            bluetoothDevice = bluetoothDevice,
            secure = secure,
            bluetoothAdapter = bluetoothAdapter
        ) { bluetoothSocket ->

            connectToOtherDeviceInteractor.execute(bluetoothSocket = bluetoothSocket).onEach {
                if (it == ConnectionState.Connected) {
                    onTriggerEvent(BluetoothManagerEvent.ReadFromTransferring(bluetoothSocket = bluetoothSocket))
                } else {
                    appendToMessageQueue(FAILED_To_Connect_DIALOG)
                }

            }.launchIn(coroutineScope)


        }


    }


    private fun initConnectToOtherDevice(
        bluetoothDevice: BluetoothDevice,
        secure: Boolean,
        bluetoothAdapter: BluetoothAdapter,
        onFinish: (BluetoothSocket) -> Unit
    ) {
        initConnectToOtherDeviceInteractor.execute(
            bluetoothDevice = bluetoothDevice,
            secure = secure,
            bluetoothAdapter = bluetoothAdapter
        ).onEach {
            if (it != null) {
                //  updateBluetoothServerSocket(bluetoothServerSocket = it)
                onFinish(it)
            } else {
                appendToMessageQueue(FAILED_To_Init_Connect_DIALOG)
            }
        }.launchIn(coroutineScope)
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