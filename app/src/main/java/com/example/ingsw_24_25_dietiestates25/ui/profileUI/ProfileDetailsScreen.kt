package com.example.ingsw_24_25_dietiestates25.ui.profileUI

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
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
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import com.example.ingsw_24_25_dietiestates25.ui.utils.uriToBase64


@Composable
fun ProfileDetailsScreen(
    navController: NavController,
    pm: ProfileViewModel
) {
    val user by pm.user.collectAsState()
    val context = LocalContext.current

    val fullName by remember { mutableStateOf((user?.name ?: "" ) +  " " + (user?.surname ?: "") ) }
    val username by remember { mutableStateOf(user?.username ?: "") }
    val state by pm.state.collectAsState()

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
                            pm.clearResultMessage()
                            navController.popBackStack()
                        }
                )
            }

            Spacer(Modifier.height(60.dp))

            Box(
                modifier = Modifier.size(150.dp),
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
                            pm.clearResultMessage()
                            openGallery()
                        }
                )
            }

            Spacer(Modifier.height(64.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                //.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {

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

                Spacer(Modifier.height(46.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp) // margine esterno
                        .background(Color(0xFFECF2F6), RoundedCornerShape(12.dp))
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .clickable {
                            pm.clearResultMessage()
                            navController.navigate(NavigationItem.ProfileEditDetails.route)
                            pm.setEditDetails(
                                label = "Username",
                                value = username,
                                description = "The Username in a real estate app is used to identify a user in a simple and recognizable way," +
                                        " without displaying sensitive information like their full name."
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Username", style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp, fontWeight = FontWeight.Medium
                    ))
                    Text(user!!.username, style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Gray
                    ))
                }

                Spacer(Modifier.height(46.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp) // margine esterno
                        .background(Color(0xFFECF2F6), RoundedCornerShape(12.dp))
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .clickable {
                            pm.clearResultMessage()
                            navController.navigate(NavigationItem.ProfileEditDetails.route)
                            pm.setEditDetails(
                                label = "Name And Surname",
                                value = fullName,
                                description = "You can set your personal name and surname. It's used for identification in profiles, listings, and interactions within the Dieti Estates platform."
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Name and Surname", style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp, fontWeight = FontWeight.Medium
                    ))
                    Text(if (fullName.isEmpty()) "N/D" else fullName, style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Gray
                    ))
                }
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


//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    name = "Profile MockUp Preview"
//)
//@Composable
//fun ProfileMockUpPreview() {
//    MaterialTheme {
//        ProfileMockUp()
//    }
//}