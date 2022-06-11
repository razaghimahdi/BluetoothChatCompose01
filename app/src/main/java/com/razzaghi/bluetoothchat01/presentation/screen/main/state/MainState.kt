package com.razzaghi.bluetoothchat01.presentation.screen.main.state

import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.business.domain.DeviceData

data class MainState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val pairedDevices:ArrayList<DeviceData> = arrayListOf(),
    val searchedDevices:ArrayList<DeviceData> = arrayListOf(),
    val isBluetoothOn:Boolean = false,
    val shouldBluetoothStartScan:Boolean = false,
    val shouldMakeDeviceVisible:Boolean = false,
)
