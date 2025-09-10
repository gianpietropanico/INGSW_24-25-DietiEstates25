package com.example.ingsw_24_25_dietiestates25.di

import com.example.ingsw_24_25_dietiestates25.data.api.authApi.AuthApi
import com.example.ingsw_24_25_dietiestates25.data.api.imageApi.ImageApi
import com.example.ingsw_24_25_dietiestates25.data.api.propertyListingApi.PropertyListingApi
import com.example.ingsw_24_25_dietiestates25.data.api.authApi.AuthApiImpl
import com.example.ingsw_24_25_dietiestates25.data.api.imageApi.ImageApiImpl
import com.example.ingsw_24_25_dietiestates25.data.api.propertyListingApi.PropertyListingApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.BODY
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
            }
        }

    @Provides
    @Singleton
    fun provideAuthApi(httpClient: HttpClient): AuthApi =
        AuthApiImpl(httpClient)

    @Provides
    @Singleton
    fun providePropertyApi(httpClient: HttpClient): PropertyListingApi =
        PropertyListingApiImpl(httpClient)

    @Provides
    @Singleton
    fun provideImageApi(httpClient: HttpClient): ImageApi =
        ImageApiImpl(httpClient)
}