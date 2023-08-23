package com.razzaaq.weatherApp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razzaaq.weatherApp.data.dto.GeoCodingApiResponseDTO
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
class GeoCodingViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    private var _geoCodingLiveData: MutableLiveData<NetworkResult<GeoCodingApiResponseDTO>> =
        MutableLiveData(NetworkResult.Loading())
    val geoCodingLiveData get() = _geoCodingLiveData

    fun getGeoCodingResponse(cityName: String, appID: String) = viewModelScope.launch {
        val response = repository.getGeoCodingResponse(cityName = cityName, appID = appID)
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

}