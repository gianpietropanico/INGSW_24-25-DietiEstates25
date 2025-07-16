package com.example.ingsw_24_25_dietiestates25.ui.authUI.social

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class GitHubCallbackActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink(intent)
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
        finish()
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.let { uri ->
            val code  = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            GitHubCallbackManager.invokeCallback(code, state)
        }
    }
}




