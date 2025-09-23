package com.example.ingsw_24_25_dietiestates25.ui.agentUI

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo.AdminRepo
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.ui.utils.uriToBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.agentRepo.AgentRepo
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AgentViewModel  @Inject constructor (
    private val userSessionManager: UserSessionManager,
    private val imageRepo : ImageRepository,
    private val agentRepo : AgentRepo
): ViewModel() {

    private val _state = MutableStateFlow(AgentState())
    val state: StateFlow<AgentState> = _state.asStateFlow()

    var user = userSessionManager.currentUser
    val agency = userSessionManager.currentAgency


    fun getAdminRole(): String {
        return user.value?.role!!.name
    }

    fun clearResultMessage() {
        _state.update { it.copy(resultMessage = null, success = false, localError = false) }
    }

    fun loadAgents() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            var result: ApiResult<List<User>> = agentRepo.getAllAgent(agency.value!!.agencyEmail)

            if (result is ApiResult.Success) {
                _state.update {
                    it.copy(
                        agents = result.data ?: emptyList()
                    )
                }

                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result = result)
            }
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

    fun updateProfilePicture(base64: String) {
        clearResultMessage()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val result = imageRepo.insertProfilePicture(agency.value!!.id, base64, "agency")

            handleResult(result)

        }
    }

    fun updateAgencyName(name : String) {
        clearResultMessage()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }
            val result = agentRepo.updateAgencyName(
                agencyName = name,
                agencyEmail = agency.value!!.agencyEmail
            )
            handleResult(result)

        }
    }

}