package com.razzaghi.bluetoothchat01.presentation.screen.chat.state

import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.presentation.screen.main.state.MainEvents

sealed class ChatEvents{

    data class UpdateProgressBarState(val value: ProgressBarState) : ChatEvents()

    data class UpdateIsBluetoothOn(val value: Boolean) : ChatEvents()

    data class UpdateInputChat(val value: String) : ChatEvents()

}
