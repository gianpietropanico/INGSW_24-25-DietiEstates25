package com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo


import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.request.PropertySearchRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface PropertyListingRepository {

    suspend fun addPropertyListing(propertyListing: PropertyListing): ApiResult<String>
    suspend fun getPropertiesListingByAgent(agentEmail: String): ApiResult<List<PropertyListing>>
    suspend fun getAllListings(): ApiResult<List<PropertyListing>>
    suspend fun getListingsWithinRadius(lat: Double, lon: Double, radius: Double): ApiResult<List<PropertyListing>>
    suspend fun searchProperties(type: String, location: String): ApiResult<List<PropertyListing>>

    suspend fun getListingById( id:String ): ApiResult<PropertyListing>
    suspend fun searchWithFilters(request: PropertySearchRequest): Any

}

