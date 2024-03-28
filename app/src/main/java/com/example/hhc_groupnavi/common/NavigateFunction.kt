package com.example.hhc_groupnavi.common

import androidx.navigation.NavController
import com.example.hhc_groupnavi.system.DestinationScreen

fun navigateTo(
    navController: NavController?,
    dest: DestinationScreen
) {
    navController?.navigate(dest.route) {
        popUpTo(dest.route)
        launchSingleTop = true
    }
}