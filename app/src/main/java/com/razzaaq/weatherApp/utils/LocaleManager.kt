package com.razzaaq.weatherApp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build.VERSION_CODES
import android.preference.PreferenceManager
import com.razzaaq.weatherApp.utils.Utils.isAtLeastVersion
import java.util.Locale

class LocaleManager internal constructor(context: Context?) {
    private val prefs: SharedPreferences

    init {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setLocale(c: Context): Context {
        return updateResources(c, language)
    }

    fun setNewLocale(c: Context, language: String): Context {
        persistLanguage(language)
        return updateResources(c, language)
    }

    val language: String?
        get() = prefs.getString(Utils.LANGUAGE, LANGUAGE_ENGLISH)

    @SuppressLint("ApplySharedPref")
    private fun persistLanguage(language: String) {
        prefs.edit().putString(Utils.LANGUAGE, language).commit()
    }

    private fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        if (isAtLeastVersion(VERSION_CODES.JELLY_BEAN_MR1)) {
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return context
    }

    companion object {
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_ARAB = "ar"
        fun getLocale(res: Resources): Locale {
            val config = res.configuration
            return if (isAtLeastVersion(VERSION_CODES.N)) config.locales[0] else config.locale
        }
    }
}