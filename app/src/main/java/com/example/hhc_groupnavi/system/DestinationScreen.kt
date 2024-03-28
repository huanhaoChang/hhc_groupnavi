package com.example.hhc_groupnavi.system

sealed class DestinationScreen(val route: String) {
    data object Signup: DestinationScreen("signup")
    data object Login: DestinationScreen("login")
    data object Profile: DestinationScreen("profile")
    data object EditProfile: DestinationScreen("editprofile")
    data object Group: DestinationScreen("group")
    data object CreateGroup: DestinationScreen("creategroup")
    data object EditGroup: DestinationScreen("editgroup")
    data object JoinGroup: DestinationScreen("joingroup")
    data object AddMember: DestinationScreen("addmember")
    data object Map: DestinationScreen("map")
    data object RoutePlan: DestinationScreen("routeplan")

    data object NavigationTest: DestinationScreen("navigationtest")
}