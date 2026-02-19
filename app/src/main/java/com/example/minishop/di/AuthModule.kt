package com.example.minishop.di

import com.example.minishop.core.AuthTokenProvider
import com.example.minishop.core.AuthTokenProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthTokenProviderModule {
    @Binds
    @Singleton
    abstract fun bindAuthTokenProvider(
        authTokenProviderImpl: AuthTokenProviderImpl
    ): AuthTokenProvider
}