package com.razzaghi.bluetoothchat01.presentation.screen.main.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.razzaghi.bluetoothchat01.business.domain.DeviceData


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceListItem(device: DeviceData, onSelect: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp), onClick = onSelect
    ) {

        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {

            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
                Text(text = device.deviceName, modifier = Modifier.padding(12.dp))
            }

            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                Text(text = device.deviceHardwareAddress, modifier = Modifier.padding(12.dp))
            }

        }
    }


}

