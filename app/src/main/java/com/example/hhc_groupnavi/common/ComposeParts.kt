package com.example.hhc_groupnavi.common

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.hhc_groupnavi.R
import com.example.hhc_groupnavi.system.GroupNaviViewModel
import com.example.hhc_groupnavi.ui.theme.Hhc_groupnaviTheme


@OptIn(ExperimentalCoilApi::class)
@Composable
fun CommonImage(
    data: String?,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
        .wrapContentSize()
        .fillMaxSize(),
) {
    val painter = rememberImagePainter(data = data)

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier,
    )
    if (painter.state is ImagePainter.State.Loading) {
        ProgressSpinner()
    }
}

@Composable
fun GroupImageCard(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        if (imageUrl.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.temp_groupicon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            CommonImage(data = imageUrl)
        }
    }
}

@Composable
fun UserImageCard(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier,
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(3.dp)
                .border(
                    width = 2.dp,
                    color = Color.LightGray,
                    shape = CircleShape,
                )
        ) {
            if (imageUrl.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.temp_profilephoto),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                CommonImage(data = imageUrl)
            }
        }
    }
}

@Composable
fun CarImageCard(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
    ) {
        if (imageUrl.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.temp_caricon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            CommonImage(data = imageUrl)
        }
    }
}

@Composable
fun CarImageCover(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RectangleShape,
        modifier = modifier,
    ) {
        if (imageUrl.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.temp_caricon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Gray),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        } else {
            CommonImage(
                data = imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
    }
}

@Composable
fun CircleIconCard(
    imageVector: ImageVector,
    colors: CardColors = CardDefaults.cardColors(containerColor = Color.Transparent),
    modifier: Modifier,
) {
    Card(
        shape = CircleShape,
        colors = colors,
        modifier = modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
            )
        }
    }
}

@Composable
fun TitleWithBackArrow(
    title: String,
    onBack: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp)
        )
        CircleIconCard(
            imageVector = Icons.Default.ArrowBack,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(44.dp)
                .padding(4.dp)
                .clickable { onBack.invoke() }
        )
    }
}

@Composable
fun SearchBar(
    searchTerm: String,
    onSearchChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    TextField(
        label = { Text(text = "Search by ID") },
        value = searchTerm,
        onValueChange = onSearchChange,
        shape = CircleShape,
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    onSearch()
                    focusManager.clearFocus()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    )
}

@Composable
fun NotificationMessage(vm: GroupNaviViewModel) {
    val notificationState = vm.popupNotification.value
    val notificationMessage = notificationState?.getContentOrNull()
    if (notificationMessage != null) {
        Toast.makeText(
            LocalContext.current,
            notificationMessage,
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
fun ProgressSpinner() {
    Row(
        modifier = Modifier
            .alpha(0.3f)
            .background(Color.LightGray)
            .clickable(enabled = false) { }
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator()
    }
}

