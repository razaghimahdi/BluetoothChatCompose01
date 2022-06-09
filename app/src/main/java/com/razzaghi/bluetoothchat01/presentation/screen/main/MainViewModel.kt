package com.razzaghi.bluetoothchat01.presentation.screen.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.razzaghi.bluetoothchat01.business.domain.DeviceData
import com.razzaghi.bluetoothchat01.presentation.screen.main.state.MainEvents
import com.razzaghi.bluetoothchat01.presentation.screen.main.state.MainState

class MainViewModel:ViewModel() {

    val TAG = "AppDebug MainViewModel"

    val state: MutableState<MainState> = mutableStateOf(MainState())


    fun onTriggerEvent(event: MainEvents) {
        when (event) {
            is MainEvents.UpdatePairedDevice -> {
                updatePairedDevice(list = event.list)
            }
            is MainEvents.UpdateSearchedDevice -> {
                updateSearchedDevice(list = event.list)
            }
        }
    }

    private fun updateSearchedDevice(list: List<DeviceData>) {
        Log.i(TAG, "updateSearchedDevice list: "+list)
        state.value = state.value.copy(searchedDevices = list)
    }

    private fun updatePairedDevice(list: List<DeviceData>) {
        Log.i(TAG, "updatePairedDevice list: "+list)
        state.value = state.value.copy(pairedDevices = list)
    }


}