package com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed


@Composable
fun SysAdminHomeScreen(
    navController: NavController,
    sysAdminVm: SysAdminViewModel
) {
    val user = sysAdminVm.user
    val isAdmin = sysAdminVm.getAdminRole() == "SUPER_ADMIN"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {



        // Contenuto centrale
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(modifier = Modifier.height(4.dp))

            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Icona personalizzata",
                tint = Color.Unspecified,
                modifier = Modifier.size(210.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Welcome back " + (user.value?.username ?: "N/D"),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                )
                Text(
                    text = if (isAdmin)
                        "Here you can create support admins to help you manage the platform. You can also review, accept, and monitor agency requests to ensure they meet the required standards"
                    else
                        "Here you can review, accept, and monitor agency requests to ensure they meet the required standards",
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(21.dp))
            }

            Spacer(modifier = Modifier.height(28.dp))

            if (isAdmin) {
                Button(
                    onClick = { navController.navigate(NavigationItem.SysAdminSupp.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEFF9FA),
                        contentColor = Color(0xFF004C77)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFF3A7CA5)),
                    modifier = Modifier
                        .height(48.dp)
                        .width(350.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            clip = true,
                            ambientColor = Color.Black,
                            spotColor = Color.Black
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.supp_admin),
                        contentDescription = null,
                        tint = Color(0xFF3A7CA5),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "View support Admins",
                        color = Color(0xFF3A7CA5),
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            Button(
                onClick = { navController.navigate(NavigationItem.SysAdminAgency.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFF9FA),
                    contentColor = Color(0xFF004C77)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF3A7CA5)),
                modifier = Modifier
                    .width(350.dp)
                    .height(48.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        clip = true,
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.agency),
                    contentDescription = null,
                    tint = Color(0xFF3A7CA5),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "View Agencies",
                    color = Color(0xFF3A7CA5),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { navController.navigate(NavigationItem.Profile.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFF9FA),
                    contentColor = Color(0xFF004C77)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF3A7CA5)),
                modifier = Modifier
                    .width(350.dp)
                    .height(48.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        clip = true,
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF3A7CA5),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "View your Profile",
                    color = Color(0xFF3A7CA5),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp)
                )
            }
        }
    }
}