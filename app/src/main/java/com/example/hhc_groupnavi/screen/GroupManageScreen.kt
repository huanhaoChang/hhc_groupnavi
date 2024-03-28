package com.example.hhc_groupnavi.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hhc_groupnavi.R
import com.example.hhc_groupnavi.common.BottomNavigationMenu
import com.example.hhc_groupnavi.common.CircleIconCard
import com.example.hhc_groupnavi.common.GroupImageCard
import com.example.hhc_groupnavi.common.ProgressSpinner
import com.example.hhc_groupnavi.common.SearchBar
import com.example.hhc_groupnavi.common.TitleWithBackArrow
import com.example.hhc_groupnavi.common.UserImageCard
import com.example.hhc_groupnavi.common.navigateTo
import com.example.hhc_groupnavi.data.GroupData
import com.example.hhc_groupnavi.data.UserData
import com.example.hhc_groupnavi.system.DestinationScreen
import com.example.hhc_groupnavi.system.GroupNaviViewModel
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme
import kotlinx.coroutines.launch

@Composable
fun CreateGroupScreen(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null
) {
    val currentUserSystemID = vm?.userData?.value?.systemId
    val focus = LocalFocusManager.current

    var groupimageUrlLocal by rememberSaveable {
        mutableStateOf("")
    }
    var groupid by rememberSaveable {
        mutableStateOf("")
    }
    var groupname by rememberSaveable {
        mutableStateOf("")
    }
    val launchNewGroupImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            groupimageUrlLocal = it.toString()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
            ) {
                TitleWithBackArrow(title = "CREATE GROUP") {
                    focus.clearFocus(force = true)
                    navController?.popBackStack()
                }
                Box(contentAlignment = Alignment.Center) {
                    GroupImageCard(
                        imageUrl = groupimageUrlLocal,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .size(220.dp),
                    )
                    CircleIconCard(
                        imageVector = Icons.Default.Create,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(44.dp)
                            .padding(0.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Black,
                                shape = CircleShape,
                            )
                            .clickable {
                                launchNewGroupImage.launch("image/*")
                            }
                    )
                }
                OutlinedTextField(
                    value = groupid,
                    onValueChange = { groupid = it },
                    label = { Text(text = "Group ID") },
                    modifier = Modifier
                        .width(300.dp)
                        .padding(8.dp),
                )
                OutlinedTextField(
                    value = groupname,
                    onValueChange = { groupname = it },
                    label = { Text(text = "Group Name") },
                    modifier = Modifier
                        .width(300.dp)
                        .padding(8.dp),
                )
                Button(
                    onClick = {
                        focus.clearFocus(force = true)
                        val newMemberList = arrayListOf<String>()
                        newMemberList.add(currentUserSystemID ?: "")
                        vm?.createNewGroup(
                            groupid = groupid,
                            groupname = groupname,
                            groupmembers = newMemberList,
                            groupimageUrlLocal = groupimageUrlLocal,
                        )
                        navigateTo(
                            navController = navController,
                            dest = DestinationScreen.Group
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .size(300.dp, 80.dp)
                        .padding(8.dp),
                ) {
                    Text(text = "CREATE")
                }
            }
        }
        BottomNavigationMenu(navController = navController)
    }
}

@Composable
fun EditGroupScreen(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null
) {
    val currentGroup = vm?.groupData?.value
    val focus = LocalFocusManager.current

    var groupid by rememberSaveable {
        mutableStateOf(currentGroup?.groupId ?: "")
    }
    var groupname by rememberSaveable {
        mutableStateOf(currentGroup?.groupName ?: "")
    }
    val groupsystemid = currentGroup?.systemId ?: ""
    val groupimageurl = currentGroup?.groupImageUrl ?: ""

    val launchGroupImageChange = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            vm?.updateGroupImage(
                uri = uri,
                groupsystemid = groupsystemid,
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
            ) {
                TitleWithBackArrow(title = "EDIT GROUP") {
                    focus.clearFocus(force = true)
                    navController?.popBackStack()
                }
                Box(contentAlignment = Alignment.Center) {
                    GroupImageCard(
                        imageUrl = groupimageurl,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .size(220.dp),
                    )
                    CircleIconCard(
                        imageVector = Icons.Default.Create,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(44.dp)
                            .padding(0.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Black,
                                shape = CircleShape,
                            )
                            .clickable {
                                launchGroupImageChange.launch("image/*")
                            }
                    )
                }
                OutlinedTextField(
                    value = groupid,
                    onValueChange = { groupid = it },
                    label = { Text(text = "Group ID") },
                    modifier = Modifier
                        .width(300.dp)
                        .padding(8.dp),
                )
                OutlinedTextField(
                    value = groupname,
                    onValueChange = { groupname = it },
                    label = { Text(text = "Group Name") },
                    modifier = Modifier
                        .width(300.dp)
                        .padding(8.dp),
                )
                Button(
                    onClick = {
                        focus.clearFocus(force = true)
                        vm?.updateGroupProfileData(
                            groupsystemid = groupsystemid,
                            groupid = groupid,
                            groupname = groupname,
                        )
                        navigateTo(
                            navController = navController,
                            dest = DestinationScreen.Group
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .size(300.dp, 80.dp)
                        .padding(8.dp),
                ) {
                    Text(text = "SAVE")
                }
            }
        }
        BottomNavigationMenu(navController = navController)
    }
}

@Composable
fun JoinGroupScreen(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null
) {
    val searchGroupLoading = vm?.searchGroupProgress?.value
    val searchGroup = vm?.searchGroup?.value
    var searchGroupTerm by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
            ) {
                TitleWithBackArrow(title = "JOIN GROUP") {
                    navController?.popBackStack()
                }
                SearchBar(
                    searchTerm = searchGroupTerm,
                    onSearchChange = { searchGroupTerm = it },
                    onSearch = { vm?.searchGroup(searchGroupTerm) }
                )
                if (searchGroupLoading == true) {
                    ProgressSpinner()
                } else if (searchGroup == null) {
                    Text(
                        text = "No Group Found",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp),
                    )
                } else {
                    GroupImageCard(
                        imageUrl = searchGroup.groupImageUrl,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .size(220.dp),
                    )
                    Text(
                        text = searchGroup.groupName ?: "(Group Name Not Found)",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp),
                    )
                    Button(
                        onClick = {
                            vm.joinGroup(searchGroup.systemId)
                            navigateTo(
                                navController = navController,
                                dest = DestinationScreen.Group
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(300.dp, 80.dp)
                            .padding(16.dp),
                    ) {
                        Text(text = "JOIN")
                    }
                }
            }
        }
        BottomNavigationMenu(navController = navController)
    }
}

@Composable
fun AddMemberScreen(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null
) {
    val searchMemberLoading = vm?.searchMemberProgress?.value
    val searchMember = vm?.searchMember?.value
    var searchMemberTerm by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
            ) {
                TitleWithBackArrow(title = "ADD MEMBER") {
                    navController?.popBackStack()
                }
                SearchBar(
                    searchTerm = searchMemberTerm,
                    onSearchChange = { searchMemberTerm = it },
                    onSearch = { vm?.searchMember(searchMemberTerm) }
                )
                if (searchMemberLoading == true) {
                    ProgressSpinner()
                } else if (searchMember == null) {
                    Text(
                        text = "No Member Found",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp),
                    )
                } else {
                    UserImageCard(
                        imageUrl = searchMember.userImageUrl,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .size(200.dp),
                    )
                    Text(
                        text = searchMember.userName ?: "(Name Not Found)",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp),
                    )
                    Button(
                        onClick = {
                            vm.addMember(
                                membersystemid = searchMember.systemId!!,
                                groupsystemid = vm?.groupData?.value?.systemId!!
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(300.dp, 80.dp)
                            .padding(16.dp),
                    ) {
                        Text(text = "ADD")
                    }
                }
                Button(
                    onClick = {
                        navigateTo(
                            navController = navController,
                            dest = DestinationScreen.Group
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .size(300.dp, 80.dp)
                        .padding(16.dp),
                ) {
                    Text(text = "BACK TO GROUP")
                }
            }
        }
        BottomNavigationMenu(navController = navController)
    }
}


// PREVIEWS
@Preview(showBackground = true)
@Composable
fun CreateGroupScreenPreview() {
    Hhc_groupnaviTheme {
        CreateGroupScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun JoinGroupScreenPreview() {
    Hhc_groupnaviTheme {
        JoinGroupScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun EditGroupScreenPreview() {
    Hhc_groupnaviTheme {
        EditGroupScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun AddMemberScreenPreview() {
    Hhc_groupnaviTheme {
        AddMemberScreen()
    }
}