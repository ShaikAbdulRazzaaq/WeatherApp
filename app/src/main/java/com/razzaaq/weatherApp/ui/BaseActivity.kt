package com.razzaaq.weatherApp.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.razzaaq.weatherApp.WeatherApp

abstract class BaseActivity<viewBinding : ViewBinding> : AppCompatActivity() {

    private var _binding: viewBinding? = null
    val binding get() = _binding!!

    abstract fun getViewBinding(): viewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
    }

    override fun attachBaseContext(base: Context?) {
        base?.let { WeatherApp.localeManager.setLocale(it) }
        super.attachBaseContext(base)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}