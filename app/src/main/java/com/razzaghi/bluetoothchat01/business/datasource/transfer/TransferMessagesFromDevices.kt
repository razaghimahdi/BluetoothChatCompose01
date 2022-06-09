package com.razzaghi.bluetoothchat01.business.datasource.transfer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState
import com.razzaghi.bluetoothchat01.business.util.SocketTools.toCustomString
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("LongLogTag")
class TransferMessagesFromDevices {

    private val TAG = "AppDebug TransferMessagesFromDevices"


    private val _isMessageTransferring: MutableLiveData<ConnectionState> =
        MutableLiveData(ConnectionState.None)
    val isMessageTransferring get() = _isMessageTransferring

    private lateinit var bluetoothSocket: BluetoothSocket

    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream

    fun init(bluetoothSocket: BluetoothSocket) {
        Log.i(TAG, "init bluetoothSocket: " + bluetoothSocket)
        try {
            this.bluetoothSocket = bluetoothSocket
            inputStream = this.bluetoothSocket.inputStream
            outputStream = this.bluetoothSocket.outputStream

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

    fun write(buffer: ByteArray): String? {
        return try {
            outputStream.write(buffer)
            // _isMessageTransferring.value = ConnectionState.Wrote

            buffer.toCustomString()
        } catch (e: Exception) {
            _isMessageTransferring.value = ConnectionState.Failed
            Log.i(TAG, "write e: " + e.message)
            close()
            null
        }
    }

    fun read(): String? {
        Log.i(TAG, "run")
        val buffer = ByteArray(1024)
        var bytes: Int

        return try {
            // Read from the InputStream
            bytes = inputStream.read(buffer) ?: 0
            _isMessageTransferring.value = ConnectionState.Connected
            return inputStream.bufferedReader().use { it.readText() }  // defaults to UTF-8

            //buffer.toCustomString()
        } catch (e: Exception) {
            _isMessageTransferring.value = ConnectionState.Failed
            Log.i(TAG, "run e: " + e.message)
            close()
            null
        }
    }

    fun currentStateIsNotFailed() = isMessageTransferring.value != ConnectionState.Failed

}