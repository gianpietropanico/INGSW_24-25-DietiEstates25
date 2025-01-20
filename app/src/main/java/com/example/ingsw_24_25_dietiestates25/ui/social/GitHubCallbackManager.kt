package com.example.ingsw_24_25_dietiestates25.ui.social

import android.content.Intent
import android.util.Log

object GitHubCallbackManager {
    private var onCodeReceived: ((String?, String?) -> Unit)? = null

    fun registerCallback(callback: (String?, String?) -> Unit) {
        Log.d("GitHubCallbackManager", "Callback registered.")
        onCodeReceived = callback
    }

    fun handleIntent(intent: Intent?) {
        Log.d("GitHubCallbackManager", "Handling intent.")

        val uri = intent?.data
        if (uri != null) {
            Log.d("GitHubCallbackManager", "URI received: $uri")

            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")

            if (!code.isNullOrBlank() && !state.isNullOrBlank()) {
                Log.d("GitHubCallbackManager", "Code: $code, State: $state")

                if (onCodeReceived == null) {
                    Log.e("GitHubCallbackManager", "No callback registered.")
                } else {
                    onCodeReceived?.invoke(code, state)
                }
            } else {
                Log.e("GitHubCallbackManager", "Missing code or state in URI.")
                onCodeReceived?.invoke(null, null)
            }
        } else {
            Log.e("GitHubCallbackManager", "No URI data in intent.")
            onCodeReceived?.invoke(null, null)
        }
    }

}








