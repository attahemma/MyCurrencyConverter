package com.itech.mycurrencyconverter.di

import com.itech.mycurrencyconverter.BuildConfig
import com.itech.mycurrencyconverter.data.CurrencyApi
import com.itech.mycurrencyconverter.main.DMainRepository
import com.itech.mycurrencyconverter.main.MainRepository
import com.itech.mycurrencyconverter.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private val Any.java: Class<CurrencyApi>?
    get() {
        return CurrencyApi::class. java
    }

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi() : CurrencyApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CurrencyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi): MainRepository = DMainRepository(api)

    @Singleton
    @Provides
    fun dispatcherProviders(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }

}