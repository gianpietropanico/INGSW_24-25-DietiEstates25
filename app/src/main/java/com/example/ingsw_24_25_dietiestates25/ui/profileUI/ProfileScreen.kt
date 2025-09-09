package com.example.ingsw_24_25_dietiestates25.ui.profileUI
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.DietiNavBar
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.Screen
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap


@Composable
fun ProfileScreen(
    navController : NavController,
    pm : ProfileViewModel
) {

    val user by pm.user.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (user != null) {
        Scaffold(
            bottomBar = {
                DietiNavBar(
                    currentRoute = currentRoute ?: Screen.Home.route,
                    onRouteSelected = { newRoute ->
                        navController.navigate(newRoute) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
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

                        Box(
                            modifier = Modifier
                                .size(100.dp)
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
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (pm.checkNullUserInfo()) {
                            Text(
                                text = user!!.username,
                                fontSize = 30.sp,
                                style = MaterialTheme.typography.labelMedium
                            )
                        } else {
                            Text(
                                text = "${user?.name} ${user?.surname}",
                                fontSize = 30.sp,
                                style = MaterialTheme.typography.labelMedium
                            )

                        }

                        Text(user!!.email, fontSize = 14.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(32.dp))

                        // Voci menu
                        ProfileOption(
                            ImageVector.vectorResource(R.drawable.account_circle),
                            "Edit personal details",
                            pm.checkNullUserInfo(),
                            onClick = {
                                pm.clearResultMessage()
                                navController.navigate(NavigationItem.ProfileDetails.route)
                            })
                        ProfileOption(
                            ImageVector.vectorResource(R.drawable.lock),
                            "Security and password",
                            onClick = {
                                pm.clearResultMessage()
                                navController.navigate(NavigationItem.ProfileEditDetails.route)
                                pm.setEditDetails(
                                    label = "Password",
                                    value = "password",
                                    description = "The password in a real estate app is a confidential credential used to protect " +
                                            "the user's account and ensure secure access to personal data and services."
                                )
                            })

                        ProfileOption(Icons.Default.History, "Your activities", onClick = {})
                        ProfileOption(
                            Icons.AutoMirrored.Filled.Logout,
                            "Logout",
                            onClick = {
                                pm.logout()
                                navController.navigate(NavigationItem.Welcome.route) {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                }
            }
        }

    }else{
        LoadingOverlay(isVisible = true)
    }
}

@Composable
fun ProfileOption(
    icon: ImageVector,
    title: String,
    showNotification: Boolean = false,
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
            if (showNotification) {
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


