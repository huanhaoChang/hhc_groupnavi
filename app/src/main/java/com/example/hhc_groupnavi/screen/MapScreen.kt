package com.example.hhc_groupnavi.screen

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.hhc_groupnavi.R
import com.example.hhc_groupnavi.common.BottomNavigationMenu
import com.example.hhc_groupnavi.common.CommonImage
import com.example.hhc_groupnavi.common.UserImageCard
import com.example.hhc_groupnavi.system.GroupNaviViewModel
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState

@Composable
fun MapScreen(navController: NavController? = null, vm: GroupNaviViewModel? = null) {

    val context = LocalContext.current

    val userSystemId = vm?.userData?.value?.systemId
    val userName = vm?.userData?.value?.userName
    val userGroup = vm?.groupData?.value
    val groupMembers = userGroup?.groupMembers

    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    )

    // USER LOCATION
    var currentLocation by remember {
        mutableStateOf<LatLng?>(null)
    }
    var cameraPositionState by remember {
        mutableStateOf(CameraPositionState())
    }
    var cameraFocusCurrentLocation by remember {
        mutableStateOf(true)
    }

    // GROUP MEMBERS LOCATION
    var groupMembersLocation by remember {
        mutableStateOf<MutableMap<String, LatLng?>>(mutableMapOf())
    }
    var visibleMemberLocationMarker by remember {
        mutableStateOf(false)
    }

    // PASS & RECEIVE LOCATION
    var passLocation by remember {
        mutableStateOf<LatLng?>(null)
    }
    var isPassingActive by remember {
        mutableStateOf(false)
    }

    // LOCATION MANAGER
    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val locationCallback: LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    if (cameraFocusCurrentLocation) {
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(currentLocation!!, 15f)
                        )
                    }
                    if (isPassingActive) {
                        vm?.uploadUserLocation(userSystemId, currentLocation!!)
                        passLocation = currentLocation as LatLng
                    }
                }
            }
        }

    // FUNCTIONS
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        try {
            val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasFineLocationPermission && hasCoarseLocationPermission) {
                val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(3000)
                    .setMaxUpdateDelayMillis(100)
                    .build()

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(context, "Error obtaining location permissions", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun startPassLocation() {
        isPassingActive = true
    }

    fun startReceiveMembersLocation(memberSystemId: String) {
        vm?.fetchUserLocation(memberSystemId) { location ->
            location?.let {
                val memberLocation = LatLng(it.latitude, it.longitude)
                groupMembersLocation[memberSystemId] = memberLocation
            }
        }
    }

    fun stopPassLocation() {
        isPassingActive = false
        passLocation = null
        vm?.nullifyUserLocation(userSystemId)
    }

    fun stopReceiveMembersLocation(memberSystemId: String) {
        vm?.stopFetchUserLocation(memberSystemId)
        vm?.nullifyUserLocation(memberSystemId)
    }

    // LAUNCHERS
    val launchMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMaps ->
        val areGranted = permissionMaps.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            cameraFocusCurrentLocation = true
            startLocationUpdates()
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    // EFFECTS
    if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
        cameraFocusCurrentLocation = false
    }

    // COMPOSABLE
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {

            Box(modifier = Modifier.fillMaxSize()) {

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    currentLocation?.let {
                        Marker(
                            state = MarkerState(position = currentLocation!!),
                            title = "Current Location of $userName",
                        )
                    }

                    groupMembers?.forEach { memberSystemId ->
                        val memberLocation = groupMembersLocation[memberSystemId]
                        memberLocation?.let {
                            Marker(
                                state = MarkerState(position = memberLocation),
                                title = "Location of Member",
                                visible = visibleMemberLocationMarker,
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Your Location" +
                                " ${currentLocation?.latitude}, " +
                                "${currentLocation?.longitude}"
                    )
                    Button(
                        onClick = {
                            launchMultiplePermissions.launch(permissions)
                        }
                    ) {
                        Text(text = "Get Location")
                    }
                    Button(
                        onClick = {
                            cameraFocusCurrentLocation = true
                        }
                    ) {
                        Text(text = "Go to Current Location")
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Passing Number: " +
                                "${passLocation?.latitude}, " +
                                "${passLocation?.longitude}"
                    )

                    Button(
                        onClick = {
                            startPassLocation()
                        }
                    ) {
                        Text(text = "Start Pass")
                    }

                    Button(
                        onClick = {
                            visibleMemberLocationMarker = true
                            groupMembers?.forEach { memberSystemId ->
                                startReceiveMembersLocation(memberSystemId)
                            }
                        }
                    ) {
                        Text(text = "Start Receive")
                    }

                    Button(
                        onClick = {
                            stopPassLocation()
                        }
                    ) {
                        Text(text = "Stop Pass")
                    }

                    Button(
                        onClick = {
                            visibleMemberLocationMarker = false
                            groupMembers?.forEach { memberSystemId ->
                                stopReceiveMembersLocation(memberSystemId)
                            }
                        }
                    ) {
                        Text(text = "Stop Receive")
                    }
                }

            }

        }
        BottomNavigationMenu(navController = navController)
    }
}
