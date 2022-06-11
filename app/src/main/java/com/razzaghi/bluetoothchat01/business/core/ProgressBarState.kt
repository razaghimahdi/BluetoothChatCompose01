package com.razzaghi.bluetoothchat01.business.core

sealed class ProgressBarState{

    object Loading: ProgressBarState()

    object Idle: ProgressBarState()

}