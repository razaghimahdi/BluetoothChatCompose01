package com.razzaghi.bluetoothchat01.presentation.screen.main.state

import com.razzaghi.bluetoothchat01.business.domain.DeviceData

sealed class MainEvents {

    data class UpdatePairedDevice(val list: List<DeviceData>) : MainEvents()

    data class UpdateSearchedDevice(val list: List<DeviceData>) : MainEvents()

}
