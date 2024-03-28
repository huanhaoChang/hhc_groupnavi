package com.example.hhc_groupnavi.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.hhc_groupnavi.system.GroupNaviViewModel
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme

@Composable
fun RoutePlanScreen(navController: NavController? = null, vm: GroupNaviViewModel? = null) {
    Text(
        text = "RoutePlanScreen"
    )
}


@Preview(showBackground = true)
@Composable
fun RoutePlanScreenPreview() {
    Hhc_groupnaviTheme {
        RoutePlanScreen()
    }
}