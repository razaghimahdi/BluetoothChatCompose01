package com.razzaghi.bluetoothchat01.presentation.screen.chat

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.razzaghi.bluetoothchat01.R
import com.razzaghi.bluetoothchat01.business.constatnts.BluetoothConstants.MESSAGE_TYPE_SENT
import com.razzaghi.bluetoothchat01.business.domain.BluetoothConnectionState
import com.razzaghi.bluetoothchat01.business.domain.Message
import com.razzaghi.bluetoothchat01.business.util.MessageTools.convertToString
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.BluetoothManagerEvent
import com.razzaghi.bluetoothchat01.presentation.bluetooth_manager.ChatBluetoothManager
import com.razzaghi.bluetoothchat01.presentation.screen.chat.state.ChatEvents
import com.razzaghi.bluetoothchat01.presentation.screen.chat.state.ChatState
import com.razzaghi.bluetoothchat01.presentation.screen.main.checkIsLoading
import com.razzaghi.bluetoothchat01.presentation.ui.component.DefaultScreenUI
import com.razzaghi.bluetoothchat01.presentation.ui.theme.blue_400
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
            chatBluetoothManager.state.errorQueue.isEmpty()) {
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
                Messages(
                    messages = chatBluetoothManager.state.messagesList,
                    modifier = Modifier.weight(1f),
                )

                UserInputText(state = state, events = events)

            }
        }


    }

}


@Composable
fun Messages(
    messages: List<Message>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {

        LazyColumn(
            reverseLayout = true,
            contentPadding =
            WindowInsets.statusBars.add(WindowInsets(top = 90.dp)).asPaddingValues(),
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (index in messages.indices) {
                val prevType = messages.getOrNull(index - 1)?.type
                val nextType = messages.getOrNull(index + 1)?.type
                val content = messages[index]
                val isFirstMessageByAuthor = prevType != content.type
                val isLastMessageByAuthor = nextType != content.type

                item {
                    ShowMessage(
                        msg = content,
                        isUserMe = content.type == MESSAGE_TYPE_SENT,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }
            }
        }


    }
}


@Composable
fun ShowMessage(
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.primaryVariant
    }

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            // Avatar
            Image(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colors.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painterResource(id = R.drawable.ic_user00),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            msg = msg,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}


@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameTimestamp(msg)
        }
        ChatItemBubble(msg, isUserMe)
        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun AuthorNameTimestamp(msg: Message) {
    // Combine author and timestamp for a11y.
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = msg.type.convertToString(),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = msg.time.convertToString(),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colors.primaryVariant
        )
    }
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)


@Composable
fun ChatItemBubble(
    message: Message,
    isUserMe: Boolean,
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.primaryVariant
    }

    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = ChatBubbleShape
        ) {
            ClickableMessage(
                message = message,
            )
        }

    }
}


@Composable
fun ClickableMessage(
    message: Message,
) {


    Text(
        text = message.message,
        style = MaterialTheme.typography.button.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
    )
}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    state: ChatState,
    events: (ChatEvents) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(color = blue_400),
        horizontalArrangement = Arrangement.End
    ) {


        BasicTextField(
            value = state.chatInput,
            onValueChange = {
                events(ChatEvents.UpdateInputChat(it))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            maxLines = 1,
            cursorBrush = SolidColor(LocalContentColor.current),
            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
        )
        Card(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .align(Alignment.CenterVertically),
            onClick = {}

        ) {
            Icon(
                imageVector = Icons.Default.Send,
                modifier = Modifier
                    .size(40.dp)
                    .padding(6.dp),
                contentDescription = null
            )
        }
    }
}




