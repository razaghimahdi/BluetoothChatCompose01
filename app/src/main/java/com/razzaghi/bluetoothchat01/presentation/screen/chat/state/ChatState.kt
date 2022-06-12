package com.razzaghi.bluetoothchat01.presentation.screen.chat.state

import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.business.domain.DeviceData

data class ChatState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val isBluetoothOn:Boolean = true,
    val chatInput:String = "",
)
