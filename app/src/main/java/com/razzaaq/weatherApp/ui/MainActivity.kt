package com.razzaaq.weatherApp.ui

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.razzaaq.weatherApp.data.remote.helper.onError
import com.razzaaq.weatherApp.data.remote.helper.onException
import com.razzaaq.weatherApp.data.remote.helper.onLoading
import com.razzaaq.weatherApp.data.remote.helper.onSuccess
import com.razzaaq.weatherApp.databinding.ActivityMainBinding
import com.razzaaq.weatherApp.ui.viewModels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etLocation.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (v.text.isNullOrEmpty()) {
                    Toast.makeText(this, "Please Enter Valid Location", Toast.LENGTH_SHORT).show()
                } else weatherViewModel.getGeoCodingResponse(v.text.toString())
                v.clearFocus()
                return@setOnEditorActionListener true
            }
            v.clearFocus()
            return@setOnEditorActionListener true
        }

        weatherViewModel.geoCodingLiveData.observe(this) {
            lifecycleScope.launch(Dispatchers.Main) {
                it.apply {
                    onLoading {
                        Log.d(TAG, "onCreate: GeoCodingLoading")
                    }
                    onSuccess {
                        Log.d(TAG, "onCreate: $it")
                        it.firstOrNull()?.let { item ->
                            if (item.lat != null && item.lon != null) {
                                weatherViewModel.getCurrentWeatherResponse(
                                    item.lat, item.lon
                                )
                                weatherViewModel.getWeatherForecastResponse(
                                    item.lat, item.lon
                                )
                            }
                        }
                    }
                    onError { code, message ->
                        Toast.makeText(
                            this@MainActivity, "Error $message, code $code", Toast.LENGTH_SHORT
                        ).show()
                    }
                    onException {
                        Toast.makeText(
                            this@MainActivity,
                            "Exception ${it.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
        weatherViewModel.currentWeatherLiveData.observe(this) { currentWeatherResponse ->
            currentWeatherResponse.apply {
                lifecycleScope.launch(Dispatchers.Main) {
                    onLoading {
                        Log.d(TAG, "onCreate: loading Current WeatherData")
                    }
                    onError { code, message ->
                        Toast.makeText(
                            this@MainActivity, "Error $message, code $code", Toast.LENGTH_SHORT
                        ).show()
                    }
                    onException {
                        Toast.makeText(
                            this@MainActivity,
                            "Exception ${it.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onSuccess {
                        Log.d(TAG, "onCreate:Current Weather Api Response $it")
                    }
                }
            }
        }
        weatherViewModel.weatherForecastLiveData.observe(this) { forecastResponse ->
            forecastResponse.apply {
                lifecycleScope.launch(Dispatchers.Main) {
                    onLoading {
                        Log.d(TAG, "onCreate: loading Weather forecast")
                    }
                    onError { code, message ->
                        Toast.makeText(
                            this@MainActivity, "Error $message, code $code", Toast.LENGTH_SHORT
                        ).show()
                    }
                    onException {
                        Toast.makeText(
                            this@MainActivity,
                            "Exception ${it.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onSuccess {
                        Log.d(TAG, "onCreate:Forecast Weather Api Response $it")
                    }
                }
            }

        }

    }


    companion object {
        private const val TAG = "MainActivity"
    }
}