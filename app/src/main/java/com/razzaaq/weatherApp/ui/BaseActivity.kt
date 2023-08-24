package com.razzaaq.weatherApp.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
import java.util.Locale

abstract class BaseActivity<viewBinding : ViewBinding> : AppCompatActivity() {

    private var _binding: viewBinding? = null
    val binding get() = _binding!!

    abstract fun getViewBinding(): viewBinding

    private val localeDelegate = LocaleHelperActivityDelegateImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localeDelegate.onCreate(this)
        _binding = getViewBinding()
    }

    override fun getDelegate() = localeDelegate.getAppCompatDelegate(super.getDelegate())

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(localeDelegate.attachBaseContext(newBase))
    }

    override fun onResume() {
        super.onResume()
        localeDelegate.onResumed(this)
    }

    override fun onPause() {
        super.onPause()
        localeDelegate.onPaused()
    }

    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        val context = super.createConfigurationContext(overrideConfiguration)
        return LocaleHelper.onAttach(context)
    }

    override fun getApplicationContext(): Context =
        localeDelegate.getApplicationContext(super.getApplicationContext())

    open fun updateLocale(locale: Locale) {
        localeDelegate.setLocale(this, locale)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_ARAB = "ar"
    }
}