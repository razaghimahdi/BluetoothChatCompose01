package com.razzaghi.bluetoothchat01.business.domain

sealed class BluetoothState {

    object None : BluetoothState() { // we're doing nothing
        override fun toString(): String {
            return "None"
        }
    }

    object Listen : BluetoothState() { // now listening for incoming connections
        override fun toString(): String {
            return "Listen"
        }
    }

    object Connecting : BluetoothState() {
        // now initiating an outgoing connection
        override fun toString(): String {
            return "Connecting"
        }
    }

    object Connected : BluetoothState() {
        // now connected to a remote device
        override fun toString(): String {
            return "Connected"
        }
    }

}
