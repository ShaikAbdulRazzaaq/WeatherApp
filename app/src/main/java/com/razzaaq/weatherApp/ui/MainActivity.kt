package com.razzaaq.weatherApp.ui

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import com.razzaaq.weatherApp.R
import com.razzaaq.weatherApp.Utils
import com.razzaaq.weatherApp.data.remote.helper.onError
import com.razzaaq.weatherApp.data.remote.helper.onException
import com.razzaaq.weatherApp.data.remote.helper.onLoading
import com.razzaaq.weatherApp.data.remote.helper.onSuccess
import com.razzaaq.weatherApp.databinding.ActivityMainBinding
import com.razzaaq.weatherApp.ui.viewModels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        hideWeatherCards()

        binding.etLocation.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (v.text.isNullOrEmpty()) {
                    Toast.makeText(this, "Please Enter Valid Location", Toast.LENGTH_SHORT).show()
                } else {
                    weatherViewModel.getGeoCodingResponse(v.text.toString())
                    binding.progressLoader.isVisible = true
                }
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
                        binding.progressLoader.isVisible = false
                        Log.d(TAG, "onCreate: $it")
                        it.firstOrNull()?.let { item ->
                            if (item.lat != null && item.lon != null) {
                                binding.progressLoader.isVisible = true
                                weatherViewModel.getCurrentWeatherResponse(
                                    item.lat, item.lon, "EN"
                                )
                                weatherViewModel.getWeatherForecastResponse(
                                    item.lat, item.lon, "EN"
                                )
                            }
                        }
                    }
                    onError { code, message ->
                        binding.progressLoader.isVisible = false
                        Toast.makeText(
                            this@MainActivity, "Error $message, code $code", Toast.LENGTH_SHORT
                        ).show()
                    }
                    onException {
                        binding.progressLoader.isVisible = false
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
                        binding.progressLoader.isVisible = false
                        Toast.makeText(
                            this@MainActivity, "Error $message, code $code", Toast.LENGTH_SHORT
                        ).show()
                    }
                    onException {
                        binding.progressLoader.isVisible = false
                        Toast.makeText(
                            this@MainActivity,
                            "Exception ${it.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onSuccess { currentWeatherApiResponseDTO ->
                        binding.progressLoader.isVisible = false
                        binding.currentCard.isVisible = true
                        binding.tvDescription.text =
                            currentWeatherApiResponseDTO.weather?.firstOrNull()?.description
                        binding.tvHumidity.text = getString(
                            R.string.humidity, currentWeatherApiResponseDTO.main?.humidity
                        )
                        binding.tvTime.text = currentWeatherApiResponseDTO.dt?.let {
                            SimpleDateFormat(
                                "dd MMM yyyy HH:mm", Locale.getDefault()
                            ).format(it * 1000)
                        }
                        binding.tvTemperature.text = buildString {
                            append(currentWeatherApiResponseDTO.main?.temp?.toInt())
                            append("°C")
                        }
                        binding.tvWind.text = getString(
                            R.string.wind, currentWeatherApiResponseDTO.wind?.speed
                        )
                        binding.tvPressure.text = getString(
                            R.string.pressure, currentWeatherApiResponseDTO.main?.pressure
                        )
                        binding.tvFeelsLike.text = getString(
                            R.string.feels_like_,
                            currentWeatherApiResponseDTO.main?.feelsLike?.toInt()
                        )

                        binding.tvVisibility.text = getString(
                            R.string.visibility, currentWeatherApiResponseDTO.visibility?.div(1000)
                        )
                        val link = currentWeatherApiResponseDTO.weather?.firstOrNull()?.icon?.let {
                            Utils.returnImageUrlFromCode(
                                it
                            )
                        }
                        link?.let {
                            binding.ivIcon.load(link)
                        }
                        Log.d(
                            TAG,
                            "onCreate:Current Weather Api Response $currentWeatherApiResponseDTO"
                        )
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
                        binding.progressLoader.isVisible = false
                        Toast.makeText(
                            this@MainActivity, "Error $message, code $code", Toast.LENGTH_SHORT
                        ).show()
                    }
                    onException {
                        binding.progressLoader.isVisible = false
                        Toast.makeText(
                            this@MainActivity,
                            "Exception ${it.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onSuccess {
                        binding.progressLoader.isVisible = false
                        binding.rvForecastList.isVisible = true
                        Log.d(TAG, "onCreate:Forecast Weather Api Response $it")
                    }
                }
            }

        }

    }

    private fun hideWeatherCards() {
        binding.currentCard.isVisible = false
        binding.rvForecastList.isVisible = false
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}