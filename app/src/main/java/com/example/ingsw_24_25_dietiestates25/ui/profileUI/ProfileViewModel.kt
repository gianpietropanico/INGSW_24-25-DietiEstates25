package com.example.ingsw_24_25_dietiestates25.ui.profileUI

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.UserActivity
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo.ProfileRepo
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.ui.authUI.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (
    private val userSessionManager: UserSessionManager,
    private val imageRepository: ImageRepository,
    private val profileRepo: ProfileRepo
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()


    var user = userSessionManager.currentUser
    var label: String = ""
    var value: String = ""
    var description: String = ""

    fun setEditDetails(label: String, value: String, description: String) {
        this.label = label
        this.value = value
        this.description = description
    }

    fun getName() : String {
        return user.value?.name ?: ""
    }

    fun getSurname() : String{
        return user.value?.surname ?: ""
    }

    fun getUserType ( ) : String{
        return user.value!!.role.name
    }

    fun getUserId(): String{
        return user.value!!.id
    }

    fun checkNullUserInfo(): Boolean{
        return (user.value?.name == null || user.value?.surname == null)
    }

    fun clearResultMessage() {
        _state.update { it.copy(resultMessage = null, success = false, localError = false) }
    }

    fun loadActivities() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            var result: ApiResult<List<UserActivity>> = profileRepo.getActivities(user.value!!.id)

            if (result is ApiResult.Success) {
                _state.update {
                    it.copy(
                        activities = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                _state.update { it.copy(isLoading = false) }
                handleResult(result = result)
            }
        }
    }


    fun updateProfilePicture(base64: String) {
        clearResultMessage()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }
            val result = imageRepository.insertProfilePicture(user.value!!.id, base64, "user")
            handleResult(result)

        }
    }

    fun sendResetPasswordEmail(newPassword :String, oldPassword: String) {
        clearResultMessage()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }
            val result = profileRepo.resetPassword( oldPassword, newPassword)
            handleResult(result)
        }

    }

    fun updateUserInfo(value: String, type: String) {

        clearResultMessage()
        viewModelScope.launch {

            _state.update { it.copy(isLoading = true, resultMessage = null) }
            val result = profileRepo.updateUserInfo(value, type)
            handleResult(result)

        }
    }


    private fun handleResult(result: ApiResult<*>) {
        when (result) {
            is ApiResult.Authorized -> {
                _state.update{ it.copy(isLoading = false,success = true, resultMessage = null,localError = false)}
                user = userSessionManager.currentUser
            }

            is ApiResult.Unauthorized -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message, success = false, localError = true) }

            }

            is ApiResult.UnknownError -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message,success = false, localError = true) }
            }

            is ApiResult.Success -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message, success = true) }
                user = userSessionManager.currentUser
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _state.update { it.copy(resultMessage = null, success = false,isAuthenticated = false, localError = false) }
            profileRepo.logout()
        }

    }


}