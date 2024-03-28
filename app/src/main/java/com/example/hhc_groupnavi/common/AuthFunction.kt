package com.example.hhc_groupnavi.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.hhc_groupnavi.system.DestinationScreen
import com.example.hhc_groupnavi.system.GroupNaviViewModel

@Composable
fun CheckSignedIn(
    navController: NavController?,
    vm: GroupNaviViewModel?,
) {
    val alreadySignedIn = remember {
        mutableStateOf(false)
    }
    val signedIn = vm?.signedIn?.value
    if (signedIn == true and !alreadySignedIn.value) {
        alreadySignedIn.value = true
        navController?.navigate(DestinationScreen.Profile.route) {
            popUpTo(0)
        }
    }
}