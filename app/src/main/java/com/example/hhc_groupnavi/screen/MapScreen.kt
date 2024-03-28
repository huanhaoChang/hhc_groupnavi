package com.example.hhc_groupnavi.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.hhc_groupnavi.common.BottomNavigationMenu
import com.example.hhc_groupnavi.system.GroupNaviViewModel
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapScreen(navController: NavController? = null, vm: GroupNaviViewModel? = null) {

    var currentLocation by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }
    var cameraPositionState by remember {
        mutableStateOf(CameraPositionState())
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {

            Box(modifier = Modifier.fillMaxSize()) {

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(
                            position = currentLocation
                        ),
                    )
                }

            }

        }
        BottomNavigationMenu(navController = navController)
    }
}


@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    Hhc_groupnaviTheme {
        MapScreen()
    }
}