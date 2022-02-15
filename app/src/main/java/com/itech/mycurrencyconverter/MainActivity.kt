package com.itech.mycurrencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itech.mycurrencyconverter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindings: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindings.root)
    }
}