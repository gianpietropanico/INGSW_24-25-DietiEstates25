package com.example.ingsw_24_25_dietiestates25.ui.components.common

import android.content.Context
import android.util.Log
import android.widget.Toast


fun myToastMessage(context: Context, message :String?){
    if (!message.isNullOrEmpty()) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    } else {
        Log.e("ToastError", "Messaggio del Toast non impostato.")
    }

}