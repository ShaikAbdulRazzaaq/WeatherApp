package com.razzaaq.weatherApp.ui

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.razzaaq.weatherApp.R
import com.razzaaq.weatherApp.data.dto.CurrentWeatherApiResponseDTO
import com.razzaaq.weatherApp.data.remote.helper.onError
import com.razzaaq.weatherApp.data.remote.helper.onException
import com.razzaaq.weatherApp.data.remote.helper.onLoading
import com.razzaaq.weatherApp.data.remote.helper.onSuccess
import com.razzaaq.weatherApp.databinding.ActivityMainBinding
import com.razzaaq.weatherApp.ui.adapter.ForeCastWeatherRecycler
import com.razzaaq.weatherApp.ui.viewModels.WeatherViewModel
import com.razzaaq.weatherApp.utils.Preferences
import com.razzaaq.weatherApp.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val weatherViewModel: WeatherViewModel by viewModel()
    private val foreCastAdapter = ForeCastWeatherRecycler()
    private lateinit var currentLanguage: String
    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        currentLanguage =
            Preferences.getPreference(this, Utils.LANGUAGE, LANGUAGE_ENGLISH).toString()
        if (currentLanguage == LANGUAGE_ENGLISH) {
            binding.btnLangEng.isChecked = true
        } else binding.btnLangArabic.isChecked = true

        hideWeatherCards()

        setUpLocaleButtons()

        binding.rvForecastList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            adapter = foreCastAdapter
        }

        binding.etLocation.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (v.text.isNullOrEmpty()) {
                    Toast.makeText(this, "Please Enter Valid Location", Toast.LENGTH_SHORT).show()
                } else {
                    weatherViewModel.getGeoCodingResponse(v.text.toString())
                    binding.progressLoader.isVisible = true
                }
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
                        binding.progressLoader.isVisible = false
                        Log.d(TAG, "onCreate: $it")
                        it.firstOrNull()?.let { item ->
                            if (item.lat != null && item.lon != null) {
                                binding.progressLoader.isVisible = true
                                weatherViewModel.getCurrentWeatherResponse(
                                    item.lat, item.lon, currentLanguage
                                )
                                weatherViewModel.getWeatherForecastResponse(
                                    item.lat, item.lon, currentLanguage
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
                        setViewsFromData(currentWeatherApiResponseDTO)
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
                        foreCastAdapter.differ.submitList(it.list)
                    }
                }
            }

        }

    }

    private fun setUpLocaleButtons() {
        binding.btnLangArabic.setOnClickListener {
            if (currentLanguage == LANGUAGE_ENGLISH) {
                updateLocale(Locale(LANGUAGE_ARAB))
                Preferences.setPreference(this, Utils.LANGUAGE, LANGUAGE_ARAB)
            }
        }
        binding.btnLangEng.setOnClickListener {
            if (currentLanguage == LANGUAGE_ARAB) {
                updateLocale(Locale(LANGUAGE_ENGLISH))
                Preferences.setPreference(this, Utils.LANGUAGE, LANGUAGE_ENGLISH)
            }
        }
    }

    private fun setViewsFromData(currentWeatherApiResponseDTO: CurrentWeatherApiResponseDTO) {
        binding.progressLoader.isVisible = false
        binding.currentCard.isVisible = true
        binding.tvDescription.text =
            currentWeatherApiResponseDTO.weather?.firstOrNull()?.description
        binding.tvHumidity.text = getString(
            R.string.humidity, currentWeatherApiResponseDTO.main?.humidity
        )
        binding.tvTime.text = currentWeatherApiResponseDTO.dt?.let {
            SimpleDateFormat(
                "dd MMM yyyy HH:mm", Locale(currentLanguage)
            ).format(Date(it * 1000))
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
            R.string.feels_like_, currentWeatherApiResponseDTO.main?.feelsLike?.toInt()
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
    }

    private fun hideWeatherCards() {
        binding.currentCard.isVisible = false
        binding.rvForecastList.isVisible = false
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}