package com.example.ingsw_24_25_dietiestates25.data.repository.propertyRepo

import com.example.ingsw_24_25_dietiestates25.data.api.propertyApi.PropertyApi
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val api: PropertyApi
) : PropertyRepository {

    override suspend fun addProperty(property: Property): Property = api.addProperty(property)
}