package com.razzaghi.bluetoothchat01.business.datasource.transfer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.razzaghi.bluetoothchat01.business.domain.BluetoothState
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("LongLogTag")
class TransferMessageHandler(
    private val bluetoothSocket: BluetoothSocket?,
) {

    private val TAG = "AppDebug TransferMessage"


    private var mmInStream: InputStream? = null
    private var mmOutStream: OutputStream? = null

    init {
        Log.i(TAG, " init: ")

        // Get the BluetoothSocket input and output streams
        try {
            mmInStream = bluetoothSocket?.inputStream
            mmOutStream = bluetoothSocket?.outputStream
        } catch (e: IOException) {
            Log.e(TAG, "temp sockets not created", e)
        }

    }

    fun start(connectionLost: () -> Unit): ByteArray? {
        Log.i(TAG, "BEGIN mConnectedThread")
        val buffer = ByteArray(1024)
        var bytes: Int

        return try {
            // Read from the InputStream
            bytes = mmInStream?.read(buffer) ?: 0


            buffer
        } catch (e: IOException) {
            Log.e(TAG, "disconnected", e)
            connectionLost()
            null
        }
    }

    fun write(buffer: ByteArray): ByteArray? {
        return try {
            mmOutStream?.write(buffer)

            // Share the sent message back to the UI Activity
            buffer
        } catch (e: IOException) {
            Log.e(TAG, "Exception during write", e)
            null
        }
    }

    fun cancelSocket() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "close() of connect socket failed", e)
        }

    }


}