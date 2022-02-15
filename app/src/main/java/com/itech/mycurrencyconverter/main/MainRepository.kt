package com.itech.mycurrencyconverter.main

import com.itech.mycurrencyconverter.data.models.CurrencyResponse
import com.itech.mycurrencyconverter.util.Resource

interface MainRepository {
    suspend fun getRates(base: String): Resource<CurrencyResponse>
}