package com.razzaghi.bluetoothchat01.business.datasource.transfer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants
import com.razzaghi.bluetoothchat01.business.domain.BluetoothState
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("LongLogTag")
class TransferMessagesFromDevices {

    private val TAG = "AppDebug TransferMessagesFromDevices"


    private val _isMessageTransferring: MutableLiveData<ConnectionState> =
        MutableLiveData(ConnectionState.None)
    val isMessageTransferring get() = _isMessageTransferring

    private lateinit var bluetoothSocket: BluetoothSocket

    private lateinit var mmInStream: InputStream
    private lateinit var mmOutStream: OutputStream

    fun init(bluetoothSocket: BluetoothSocket) {
        Log.i(TAG, "init bluetoothSocket: " + bluetoothSocket)
        try {
            this.bluetoothSocket = bluetoothSocket
            mmInStream = this.bluetoothSocket.inputStream
            mmOutStream = this.bluetoothSocket.outputStream

            _isMessageTransferring.value = ConnectionState.Inited
        } catch (e: Exception) {
            _isMessageTransferring.value = ConnectionState.Failed
            Log.i(TAG, "init e: " + e.message)
        }
    }

    fun close() {
        Log.i(TAG, "close: ")
        try {
            bluetoothSocket.close()

            _isMessageTransferring.value = ConnectionState.Closed
        } catch (e: Exception) {
            _isMessageTransferring.value = ConnectionState.Failed
            Log.i(TAG, "close e: " + e.message)
        }
    }

    fun write(buffer: ByteArray): ByteArray? {
        return try {
            mmOutStream.write(buffer)
            // _isMessageTransferring.value = ConnectionState.Wrote

            buffer
        } catch (e: Exception) {
            _isMessageTransferring.value = ConnectionState.Failed
            Log.i(TAG, "write e: " + e.message)
            null
        }
    }

    fun run(): ByteArray? {
        Log.i(TAG, "run")
        val buffer = ByteArray(1024)
        var bytes: Int

        return try {
            // Read from the InputStream
            bytes = mmInStream.read(buffer) ?: 0

            buffer
        } catch (e: Exception) {
            _isMessageTransferring.value = ConnectionState.Failed
            Log.i(TAG, "run e: " + e.message)
            null
        }
    }

}