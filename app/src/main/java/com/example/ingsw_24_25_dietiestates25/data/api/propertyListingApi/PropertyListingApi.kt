package com.example.ingsw_24_25_dietiestates25.data.api.propertyListingApi


import com.example.ingsw_24_25_dietiestates25.model.dataclass.PropertyListing

interface PropertyListingApi {
    suspend fun addPropertyListing(propertyListing: PropertyListing)
    suspend fun getPropertiesListingByAgent(agentEmail: String): List<PropertyListing>
    suspend fun getAllListings(): List<PropertyListing>
    suspend fun getListingsWithinRadius(lat: Double, lon: Double, radius: Double): List<PropertyListing>
}