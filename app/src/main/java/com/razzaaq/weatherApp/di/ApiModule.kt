package com.razzaaq.weatherApp.di

import com.google.gson.GsonBuilder
import com.razzaaq.weatherApp.data.remote.ApiService
import com.razzaaq.weatherApp.data.remote.helper.NetworkResultCallAdapterFactory
import com.razzaaq.weatherApp.data.remote.repo.WeatherRepository
import com.razzaaq.weatherApp.ui.viewModels.WeatherViewModel
import com.razzaaq.weatherApp.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {

    single {
        GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().setVersion(1.0)
            .create()
    }

    single {
        NetworkResultCallAdapterFactory.create()
    }

    single {
        provideLoggingInterceptor()
    }
    single {
        provideHttpClient(get())
    }

    single {
        provideRetrofit(get(), get())
    }

    single {
        provideWeatherApiService(get())
    }

    single { WeatherRepository(get()) }

    viewModel {
        WeatherViewModel(get())
    }
}

fun provideRetrofit(
    networkResultCallAdapterFactory: NetworkResultCallAdapterFactory,
    okHttpClient: OkHttpClient
): Retrofit =
    Retrofit.Builder().baseUrl(Utils.BASE_URL)
        .addCallAdapterFactory(networkResultCallAdapterFactory)
        .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()


fun provideLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

fun provideHttpClient(interceptor: HttpLoggingInterceptor) =
    OkHttpClient.Builder().addInterceptor(interceptor)
        .connectTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()

fun provideWeatherApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)
