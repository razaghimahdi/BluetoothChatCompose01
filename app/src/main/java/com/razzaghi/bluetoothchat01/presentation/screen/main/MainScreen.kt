package com.razzaghi.bluetoothchat01.presentation.screen.main

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.razzaghi.bluetoothchat01.business.domain.BluetoothConnectionState
import com.razzaghi.bluetoothchat01.business.domain.DeviceData
import com.razzaghi.bluetoothchat01.business.util.BluetoothTools.getPairedDevices
import com.razzaghi.bluetoothchat01.presentation.MainActivity
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.BluetoothManagerEvent
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.ChatBluetoothManager
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.ChatBluetoothManager02
import com.razzaghi.bluetoothchat01.presentation.getActivity
import com.razzaghi.bluetoothchat01.presentation.screen.main.state.MainEvents
import com.razzaghi.bluetoothchat01.presentation.screen.main.state.MainState
import com.razzaghi.bluetoothchat01.presentation.ui.component.CustomSpacer
import com.razzaghi.bluetoothchat01.presentation.ui.component.DefaultScreenUI
import com.razzaghi.bluetoothchat01.presentation.ui.theme.blue_400
import com.razzaghi.bluetoothchat01.presentation.ui.theme.green_400


private val TAG = "AppDebug MainScreen"

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MainScreen(
    state: MainState,
    events: (MainEvents) -> Unit,
    navigateToChatScreen: () -> Unit,
    chatBluetoothManager: ChatBluetoothManager,
) {


    DefaultScreenUI(
        isLoading = state.isLoading,
        queue = chatBluetoothManager.state.errorQueue,
        onRemoveHeadFromQueue = {
            chatBluetoothManager.onTriggerEvent(BluetoothManagerEvent.OnRemoveHeadFromQueue)
        }
    ) {

        // TODO(Show user bluetooth is on or off)

        val activity = LocalContext.current.getActivity<MainActivity>()
        activity?.apply {
            registerBroadCast(activity = this, events = events)
        }


        InitLauncherForActivityResult(
            events = events,
            bluetoothAdapter = chatBluetoothManager.bluetoothAdapter
        )

        // Camera permission state
        val cameraPermissionState = rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )


        if (state.shouldBluetoothStartScan) {
            if (!cameraPermissionState.allPermissionsGranted) {
                SideEffect {
                    cameraPermissionState.launchMultiplePermissionRequest()
                }
            } else {
                startDiscovery(chatBluetoothManager.bluetoothAdapter)
            }
        }

        if (state.shouldMakeDeviceVisible) {
            events(MainEvents.UpdateShouldMakeDeviceVisible(false))
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            activity?.startActivity(discoverableIntent)
        }


        if (chatBluetoothManager.state.bluetoothConnectionState == BluetoothConnectionState.Connected) {
            navigateToChatScreen()
        }


        StatelessMainScreen(
            state = state,
            events = events,
            chatBluetoothManager = chatBluetoothManager
        )

    }

}

@Composable
fun StatelessMainScreen(
    state: MainState,
    events: (MainEvents) -> Unit,
    chatBluetoothManager: ChatBluetoothManager,
) {
    Box {
        Column() {

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    events(MainEvents.UpdateIsLoading(true))
                    events(MainEvents.UpdateShouldBluetoothStartScan(true))

                }) {
                    Text("Search Devices", style = TextStyle(fontWeight = FontWeight.Bold))
                }

                CustomSpacer(size = 8)

                Button(onClick = {
                    events(MainEvents.UpdateShouldMakeDeviceVisible(true))
                }) {
                    Text("Make Visible", style = TextStyle(fontWeight = FontWeight.Bold))
                }
            }

            AnimatedVisibility(
                visible =
                state.pairedDevices.isNotEmpty() ||
                        state.searchedDevices.isNotEmpty()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    item {
                        AnimatedVisibility(
                            visible =
                            state.pairedDevices.isNotEmpty()
                        ) {
                            Text(
                                text = "Paired Devices:",
                                style = TextStyle(
                                    color = green_400,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    items(state.pairedDevices) { device ->
                        DeviceListItem(
                            device = device,
                            onSelect = {
                                connectDevice(
                                    device,
                                    chatBluetoothManager.bluetoothAdapter
                                ) { device, secure ->
                                    chatBluetoothManager.onTriggerEvent(
                                        BluetoothManagerEvent.ConnectToOtherDevice(
                                            device,
                                            secure
                                        )
                                    )
                                }
                            },
                        )
                    }


                    item {

                        AnimatedVisibility(
                            visible =
                            state.searchedDevices.isNotEmpty()
                        ) {

                            Text(
                                text = "Searched Devices:",
                                style = TextStyle(
                                    color = blue_400,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    items(state.searchedDevices) { device ->
                        DeviceListItem(
                            device = device,
                            onSelect = {
                                connectDevice(
                                    device,
                                    chatBluetoothManager.bluetoothAdapter
                                ) { device, secure ->
                                    chatBluetoothManager.onTriggerEvent(
                                        BluetoothManagerEvent.ConnectToOtherDevice(
                                            device,
                                            secure
                                        )
                                    )
                                }
                            },
                        )
                    }


                }
            }


        }
    }
}


@SuppressLint("MissingPermission")
private fun connectDevice(
    deviceData: DeviceData,
    bluetoothAdapter: BluetoothAdapter,
    connectCallback: (BluetoothDevice, Boolean) -> Unit
) {

    Log.i(TAG, "connectDevice deviceData: "+deviceData)

    // Cancel discovery because it's costly and we're about to connect
    bluetoothAdapter.cancelDiscovery()
    val deviceAddress = deviceData.deviceHardwareAddress

    val device = bluetoothAdapter.getRemoteDevice(deviceAddress)

    // Attempt to connect to the device
    connectCallback(device, true)
}


@Composable
fun InitLauncherForActivityResult(
    events: (MainEvents) -> Unit,
    bluetoothAdapter: BluetoothAdapter
) {
    val resultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) { // There are no request codes anymore
                events(MainEvents.UpdatePairedDevice(getPairedDevices(bluetoothAdapter = bluetoothAdapter)))
                events(MainEvents.UpdateIsBluetoothOn(true))
            } else {
                events(MainEvents.UpdateIsBluetoothOn(false))
            }
        }

    if (!bluetoothAdapter.isEnabled) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        SideEffect {
            resultLauncher.launch(enableBtIntent)
        }
    } else {
        events(MainEvents.UpdateIsBluetoothOn(true))
        events(MainEvents.UpdatePairedDevice(getPairedDevices(bluetoothAdapter = bluetoothAdapter)))
    }

}


fun registerBroadCast(activity: Activity, events: (MainEvents) -> Unit) {
    // Register for broadcasts when a device is discovered.
    var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    activity.registerReceiver(startBluetoothReceiver(events = events), filter)

    // Register for broadcasts when discovery has finished
    filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
    activity.registerReceiver(startBluetoothReceiver(events = events), filter)
}

@SuppressLint("MissingPermission")
private fun startDiscovery(adapter: BluetoothAdapter) {
    // If we're already discovering, stop it
    if (adapter.isDiscovering)
        adapter.cancelDiscovery()

    // Request discover from BluetoothAdapter
    adapter.startDiscovery()
}

fun startBluetoothReceiver(events: (MainEvents) -> Unit): BroadcastReceiver {
    Log.i(TAG, "startBluetoothReceiver: ")

    return object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {

            events(MainEvents.UpdateShouldBluetoothStartScan(false))

            val action = intent.action

            val devicesList = arrayListOf<DeviceData>()

            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val deviceName = device?.name
                val deviceHardwareAddress = device?.address // MAC address

                if (!deviceName.isNullOrEmpty() && !deviceHardwareAddress.isNullOrEmpty()) {
                    val deviceData = DeviceData(deviceName, deviceHardwareAddress)
                    devicesList.add(deviceData)
                }
                events(MainEvents.UpdateSearchedDevice(devicesList))

            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                Log.i(TAG, "onReceive ACTION_DISCOVERY_FINISHED: ")
                events(MainEvents.UpdateIsLoading(false))
                // TODO(Show user bluetooth has been searched)
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceListItem(device: DeviceData, onSelect: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp), onClick = onSelect
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = device.deviceName, modifier = Modifier.padding(12.dp))
        }
    }


}

