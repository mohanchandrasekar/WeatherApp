package com.mohan.weatherapp.manager

import com.mohan.weatherapp.BuildConfig
import com.mohan.weatherapp.repository.ApiProvider
import com.mohan.weatherapp.data.Location
import com.mohan.weatherapp.data.WeatherInfo
import com.mohan.weatherapp.utils.MockData
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherDataManager(
    private val backgroundSchedulers: Scheduler = Schedulers.io(),
    private val foregroundSchedulers: Scheduler = AndroidSchedulers.mainThread(),
    private val apiProvider: ApiProvider
) {
    fun getLocationData(query: String): Single<WeatherInfo> {
        return apiProvider.getLocationApi()
            .subscribeOn(backgroundSchedulers)
            .flatMap {
                it.searchLocation(query)
                    .observeOn(foregroundSchedulers)
                    .doOnSuccess {
                        Timber.d("Result of Location Api $it")
                    }
                    .map { locationData -> toMapLocationModel(locationData[0]) }
            }
            .flatMap { location ->
                weatherInfo(location.woeid, getTomorrowDate())
            }
            .doOnSuccess {
                Timber.d("Final result of weather data from the api $it")
            }
            .doOnError {
                Timber.e(" Unable to get the Location data from api %s", it.message)
            }.onErrorReturnItem(MockData.mockWeatherInfo())
    }

    private fun weatherInfo(woeid: Int, tomorrowDate: String): Single<WeatherInfo> {
        return apiProvider.getLocationApi()
            .subscribeOn(backgroundSchedulers)
            .flatMap { api ->
                api.getWeatherForDay(woeid = woeid, tomorrowDate)
                    .observeOn(foregroundSchedulers)
                    .doOnSuccess {
                        Timber.d("Result of WeatherDay Api $it")
                    }
                    .map { locationInfo ->
                        toMapWeatherInfoModelData(locationInfo[0], locationInfo[0].weather_state_abbr)
                    }
            }
            .doOnError {
                Timber.e("Unable to get the LocationInfo from API %s", it.localizedMessage)
            }
    }


    private fun toMapWeatherInfoModelData(
        locationInfo: WeatherInfo,
        abbr: String
    ): WeatherInfo {
        return WeatherInfo(
            locationInfo.id, locationInfo.weather_state_name,
            locationInfo.weather_state_abbr, locationInfo.wind_direction_compass,
            locationInfo.created, locationInfo.applicable_date,
            locationInfo.min_temp, locationInfo.max_temp,
            locationInfo.the_temp, locationInfo.wind_speed,
            locationInfo.wind_direction, locationInfo.air_pressure,
            locationInfo.humidity, locationInfo.visibility,
            locationInfo.predictability, WEATHER_URL+abbr+ IMAGE_EXT
        )

    }

    private fun toMapLocationModel(it: Location): Location {
        return Location(
            it.title,
            it.location_type,
            it.woeid,
            it.latt_long
        )

    }

    private fun getTomorrowDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        val tomorrow: Date = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        val tomor = dateFormat.format(tomorrow)
        return tomor
    }

    companion object{
        private const val WEATHER_URL= BuildConfig.DOMAIN + "static/img/weather/png/"
        private const val IMAGE_EXT = ".png"
    }
}

