package com.example.ingsw_24_25_dietiestates25.ui.profileUI

import androidx.lifecycle.ViewModel
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
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