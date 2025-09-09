package com.example.ingsw_24_25_dietiestates25.ui.profileUI

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalClickableField
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import com.example.ingsw_24_25_dietiestates25.ui.utils.uriToBase64


@Composable
fun ProfileDetailsScreen(
    navController: NavController,
    pm: ProfileViewModel
) {
    val user by pm.user.collectAsState()
    val context = LocalContext.current

    val fullName by remember { mutableStateOf((user?.name ?: "" )+ (user?.surname ?: "") ) }
    val username by remember { mutableStateOf(user?.username ?: "") }
    val state by pm.authState.collectAsState()

    val openGallery = rememberImagePicker { uri ->
        uri?.let {
            val base64 = uriToBase64(context, it)
            if (base64 != null) {
                pm.updateProfilePicture(base64)
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp)
                        .clickable { navController.popBackStack() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = bluPerchEcipiace,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                pm.clearResultMessage()
                                navController.popBackStack()
                            }
                    )
                }
                Spacer(modifier = Modifier.height(60.dp))

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (user?.profilePicture != null) {
                        Image(
                            bitmap = bse64ToImageBitmap(user!!.profilePicture!!),
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
                            .size(24.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            .padding(4.dp)
                            .clickable {
                                pm.clearResultMessage()
                                openGallery()
                            }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                if (state.resultMessage != null) {

                    Text(
                        text = state.resultMessage ?: "",
                        color = if (state.success) Color.Green else DarkRed,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )

                }

                Spacer(modifier = Modifier.height(32.dp))
                MinimalClickableField(
                    value = username,
                    label = "Username",
                    placeholder = user?.username ?: "Inserisci Il tuo username",
                    modifier = Modifier.width(320.dp),
                    onEditIconClick = {
                        pm.clearResultMessage()
                        navController.navigate(NavigationItem.ProfileEditDetails.route)
                        pm.setEditDetails(
                            label = "Username",
                            value = username,
                            description = "The Username in a real estate app is used to identify a user in a simple and recognizable way," +
                                    " without displaying sensitive information like their full name."
                        )
                    }
                )


                Spacer(modifier = Modifier.height(24.dp))

                MinimalClickableField(
                    value = fullName,
                    label = "Full Name",
                    placeholder = if (pm.checkNullUserInfo()) "Inserisci Il tuo nome completo " else fullName,
                    modifier = Modifier.width(320.dp),
                    onEditIconClick = {
                        pm.clearResultMessage()
                        navController.navigate(NavigationItem.ProfileEditDetails.route)
                        pm.setEditDetails(
                            label = "Full Name",
                            value = fullName,
                            description = "Full Name is the user's complete name (first and last). It's used for identification in profiles, listings, and interactions within the Dieti Estates platform."
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun rememberImagePicker(onImageSelected: (Uri?) -> Unit): () -> Unit {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri) // Passa l'URI reimagined selezionata alla UI
    }

    return { imagePickerLauncher.launch("image/*") } // Lancia il selettore della galleria
}


