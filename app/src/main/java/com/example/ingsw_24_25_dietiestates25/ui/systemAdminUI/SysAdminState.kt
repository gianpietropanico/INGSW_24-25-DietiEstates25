package com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User

data class SysAdminState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val resultMessage: String? = null,
    val localError: Boolean = false,
    val suppAdmins: List<User> = emptyList(),
    val agencies: List<Agency> = emptyList()

)
