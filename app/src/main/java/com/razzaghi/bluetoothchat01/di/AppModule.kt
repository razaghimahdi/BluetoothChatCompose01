package com.razzaghi.bluetoothchat01.di


import android.app.Application
import android.bluetooth.BluetoothAdapter
import com.razzaghi.bluetoothchat01.business.interactors.connect_from_other_device.AcceptFromOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_from_other_device.CloseFromOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_from_other_device.InitFromOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_to_other_device.CloseToOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_to_other_device.ConnectToOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.connect_to_other_device.InitConnectToOtherDeviceInteractor
import com.razzaghi.bluetoothchat01.business.interactors.transfer.CloseTransferMessagesFromDevicesInteractor
import com.razzaghi.bluetoothchat01.business.interactors.transfer.InitTransferMessagesFromDevicesInteractor
import com.razzaghi.bluetoothchat01.business.interactors.transfer.ReadTransferMessagesFromDevicesInteractor
import com.razzaghi.bluetoothchat01.business.interactors.transfer.WriteTransferMessagesFromDevicesInteractor
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.ChatBluetoothManager
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.ChatBluetoothManager02
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideBluetoothAdapter(): BluetoothAdapter {
        return BluetoothAdapter.getDefaultAdapter()
    }

    @Singleton
    @Provides
    fun provideChatBluetoothManager(
        bluetoothAdapter: BluetoothAdapter,
        closeTransferMessagesFromDevicesInteractor: CloseTransferMessagesFromDevicesInteractor,
        initTransferMessagesFromDevicesInteractor: InitTransferMessagesFromDevicesInteractor,
        readTransferMessagesFromDevicesInteractor: ReadTransferMessagesFromDevicesInteractor,
        writeTransferMessagesFromDevicesInteractor: WriteTransferMessagesFromDevicesInteractor,
        closeToOtherDeviceInteractor: CloseToOtherDeviceInteractor,
        connectToOtherDeviceInteractor: ConnectToOtherDeviceInteractor,
        initConnectToOtherDeviceInteractor: InitConnectToOtherDeviceInteractor,
        acceptFromOtherDeviceInteractor: AcceptFromOtherDeviceInteractor,
        closeFromOtherDeviceInteractor: CloseFromOtherDeviceInteractor,
        initFromOtherDeviceInteractor: InitFromOtherDeviceInteractor,
    ): ChatBluetoothManager {
        return ChatBluetoothManager(
            bluetoothAdapter = bluetoothAdapter,
            closeTransferMessagesFromDevicesInteractor = closeTransferMessagesFromDevicesInteractor,
            initTransferMessagesFromDevicesInteractor = initTransferMessagesFromDevicesInteractor,
            readTransferMessagesFromDevicesInteractor = readTransferMessagesFromDevicesInteractor,
            writeTransferMessagesFromDevicesInteractor = writeTransferMessagesFromDevicesInteractor,
            closeToOtherDeviceInteractor = closeToOtherDeviceInteractor,
            connectToOtherDeviceInteractor = connectToOtherDeviceInteractor,
            initConnectToOtherDeviceInteractor = initConnectToOtherDeviceInteractor,
            acceptFromOtherDeviceInteractor = acceptFromOtherDeviceInteractor,
            closeFromOtherDeviceInteractor = closeFromOtherDeviceInteractor,
            initFromOtherDeviceInteractor = initFromOtherDeviceInteractor,
        )
    }
}