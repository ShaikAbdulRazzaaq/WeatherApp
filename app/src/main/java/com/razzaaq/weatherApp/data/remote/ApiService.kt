package com.razzaaq.weatherApp.data.remote

import com.razzaaq.weatherApp.data.dto.CurrentWeatherApiResponse
import com.razzaaq.weatherApp.data.dto.GeoCodingApiResponse
import com.razzaaq.weatherApp.data.remote.helper.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("geo/1.0/direct")
    suspend fun getGeoCodingApiResponse(
        @Query("q") cityName: String, @Query("appid") appID: String
    ): NetworkResult<GeoCodingApiResponse>

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appID: String
    ): NetworkResult<CurrentWeatherApiResponse>

    @GET("data/2.5/forecast")
    fun getForeCastData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appID: String
    )


}