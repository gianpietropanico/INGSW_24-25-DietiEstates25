package com.example.ingsw_24_25_dietiestates25.data.repository.impl

import com.example.ingsw_24_25_dietiestates25.data.api.PropertyApi
import com.example.ingsw_24_25_dietiestates25.data.repository.PropertyRepository
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val api: PropertyApi
) : PropertyRepository {



    override suspend fun addProperty(property: Property): Property = api.addProperty(property)
}