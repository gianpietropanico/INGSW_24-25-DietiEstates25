package com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo.AdminRepo
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo.ProfileRepo
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.model.state.AuthState
import com.example.ingsw_24_25_dietiestates25.ui.utils.drawableToBase64
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SysAdminViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val adminRepo : AdminRepo,
    private val imageRepo : ImageRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SysAdminState())
    val state: StateFlow<SysAdminState> = _state.asStateFlow()
    var user = userSessionManager.currentUser


    fun loadAgencies() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            var result: ApiResult<List<Agency>> = adminRepo.getAllAgencies()

            if (result is ApiResult.Success) {
                _state.update {
                    it.copy(
                        agencies = result.data ?: emptyList()
                    )
                }
                // passo un Success<Unit> a handleResult per riusare la logica esistente
                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result = result)
            }
        }
    }

    fun loadSuppAdmins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            var result: ApiResult<List<User>> = adminRepo.getAllSuppAdmins()

            if (result is ApiResult.Success) {
                _state.update {
                    it.copy(
                        suppAdmins = result.data ?: emptyList()
                    )
                }

                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result = result)
            }
        }
    }



    fun acceptRequest(adminEmail: String, agencyEmail: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val result = adminRepo.decideRequest(adminEmail, agencyEmail, "accepted")
            handleResult(result)

            // dopo l'accettazione aggiorno la lista
            loadAgencies()
        }

    }

    fun rejectRequest(adminEmail: String, agencyEmail: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val result = adminRepo.decideRequest(adminEmail, agencyEmail, "rejected")
            handleResult(result)

            // dopo il rifiuto aggiorno la lista
            loadAgencies()
        }
    }

    fun addSuppAdmin(recipientEmail : String , username : String, pictureBase64 : String ){

        clearResultMessage()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val result = adminRepo.addSuppAdmin(user.value!!.email, user.value!!.id ,recipientEmail, username)

            if (result is ApiResult.Success<*>) {

                imageRepo.insertProfilePicture("$username@system.com",pictureBase64)

            }

            handleResult(result)
        }
    }

    private fun handleResult(result: ApiResult<*>) {
        when (result) {
            is ApiResult.Authorized -> {
                _state.update{ it.copy(isLoading = false,success = true, resultMessage = null,localError = false)}
            }

            is ApiResult.Unauthorized -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message, success = false, localError = true) }

            }

            is ApiResult.UnknownError -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message,success = false, localError = true) }
            }

            is ApiResult.Success -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message, success = true) }
            }
        }
    }


    fun getAdminRole( ): String{
        return user.value?.role!!.name
    }

    fun clearResultMessage() {
        _state.update { it.copy(resultMessage = null, success = false, localError = false) }
    }

}