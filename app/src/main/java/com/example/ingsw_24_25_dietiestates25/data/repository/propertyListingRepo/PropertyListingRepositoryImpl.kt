package com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo

import com.example.ingsw_24_25_dietiestates25.data.api.propertyListingApi.PropertyListingApi
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.model.dataclass.PropertyListing
import javax.inject.Inject

class PropertyListingRepositoryImpl @Inject constructor(
    private val api: PropertyListingApi
) : PropertyListingRepository {

    override suspend fun addPropertyListing(propertyListing: PropertyListing): Boolean {

        return try {
            api.addPropertyListing(propertyListing)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }
    override suspend fun getPropertiesListingByAgent(agentEmail: String): List<PropertyListing> {
        return try {
            api.getPropertiesListingByAgent(agentEmail)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAllListings(): List<PropertyListing> {
        return try {
            api.getAllListings()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getListingsWithinRadius(
        lat: Double,
        lon: Double,
        radius: Double
    ): List<PropertyListing> {
        return try {
            api.getListingsWithinRadius(lat, lon, radius)
        } catch (e: Exception) {
            emptyList()
        }
    }
}