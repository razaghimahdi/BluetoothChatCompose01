package com.razzaghi.bluetoothchat01.presentation.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.razzaghi.bluetoothchat01.presentation.ui.theme.grey_200

@Composable
fun CustomSpacer(size: Int) {
    Spacer(
        modifier = Modifier
            .height(size.dp)
            .width(size.dp)
    )
}


@Composable
fun CustomDivider() {
    CustomSpacer(4)
    Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = grey_200)
    CustomSpacer(4)
}

