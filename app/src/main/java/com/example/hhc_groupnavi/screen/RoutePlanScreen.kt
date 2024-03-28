package com.example.hhc_groupnavi.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun RoutePlanScreen(navController: NavController? = null, vm: GroupNaviViewModel? = null) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PLANNING ROUTE",
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {}
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {}
                )
            }

        }
        BottomNavigationMenu(navController = navController)
    }
}


@Preview(showBackground = true)
@Composable
fun RoutePlanScreenPreview() {
    Hhc_groupnaviTheme {
        RoutePlanScreen()
    }
}