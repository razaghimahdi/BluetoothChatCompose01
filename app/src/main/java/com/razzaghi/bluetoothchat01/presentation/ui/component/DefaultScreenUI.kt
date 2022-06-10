package com.razzaghi.bluetoothchat01.presentation.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DefaultScreenUI(
    isLoading: Boolean,
    content: @Composable () -> Unit,
) {

        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                content()

                if (isLoading) {
                    CircularIndeterminateProgressBar()
                }
            }
        }
 }






@Composable
fun CircularIndeterminateProgressBar() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        CircularProgressIndicator(
            modifier = Modifier,
            color = MaterialTheme.colors.primary
        )
    }
}





