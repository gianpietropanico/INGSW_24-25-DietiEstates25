package com.example.ingsw_24_25_dietiestates25.di

import com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo.*
import com.example.ingsw_24_25_dietiestates25.data.repository.authRepo.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.authRepo.AuthRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo.ProfileRepo
import com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo.ProfileRepoImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAdminRepository(
        adminImpl: AdminRepoImp
    ): AdminRepo

    @Binds
    abstract fun bindAuthRepository(
        authImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindPropertyRepository(
        propImpl: PropertyListingRepositoryImpl
    ): PropertyListingRepository

    @Binds
    abstract fun bindImageRepository(
        imageImpl: ImageRepositoryImpl
    ): ImageRepository

    @Binds
    abstract fun bindProfileRepository(
        profileImpl: ProfileRepoImp
    ): ProfileRepo


}