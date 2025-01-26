package com.example.ingsw_24_25_dietiestates25


import android.app.Activity
import android.app.LocaleConfig
import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker

import com.facebook.login.LoginManager
import android.util.Log
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.Composable

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable

import androidx.compose.ui.graphics.Color

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException

import com.facebook.login.LoginResult
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.ui.unit.sp
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import com.facebook.GraphRequest
import com.facebook.Profile


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }
}

