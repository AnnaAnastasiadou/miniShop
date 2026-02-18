package com.example.minishop.di

import com.example.minishop.core.AuthTokenProvider
import com.example.minishop.data.remote.AuthInterceptor
import com.example.minishop.data.remote.authorization.AuthApi
import com.example.minishop.data.remote.cart.CartApi
import com.example.minishop.data.remote.category.CategoryApi
import com.example.minishop.data.remote.products.ProductsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val BASE_URL = "https://fakestoreapi.com/"

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideAuthInterceptor(session: AuthTokenProvider): AuthInterceptor = AuthInterceptor(session)

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit) : AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideCartApi(retrofit: Retrofit) : CartApi = retrofit.create(CartApi::class.java)

    @Provides
    @Singleton
    fun provideProductsApi(retrofit: Retrofit) : ProductsApi = retrofit.create(ProductsApi::class.java)

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit: Retrofit) : CategoryApi = retrofit.create(CategoryApi::class.java)
}