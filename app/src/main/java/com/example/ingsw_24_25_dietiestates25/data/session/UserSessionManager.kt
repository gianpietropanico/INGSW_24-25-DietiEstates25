package com.example.ingsw_24_25_dietiestates25.data.session

import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UserSessionManager @Inject constructor() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    private val _currentUsername = MutableStateFlow<String?>(null)
    val currentUsername: StateFlow<String?> = _currentUsername.asStateFlow()

    private val _currentAgency = MutableStateFlow<Agency?>(null)
    val currentAgency: StateFlow<Agency?> = _currentAgency.asStateFlow()


    fun saveUser(user: User, token: String) {
        _currentUser.value = user
        _token.value = token
    }

    fun saveAgencyName ( name : String ){
        val current = _currentAgency.value
        if (current != null) {
            val updateAgency = current.copy(name = name)
            _currentAgency.value = updateAgency
        }
    }

    fun saveUsernameSession(username: String?, token: String) {
        _currentUsername.value = username
        _token.value = token
    }

    fun saveAgency(agency : Agency){
        _currentAgency.value = agency
    }

    fun saveToken(token: String) {
        _token.value = token
    }

    fun clear() {
        _currentUser.value = null
        _token.value = null
        _currentUsername.value = null
        _currentAgency.value = null
    }

    fun getToken(): String? {
        return _token.value
    }

    fun saveProfilePic(profilePicture: String) {
        val current = _currentUser.value
        if (current != null) {
            val updatedUser = current.copy(profilePicture = profilePicture)
            _currentUser.value = updatedUser
        }
    }

    fun saveAgencyProfilePic(profilePicture: String) {
        val current = _currentAgency.value
        if (current != null) {
            val updateAgency = current.copy(profilePic = profilePicture)
            _currentAgency.value = updateAgency
        }
    }
}