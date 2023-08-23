package com.razzaaq.weatherApp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razzaaq.weatherApp.data.dto.CurrentWeatherApiResponseDTO
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
class CurrentWeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {
    private var _currentWeatherLiveData: MutableLiveData<NetworkResult<CurrentWeatherApiResponseDTO>> =
        MutableLiveData(NetworkResult.Loading())
    val currentWeatherLiveData get() = _currentWeatherLiveData

    fun getCurrentWeatherResponse(lat: Double, lon: Double, appID: String) = viewModelScope.launch {
        val response = repository.getCurrentWeatherResponse(lat, lon, appID)
        response.onSuccess {
            _currentWeatherLiveData.postValue(NetworkResult.ApiSuccess(it))
        }
        response.onError { code, message ->
            _currentWeatherLiveData.value = NetworkResult.ApiError(code, message)
        }
        response.onException { e ->
            _currentWeatherLiveData.value = NetworkResult.ApiException(e)
        }
        response.onLoading {
            _currentWeatherLiveData.value = NetworkResult.Loading()
        }
    }
}