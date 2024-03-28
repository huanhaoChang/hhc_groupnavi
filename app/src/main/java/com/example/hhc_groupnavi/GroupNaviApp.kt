package com.example.hhc_groupnavi

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hhc_groupnavi.common.NotificationMessage
import com.example.hhc_groupnavi.featuretest.NavigationTestScreen
import com.example.hhc_groupnavi.screen.AddMemberScreen
import com.example.hhc_groupnavi.screen.CreateGroupScreen
import com.example.hhc_groupnavi.screen.EditGroupScreen
import com.example.hhc_groupnavi.screen.EditProfileScreen
import com.example.hhc_groupnavi.screen.GroupScreen
import com.example.hhc_groupnavi.screen.JoinGroupScreen
import com.example.hhc_groupnavi.screen.LoginScreen
import com.example.hhc_groupnavi.screen.MapScreen
import com.example.hhc_groupnavi.screen.ProfileScreen
import com.example.hhc_groupnavi.screen.RoutePlanScreen
import com.example.hhc_groupnavi.screen.SignupScreen
import com.example.hhc_groupnavi.system.DestinationScreen
import com.example.hhc_groupnavi.system.GroupNaviViewModel

@Composable
fun GroupNaviApp() {

    val vm = hiltViewModel<GroupNaviViewModel>()
    val navController = rememberNavController()

    NotificationMessage(vm = vm)

    NavHost(
        navController = navController,
        startDestination = DestinationScreen.Signup.route,
    ) {
        composable(DestinationScreen.Signup.route) {
            SignupScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.Login.route) {
            LoginScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.Profile.route) {
            ProfileScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.EditProfile.route) {
            EditProfileScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.Group.route) {
            GroupScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.CreateGroup.route) {
            CreateGroupScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.JoinGroup.route) {
            JoinGroupScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.EditGroup.route) {
            EditGroupScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.AddMember.route) {
            AddMemberScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.Map.route) {
            MapScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.RoutePlan.route) {
            RoutePlanScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.NavigationTest.route) {
            NavigationTestScreen(navController = navController, vm = vm)
        }
    }



}