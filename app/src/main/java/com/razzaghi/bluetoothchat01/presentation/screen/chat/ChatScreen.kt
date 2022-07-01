package com.razzaghi.bluetoothchat01.presentation.screen.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.razzaghi.bluetoothchat01.R
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MESSAGE_TYPE_SENT
import com.razzaghi.bluetoothchat01.business.domain.BluetoothConnectionState
import com.razzaghi.bluetoothchat01.business.domain.Message
import com.razzaghi.bluetoothchat01.business.util.MessageTools.convertToString
import com.razzaghi.bluetoothchat01.business.util.MessageTools.sortMessagesByCount
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.BluetoothManagerEvent
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.ChatBluetoothManager
import com.razzaghi.bluetoothchat01.presentation.screen.chat.component.Messages
import com.razzaghi.bluetoothchat01.presentation.screen.chat.state.ChatEvents
import com.razzaghi.bluetoothchat01.presentation.screen.chat.state.ChatState
import com.razzaghi.bluetoothchat01.presentation.screen.main.checkIsLoading
import com.razzaghi.bluetoothchat01.presentation.ui.component.DefaultScreenUI
import com.razzaghi.bluetoothchat01.presentation.ui.theme.TextFieldTheme
import com.razzaghi.bluetoothchat01.presentation.ui.theme.grey_200


private val TAG = "AppDebug MainScreen"

@SuppressLint("MissingPermission")
@OptIn(
    ExperimentalPermissionsApi::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class
)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ChatScreen(
    state: ChatState,
    events: (ChatEvents) -> Unit,
    chatBluetoothManager: ChatBluetoothManager,
    popBackStack: () -> Unit
) {
    DefaultScreenUI(
        isLoading = checkIsLoading(
            state.progressBarState,
            chatBluetoothManager.state.progressBarState
        ),
        queue = chatBluetoothManager.state.errorQueue,
        onRemoveHeadFromQueue = {
            chatBluetoothManager.onTriggerEvent(BluetoothManagerEvent.OnRemoveHeadFromQueue)
        }
    ) {

        if (chatBluetoothManager.state.bluetoothConnectionState != BluetoothConnectionState.Connected &&
            chatBluetoothManager.state.errorQueue.isEmpty()
        ) {
            popBackStack()
        }


        BackHandler {
            chatBluetoothManager.state.activeBluetoothSocket?.let { bluetoothSocket ->
                chatBluetoothManager.onTriggerEvent(
                    BluetoothManagerEvent.CloseTransferring(
                        bluetoothSocket
                    )
                )
            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(MaterialTheme.colors.primary),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = {
                        chatBluetoothManager.state.activeBluetoothSocket?.let { bluetoothSocket ->
                            chatBluetoothManager.onTriggerEvent(
                                BluetoothManagerEvent.CloseTransferring(
                                    bluetoothSocket
                                )
                            )
                        }

                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = grey_200
                        )
                    }
                }



                Messages(
                    messages = chatBluetoothManager.state.messagesList.sortMessagesByCount(),
                    modifier = Modifier.weight(1f),
                )

                UserInputText(
                    state = state,
                    events = events,
                    chatBluetoothManager = chatBluetoothManager
                )

            }
        }


    }

}


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    chatBluetoothManager: ChatBluetoothManager,
    state: ChatState,
    events: (ChatEvents) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
    ) {


        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                modifier = Modifier.fillMaxWidth(.80f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send,
                ),
                value = state.chatInput,
                onValueChange = {
                    events(ChatEvents.UpdateInputChat(it))
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                label = { Text("Message here...", color = grey_200) },
                colors = TextFieldTheme(),
            )

            Button(onClick = {
                if (state.chatInput.isNotEmpty()) {
                    chatBluetoothManager.onTriggerEvent(
                        BluetoothManagerEvent.WriteFromTransferring(
                            state.chatInput.toByteArray()
                        )
                    )
                }
            }) {
                Text(
                    text = "SEND",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
            }

        }

    }
}



