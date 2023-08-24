package com.razzaaq.weatherApp.utils

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private const val TAG = "Preferences"

    /**
     * @param context - pass context
     * @return SharedPreferences
     */
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }

    /**
     * @param context - context
     * @param key     - Constant key, will be used for accessing the stored value
     * @param val     - String value to be stored
     */
    fun setPreference(context: Context, key: String?, `val`: String?) {
        val settings = getSharedPreferences(context)
        val editor = settings.edit()
        editor.putString(key, `val`)
        editor.apply()
    }

    /**
     * Get preference value by passing related key
     *
     * @param context - context
     * @param key     - key value used when adding preference
     * @return - String value
     */
    fun getPreference(context: Context, key: String?, defValue: String?): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(key, defValue)
    }
}