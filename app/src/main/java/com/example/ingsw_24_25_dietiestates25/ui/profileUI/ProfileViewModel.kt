package com.example.ingsw_24_25_dietiestates25.ui.profileUI

import androidx.lifecycle.ViewModel
import com.example.ingsw_24_25_dietiestates25.data.repository.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (
    private val authRepository: AuthRepository,
    val userSessionManager: UserSessionManager
) : ViewModel() {

}