package com.razzaaq.weatherApp.utils

object Utils {
    const val BASE_URL = "https://api.openweathermap.org/"
    const val API_KEY = "fdd399e166e30d7c78ff287c74adc9f1"

    const val LANGUAGE = "language"

    fun returnImageUrlFromCode(code: String): String =
        "https://openweathermap.org/img/wn/$code@2x.png"


}