package com.example.ingsw_24_25_dietiestates25.ui.profileUI
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace


@Composable
fun ProfileScreen(
    fpm: FakeProfileVM
) {


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

        IconButton(
            onClick = {  },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier.size(36.dp),
                contentDescription = "Back",
                tint = bluPerchEcipiace
            )
        }
        // Contenuto sovrapposto
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    // Immagine profilo
                    Image(
                        painter = painterResource(id = R.drawable.adolfo),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if(!fpm.checkUserInfo()){
                        Text(
                            text = "${fpm.getUser()?.name} ${fpm.getUser()?.surname}",
                            fontSize = 30.sp,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }else{
                        Text(
                            text = "Update Your Name and Surname",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkRed
                        )

                    }

                    Text(fpm.getUser()?.email.toString(), fontSize = 14.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Voci menu
                    ProfileOption(ImageVector.vectorResource( R.drawable.account_circle), "Edit personal details",!fpm.checkUserInfo(), onClick = {} )
                    ProfileOption(ImageVector.vectorResource( R.drawable.lock), "Security and password", onClick = {})

                    ProfileOption(Icons.Default.History, "Your activities", onClick = {})
                    ProfileOption(Icons.AutoMirrored.Filled.Logout, "Logout", onClick = {})

                    Spacer(modifier = Modifier.height(24.dp))
                }

        }
    }
}


@Composable
fun ProfileOption(
    icon: ImageVector,
    title: String,
    showNotification: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick()}
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(title, fontSize = 16.sp)
            if (!showNotification) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification",
                    tint = bluPerchEcipiace,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}


@Preview(showBackground = true, name = "Sign Up Screen Preview")
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()

    val fakeViewModel = FakeProfileVM(
    )

    ProfileScreen(
        fakeViewModel
    )
}

