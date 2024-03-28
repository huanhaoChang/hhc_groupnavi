package com.example.hhc_groupnavi.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.hhc_groupnavi.R
import com.example.hhc_groupnavi.common.BottomNavigationMenu
import com.example.hhc_groupnavi.common.CarImageCard
import com.example.hhc_groupnavi.common.CarImageCover
import com.example.hhc_groupnavi.common.CircleIconCard
import com.example.hhc_groupnavi.common.CommonImage
import com.example.hhc_groupnavi.common.ProgressSpinner
import com.example.hhc_groupnavi.common.TitleWithBackArrow
import com.example.hhc_groupnavi.common.UserImageCard
import com.example.hhc_groupnavi.common.navigateTo
import com.example.hhc_groupnavi.data.DEFAULT_CAR_BRAND
import com.example.hhc_groupnavi.data.DEFAULT_CAR_MODEL
import com.example.hhc_groupnavi.data.DEFAULT_USER_ID
import com.example.hhc_groupnavi.data.DEFAULT_USER_NAME
import com.example.hhc_groupnavi.data.UserData
import com.example.hhc_groupnavi.system.DestinationScreen
import com.example.hhc_groupnavi.system.GroupNaviViewModel
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme

@Composable
fun ProfileScreen(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null
) {
    val isLoading = vm?.inProgress?.value

    if (isLoading == true) {
        ProgressSpinner()
    } else {
        val currentUserData = vm?.userData?.value

        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f)) {
                ProfileContent(
                    currentUserData = currentUserData,
                    onEdit = {
                        navigateTo(
                            navController = navController,
                            dest = DestinationScreen.EditProfile,
                        )
                    },
                    onLogout = {
                        vm?.onLogOut()
                        navigateTo(
                            navController = navController,
                            dest = DestinationScreen.Login,
                        )
                    },
                )
            }
            BottomNavigationMenu(navController = navController)
        }
    }
}

@Composable
fun ProfileContent(
    currentUserData: UserData? = null,
    onEdit: () -> Unit,
    onLogout: () -> Unit,
) {
    val username = currentUserData?.userName ?: DEFAULT_USER_NAME
    val userid = currentUserData?.userId ?: DEFAULT_USER_ID
    val carbrand = currentUserData?.userCar?.carBrand ?: DEFAULT_CAR_BRAND
    val carmodel = currentUserData?.userCar?.carModel ?: DEFAULT_CAR_MODEL
    val userimageurl = currentUserData?.userImageUrl
    val carimageurl = currentUserData?.userCar?.carImageUrl

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CarImageCover(imageUrl = carimageurl)
            UserImageCard(
                imageUrl = userimageurl,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 200.dp)
                    .border(
                        width = 4.dp,
                        color = Color.White,
                        shape = CircleShape,
                    )
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 100.dp)
        ) {
            Text(
                text = username,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "ID: $userid",
                fontSize = 15.sp,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = carbrand,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = carmodel,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        OutlinedButton(
            onClick = { onEdit.invoke() },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp
            ),
            modifier = Modifier
                .width(300.dp)
                .padding(8.dp)
        ) {
            Text(
                text = "EDIT",
                color = Color.Black
            )
        }
        OutlinedButton(
            onClick = { onLogout.invoke() },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp
            ),
            modifier = Modifier
                .width(300.dp)
                .padding(8.dp)
        ) {
            Text(
                text = "LOG OUT",
                color = Color.Black
            )
        }
    }
}


@Composable
fun EditProfileScreen(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null
) {
    val isLoading = vm?.inProgress?.value

    if (isLoading == true) {
        ProgressSpinner()
    } else {
        val currentUserData = vm?.userData?.value
        val focus = LocalFocusManager.current

        var userid by rememberSaveable {
            mutableStateOf(currentUserData?.userId ?: "")
        }
        var username by rememberSaveable {
            mutableStateOf(currentUserData?.userName ?: "")
        }
        var carbrand by rememberSaveable {
            mutableStateOf(currentUserData?.userCar?.carBrand ?: "")
        }
        var carmodel by rememberSaveable {
            mutableStateOf(currentUserData?.userCar?.carModel ?: "")
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f)) {
                EditProfile(
                    vm = vm,
                    userid = userid,
                    username = username,
                    carbrand = carbrand,
                    carmodel = carmodel,
                    onUserIdChange = { userid = it },
                    onUserNameChange = { username = it },
                    onCarBrandChange = { carbrand = it },
                    onCarModelChange = { carmodel = it },
                    onBack = {
                        navController?.popBackStack()
                    },
                    onSave = {
                        focus.clearFocus(force = true)
                        vm?.updateProfileData(
                            userid = userid,
                            username = username,
                            carbrand = carbrand,
                            carmodel = carmodel,
                        )
                        navigateTo(
                            navController = navController,
                            dest = DestinationScreen.Profile
                        )
                    }
                )
            }
            BottomNavigationMenu(navController = navController)
        }
    }
}

@Composable
fun EditProfile(
    vm: GroupNaviViewModel? = null,
    userid: String,
    username: String,
    carbrand: String,
    carmodel: String,
    onUserIdChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit,
    onCarBrandChange: (String) -> Unit,
    onCarModelChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    val userimageurl = vm?.userData?.value?.userImageUrl ?: ""
    val carimageurl = vm?.userData?.value?.userCar?.carImageUrl ?: ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        TitleWithBackArrow(title = "EDIT PROFILE") {
            onBack.invoke()
        }
        EditUserImage(
            imageUrl = userimageurl,
            vm = vm
        )
        TextField(
            value = userid,
            onValueChange = onUserIdChange,
            label = { Text(text = "ID") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            modifier = Modifier.padding(8.dp),
        )
        TextField(
            value = username,
            onValueChange = onUserNameChange,
            label = { Text(text = "NAME") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            modifier = Modifier.padding(8.dp),
        )

        Spacer(modifier = Modifier.height(50.dp))

        EditCarImage(
            imageUrl = carimageurl,
            vm = vm
        )
        TextField(
            value = carbrand,
            onValueChange = onCarBrandChange,
            label = {
                Text(text = "BRAND")
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            modifier = Modifier.padding(8.dp),
        )

        TextField(
            value = carmodel,
            onValueChange = onCarModelChange,
            label = {
                Text(text = "MODEL")
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            modifier = Modifier.padding(8.dp),
        )

        Button(
            onClick = { onSave.invoke() },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp)
        ) {
            Text(text = "SAVE")
        }

    }
}

@Composable
fun EditUserImage(
    imageUrl: String?,
    vm: GroupNaviViewModel?,
) {
    val launcherUserImageChange = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            vm?.updateUserImageData(uri)
        }
    }

    Box(contentAlignment = Alignment.Center) {
        UserImageCard(
            imageUrl = imageUrl,
            modifier = Modifier.size(150.dp),
        )
        CircleIconCard(
            imageVector = Icons.Default.Create,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(44.dp)
                .padding(4.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = CircleShape,
                )
                .clickable {
                    launcherUserImageChange.launch("image/*")
                }
        )
        val isLoading = vm?.inProgress?.value
        if (isLoading == true) {
            ProgressSpinner()
        }
    }
}

@Composable
fun EditCarImage(
    imageUrl: String?,
    vm: GroupNaviViewModel?,
) {
    val launcherCarImageChange = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            vm?.updateCarImageData(uri)
        }
    }

    Box(contentAlignment = Alignment.Center) {
        CarImageCard(
            imageUrl = imageUrl,
            modifier = Modifier.size(width = 300.dp, height = 150.dp)
        )
        CircleIconCard(
            imageVector = Icons.Default.Create,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(44.dp)
                .padding(4.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = CircleShape,
                )
                .clickable {
                    launcherCarImageChange.launch("image/*")
                }
        )
        val isLoading = vm?.inProgress?.value
        if (isLoading == true) {
            ProgressSpinner()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Hhc_groupnaviTheme {
        ProfileScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    Hhc_groupnaviTheme {
        EditProfileScreen()
    }
}