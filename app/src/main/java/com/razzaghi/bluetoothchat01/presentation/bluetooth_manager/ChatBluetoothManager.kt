package com.razzaghi.bluetoothchat01.presentation.bluetooth_manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MESSAGE_TYPE_RECEIVED
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MESSAGE_TYPE_SENT
import com.razzaghi.bluetoothchat01.business.core.Dialog
import com.razzaghi.bluetoothchat01.business.core.*
import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.business.core.Queue
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
class ChatBluetoothManager(
    val bluetoothAdapter: BluetoothAdapter,
    private val closeTransferMessagesFromDevicesInteractor: CloseTransferMessagesFromDevicesInteractor,
    private val initTransferMessagesFromDevicesInteractor: InitTransferMessagesFromDevicesInteractor,
    private val readTransferMessagesFromDevicesInteractor: ReadTransferMessagesFromDevicesInteractor,
    private val writeTransferMessagesFromDevicesInteractor: WriteTransferMessagesFromDevicesInteractor,
    private val closeToOtherDeviceInteractor: CloseToOtherDeviceInteractor,
    private val connectToOtherDeviceInteractor: ConnectToOtherDeviceInteractor,
    private val initConnectToOtherDeviceInteractor: InitConnectToOtherDeviceInteractor,
    private val acceptFromOtherDeviceInteractor: AcceptFromOtherDeviceInteractor,
    private val closeFromOtherDeviceInteractor: CloseFromOtherDeviceInteractor,
    private val initFromOtherDeviceInteractor: InitFromOtherDeviceInteractor,
) {

    private val TAG = "AppDebug ChatBluetoothImpl"

    private val _state: MutableState<BluetoothManagerState> =
        mutableStateOf(BluetoothManagerState())
    val state get() = _state.value

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun onTriggerEvent(event: BluetoothManagerEvent) {
        when (event) {
            is BluetoothManagerEvent.CloseTransferring -> {
                closeTransferring(bluetoothSocket = event.bluetoothSocket)
            }
            is BluetoothManagerEvent.UpdateProgressBarState -> {
                updateProgressBarState(value = event.value)
            }
            is BluetoothManagerEvent.ReadFromTransferring -> {
                readFromTransferring(
                    bluetoothSocket = event.bluetoothSocket,
                )
            }
            is BluetoothManagerEvent.WriteFromTransferring -> {
                writeFromTransferring(
                    message = event.message,
                    bluetoothSocket = event.bluetoothSocket,
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

    private fun closeTransferring(bluetoothSocket: BluetoothSocket) {
        closeTransferMessagesFromDevicesInteractor.execute(bluetoothSocket).onEach { dataState ->

            when (dataState) {
                is DataState.Data -> {
                    if (dataState.connectionState == ConnectionState.Closed) {
                        updateBluetoothConnectionState(BluetoothConnectionState.None)
                        updateActiveBluetoothSocket(bluetoothSocket = null)
                    }
                }
                is DataState.Loading -> {
                    _state.value =
                        _state.value.copy(progressBarState = dataState.progressBarState)
                }
            }

        }.launchIn(coroutineScope)
    }

    private fun updateActiveBluetoothSocket(bluetoothSocket: BluetoothSocket?) {
        _state.value = _state.value.copy(activeBluetoothSocket = bluetoothSocket)
    }

    private fun updateBluetoothConnectionState(bluetoothConnectionState: BluetoothConnectionState) {
        _state.value = _state.value.copy(bluetoothConnectionState = bluetoothConnectionState)
    }

    private fun writeFromTransferring(message: ByteArray, bluetoothSocket: BluetoothSocket) {

        writeTransferMessagesFromDevicesInteractor.execute(message, state.outputStream)
            .onEach { dataState ->

                when (dataState) {
                    is DataState.Data -> {
                        if (dataState.connectionState != ConnectionState.Failed) {
                            updateBluetoothConnectionState(BluetoothConnectionState.Connected)
                            addedToMessageList(dataState.data, MESSAGE_TYPE_SENT)
                        } else {
                            onTriggerEvent(BluetoothManagerEvent.CloseTransferring(bluetoothSocket))
                            TransferringMessageHasBeenFailed(FAILED_Write_messages_DIALOG)
                        }
                    }
                    is DataState.Loading -> {
                        _state.value =
                            _state.value.copy(progressBarState = dataState.progressBarState)
                    }
                }
            }.launchIn(coroutineScope)
    }

    private fun readFromTransferring(bluetoothSocket: BluetoothSocket) {
        initTransferMessagesFromDevices(bluetoothSocket = bluetoothSocket) {
            readTransferMessagesFromDevicesInteractor.execute(state.inputStream)
                .onEach { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            if (dataState.connectionState != ConnectionState.Failed) {
                                updateBluetoothConnectionState(BluetoothConnectionState.Connected)
                                addedToMessageList(dataState.data, MESSAGE_TYPE_RECEIVED)
                            } else {
                                onTriggerEvent(BluetoothManagerEvent.CloseTransferring(bluetoothSocket))
                                TransferringMessageHasBeenFailed(FAILED_Read_messages_DIALOG)
                            }
                        }
                        is DataState.Loading -> {
                            _state.value =
                                _state.value.copy(progressBarState = dataState.progressBarState)
                        }
                    }
                }.launchIn(coroutineScope)
        }
    }

    private fun initTransferMessagesFromDevices(
        bluetoothSocket: BluetoothSocket,
        onFinish: () -> Unit
    ) {
        initTransferMessagesFromDevicesInteractor.execute(bluetoothSocket).onEach { dataState ->

            when (dataState) {
                is DataState.Data -> {

                    if (dataState.data != null) {
                        updateActiveBluetoothSocket(bluetoothSocket)
                        updateOutputStream(outputStream = bluetoothSocket.outputStream)
                        updateInputStream(inputStream = bluetoothSocket.inputStream)
                        onFinish()
                    } else {
                        TransferringMessageHasBeenFailed(FAILED_to_init_transferring_DIALOG)
                    }

                }
                is DataState.Loading -> {
                    _state.value =
                        _state.value.copy(progressBarState = dataState.progressBarState)
                }
            }

        }.launchIn(coroutineScope)
    }

    private fun TransferringMessageHasBeenFailed(dialog: Dialog) {
        updateBluetoothConnectionState(BluetoothConnectionState.None)
        onTriggerEvent(BluetoothManagerEvent.Start)
        appendToMessageQueue(dialog)
    }

    private fun makeConnectFromOtherDevice(bluetoothAdapter: BluetoothAdapter) {


        initConnectFromOtherDevice(bluetoothAdapter = bluetoothAdapter, secure = true) {
            acceptFromOtherDeviceInteractor.execute(it).onEach { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        if (dataState.data != null) {
                            onTriggerEvent(
                                BluetoothManagerEvent.ReadFromTransferring(
                                    bluetoothSocket = dataState.data
                                )
                            )
                        } else {
                            appendToMessageQueue(FAILED_From_Connect_DIALOG)
                        }
                    }
                    is DataState.Loading -> {
                        _state.value =
                            _state.value.copy(progressBarState = dataState.progressBarState)
                    }
                }
            }.launchIn(coroutineScope)
        }


        initConnectFromOtherDevice(bluetoothAdapter = bluetoothAdapter, secure = false) {
            acceptFromOtherDeviceInteractor.execute(it).onEach { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        if (dataState.data != null) {
                            onTriggerEvent(
                                BluetoothManagerEvent.ReadFromTransferring(
                                    bluetoothSocket = dataState.data
                                )
                            )
                        } else {
                            appendToMessageQueue(FAILED_From_Connect_DIALOG)
                        }
                    }
                    is DataState.Loading -> {
                        _state.value =
                            _state.value.copy(progressBarState = dataState.progressBarState)
                    }
                }
            }.launchIn(coroutineScope)
        }


    }

    private fun initConnectFromOtherDevice(
        bluetoothAdapter: BluetoothAdapter,
        secure: Boolean,
        onFinish: (BluetoothServerSocket) -> Unit
    ) {
        initFromOtherDeviceInteractor.execute(bluetoothAdapter, secure).onEach { dataState ->
            when (dataState) {
                is DataState.Data -> {
                    if (dataState.data != null) {
                        onFinish(dataState.data)
                    } else {
                        appendToMessageQueue(FAILED_From_Init_Connect_DIALOG)
                    }
                }
                is DataState.Loading -> {
                    _state.value =
                        _state.value.copy(progressBarState = dataState.progressBarState)
                }
            }
        }.launchIn(coroutineScope)
    }

    private fun makeConnectToOtherDevice(bluetoothDevice: BluetoothDevice, secure: Boolean) {

        initConnectToOtherDevice(
            bluetoothDevice = bluetoothDevice,
            secure = secure,
            bluetoothAdapter = bluetoothAdapter
        ) { bluetoothSocket ->

            connectToOtherDeviceInteractor.execute(bluetoothSocket = bluetoothSocket)
                .onEach { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            if (dataState.connectionState == ConnectionState.Connected) {
                                onTriggerEvent(
                                    BluetoothManagerEvent.ReadFromTransferring(
                                        bluetoothSocket = bluetoothSocket
                                    )
                                )
                            } else {
                                appendToMessageQueue(FAILED_To_Connect_DIALOG)
                            }
                        }
                        is DataState.Loading -> {
                            _state.value =
                                _state.value.copy(progressBarState = dataState.progressBarState)
                        }
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
        ).onEach { dataState ->
            when (dataState) {
                is DataState.Data -> {
                    if (dataState.data != null) {
                        onFinish(dataState.data)
                    } else {
                        appendToMessageQueue(FAILED_To_Init_Connect_DIALOG)
                    }
                }
                is DataState.Loading -> {
                    _state.value =
                        _state.value.copy(progressBarState = dataState.progressBarState)
                }
            }
        }.launchIn(coroutineScope)
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


    private fun addedToMessageList(msg: String?, type: Int) {
        val currentList = _state.value.messagesList
        Log.i(TAG, "addedToMessageList message  : " + msg)

        val milliSecondsTime = System.currentTimeMillis()
        val message = Message(message = msg ?: "", time = milliSecondsTime, type = type)

        currentList.add(message)

        _state.value = _state.value.copy(messagesList = currentList)
    }

    private fun start() {
        onTriggerEvent(BluetoothManagerEvent.ConnectFromOtherDevice(bluetoothAdapter = bluetoothAdapter))
    }


    private fun updateProgressBarState(value: ProgressBarState) {
        _state.value = _state.value.copy(progressBarState = value)
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