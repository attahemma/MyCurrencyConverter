package com.itech.mycurrencyconverter.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itech.mycurrencyconverter.data.models.Rates
import com.itech.mycurrencyconverter.util.DispatcherProvider
import com.itech.mycurrencyconverter.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Math.round


class MainViewModel @ViewModelInject constructor(
     private val repository: MainRepository,
     private val dispatchers: DispatcherProvider
): ViewModel() {
    sealed class CurrencyEvent{
        class Success(val resultText: String) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val _conversion: StateFlow<CurrencyEvent> = conversion

    fun convert(
        amount: String,
        fromCurr: String,
        toCurr: String
    ){
        //var fromAmount = amount.toFloatorNull();
        val fromAmount = amount.toFloatOrNull()

        if (fromAmount == null){
            conversion.value = CurrencyEvent.Failure("must be a valid amount")
            return
        }
        viewModelScope.launch(dispatchers.io){
            conversion.value = CurrencyEvent.Loading
            when(val rateResponse = repository.getRates("cacbb86e4611fef1d72700f36a398d3e")){
                is Resource.Error -> conversion.value = CurrencyEvent.Failure(rateResponse.message!!)
                is Resource.Success -> {
                    val rates = rateResponse.data!!.rates
                    val rate = getRateForCurrency(toCurr, rates)
                    if (rate == null){
                        conversion.value = CurrencyEvent.Failure("an error occured")
                    }else{
                        val convertedCurr = round( fromAmount * rate * 100)/100
                        conversion.value = CurrencyEvent.Success(
                                "$fromAmount $fromCurr = $convertedCurr $toCurr"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "ALL" -> rates.ALL
        "DZD" -> rates.ALL
        "AOA" -> rates.ALL
        "ARS" -> rates.ALL
        "AMD" -> rates.ALL
        "AUD" -> rates.ALL
        "EUR" -> rates.ALL
        "AZN" -> rates.ALL
        "BHD" -> rates.ALL
        "BBD" -> rates.ALL
        "BYN" -> rates.ALL
        "BMD" -> rates.ALL
        "BOB" -> rates.ALL
        "BAM" -> rates.ALL
        "CVE" -> rates.ALL
        "KHR" -> rates.ALL
        "XAF" -> rates.ALL
        "CAD" -> rates.ALL
        "USD" -> rates.ALL
        "GBP" -> rates.ALL
        "KES" -> rates.ALL
        "MYR" -> rates.ALL
        "NGN" -> rates.ALL
        "VND" -> rates.ALL
        "JPY" -> rates.ALL
        "INR" -> rates.ALL
        else -> null
    }
}