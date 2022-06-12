package com.razzaghi.bluetoothchat01.presentation.screen.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.razzaghi.bluetoothchat01.business.core.ProgressBarState
import com.razzaghi.bluetoothchat01.presentation.screen.chat.state.ChatEvents
import com.razzaghi.bluetoothchat01.presentation.screen.chat.state.ChatState

class ChatViewModel : ViewModel() {

    val TAG = "AppDebug MainViewModel"

    val state: MutableState<ChatState> = mutableStateOf(ChatState())

    fun onTriggerEvent(event: ChatEvents) {
        when (event) {
            is ChatEvents.UpdateProgressBarState -> {
                updateProgressBarState(value = event.value)
            }
            is ChatEvents.UpdateIsBluetoothOn -> {
                updateIsBluetoothOn(value = event.value)
            }
            is ChatEvents.UpdateInputChat -> {
                updateInputChat(value = event.value)
            }
        }
    }


    private fun updateProgressBarState(value: ProgressBarState) {
        state.value = state.value.copy(progressBarState = value)
    }

    private fun updateIsBluetoothOn(value: Boolean) {
        state.value = state.value.copy(isBluetoothOn = value)
    }
    private fun updateInputChat(value: String) {
        state.value = state.value.copy(chatInput = value)
    }
}