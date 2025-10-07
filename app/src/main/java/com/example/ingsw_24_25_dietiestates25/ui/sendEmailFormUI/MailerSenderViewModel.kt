package com.example.ingsw_24_25_dietiestates25.ui.sendEmailFormUI

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo.AdminRepo
import com.example.ingsw_24_25_dietiestates25.data.repository.agentRepo.AgentRepo
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MailerSenderViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val agentRepo: AgentRepo ,
    private val imageRepo : ImageRepository
): ViewModel() {

    private val _state = MutableStateFlow(MailSenderState())
    val state: StateFlow<MailSenderState> = _state.asStateFlow()
    var user = userSessionManager.currentUser
    val agency = userSessionManager.currentAgency

    fun addUserBySendingEmail(recipientEmail : String , username : String, pictureBase64 : String ) {

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val finalEmail = if(user.value?.role?.name == "SUPER_ADMIN") "$username@system.com" else "$username@${agency.value?.name}.com"

            val result = agentRepo.addUserBySendingEmail(
                user = user.value!!,
                recipientEmail = recipientEmail,
                userEmail = finalEmail
            )

            Log.d("Sending Email","Tipo result dopo adduserBySendingEmail: ${result::class.simpleName}, valore=$result")

            when (result) {
                is ApiResult.Success -> {

                    val addedUserId = agentRepo.getUserIdByEmail(finalEmail)

                    if (finalEmail.contains("system")) {
                        imageRepo.insertProfilePicture(addedUserId.data.toString(), pictureBase64,"user")
                    } else {

                        agentRepo.addAgent(agencyEmail = agency.value!!.agencyEmail, agentEmail = finalEmail)
                        Log.d("Sending Email","Tipo result dopo adduserBySendingEmail: ${result::class.simpleName}, valore=$result")

                        imageRepo.insertProfilePicture(addedUserId.data.toString(), pictureBase64,"user")
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            success = true,
                            resultMessage = result.message,
                            localError = false
                        )
                    }
                }

                is ApiResult.Unauthorized -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            success = false,
                            resultMessage = result.message,
                            localError = true
                        )
                    }
                }

                is ApiResult.UnknownError -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            success = false,
                            resultMessage = result.message,
                            localError = true
                        )
                    }
                }

                is ApiResult.Authorized -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            success = true,
                            resultMessage = null,
                            localError = false
                        )
                    }
                }
                else -> {}
            }
        }
    }
}