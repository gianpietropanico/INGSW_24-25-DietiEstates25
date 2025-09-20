package com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.ingsw_24_25_dietiestates25.ui.utils.GenericListItem
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.SearchableList

@Composable
fun SysAdminSupportsScreen(
    navController: NavController,
    sysAdminVm: SysAdminViewModel
) {

    val errorMsg = false
    val state by sysAdminVm.state.collectAsState()

    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        sysAdminVm.loadSuppAdmins()
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
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Support Admins",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = bluPerchEcipiace,
                    modifier = Modifier.weight(1f), // prende lo spazio rimanente
                    textAlign = TextAlign.Center     // centra dentro allo spazio
                )
            }


            Spacer(Modifier.height(82.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Here you can review all incoming agency requests. You have the option to accept them and grant access, or reject them if they don’t meet the requirements.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
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
                    items = state.suppAdmins,
                    query = query,
                    onQueryChange = { query = it },
                    extraFilter = { _ -> true},
                    searchFilter = { supadmins, text ->
                        (supadmins.name ?: "").contains(text, ignoreCase = true) ||
                                (supadmins.email ?: "").contains(text, ignoreCase = true)
                    }
                ) { supadmins ->
                    GenericListItem(
                        false,
                        icon = painterResource(id = R.drawable.supp_admin),
                        title = supadmins.name ?: "N/D",
                        subtitle = supadmins.email,
                        onAccept = {  },
                        onReject = {  }
                    )
                }
            }

        }

        FloatingActionButton(
            onClick = {
                sysAdminVm.clearResultMessage()
                Log.d("NAV", "Click su FAB → ${NavigationItem.SysFormAdminSupp.route}")
                navController.navigate(NavigationItem.SysFormAdminSupp.route)
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