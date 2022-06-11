package com.razzaghi.bluetoothchat01.business.core

data class Dialog(
    val title: String,
    val description: String
)

val FAILED_To_Connect_DIALOG = Dialog(
    title = "Request Failed",
    description = "Request Failed To Make Connect To Device, Since request has been failed, please try again!"
)


val FAILED_To_Init_Connect_DIALOG = Dialog(
    title = "Request Failed",
    description = "Request Failed To Make Initialising For Connect To Device, Since request has been failed, please try again!"
)



val FAILED_From_Connect_DIALOG = Dialog(
    title = "Request Failed",
    description = "Request Failed From Device To Make Connect To You, Since request has been failed, please try again!"
)


val FAILED_From_Init_Connect_DIALOG = Dialog(
    title = "Request Failed",
    description = "Request Failed To Make Initialising To Connect From Device, Since request has been failed, please try again!"
)


val FAILED_Read_messages_DIALOG = Dialog(
    title = "Request Failed",
    description = "Request Failed From Reading Messages, Since request has been failed, please try again!"
)

val FAILED_Write_messages_DIALOG = Dialog(
    title = "Request Failed",
    description = "Request Failed From Writing Messages, Since request has been failed, please try again!"
)

val FAILED_to_init_transferring_DIALOG = Dialog(
    title = "Request Failed",
    description = "Request Failed To Make Initialising Transferring, Since request has been failed, please try again!"
)
