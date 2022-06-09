package com.razzaghi.bluetoothchat01.presentation.ui.navigation

import androidx.navigation.NamedNavArgument

sealed class MainNavigation(val route: String, val arguments: List<NamedNavArgument>){

    object Main: MainNavigation(
        route = "Main",
        arguments = emptyList()
    )

    object Chat: MainNavigation(
        route = "Chat",
        arguments = emptyList()
    )
}
