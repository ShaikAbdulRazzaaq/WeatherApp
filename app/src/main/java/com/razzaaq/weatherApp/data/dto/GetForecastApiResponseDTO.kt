package com.razzaaq.weatherApp.data.dto


import com.google.gson.annotations.SerializedName

data class GetForecastApiResponseDTO(
    @SerializedName("cod") val cod: String?, // 200
    @SerializedName("message") val message: Int?, // 0
    @SerializedName("cnt") val cnt: Int?, // 40
    @SerializedName("list") val list: List<Items?>?, @SerializedName("city") val city: City?
) {
    data class Items(
        @SerializedName("dt") val dt: Long?, // 1692802800
        @SerializedName("main") val main: Main?,
        @SerializedName("weather") val weather: List<Weather?>?,
        @SerializedName("clouds") val clouds: Clouds?,
        @SerializedName("wind") val wind: Wind?,
        @SerializedName("visibility") val visibility: Int?, // 10000
        @SerializedName("pop") val pop: Double?, // 0.2
        @SerializedName("sys") val sys: Sys?,
        @SerializedName("dt_txt") val dtTxt: String?, // 2023-08-23 15:00:00
        @SerializedName("rain") val rain: Rain?
    ) {
        data class Main(
            @SerializedName("temp") val temp: Double?, // 301.34
            @SerializedName("feels_like") val feelsLike: Double?, // 302.43
            @SerializedName("temp_min") val tempMin: Double?, // 300.73
            @SerializedName("temp_max") val tempMax: Double?, // 301.34
            @SerializedName("pressure") val pressure: Int?, // 1012
            @SerializedName("sea_level") val seaLevel: Int?, // 1012
            @SerializedName("grnd_level") val grndLevel: Int?, // 910
            @SerializedName("humidity") val humidity: Int?, // 56
            @SerializedName("temp_kf") val tempKf: Double? // 0.61
        )

        data class Weather(
            @SerializedName("id") val id: Int?, // 802
            @SerializedName("main") val main: String?, // Clouds
            @SerializedName("description") val description: String?, // scattered clouds
            @SerializedName("icon") val icon: String? // 03n
        )

        data class Clouds(
            @SerializedName("all") val all: Int? // 40
        )

        data class Wind(
            @SerializedName("speed") val speed: Double?, // 2.63
            @SerializedName("deg") val deg: Int?, // 226
            @SerializedName("gust") val gust: Double? // 4.71
        )

        data class Sys(
            @SerializedName("pod") val pod: String? // n
        )

        data class Rain(
            @SerializedName("3h") val h: Double? // 0.19
        )
    }

    data class City(
        @SerializedName("id") val id: Int?, // 6695236
        @SerializedName("name") val name: String?, // Kanija Bhavan
        @SerializedName("coord") val coord: Coord?,
        @SerializedName("country") val country: String?, // IN
        @SerializedName("population") val population: Int?, // 2000
        @SerializedName("timezone") val timezone: Int?, // 19800
        @SerializedName("sunrise") val sunrise: Long?, // 1692751097
        @SerializedName("sunset") val sunset: Long? // 1692796019
    ) {
        data class Coord(
            @SerializedName("lat") val lat: Double?, // 12.9768
            @SerializedName("lon") val lon: Double? // 77.5901
        )
    }
}