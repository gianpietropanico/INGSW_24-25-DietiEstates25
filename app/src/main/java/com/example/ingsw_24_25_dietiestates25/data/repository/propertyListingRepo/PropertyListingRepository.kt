package com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo


import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface PropertyListingRepository {

    suspend fun addPropertyListing(propertyListing: PropertyListing): ApiResult<Unit>
    suspend fun getPropertiesListingByAgent(agentEmail: String): ApiResult<List<PropertyListing>>
    suspend fun getAllListings(): ApiResult<List<PropertyListing>>
    suspend fun getListingsWithinRadius(lat: Double, lon: Double, radius: Double): ApiResult<List<PropertyListing>>
}