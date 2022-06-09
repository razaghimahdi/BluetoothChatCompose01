package com.razzaghi.bluetoothchat01.di


 import android.app.Application
 import android.bluetooth.BluetoothAdapter
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
        bluetoothAdapter: BluetoothAdapter
    ): ChatBluetoothManager02 {
        return ChatBluetoothManager02(bluetoothAdapter)
    }
}