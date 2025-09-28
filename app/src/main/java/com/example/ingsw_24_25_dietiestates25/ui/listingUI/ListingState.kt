package com.example.ingsw_24_25_dietiestates25.ui.listingUI

sealed class ListingState {
    object Idle : ListingState()
    object Loading : ListingState()
    object Success : ListingState()
    data class Error(val message: String) : ListingState()


    fun isLoading() = this is Loading
    fun hasError() = this is Error
    fun isSuccess() = this is Success
}
