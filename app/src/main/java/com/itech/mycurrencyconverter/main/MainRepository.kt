package com.itech.mycurrencyconverter.main

import com.itech.mycurrencyconverter.data.models.CurrencyResponse
import com.itech.mycurrencyconverter.util.Resource
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

//@Module
//@InstallIn(ViewModelComponent::class)
interface MainRepository {
    suspend fun getRates(access_key: String): Resource<CurrencyResponse>
}