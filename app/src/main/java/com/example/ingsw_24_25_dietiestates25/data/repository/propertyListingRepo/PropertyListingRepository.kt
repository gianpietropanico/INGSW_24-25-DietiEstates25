package com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo


import com.example.ingsw_24_25_dietiestates25.model.dataclass.PropertyListing

interface PropertyListingRepository {

    suspend fun addPropertyListing(propertyListing: PropertyListing): Boolean
    suspend fun getPropertiesListingByAgent(agentEmail: String): List<PropertyListing>
    suspend fun getAllListings(): List<PropertyListing>
    suspend fun getListingsWithinRadius(lat: Double, lon: Double, radius: Double): List<PropertyListing>
}