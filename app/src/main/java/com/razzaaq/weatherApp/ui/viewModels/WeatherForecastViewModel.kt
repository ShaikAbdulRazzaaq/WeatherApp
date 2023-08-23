package com.razzaaq.weatherApp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razzaaq.weatherApp.data.dto.GetForecastApiResponseDTO
import com.razzaaq.weatherApp.data.remote.helper.NetworkResult
import com.razzaaq.weatherApp.data.remote.helper.onError
import com.razzaaq.weatherApp.data.remote.helper.onException
import com.razzaaq.weatherApp.data.remote.helper.onLoading
import com.razzaaq.weatherApp.data.remote.helper.onSuccess
import com.razzaaq.weatherApp.data.remote.repo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {
    private var _weatherForecastLiveData: MutableLiveData<NetworkResult<GetForecastApiResponseDTO>> =
        MutableLiveData(NetworkResult.Loading())
    val weatherForecastLiveData get() = _weatherForecastLiveData

    fun getWeatherForecastResponse(lat: Double, lon: Double, appID: String) =
        viewModelScope.launch {
            val response = repository.getForecastWeatherResponse(lat, lon, appID)
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