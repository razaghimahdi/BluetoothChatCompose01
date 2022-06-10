package com.razzaghi.bluetoothchat01.business.core

import com.razzaghi.bluetoothchat01.business.domain.Dialog

sealed class DataState<T> {

    data class Response<T>(val dialog: Dialog): DataState<T>()

    data class Data<T>(val data:T?=null, ): DataState<T>()

}