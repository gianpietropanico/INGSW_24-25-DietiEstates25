package com.example.ingsw_24_25_dietiestates25.ui.authUI.social



object GitHubCallbackManager {
    private var callback: ((String?, String?) -> Unit)? = null

    fun register(cb: (code: String?, state: String?) -> Unit) {
        callback = cb
    }

    fun invokeCallback(code: String?, state: String?) {
        callback?.invoke(code, state)
        callback = null
    }
}









