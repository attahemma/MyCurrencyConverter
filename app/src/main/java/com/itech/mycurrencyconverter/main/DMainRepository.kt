package com.itech.mycurrencyconverter.main

import com.itech.mycurrencyconverter.data.CurrencyApi
import com.itech.mycurrencyconverter.data.models.CurrencyResponse
import com.itech.mycurrencyconverter.util.Resource
import javax.inject.Inject

class DMainRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository{
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null ){
                Resource.Success(result)
            } else{
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }
}