package com.itech.mycurrencyconverter.main


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itech.mycurrencyconverter.data.models.Rates
import com.itech.mycurrencyconverter.util.DispatcherProvider
import com.itech.mycurrencyconverter.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Math.round
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
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
                    Log.d("RATES ==>", rateResponse.data.toString())
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
        "DZD" -> rates.DZD
        "AOA" -> rates.AOA
        "ARS" -> rates.ARS
        "AMD" -> rates.AMD
        "AUD" -> rates.AUD
        "EUR" -> rates.EUR
        "AZN" -> rates.AZN
        "BHD" -> rates.BHD
        "BBD" -> rates.BBD
        "BYN" -> rates.BYN
        "BMD" -> rates.BMD
        "BOB" -> rates.BOB
        "BAM" -> rates.BAM
        "CVE" -> rates.CVE
        "KHR" -> rates.KHR
        "XAF" -> rates.XAF
        "CAD" -> rates.CAD
        "USD" -> rates.USD
        "GBP" -> rates.GBP
        "KES" -> rates.KES
        "MYR" -> rates.MYR
        "NGN" -> rates.NGN
        "VND" -> rates.VND
        "JPY" -> rates.JPY
        "INR" -> rates.INR
        else -> null
    }
}