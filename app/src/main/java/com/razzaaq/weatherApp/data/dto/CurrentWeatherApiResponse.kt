package com.razzaaq.weatherApp.data.dto


import com.google.gson.annotations.SerializedName

data class CurrentWeatherApiResponse(
    @SerializedName("coord") val coord: Coord?,
    @SerializedName("weather") val weather: List<Weather?>?,
    @SerializedName("base") val base: String?, // stations
    @SerializedName("main") val main: Main?,
    @SerializedName("visibility") val visibility: Int?, // 10000
    @SerializedName("wind") val wind: Wind?,
    @SerializedName("rain") val rain: Rain?,
    @SerializedName("clouds") val clouds: Clouds?,
    @SerializedName("dt") val dt: Int?, // 1661870592
    @SerializedName("sys") val sys: Sys?,
    @SerializedName("timezone") val timezone: Int?, // 7200
    @SerializedName("id") val id: Int?, // 3163858
    @SerializedName("name") val name: String?, // Zocca
    @SerializedName("cod") val cod: Int? // 200
) {
    data class Coord(
        @SerializedName("lon") val lon: Double?, // 10.99
        @SerializedName("lat") val lat: Double? // 44.34
    )

    data class Weather(
        @SerializedName("id") val id: Int?, // 501
        @SerializedName("main") val main: String?, // Rain
        @SerializedName("description") val description: String?, // moderate rain
        @SerializedName("icon") val icon: String? // 10d
    )

    data class Main(
        @SerializedName("temp") val temp: Double?, // 298.48
        @SerializedName("feels_like") val feelsLike: Double?, // 298.74
        @SerializedName("temp_min") val tempMin: Double?, // 297.56
        @SerializedName("temp_max") val tempMax: Double?, // 300.05
        @SerializedName("pressure") val pressure: Int?, // 1015
        @SerializedName("humidity") val humidity: Int?, // 64
        @SerializedName("sea_level") val seaLevel: Int?, // 1015
        @SerializedName("grnd_level") val grndLevel: Int? // 933
    )

    data class Wind(
        @SerializedName("speed") val speed: Double?, // 0.62
        @SerializedName("deg") val deg: Int?, // 349
        @SerializedName("gust") val gust: Double? // 1.18
    )

    data class Rain(
        @SerializedName("1h") val h: Double? // 3.16
    )

    data class Clouds(
        @SerializedName("all") val all: Int? // 100
    )

    data class Sys(
        @SerializedName("type") val type: Int?, // 2
        @SerializedName("id") val id: Int?, // 2075663
        @SerializedName("country") val country: String?, // IT
        @SerializedName("sunrise") val sunrise: Int?, // 1661834187
        @SerializedName("sunset") val sunset: Int? // 1661882248
    )
}