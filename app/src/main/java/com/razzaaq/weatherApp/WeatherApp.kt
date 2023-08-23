package com.razzaaq.weatherApp

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.razzaaq.weatherApp.utils.LocaleManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp : Application() {
    companion object {
        lateinit var localeManager: LocaleManager
    }

    override fun attachBaseContext(base: Context?) {
        localeManager = LocaleManager(base)
        super.attachBaseContext(base?.let {
            localeManager.setLocale(
                it
            )
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager.setLocale(this)
    }

}