package com.itech.mycurrencyconverter

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
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
        setupActionBar()
        progressDialog = ProgressDialog(this);
        setCurrencyToSpinner1()


        bindings.convertBtn.setOnClickListener{

            viewModel.convert(
                bindings.edFirstAmount.text.toString(),
                fromCurr,
                toCurr
            )
        }
        setCurrencyToSpinner2()

        lifecycleScope.launchWhenStarted {
            viewModel._conversion.collect{ event ->
                when(event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        hideLoading()
                        Log.d("API SUCCESS ==>", event.resultText)
                        bindings.edSecondAmount.setText(event.resultText)
                       // bindings.edCurreny.text = fromCurr
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        hideLoading()
                        Log.e("API ERROR ==>", event.errorText)
                        //Toast().makeText(this,event.errorText,Toast.LENGTH_SHORT).show()
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

    private fun setupActionBar() {
        val actionBar: ActionBar? = this.supportActionBar
        actionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setCustomView(R.layout.app_bar)
        actionBar?.elevation = 1.8f
        actionBar?.setBackgroundDrawable(getDrawable(R.drawable.app_bar_bg))
    }

    private fun setCurrencyToSpinner1() {

        //toCurr = bindings.currency2.editableText.toString()
        val currencies = resources.getStringArray(R.array.currencies)
        val adapter =   ArrayAdapter(this,R.layout.spinner_item, currencies)
        bindings.currency1.setAdapter(adapter)
        bindings.currency1.setOnItemClickListener { adapterView, view, l, k ->
            fromCurr =  bindings.currency1.text.toString()
            bindings.edCurreny.text = fromCurr
            Log.d("Spinner 1", fromCurr)
        }
    }

    private fun setCurrencyToSpinner2() {
        //toCurr = bindings.currency1.editableText.toString()

        val currencies2 = resources.getStringArray(R.array.currencies_two)
        val adapter2 =   ArrayAdapter(this,R.layout.spinner_item2, currencies2)
        bindings.currency2.setAdapter(adapter2)
        bindings.currency2.setOnItemClickListener { adapterView, view, i, l ->
            toCurr = bindings.currency2.text.toString()
            bindings.edCurreny2.text = toCurr
            Log.d("Spinner 2", toCurr)
        }

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