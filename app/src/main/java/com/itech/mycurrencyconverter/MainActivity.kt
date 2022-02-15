package com.itech.mycurrencyconverter

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.itech.mycurrencyconverter.databinding.ActivityMainBinding
import com.itech.mycurrencyconverter.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bindings: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog

    private var fromCurr: String = ""
    private var toCurr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        progressDialog = ProgressDialog(this);
        setCurrencyToSpinner1()
        setCurrencyToSpinner2()

        bindings.convertBtn.setOnClickListener{
            bindings.edCurreny.text = fromCurr
            bindings.edCurreny.text = fromCurr
            viewModel.convert(
                bindings.edFirstAmount.text.toString(),
                fromCurr,
                toCurr
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel._conversion.collect{ event ->
                when(event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        hideLoading()
                        bindings.edSecondAmount.setText(event.resultText)
                       // bindings.edCurreny.text = fromCurr
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        hideLoading()
                        //bindings.edCurreny.text = fromCurr

                    }
                    is MainViewModel.CurrencyEvent.Loading -> {
                        showLoading()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setCurrencyToSpinner1() {

        //toCurr = bindings.currency2.editableText.toString()
        val adapter =   ArrayAdapter(this,R.layout.spinner_item, mutableListOf(R.array.currencies))
        bindings.currency1.setAdapter(adapter)
        bindings.currency1.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("Converter", bindings.currency1.text.get(i).toString())}
        fromCurr = bindings.currency1.editableText.toString()
    }

    private fun setCurrencyToSpinner2() {
        //toCurr = bindings.currency1.editableText.toString()

        val adapter =   ArrayAdapter(this,R.layout.spinner_item, mutableListOf(R.array.currencies))
        bindings.currency2.setAdapter(adapter)
        bindings.currency2.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("Converter", bindings.currency1.text.get(i).toString())}
        toCurr = bindings.currency2.editableText.toString()
    }

    fun showLoading(){
        progressDialog.show()
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.getWindow()!!.setBackgroundDrawableResource(
            android.R.color.transparent
        );
    }

    fun hideLoading(){
        progressDialog.dismiss()
    }
}