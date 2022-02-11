package com.mohan.weatherapp.repository

import retrofit2.Retrofit

class RestApiServiceMapper : RetrofitApiFlatMapper<WeatherApiService>() {
    override fun getServiceInstance(retrofit: Retrofit): WeatherApiService {
       return retrofit.create(WeatherApiService::class.java)
    }
}