package com.example.ingsw_24_25_dietiestates25.data.repository.offerRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.data.model.request.MessageRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.OfferRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface OfferRepository {
    suspend fun makeOffer(request: OfferRequest): ApiResult<Offer>
    suspend fun addOfferMessage(request: MessageRequest): ApiResult<Unit>
    suspend fun acceptOffer(offerId: String): ApiResult<Unit>
    suspend fun declineOffer(offerId: String): ApiResult<Unit>
    suspend fun getAllOffers(): ApiResult<List<OfferSummary>>
    suspend fun getOffersByUser(userId: String): ApiResult<List<Offer>>

    suspend fun getAgentNameByEmail(email: String): ApiResult<String>
}