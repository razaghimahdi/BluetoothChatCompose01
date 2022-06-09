package com.razzaghi.bluetoothchat01.business.domain

sealed class BluetoothConnectionState {

    object None : BluetoothConnectionState() { // we're doing nothing
        override fun toString(): String {
            return "None"
        }
    }

    object Listen : BluetoothConnectionState() { // now listening for incoming connections
        override fun toString(): String {
            return "Listen"
        }
    }

    object Connecting : BluetoothConnectionState() {
        // now initiating an outgoing connection
        override fun toString(): String {
            return "Connecting"
        }
    }

    object Connected : BluetoothConnectionState() {
        // now connected to a remote device
        override fun toString(): String {
            return "Connected"
        }
    }

}
