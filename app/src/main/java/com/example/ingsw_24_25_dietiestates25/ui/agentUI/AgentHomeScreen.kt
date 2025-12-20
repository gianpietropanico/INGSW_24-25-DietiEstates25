package com.example.ingsw_24_25_dietiestates25.ui.agentUI

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.utils.DietiNavBar
import com.example.ingsw_24_25_dietiestates25.ui.utils.Screen

@Composable
fun AgentHomeScreen(
    navController : NavController,
    agentVm : AgentViewModel
){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val user = agentVm.user
    val isAdmin = agentVm.getAdminRole() == "AGENT_ADMIN"

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(modifier = Modifier.height(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Custom Icon",
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
                    text = if( isAdmin )
                        "here you can easily manage your property listings and add new support agents to your team.\n" +
                                "Keep everything under control in one place, so you can focus on what really matters: growing your business."
                    else "Here you can create and manage property listings with ease.\n" +
                            "Focus on keeping the catalog updated and helping clients find their perfect home",
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

            if(isAdmin){
                Button(
                    onClick = { navController.navigate(NavigationItem.AgentAgency.route) },
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
                )  {
                    Icon(
                        painter = painterResource(id = R.drawable.agency),
                        contentDescription = null,
                        tint = Color(0xFF3A7CA5),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "View your Agency",
                        color = Color(0xFF3A7CA5),
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            Button(

                onClick = { navController.navigate(NavigationItem.AgentListings.route) },
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
                    painter = painterResource(id = R.drawable.annuncio),
                    contentDescription = null,
                    tint = Color(0xFF3A7CA5),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "View Your Listings",
                    color = Color(0xFF3A7CA5),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp)
                )
            }

        }

        //Funzione per mettere a schermo il caricamento
        //LoadingOverlay(isVisible = state.isLoading)
    }
}