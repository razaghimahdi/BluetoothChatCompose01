package com.razzaghi.bluetoothchat01.business.domain


data class Message(
    val message: String,
    val time: Long,
    val type: Int
)