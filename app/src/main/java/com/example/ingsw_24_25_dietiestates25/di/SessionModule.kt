package com.example.ingsw_24_25_dietiestates25.di

import android.content.Context
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Provides
    @Singleton
    fun provideUserSessionManager(): UserSessionManager {
        return UserSessionManager()
    }
}