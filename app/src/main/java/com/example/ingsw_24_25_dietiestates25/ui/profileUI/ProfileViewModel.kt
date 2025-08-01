package com.example.ingsw_24_25_dietiestates25.ui.profileUI

import androidx.lifecycle.ViewModel
import com.example.ingsw_24_25_dietiestates25.data.repository.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (
    userSessionManager: UserSessionManager
) : ViewModel() {

    val user = userSessionManager.currentUser

    fun checkNullUserInfo(): Boolean{
        return (user.value?.name == null || user.value?.surname == null)
    }


}