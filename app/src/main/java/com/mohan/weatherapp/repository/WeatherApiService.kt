package com.mohan.weatherapp.repository

import com.mohan.weatherapp.data.Location
import com.mohan.weatherapp.data.WeatherInfo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {

    @GET("/api/location/search")
    fun searchLocation(@Query("query") txt: String): Single<List<Location>>

    @GET("/api/location/{woeid}/{date}")
    fun getWeatherForDay(
        @Path("woeid") woeid: Int,
        @Path("date") year: String,
    ): Single<List<WeatherInfo>>

}