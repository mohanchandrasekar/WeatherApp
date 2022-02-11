package com.mohan.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohan.weatherapp.data.Forecast
import com.mohan.weatherapp.data.WeatherInfo
import com.mohan.weatherapp.data.manager.WeatherDataManager
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import kotlin.math.roundToInt

class WeatherInfoViewModel(private val weatherDataManager: WeatherDataManager) : ViewModel() {

    private var compositeDisposable = CompositeDisposable()
    private var forecasts: MutableList<Forecast> = mutableListOf()
    private val _weatherInfo = MutableLiveData<MutableList<Forecast>>()
    val weatherInfoLiveData: LiveData<MutableList<Forecast>> = _weatherInfo

    fun locationSearch(query: String): Disposable {
        return getWeatherInfo(query)
            .doOnSuccess {
                forecasts.add(
                    Forecast(
                        it.the_temp.roundToInt(),
                        it.min_temp.roundToInt(),
                        it.max_temp.roundToInt(),
                        it.weatherUrl,
                        it.weather_state_name,
                        it.humidity,
                        it.wind_speed.roundToInt(),
                    )
                )
                _weatherInfo.postValue(forecasts)
            }
            .subscribeBy(onError = ::logError)
            .addTo(compositeDisposable)
    }

    private fun getWeatherInfo(query: String): Single<WeatherInfo> {
        return weatherDataManager.getLocationData(query)

    }

    private fun logError(error: Throwable) = Timber.e(error)

    fun destroy() {
        compositeDisposable.clear()
    }
}