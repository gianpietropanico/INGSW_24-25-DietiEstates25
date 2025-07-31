package com.example.ingsw_24_25_dietiestates25.ui.profileUI

import androidx.lifecycle.ViewModel
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeProfileVM : ViewModel() {
    private val _user = MutableStateFlow<User?>(
        User(
            name = "Adolfo",
            surname = null,
            username = "torcyGOAL",
            id = "261255",
            email = "torcyGOAL@sasso.com",
            type = "localuser"
        )
    )
    val user: StateFlow<User?> = _user.asStateFlow()

    fun checkUserInfo(): Boolean {
        return user.value != null && (user.value?.name == null || user.value?.surname == null)
    }

    fun getUser(): User?{
        return user.value
    }

}