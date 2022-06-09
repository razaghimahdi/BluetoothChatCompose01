package com.razzaghi.bluetoothchat01.business.domain

data class DeviceData(val deviceName: String,
                      val deviceHardwareAddress: String){

    override fun equals(other: Any?): Boolean {
        val deviceData = other as DeviceData
        return deviceHardwareAddress == deviceData.deviceHardwareAddress
    }

    override fun hashCode(): Int {
        return deviceHardwareAddress.hashCode()
    }

}