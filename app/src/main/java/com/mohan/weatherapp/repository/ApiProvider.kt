package com.mohan.weatherapp.repository

import io.reactivex.Single
import retrofit2.Retrofit

interface ApiProvider {
    fun getLocationApi(): Single<WeatherApiService>
    fun getRetrofit(): Single<Retrofit>
}