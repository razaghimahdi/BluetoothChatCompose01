package com.razzaghi.bluetoothchat01.business.core

import com.razzaghi.bluetoothchat01.business.core.Dialog
import com.razzaghi.bluetoothchat01.business.domain.ConnectionState


sealed class DataState<T> {

    data class Data<T>(
        val connectionState: ConnectionState = ConnectionState.None,
        val data: T? = null,
    ) : DataState<T>()

    data class Loading<T>(val progressBarState: ProgressBarState = ProgressBarState.Idle) :
        DataState<T>()

}