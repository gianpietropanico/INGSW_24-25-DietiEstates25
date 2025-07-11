package com.example.ingsw_24_25_dietiestates25.data.session

import com.example.ingsw_24_25_dietiestates25.model.authenticate.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionManager @Inject constructor() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    fun saveUser(user: User, token: String) {
        _currentUser.value = user
        _token.value = token
    }

    fun clear() {
        _currentUser.value = null
        _token.value = null
    }
}