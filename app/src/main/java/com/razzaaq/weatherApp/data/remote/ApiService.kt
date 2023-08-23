package com.razzaaq.weatherApp.data.remote

import com.razzaaq.weatherApp.data.dto.CurrentWeatherApiResponseDTO
import com.razzaaq.weatherApp.data.dto.GeoCodingApiResponseDTO
import com.razzaaq.weatherApp.data.dto.GetForecastApiResponseDTO
import com.razzaaq.weatherApp.data.remote.helper.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("geo/1.0/direct")
    suspend fun getGeoCodingApiResponse(
        @Query("q") cityName: String, @Query("appid") appID: String
    ): NetworkResult<GeoCodingApiResponseDTO>

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appID: String
    ): NetworkResult<CurrentWeatherApiResponseDTO>

    @GET("data/2.5/forecast")
    suspend fun getForeCastData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appID: String
    ): NetworkResult<GetForecastApiResponseDTO>
}