package com.example.ingsw_24_25_dietiestates25.data.repository

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property

interface PropertyRepository {

    suspend fun addProperty(property: Property): Property
}