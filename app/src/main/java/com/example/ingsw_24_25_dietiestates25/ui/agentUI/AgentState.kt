package com.example.ingsw_24_25_dietiestates25.ui.agentUI

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User


data class AgentState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val resultMessage: String? = null,
    val localError: Boolean = false,
    val agents : List<User> = emptyList()
)