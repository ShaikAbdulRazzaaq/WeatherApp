package com.razzaaq.weatherApp.data.remote.helper

sealed class NetworkResult<T : Any> {
    class Loading<T : Any> : NetworkResult<T>()
    class ApiSuccess<T : Any>(val data: T) : NetworkResult<T>()
    class ApiError<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class ApiException<T : Any>(val e: Throwable) : NetworkResult<T>()
}

suspend fun <T : Any> NetworkResult<T>.onLoading(
    executable: suspend () -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Loading<T>) {
        executable()
    }
}

suspend fun <T : Any> NetworkResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.ApiSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> NetworkResult<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.ApiError<T>) {
        executable(code, message)
    }
}

suspend fun <T : Any> NetworkResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.ApiException<T>) {
        executable(e)
    }
}