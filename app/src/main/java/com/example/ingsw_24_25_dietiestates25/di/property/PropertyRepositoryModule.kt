package com.example.ingsw_24_25_dietiestates25.di.property

import com.example.ingsw_24_25_dietiestates25.data.repository.PropertyRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.impl.PropertyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PropertyRepositoryModule {

    @Binds
    abstract fun bindPropertyRepository(
        impl: PropertyRepositoryImpl
    ): PropertyRepository
}