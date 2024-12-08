package com.example.ingsw_24_25_dietiestates25.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthApiImpl
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.network.NetworkClient

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val authRepository = AuthRepositoryImpl(
        api = AuthApiImpl(NetworkClient.httpClient),
        prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    )
    val factory = AuthViewModelFactory(authRepository)
    val viewModel: AuthViewModel = viewModel(factory = factory)

    LoginScreen(viewModel = viewModel)
}

