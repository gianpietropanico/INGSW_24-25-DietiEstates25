package com.example.ingsw_24_25_dietiestates25.data.repository.offerRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.data.model.request.AppointmentRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.MessageRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.OfferAppointmentRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.OfferRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface OfferRepository {
    suspend fun createOffer(request: OfferRequest): ApiResult<Offer?>
    suspend fun makeOffer(request: MessageRequest): ApiResult<Unit>
    suspend fun acceptOffer(offerId: String): ApiResult<Unit>
    suspend fun declineOffer(offerId: String): ApiResult<Unit>
    suspend fun getOffersSummary(propertyId: String): ApiResult<List<OfferSummary>>
    suspend fun getOffersByUser(userId: String): ApiResult<List<Offer>>
    suspend fun getAgentNameByEmail(email: String): ApiResult<String>
    suspend fun loadOfferChat(offerId : String): ApiResult<Offer>
    suspend fun getOffer(propertyId : String, userId : String ): ApiResult<Offer?>
    suspend fun createAppointmentOffer(request : OfferAppointmentRequest): ApiResult<Offer>

}