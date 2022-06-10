package com.razzaghi.bluetoothchat01.presentation.screen.main.state

import com.razzaghi.bluetoothchat01.business.domain.DeviceData

sealed class MainEvents {

    data class UpdateIsLoading(val value: Boolean) : MainEvents()

    data class UpdateShouldBluetoothStartScan(val value: Boolean) : MainEvents()

    data class UpdateShouldMakeDeviceVisible(val value: Boolean) : MainEvents()

    data class UpdatePairedDevice(val list: ArrayList<DeviceData>) : MainEvents()

    data class AddToSearchedDevice(val item: DeviceData) : MainEvents()

    data class UpdateSearchedDevice(val list: ArrayList<DeviceData>) : MainEvents()

    data class UpdateIsBluetoothOn(val value: Boolean) : MainEvents()

}
