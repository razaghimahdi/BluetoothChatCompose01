package com.razzaghi.bluetoothchat01.presentation.screen.main.state

import com.razzaghi.bluetoothchat01.business.domain.DeviceData

data class MainState(
    val pairedDevices:List<DeviceData> = listOf(),
    val searchedDevices:List<DeviceData> = listOf()
)
