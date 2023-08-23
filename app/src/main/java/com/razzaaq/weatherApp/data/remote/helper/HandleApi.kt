package com.razzaaq.weatherApp.data.remote.helper

import android.util.Log
import com.google.gson.Gson
import com.razzaaq.weatherApp.data.dto.ErrorBody
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

const val TAG = "HandleApiMethod"

fun <T : Any> handleApi(
    execute: () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        when {
            response.isSuccessful && body != null -> {
                Log.d(TAG, "handleApi: $body")
                NetworkResult.ApiSuccess(body)
            }

            else -> {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.charStream(), ErrorBody::class.java
                )
                val message = errorResponse.message ?: response.message()
                Log.d(TAG, "handleApi: ${response.code()} and $message")
                NetworkResult.ApiError(code = response.code(), message = message)
            }
        }
    } catch (e: Exception) {
        when (e) {
            is UnknownHostException -> NetworkResult.ApiException(Exception("Unknown Host, Please retry or reset Internet Connection"))
            is ConnectException -> NetworkResult.ApiException(Exception("Connection Exception, Please retry or reset Internet Connection"))
            else -> NetworkResult.ApiException(e)
        }
    }
}
