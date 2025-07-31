package com.example.ingsw_24_25_dietiestates25.di.property

import com.example.ingsw_24_25_dietiestates25.data.api.PropertyApi
import com.example.ingsw_24_25_dietiestates25.data.api.impl.PropertyApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import io.ktor.client.HttpClient
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PropertyNetworkModule {

    @Provides
    @Singleton
    fun providePropertyApi(httpClient: HttpClient): PropertyApi =
        PropertyApiImpl(httpClient)
}