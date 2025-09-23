package com.example.ingsw_24_25_dietiestates25.ui.agentUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.rememberImagePicker
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.GenericListItem
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.SearchableList
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import com.example.ingsw_24_25_dietiestates25.ui.utils.uriToBase64

@Composable
fun AgencySettingsScreen(
    agentVm: AgentViewModel,
    navController: NavController
){
    val agency by  agentVm.agency.collectAsState()
    val errorMsg = false
    val state by agentVm.state.collectAsState()
    val context = LocalContext.current

    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        agentVm.loadAgents()
    }

    val openGallery = rememberImagePicker { uri ->
        uri?.let {
            val base64 = uriToBase64(context, it)
            if (base64 != null) {
                agentVm.updateProfilePicture(base64)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.rectangleblu),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .offset(y = (-40).dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            agentVm.clearResultMessage()
                            navController.popBackStack()
                        }
                )
            }

            Spacer(Modifier.height(60.dp))

            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                if (agency?.profilePic != null) {
                    Image(
                        bitmap = bse64ToImageBitmap(agency!!.profilePic!!),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }else{
                    Image(
                        painter = painterResource(id = R.drawable.defaultprofilepic),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change photo",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-6).dp, y = (-6).dp)
                        .size(24.dp)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 8.dp)
                        )
                        .padding(2.dp)
                        .clickable {
                            agentVm.clearResultMessage()
                            openGallery()
                        }
                )
            }

            Spacer(Modifier.height(32.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = agency!!.name,
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )


                if (errorMsg) {

                    Text(
                        text = "ERROR",
                        color = if (errorMsg) Color.Green else DarkRed,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )

                }

                Spacer(Modifier.height(46.dp))

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SearchableList(
                    modifier = Modifier.fillMaxWidth(),
                    items = state.agents,
                    query = query,
                    onQueryChange = { query = it },
                    extraFilter = { _ -> true},
                    searchFilter = { agents, text ->
                        (agents.name ?: "").contains(text, ignoreCase = true) ||
                                (agents.email ?: "").contains(text, ignoreCase = true)
                    }
                ) { agents ->
                    GenericListItem(
                        false,
                        icon = painterResource(id = R.drawable.supp_admin),
                        title = agents.name ?: "N/D",
                        subtitle = agents.email,
                        onAccept = {  },
                        onReject = {  }
                    )
                }
            }

        }

        FloatingActionButton(
            onClick = {
                agentVm.clearResultMessage()
                navController.navigate(NavigationItem.FormUser.route)
            },
            containerColor = Color(0xFF0097A7),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(60.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    clip = false
                )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(32.dp)
            )
        }
    }
    LoadingOverlay(isVisible = state.isLoading)
}