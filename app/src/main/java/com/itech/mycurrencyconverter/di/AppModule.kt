package com.itech.mycurrencyconverter.di

import com.itech.mycurrencyconverter.BuildConfig
import com.itech.mycurrencyconverter.data.CurrencyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

private val Any.java: Class<CurrencyApi>?
    get() {
        return CurrencyApi::class. java
    }

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi() : CurrencyApi {
        val retrofitInstance = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofitInstance.create(CurrencyApi::class.java)
    }

}