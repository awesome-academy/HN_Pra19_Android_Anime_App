package com.example.anidb

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.anidb.screen.home.HomeFragment
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences?.getString(PREF_KEY_LANGUAGE, EN)?.let {
            setLocale(it)
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .addToBackStack(HomeFragment::javaClass.name)
                .replace(R.id.layoutContainer, HomeFragment())
                .commit()
        }
    }

    fun setLocale(localeName: String) {
        val locale = Locale(localeName)
        val config = Configuration(baseContext.resources.configuration)
        val currentLocale = config.locales.get(0)

        if (currentLocale.language != locale.language) {
            Locale.setDefault(locale)
            config.setLocale(locale)
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
            sharedPreferences?.edit()?.putString(PREF_KEY_LANGUAGE, localeName)?.apply()
            recreate()
        }
    }

    companion object {
        const val PREFERENCE_NAME = "app_prefs"
        const val PREF_KEY_LANGUAGE = "language"
        const val VN = "vi"
        const val EN = "en"
    }
}
