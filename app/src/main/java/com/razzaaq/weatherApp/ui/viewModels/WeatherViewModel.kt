package com.razzaaq.weatherApp.ui.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.razzaaq.weatherApp.R
import com.razzaaq.weatherApp.data.dto.CurrentWeatherApiResponseDTO
import com.razzaaq.weatherApp.data.dto.GeoCodingApiResponseDTO
import com.razzaaq.weatherApp.data.dto.GetForecastApiResponseDTO
import com.razzaaq.weatherApp.data.remote.helper.NetworkResult
import com.razzaaq.weatherApp.data.remote.helper.onError
import com.razzaaq.weatherApp.data.remote.helper.onException
import com.razzaaq.weatherApp.data.remote.helper.onLoading
import com.razzaaq.weatherApp.data.remote.helper.onSuccess
import com.razzaaq.weatherApp.data.remote.repo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository, private val application: Application
) : BaseViewModel(application) {

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
        if (hasInternetConnection()) {
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
        } else _geoCodingLiveData.postValue(
            NetworkResult.ApiError(
                0, application.getString(
                    R.string.internet_connection
                )
            )
        )
    }

    fun getCurrentWeatherResponse(lat: Double, lon: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            if (hasInternetConnection()) {
                val response = repository.getCurrentWeatherResponse(lat, lon)
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
            } else {
                _currentWeatherLiveData.postValue(
                    NetworkResult.ApiError(
                        0, application.getString(
                            R.string.internet_connection
                        )
                    )
                )
            }

        }


    fun getWeatherForecastResponse(lat: Double, lon: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            if (hasInternetConnection()) {
                val response = repository.getForecastWeatherResponse(lat, lon)
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
            } else {
                _weatherForecastLiveData.postValue(
                    NetworkResult.ApiError(
                        0, application.getString(
                            R.string.internet_connection
                        )
                    )
                )
            }
        }


}