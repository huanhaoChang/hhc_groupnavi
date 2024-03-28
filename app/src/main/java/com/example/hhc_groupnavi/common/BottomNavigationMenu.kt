package com.example.hhc_groupnavi.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hhc_groupnavi.R
import com.example.hhc_groupnavi.system.DestinationScreen
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme

enum class BottomNavigationItem(
    val icon: Int,
    val navDestination: DestinationScreen,
) {
    GROUP(
        icon = R.drawable.ic_group,
        navDestination = DestinationScreen.Group,
    ),
    MAP(
        icon = R.drawable.ic_map,
        navDestination = DestinationScreen.Map,
    ),
    ROUTEPLAN(
        icon = R.drawable.ic_route,
        navDestination = DestinationScreen.RoutePlan,
    ),
    MYPROFILE(
        icon = R.drawable.ic_person,
        navDestination = DestinationScreen.Profile
    )
}

@Composable
fun BottomNavigationMenu(
    navController: NavController?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(color = Color.White)
    ) {
        for (item in BottomNavigationItem.entries) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp)
                    .weight(1f)
                    .clickable {
                        navigateTo(
                            navController = navController,
                            dest = item.navDestination
                        )
                    }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavigationItemPreview() {
    Hhc_groupnaviTheme{
        BottomNavigationMenu(navController = null)
    }
}