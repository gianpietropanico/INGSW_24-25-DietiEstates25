package com.example.ingsw_24_25_dietiestates25.ui.social

import android.app.Activity
import android.os.Bundle
import android.util.Log
class GitHubCallbackActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("GitHubCallbackActivity", "Callback activity launched.")

        val uri = intent?.data
        if (uri != null) {
            Log.d("GitHubCallbackActivity", "Received URI: $uri")

            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")

            if (!code.isNullOrBlank() && !state.isNullOrBlank()) {
                Log.d("GitHubCallbackActivity", "Code: $code, State: $state")
                GitHubCallbackManager.handleIntent(intent)
            } else {
                Log.e("GitHubCallbackActivity", "Missing code or state in callback URI.")
            }
        } else {
            Log.e("GitHubCallbackActivity", "No URI data in callback intent.")
        }

        finish()
        Log.d("GitHubCallbackActivity", "Callback activity finished.")
    }
}



