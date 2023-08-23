package com.razzaaq.weatherApp.di


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.razzaaq.weatherApp.Constants
import com.razzaaq.weatherApp.data.remote.ApiService
import com.razzaaq.weatherApp.data.remote.helper.NetworkResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().setVersion(1.0)
            .create()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    @Provides
    @Singleton
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
        .connectTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun provideNetworkResultCallAdapterFactory(): NetworkResultCallAdapterFactory =
        NetworkResultCallAdapterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        client: OkHttpClient,
        networkResultCallAdapterFactory: NetworkResultCallAdapterFactory
    ): Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addCallAdapterFactory(networkResultCallAdapterFactory)
        .addConverterFactory(GsonConverterFactory.create(gson)).client(client).build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}
