package com.example.minishop.di

import android.content.Context
import com.example.minishop.data.local.database.MySqliteOpenHelper
import com.example.minishop.data.local.datasource.FavoriteProductsDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providesMySqliteOpenHelper(@ApplicationContext context: Context) : MySqliteOpenHelper {
        return MySqliteOpenHelper(context)
    }

    @Provides
    @Singleton
    fun providesFavoriteProductsDatasource( mySqliteOpenHelper: MySqliteOpenHelper ): FavoriteProductsDatasource {
        return FavoriteProductsDatasource(mySqliteOpenHelper)
    }
}