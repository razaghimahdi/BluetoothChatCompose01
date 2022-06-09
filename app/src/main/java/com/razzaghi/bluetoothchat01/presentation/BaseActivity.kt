package com.razzaghi.bluetoothchat01.presentation

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.ChatBluetoothManager02
import javax.inject.Inject

abstract class BaseActivity : ComponentActivity() {


    @Inject
    lateinit var chatBluetoothManager: ChatBluetoothManager02

}
inline fun <reified Activity : ComponentActivity> Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        else -> {
            var context = this
            while (context is ContextWrapper) {
                context = context.baseContext
                if (context is Activity) return context
            }
            null
        }
    }
}

