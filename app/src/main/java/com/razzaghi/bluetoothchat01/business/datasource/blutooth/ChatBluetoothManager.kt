package com.razzaghi.bluetoothchat01.business.datasource.blutooth
/*
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.razzaghi.bluetoothchat01.business.datasource.bluetooth_server_socket.BluetoothSecureServerSocketHandler
import com.razzaghi.bluetoothchat01.business.datasource.bluetooth_socket.BluetoothSocketSetupHandler
import com.razzaghi.bluetoothchat01.business.datasource.transfer.TransferMessageHandler
import com.razzaghi.bluetoothchat01.business.domain.BluetoothConnectionState
import com.razzaghi.bluetoothchat01.business.util.SocketTools.getSocketType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("LongLogTag", "MissingPermission")
class ChatBluetoothManager(
    private val bluetoothAdapter: BluetoothAdapter,
) : ChatBluetooth {

    private val TAG = "AppDebug ChatBluetoothImpl"


    private val bluetoothConnectionState: MutableLiveData<BluetoothConnectionState> =
        MutableLiveData(BluetoothConnectionState.None)

    private var bluetoothSocketSetupHandler: BluetoothSocketSetupHandler? = null
    private var bluetoothInSecureServerSocketHandler: BluetoothSecureServerSocketHandler? =
        null // TODO()
    private var bluetoothSecureServerSocketHandler: BluetoothSecureServerSocketHandler? =
        null // TODO()
    private var transferMessageHandler: TransferMessageHandler? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            start()
        }
    }


    fun getState(): MutableLiveData<BluetoothConnectionState> {
        return bluetoothConnectionState
    }

    override suspend fun start() {

        Log.i(TAG, "start")

        // Cancel any thread attempting to make a connection
        stopBluetoothSocketSetupHandler()

        // Cancel any thread currently running a connection TODO()
        stopTransferMessageHandler()

        // Start the thread to listen on a BluetoothServerSocket
        connectToBluetoothServerSocket(bluetoothAdapter, true)
        connectToBluetoothServerSocket(bluetoothAdapter, false)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun connectToBluetoothSocket(device: BluetoothDevice, secure: Boolean) {

        // Cancel any thread attempting to make a connection
        if (bluetoothConnectionState.value == BluetoothConnectionState.Connecting) {
            stopBluetoothSocketSetupHandler()
        }

        // Cancel any thread currently running a connection TODO()
        stopTransferMessageHandler()


        // Start the thread to connect with the given device
        bluetoothSocketSetupHandler = BluetoothSocketSetupHandler(device, secure)
        bluetoothSocketSetupHandler?.apply {
            this.connect()
            bluetoothConnectionState.value = BluetoothConnectionState.Connecting
            TransferMessage(
                socket = this.getSocket(),
                device = device,
                socketType = secure.getSocketType()
            )
        }
    }

    override suspend fun connectToBluetoothServerSocket(
        adapter: BluetoothAdapter,
        secure: Boolean
    ) {

        // Cancel any thread attempting to make a connection
        stopBluetoothSocketSetupHandler()

        // Cancel any thread currently running a connection TODO()
        stopTransferMessageHandler()


        // Start the thread to manage the connection and perform transmissions
        if (bluetoothSecureServerSocketHandler == null) {
            bluetoothSecureServerSocketHandler =
                BluetoothSecureServerSocketHandler(bluetoothAdapter, secure)
          //  bluetoothSecureServerSocketHandler?.accept()
            bluetoothConnectionState.value = BluetoothConnectionState.Listen
        }
    }

    override suspend fun TransferMessage(
        socket: BluetoothSocket?,
        device: BluetoothDevice?,
        socketType: String
    ) {

        // Cancel the thread that completed the connection
        stopBluetoothSocketSetupHandler()

        // Cancel any thread currently running a connection
        stopTransferMessageHandler()

        // Cancel the accept thread because we only want to connect to one device
        stopBluetoothSecureServerSocketHandler()
        stopBluetoothInSecureServerSocketHandler()

        // Start the thread to manage the connection and perform transmissions
        transferMessageHandler = TransferMessageHandler(socket)
        bluetoothConnectionState.value = BluetoothConnectionState.Connected
        // if (bluetoothState.value == BluetoothState.Connected) {
        transferMessageHandler?.start() {
            connectionLost()
        }
        // }
        // Send the name of the connected device back to the UI Activity
        // TODO()
    }

    override suspend fun stop() {

        Log.i(TAG, "stop")

        stopBluetoothSocketSetupHandler()
        stopBluetoothSecureServerSocketHandler()
        stopBluetoothInSecureServerSocketHandler()
        stopTransferMessageHandler()

        bluetoothConnectionState.value = BluetoothConnectionState.None
    }

    private fun stopTransferMessageHandler() {
        if (transferMessageHandler != null) {
            transferMessageHandler?.cancelSocket()
            transferMessageHandler = null
        }
    }

    private fun stopBluetoothInSecureServerSocketHandler() {
        if (bluetoothInSecureServerSocketHandler != null) {
            bluetoothInSecureServerSocketHandler?.closeServerSocket()
            bluetoothInSecureServerSocketHandler = null
        }
    }

    private fun stopBluetoothSecureServerSocketHandler() {
        if (bluetoothSecureServerSocketHandler != null) {
            bluetoothSecureServerSocketHandler?.closeServerSocket()
            bluetoothSecureServerSocketHandler = null
        }
    }

    private fun stopBluetoothSocketSetupHandler() {
        if (bluetoothSocketSetupHandler != null) {
            bluetoothSocketSetupHandler?.closeSocket()
            bluetoothSocketSetupHandler = null
        }
    }

    override suspend fun write(out: ByteArray) {
        Log.i(TAG, "write out: " + out)
        if (bluetoothConnectionState.value == BluetoothConnectionState.Connected) {
            transferMessageHandler?.write(out)
        }
    }

    override suspend fun connectionFailed() {
        Log.i(TAG, "connectionFailed: ")
        // Send a failure message back to the Activity
        // TODO()

        bluetoothConnectionState.value = BluetoothConnectionState.None


        // Start the service over to restart listening mode
        start()
    }

    override fun connectionLost() {
        Log.i(TAG, "connectionLost: ")
        // Send a failure message back to the Activity
        // TODO()

        bluetoothConnectionState.value = BluetoothConnectionState.None

        // Start the service over to restart listening mode
        coroutineScope.launch {
            start()
        }
    }


}
*/