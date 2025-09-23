package com.example.ingsw_24_25_dietiestates25.data.model.result

sealed class ApiResult<T>(val data: T? = null, val message: String? = null) {

    class Authorized<T>(data: T? = null) : ApiResult<T>(data)

    class Unauthorized<T>(message: String? = "User not authorized") : ApiResult<T>(message = message)

    class UnknownError<T>(message: String? = "An unknown error occurred") : ApiResult<T>(message = message)

    class Success<T>(data: T, message: String? = "ok") : ApiResult<T>(data = data, message = message)
}

