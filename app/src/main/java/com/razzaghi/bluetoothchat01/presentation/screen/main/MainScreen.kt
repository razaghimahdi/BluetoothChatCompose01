package com.razzaghi.bluetoothchat01.presentation.screen.main

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.util.Log
import android.view.View
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
import com.razzaghi.bluetoothchat01.business.domain.DeviceData
import com.razzaghi.bluetoothchat01.business.util.BluetoothTools.getPairedDevices
import com.razzaghi.bluetoothchat01.presentation.MainActivity
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.ChatBluetoothManager02
import com.razzaghi.bluetoothchat01.presentation.getActivity
import com.razzaghi.bluetoothchat01.presentation.screen.main.state.MainEvents
import com.razzaghi.bluetoothchat01.presentation.screen.main.state.MainState
import com.razzaghi.bluetoothchat01.presentation.ui.component.CustomSpacer
import com.razzaghi.bluetoothchat01.presentation.ui.component.DefaultScreenUI
import com.razzaghi.bluetoothchat01.presentation.ui.theme.blue_400
import com.razzaghi.bluetoothchat01.presentation.ui.theme.green_400


@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MainScreen(
    state: MainState,
    events: (MainEvents) -> Unit,
    navigateToChatScreen: () -> Unit,
    chatBluetoothManager: ChatBluetoothManager02,
) {

    val TAG = "AppDebug MainScreen"

    DefaultScreenUI(){

    val resultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i(TAG, "MainScreen result.data: "+result.data)
            Log.i(TAG, "MainScreen result.resultCode: "+result.resultCode)
            if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
                events(MainEvents.UpdatePairedDevice(getPairedDevices(bluetoothAdapter = chatBluetoothManager.bluetoothAdapter)))
            }
        }

    if (!chatBluetoothManager.bluetoothAdapter.isEnabled) {
        Log.i(TAG, "MainScreen !chatBluetoothManager.bluetoothAdapter.isEnabled: ")
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        SideEffect {
            resultLauncher.launch(enableBtIntent)
        }
    } else {
        Log.i(TAG, "MainScreen chatBluetoothManager.bluetoothAdapter.isEnabled: ")
        events(MainEvents.UpdatePairedDevice(getPairedDevices(bluetoothAdapter = chatBluetoothManager.bluetoothAdapter)))
    }


    Log.i(TAG, "MainScreen state.pairedDevices: "+state.pairedDevices)


        Box {

            Column() {

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { /*TODO*/ }) {
                        Text("Search Devices", style = TextStyle(fontWeight = FontWeight.Bold))
                    }

                    CustomSpacer(size = 8)

                    Button(onClick = { /*TODO*/ }) {
                        Text("Make Visible", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                }

                AnimatedVisibility(visible = state.pairedDevices.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                    ) {

                        item {
                            Text(
                                text = "Paired Devices:",
                                style = TextStyle(
                                    color = green_400,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        items(state.pairedDevices) { device ->
                            DeviceListItem(
                                device = device,
                                onSelect = navigateToChatScreen,
                            )
                        }
                    }
                }



                AnimatedVisibility(visible = state.searchedDevices.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                    ) {

                        item {
                            Text(
                                text = "Searched Devices:",
                                style = TextStyle(
                                    color = blue_400,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        items(state.searchedDevices) { device ->
                            DeviceListItem(
                                device = device,
                                onSelect = navigateToChatScreen,
                            )
                        }
                    }
                }


            }


        }
    }

}


@SuppressLint("MissingPermission")
@OptIn(ExperimentalComposeUiApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun CheckBluetoothIsEnabled(enabled: Boolean, resultLauncher: ActivityResultLauncher<Intent>?) {
    if (!enabled) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        resultLauncher?.launch(enableBtIntent)
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

