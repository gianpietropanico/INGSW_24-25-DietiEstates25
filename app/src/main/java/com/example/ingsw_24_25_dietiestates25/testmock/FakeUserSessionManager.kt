package com.example.ingsw_24_25_dietiestates25.testmock

import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeUserSessionManager : UserSessionManager() {

    // Override dei flow per poterli modificare liberamente nei test
    private val mockUserFlow = MutableStateFlow<User?>(null)
    private val mockTokenFlow = MutableStateFlow<String?>(null)
    private val mockUsernameFlow = MutableStateFlow<String?>(null)


    fun mockSaveUser(user: User?, token: String?) {
        mockUserFlow.value = user
        mockTokenFlow.value = token
    }

    fun mockSaveUsername(username: String?, token: String?) {
        mockUsernameFlow.value = username
        mockTokenFlow.value = token
    }

    fun mockClear() {
        mockUserFlow.value = null
        mockTokenFlow.value = null
        mockUsernameFlow.value = null
    }

    fun mockGetUser(): User? {
        return mockUserFlow.value
    }

}