package com.example.ingsw_24_25_dietiestates25.data.api

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property

interface PropertyApi {
    suspend fun addProperty(property: Property): Property
}