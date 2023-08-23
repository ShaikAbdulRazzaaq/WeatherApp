package com.razzaaq.weatherApp.data.remote.repo

import com.razzaaq.weatherApp.data.remote.ApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getGeoCodingResponse(
        cityName: String
    ) = apiService.getGeoCodingApiResponse(cityName)

    suspend fun getCurrentWeatherResponse(
        lat: Double, lon: Double, lang: String
    ) = apiService.getCurrentWeather(latitude = lat, longitude = lon, language = lang)

    suspend fun getForecastWeatherResponse(lat: Double, lon: Double, lang: String) =
        apiService.getForeCastData(latitude = lat, longitude = lon, language = lang)
}