package com.example.minishop.di

import com.example.minishop.data.local.datasource.CartDatasource
import com.example.minishop.data.local.datasource.CartDatasourceImpl
import com.example.minishop.data.local.datasource.SharedPreferencesDatasource
import com.example.minishop.data.local.datasource.SharedPreferencesDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindSharedPreferencesDatasource(
        sharedPreferencesDatasourceImpl: SharedPreferencesDatasourceImpl
    ) : SharedPreferencesDatasource

    @Binds
    @Singleton
    abstract fun bindCartDatasource(
        cartDatasourceImpl: CartDatasourceImpl
    ) : CartDatasource
}