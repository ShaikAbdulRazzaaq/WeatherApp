package com.razzaaq.weatherApp

object Utils {
    const val BASE_URL = "https://api.openweathermap.org/"
    const val API_KEY = "fdd399e166e30d7c78ff287c74adc9f1"

    fun returnImageUrlFromCode(code: String): String =
        "https://openweathermap.org/img/wn/$code@2x.png"

    fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) * 5 / 9
    }

}