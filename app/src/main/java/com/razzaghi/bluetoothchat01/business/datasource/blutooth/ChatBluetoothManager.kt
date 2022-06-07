package com.razzaghi.bluetoothchat01.business.datasource.blutooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.razzaghi.bluetoothchat01.business.datasource.bluetooth_server_socket.BluetoothInSecureServerSocketHandler
import com.razzaghi.bluetoothchat01.business.datasource.bluetooth_server_socket.BluetoothSecureServerSocketHandler
import com.razzaghi.bluetoothchat01.business.datasource.bluetooth_socket.BluetoothSocketSetupHandler
import com.razzaghi.bluetoothchat01.business.domain.BluetoothState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("LongLogTag", "MissingPermission")
class ChatBluetoothManager(
    private val bluetoothAdapter: BluetoothAdapter,
) : ChatBluetooth {

    private val TAG = "AppDebug ChatBluetoothImpl"


    private val bluetoothState: MutableLiveData<BluetoothState> =
        MutableLiveData(BluetoothState.None)

    private var bluetoothSocketSetupHandler: BluetoothSocketSetupHandler? = null
    private var bluetoothSecureServerSocketHandler: BluetoothSecureServerSocketHandler? = null
    private var bluetoothInSecureServerSocketHandler: BluetoothInSecureServerSocketHandler? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            start()
        }
    }


     fun getState(): MutableLiveData<BluetoothState> {
        return bluetoothState
    }

    override suspend fun start() {

        Log.i(TAG, "start")

        // Cancel any thread attempting to make a connection
        if (bluetoothSocketSetupHandler != null) {
            bluetoothSocketSetupHandler?.closeSocket()
            bluetoothSocketSetupHandler = null
        }

        // Cancel any thread currently running a connection TODO()
        /* if (mConnectedThread != null) {
             mConnectedThread?.cancel()
             mConnectedThread = null
         }*/

        // Start the thread to listen on a BluetoothServerSocket
        if (bluetoothSecureServerSocketHandler == null) {
            bluetoothSecureServerSocketHandler =
                BluetoothSecureServerSocketHandler(bluetoothAdapter)
            bluetoothSecureServerSocketHandler?.accept()
        }
        if (bluetoothInSecureServerSocketHandler == null) {
            bluetoothInSecureServerSocketHandler =
                BluetoothInSecureServerSocketHandler(bluetoothAdapter)
            bluetoothInSecureServerSocketHandler?.accept()
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun connectToBluetoothSocket(device: BluetoothDevice, secure: Boolean) {

        // Cancel any thread attempting to make a connection
        if (bluetoothState.value == BluetoothState.Connecting) {
            if (bluetoothSocketSetupHandler != null) {
                bluetoothSocketSetupHandler?.closeSocket()
                bluetoothSocketSetupHandler = null
            }
        }

        // Cancel any thread currently running a connection TODO()
        /* if (mConnectedThread != null) {
             mConnectedThread?.cancel()
             mConnectedThread = null
         }*/


        // Start the thread to connect with the given device
        bluetoothSocketSetupHandler = BluetoothSocketSetupHandler(device, secure)
        bluetoothSocketSetupHandler?.apply {
            this.connect()
            bluetoothState.value = BluetoothState.Connecting
        }
    }


    override suspend fun connectToBluetoothServerSocket(
        adapter: BluetoothAdapter,
        secure: Boolean
    ) {

        // Cancel any thread attempting to make a connection
        if (bluetoothSocketSetupHandler != null) {
            bluetoothSocketSetupHandler?.closeSocket()
            bluetoothSocketSetupHandler = null
        }

        // Cancel any thread currently running a connection TODO()
        /*   if (mConnectedThread != null) {
               mConnectedThread?.cancel()
               mConnectedThread = null
           }*/


        // Start the thread to listen on a BluetoothServerSocket
        if (bluetoothSecureServerSocketHandler == null) {
            bluetoothSecureServerSocketHandler =
                BluetoothSecureServerSocketHandler(bluetoothAdapter)
            bluetoothSecureServerSocketHandler?.accept()
            bluetoothState.value = BluetoothState.Listen
        }
        if (bluetoothInSecureServerSocketHandler == null) {
            bluetoothInSecureServerSocketHandler =
                BluetoothInSecureServerSocketHandler(bluetoothAdapter)
            bluetoothInSecureServerSocketHandler?.accept()
            bluetoothState.value = BluetoothState.Listen
        }
    }


    override suspend fun TransferMessage(
        socket: BluetoothSocket?,
        device: BluetoothDevice?,
        socketType: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {

        Log.i(TAG, "stop")

        /* if (mConnectThread != null) {
             mConnectThread?.cancel()
             mConnectThread = null
         }

         if (mConnectedThread != null) {
             mConnectedThread?.cancel()
             mConnectedThread = null
         }

         if (mSecureAcceptThread != null) {
             mSecureAcceptThread?.cancel()
             mSecureAcceptThread = null
         }

         if (mInsecureAcceptThread != null) {
             mInsecureAcceptThread?.cancel()
             mInsecureAcceptThread = null
         }*/
        bluetoothState.value = BluetoothState.None
    }

    fun write(out: ByteArray) {
        TODO("Not yet implemented")
    }

    override suspend fun connectionFailed() {
        TODO("Not yet implemented")
    }

    override suspend fun connectionLost() {
        TODO("Not yet implemented")
    }


}