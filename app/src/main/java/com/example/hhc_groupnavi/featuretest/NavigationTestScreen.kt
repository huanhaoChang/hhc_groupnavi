package com.example.hhc_groupnavi.featuretest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.hhc_groupnavi.common.navigateTo
import com.example.hhc_groupnavi.system.DestinationScreen
import com.example.hhc_groupnavi.system.GroupNaviViewModel
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme

@Composable
fun NavigationTestScreen(navController: NavController? = null, vm: GroupNaviViewModel? = null) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.Signup,
                )
            }
        ) {
            Text(text = "SIGNUP")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.Login,
                )
            }
        ) {
            Text(text = "LOGIN")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.Profile,
                )
            }
        ) {
            Text(text = "PROFILE")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.EditProfile,
                )
            }
        ) {
            Text(text = "EDIT PROFILE")
        }

        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.Group,
                )
            }
        ) {
            Text(text = "GROUP")
        }

        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.CreateGroup,
                )
            }
        ) {
            Text(text = "CREATE GROUP")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.EditGroup,
                )
            }
        ) {
            Text(text = "EDIT GROUP")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.JoinGroup,
                )
            }
        ) {
            Text(text = "JOIN GROUP")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.AddMember,
                )
            }
        ) {
            Text(text = "ADD MEMBER")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.Map,
                )
            }
        ) {
            Text(text = "MAP")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.RoutePlan,
                )
            }
        ) {
            Text(text = "ROUTE PLAN")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.NavigationTest,
                )
            }
        ) {
            Text(text = "NAVIGATION TEST")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NavigationTestScreenPreview() {
    Hhc_groupnaviTheme {
        NavigationTestScreen()
    }
}