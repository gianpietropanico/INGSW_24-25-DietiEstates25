package com.example.ingsw_24_25_dietiestates25.di

import com.example.ingsw_24_25_dietiestates25.data.repository.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.PropertyRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.impl.AuthRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.repository.impl.PropertyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        repoImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindPropertyRepository(
        impl: PropertyRepositoryImpl
    ): PropertyRepository

}