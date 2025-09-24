package com.example.ingsw_24_25_dietiestates25.ui.profileUI

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.UserActivity

data class ProfileState(

    val isAuthenticated: Boolean? = false,
    val isLoading: Boolean = false,
    var resultMessage: String? = null,
    var localError: Boolean = false,
    val success: Boolean = false,
    val activities : List<UserActivity> = emptyList()
)
