package com.razzaaq.weatherApp.data.remote.repo

import com.razzaaq.weatherApp.data.remote.ApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getGeoCodingResponse(
        cityName: String, appID: String
    ) = apiService.getGeoCodingApiResponse(cityName, appID)

    suspend fun getCurrentWeatherResponse(
        lat: Double, lon: Double, appID: String
    ) = apiService.getCurrentWeather(latitude = lat, longitude = lon, appID)

    suspend fun getForecastWeatherResponse(lat: Double, lon: Double, appID: String) =
        apiService.getForeCastData(lat, lon, appID)
}