package com.razzaghi.bluetoothchat01.business.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.util.Log
import com.razzaghi.bluetoothchat01.business.domain.DeviceData

object BluetoothTools {

    private val TAG = "AppDebug BluetoothTools"

    @SuppressLint("MissingPermission")
    fun getPairedDevices(bluetoothAdapter: BluetoothAdapter): ArrayList<DeviceData> {

        // Log.i(TAG, "getPairedDevices: ")

        // Get a set of currently paired devices
        val pairedDevices = bluetoothAdapter.bondedDevices
        val mPairedDeviceList = arrayListOf<DeviceData>()
        pairedDevices?.let { pairedDevices ->
            // If there are paired devices, add each one to the ArrayAdapter
            if (pairedDevices.isNotEmpty()) {
                // There are paired devices. Get the name and address of each paired device.
                for (device in pairedDevices) {
                   // Log.i(TAG, "getPairedDevices device: "+device.toString())
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    mPairedDeviceList.add(DeviceData(deviceName, deviceHardwareAddress))
                }
            }
        }
       // Log.i(TAG, "getPairedDevices mPairedDeviceList: "+mPairedDeviceList)

        return mPairedDeviceList
    }
}