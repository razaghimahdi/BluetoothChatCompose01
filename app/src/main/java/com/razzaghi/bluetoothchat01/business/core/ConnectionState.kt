package com.razzaghi.bluetoothchat01.business.core

sealed class ConnectionState{

    object None : ConnectionState() {
        override fun toString(): String {
            return "None"
        }
    }

    object Inited : ConnectionState() {
        override fun toString(): String {
            return "Inited"
        }
    }

    object Closed : ConnectionState() {
        override fun toString(): String {
            return "Closed"
        }
    }

    object Failed : ConnectionState() {
        override fun toString(): String {
            return "Failed"
        }
    }

    object Connected : ConnectionState() {
        override fun toString(): String {
            return "Connected"
        }
    }

    object Wrote : ConnectionState() {
        override fun toString(): String {
            return "Wrote"
        }
    }
}
