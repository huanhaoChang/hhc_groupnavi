package com.example.hhc_groupnavi.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hhc_groupnavi.R
import com.example.hhc_groupnavi.common.CheckSignedIn
import com.example.hhc_groupnavi.common.ProgressSpinner
import com.example.hhc_groupnavi.common.navigateTo
import com.example.hhc_groupnavi.system.DestinationScreen
import com.example.hhc_groupnavi.system.GroupNaviViewModel
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme

@Composable
fun LoginScreen(
    navController: NavController? = null,
    vm: GroupNaviViewModel? = null
) {
    CheckSignedIn(
        navController = navController,
        vm = vm
    )

    val isLoading = vm?.inProgress?.value
    val focus = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
        ) {
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .padding(16.dp)
                    .width(150.dp)
            )
            Text(
                text = "LOG IN",
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = {
                    Text(text = "Email")
                },
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = {
                    Text(text = "Password")
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = {
                    focus.clearFocus(force = true)
                    vm?.onLogIn(
                        email = emailState.value.text,
                        password = passwordState.value.text
                    )
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "LOG IN")
            }

            Text(
                text = "New here? Go to Signup ->",
                color = Color.DarkGray,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navigateTo(
                            navController = navController,
                            dest = DestinationScreen.Signup
                        )
                    }
            )
        }

        // PROGRESS SPANNER
        if (isLoading == true) {
            ProgressSpinner()
        }

    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Hhc_groupnaviTheme {
        LoginScreen()
    }
}