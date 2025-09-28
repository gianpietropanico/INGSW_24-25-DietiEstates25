package com.example.ingsw_24_25_dietiestates25.di

import com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo.*
import com.example.ingsw_24_25_dietiestates25.data.repository.agentRepo.AgentRepo
import com.example.ingsw_24_25_dietiestates25.data.repository.agentRepo.AgentRepoImp
import com.example.ingsw_24_25_dietiestates25.data.repository.authRepo.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.authRepo.AuthRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.repository.offerRepo.OfferRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.offerRepo.OfferRepositoryImp
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
    abstract fun bindOfferRepository(
        impl: OfferRepositoryImp
    ): OfferRepository

    @Binds
    abstract fun bindAdminRepository(
        impl: AdminRepoImp
    ): AdminRepo

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindPropertyRepository(
        impl: PropertyListingRepositoryImpl
    ): PropertyListingRepository

    @Binds
    abstract fun bindImageRepository(
        impl: ImageRepositoryImpl
    ): ImageRepository

    @Binds
    abstract fun bindProfileRepository(
        impl: ProfileRepoImp
    ): ProfileRepo

    @Binds
    abstract fun bindAgentRepository(
        impl: AgentRepoImp
    ): AgentRepo
}