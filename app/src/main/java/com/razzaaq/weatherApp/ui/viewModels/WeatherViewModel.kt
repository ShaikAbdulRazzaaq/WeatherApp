package com.razzaaq.weatherApp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razzaaq.weatherApp.data.dto.CurrentWeatherApiResponseDTO
import com.razzaaq.weatherApp.data.dto.GeoCodingApiResponseDTO
import com.razzaaq.weatherApp.data.dto.GetForecastApiResponseDTO
import com.razzaaq.weatherApp.data.remote.helper.NetworkResult
import com.razzaaq.weatherApp.data.remote.helper.onError
import com.razzaaq.weatherApp.data.remote.helper.onException
import com.razzaaq.weatherApp.data.remote.helper.onLoading
import com.razzaaq.weatherApp.data.remote.helper.onSuccess
import com.razzaaq.weatherApp.data.remote.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private var _geoCodingLiveData: MutableLiveData<NetworkResult<GeoCodingApiResponseDTO>> =
        MutableLiveData(NetworkResult.Loading())
    val geoCodingLiveData get() = _geoCodingLiveData

    private var _currentWeatherLiveData: MutableLiveData<NetworkResult<CurrentWeatherApiResponseDTO>> =
        MutableLiveData(NetworkResult.Loading())
    val currentWeatherLiveData get() = _currentWeatherLiveData


    private var _weatherForecastLiveData: MutableLiveData<NetworkResult<GetForecastApiResponseDTO>> =
        MutableLiveData(NetworkResult.Loading())
    val weatherForecastLiveData get() = _weatherForecastLiveData

    fun getGeoCodingResponse(cityName: String) = viewModelScope.launch(Dispatchers.IO) {

        val response = repository.getGeoCodingResponse(cityName = cityName)
        response.onSuccess {
            _geoCodingLiveData.postValue(NetworkResult.ApiSuccess(it))
        }
        response.onError { code, message ->
            _geoCodingLiveData.postValue(NetworkResult.ApiError(code, message))
        }
        response.onException { e ->
            _geoCodingLiveData.postValue(NetworkResult.ApiException(e))
        }
        response.onLoading {
            _geoCodingLiveData.postValue(NetworkResult.Loading())
        }
    }

    fun getCurrentWeatherResponse(lat: Double, lon: Double, lang: String) =
        viewModelScope.launch(Dispatchers.IO) {

            val response = repository.getCurrentWeatherResponse(lat, lon, lang)
            response.onSuccess {
                _currentWeatherLiveData.postValue(NetworkResult.ApiSuccess(it))
            }
            response.onError { code, message ->
                _currentWeatherLiveData.postValue(NetworkResult.ApiError(code, message))
            }
            response.onException { e ->
                _currentWeatherLiveData.postValue(NetworkResult.ApiException(e))
            }
            response.onLoading {
                _currentWeatherLiveData.postValue(NetworkResult.Loading())
            }
        }


    fun getWeatherForecastResponse(lat: Double, lon: Double, lang: String) =
        viewModelScope.launch(Dispatchers.IO) {

            val response = repository.getForecastWeatherResponse(lat, lon, lang)
            response.onSuccess {
                _weatherForecastLiveData.postValue(NetworkResult.ApiSuccess(it))
            }
            response.onError { code, message ->
                _weatherForecastLiveData.postValue(NetworkResult.ApiError(code, message))
            }
            response.onException { e ->
                _weatherForecastLiveData.postValue(NetworkResult.ApiException(e))
            }
            response.onLoading {
                _weatherForecastLiveData.postValue(NetworkResult.Loading())
            }
        }


}