package com.example.ingsw_24_25_dietiestates25.di

import com.example.ingsw_24_25_dietiestates25.data.repository.authRepo.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyRepo.PropertyRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.authRepo.AuthRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyRepo.PropertyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        authImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindPropertyRepository(
        propImpl: PropertyRepositoryImpl
    ): PropertyRepository

    @Binds
    abstract fun bindImageRepository(
        imageImpl: ImageRepositoryImpl
    ): ImageRepository

}