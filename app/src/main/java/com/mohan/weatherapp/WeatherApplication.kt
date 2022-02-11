package com.mohan.weatherapp

import android.app.Application
import com.mohan.weatherapp.module.weatherInfoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.d("Starting Application ....")
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@WeatherApplication)
            modules(listOf(weatherInfoModule))
        }
    }
}