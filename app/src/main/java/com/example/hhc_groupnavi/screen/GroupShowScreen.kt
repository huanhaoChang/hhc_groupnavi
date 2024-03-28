package com.example.hhc_groupnavi.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hhc_groupnavi.R
import com.example.hhc_groupnavi.common.BottomNavigationMenu
import com.example.hhc_groupnavi.common.CarImageCard
import com.example.hhc_groupnavi.common.CircleIconCard
import com.example.hhc_groupnavi.common.GroupImageCard
import com.example.hhc_groupnavi.common.ProgressSpinner
import com.example.hhc_groupnavi.common.UserImageCard
import com.example.hhc_groupnavi.common.navigateTo
import com.example.hhc_groupnavi.data.DEFAULT_GROUP_ID
import com.example.hhc_groupnavi.data.DEFAULT_GROUP_NAME
import com.example.hhc_groupnavi.data.DEFAULT_USER_NAME
import com.example.hhc_groupnavi.data.GroupData
import com.example.hhc_groupnavi.data.UserData
import com.example.hhc_groupnavi.system.DestinationScreen
import com.example.hhc_groupnavi.system.GroupNaviViewModel
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme

@Composable
fun GroupScreen(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null,
) {
    val isLoading = vm?.inProgress?.value

    if (isLoading == true) {
        ProgressSpinner()
    } else {
        val currentUserData = vm?.userData?.value

        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "MY GROUP",
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
                if (currentUserData?.groupSystemId.isNullOrEmpty()) {
                    WithoutGroup(
                        navController = navController,
                        vm = vm,
                    )
                } else {
                    WithGroup(
                        navController = navController,
                        vm = vm,
                        groupSystemId = currentUserData?.groupSystemId,
                    )
                }
            }
            BottomNavigationMenu(navController = navController)
        }
    }
}

@Composable
fun WithGroup(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null,
    groupSystemId: String? = null,
) {
    val currentGroupData = vm?.groupData?.value
    val groupid = currentGroupData?.groupId ?: DEFAULT_GROUP_ID
    val groupname = currentGroupData?.groupName ?: DEFAULT_GROUP_NAME
    val groupimageurl = currentGroupData?.groupImageUrl
    val groupmembers = currentGroupData?.groupMembers
    val groupsystemid = currentGroupData?.systemId

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            GroupImageCard(
                imageUrl = groupimageurl,
                modifier = Modifier.size(120.dp)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = groupname,
                    fontSize = 25.sp,
                )
                Text(
                    text = "ID: $groupid",
                    fontSize = 18.sp,
                )
                Row {
                    OutlinedButton(
                        onClick = {
                            navigateTo(
                                navController = navController,
                                dest = DestinationScreen.EditGroup,
                            )
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Edit")
                    }
                    OutlinedButton(
                        onClick = { vm?.leaveGroup(groupsystemid) },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Leave")
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "MEMBERS",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
            CircleIconCard(
                imageVector = Icons.Default.Add,
                modifier = Modifier
                    .size(44.dp)
                    .padding(4.dp)
                    .clickable {
                        navigateTo(
                            navController = navController,
                            dest = DestinationScreen.AddMember,
                        )
                    },
            )
        }
        // GET GROUP MEMBERS DATA
        groupmembers?.forEach { membersystemid ->
            GroupMemberRow(
                membersystemid = membersystemid,
                vm = vm,
            )
        } ?: run {
            for (i in 0..2)
                GroupMemberRow()
        }
    }
}


@Composable
private fun GroupMemberRow(
    membersystemid: String? = null,
    vm: GroupNaviViewModel? = null,
) {
    var memberData by remember { mutableStateOf<UserData?>(null) }
    LaunchedEffect(membersystemid) {
        membersystemid?.let {
            memberData = vm?.getMemberData(it)
        }
    }
    val membername = memberData?.userName ?: DEFAULT_USER_NAME
    val memberimageurl = memberData?.userImageUrl

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserImageCard(
                imageUrl = memberimageurl,
                modifier = Modifier.size(70.dp)
            )
            Text(
                text = membername,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

// WITHOUT GROUP COMPONENTS
@Composable
fun WithoutGroup(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_creategroup),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(16.dp)
                .size(280.dp)
        )
        Text(
            text = "No Group Now",
            fontSize = 25.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(16.dp)
        )
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.CreateGroup,
                )
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(300.dp, 80.dp)
                .padding(16.dp)
        ) {
            Text(text = "CREATE A GROUP")
        }
        Button(
            onClick = {
                navigateTo(
                    navController = navController,
                    dest = DestinationScreen.JoinGroup,
                )
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(300.dp, 80.dp)
                .padding(16.dp)
        ) {
            Text(text = "JOIN A GROUP")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GroupScreenPreview() {
    Hhc_groupnaviTheme {
        GroupScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun WithGroupPreview() {
    Hhc_groupnaviTheme {
        WithGroup()
    }
}

@Preview(showBackground = true)
@Composable
fun WithoutGroupPreview() {
    Hhc_groupnaviTheme {
        WithoutGroup()
    }
}