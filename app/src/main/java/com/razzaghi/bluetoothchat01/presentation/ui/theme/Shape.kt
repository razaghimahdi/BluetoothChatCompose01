package com.razzaghi.bluetoothchat01.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

@Composable
fun TextFieldTheme() = TextFieldDefaults.textFieldColors(
    backgroundColor =  MaterialTheme.colors.primary,
    cursorColor = Color.White,
    textColor = Color.White,
    disabledLabelColor = MaterialTheme.colors.background,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent
)