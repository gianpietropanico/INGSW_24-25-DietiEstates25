package com.example.ingsw_24_25_dietiestates25.model.result

sealed class AuthResult<T>(val data: T? = null, val message: String? = null) {

    class Authorized<T>(data: T? = null) : AuthResult<T>(data)

    class Unauthorized<T>(message: String? = "User not authorized") : AuthResult<T>(message = message)

    class UnknownError<T>(message: String? = "An unknown error occurred") : AuthResult<T>(message = message)

    class Success<T>(data: T, message: String? = "ok") : AuthResult<T>(data = data, message = message)

}