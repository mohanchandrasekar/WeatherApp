package com.mohan.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohan.weatherapp.data.Forecast
import com.mohan.weatherapp.data.WeatherInfo
import com.mohan.weatherapp.manager.WeatherDataManager
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import kotlin.math.roundToInt

class WeatherInfoViewModel(private val weatherDataManager: WeatherDataManager) : ViewModel() {

    private var compositeDisposable = CompositeDisposable()
    private val _weatherInfo = MutableLiveData<Forecast>()
    val weatherInfoLiveData: LiveData<Forecast> = _weatherInfo

    fun locationSearch(query: String): Disposable {
        return getWeatherInfo(query)
            .doOnSuccess {
               toMapForecast(it).also {
                   _weatherInfo.postValue(it)
               }
            }
            .subscribeBy(onError = ::logError)
            .addTo(compositeDisposable)
    }

    private fun toMapForecast(weatherInfo: WeatherInfo): Forecast {
        return Forecast(
            weatherInfo.the_temp.roundToInt(),
            weatherInfo.min_temp.roundToInt(),
            weatherInfo.max_temp.roundToInt(),
            weatherInfo.weatherUrl,
            weatherInfo.weather_state_name,
            weatherInfo.humidity,
            weatherInfo.wind_speed.roundToInt(),
        )
    }

    private fun getWeatherInfo(query: String): Single<WeatherInfo> {
        return weatherDataManager.getLocationData(query)

    }

    private fun logError(error: Throwable) = Timber.e(error)

    fun destroy() {
        compositeDisposable.clear()
    }
}