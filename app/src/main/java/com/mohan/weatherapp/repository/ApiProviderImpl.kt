package com.mohan.weatherapp.repository

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.mohan.weatherapp.BuildConfig
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

import retrofit2.converter.scalars.ScalarsConverterFactory


class ApiProviderImpl(private val restApiServiceMapper: RestApiServiceMapper) : ApiProvider {

    private val locationApis: Single<WeatherApiService> by lazy {
        getRetrofit()
            .subscribeOn(Schedulers.io())
            .flatMap(restApiServiceMapper)
    }

    override fun getLocationApi(): Single<WeatherApiService> {
        return locationApis
    }

    override fun getRetrofit(): Single<Retrofit> {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Single.just(
            Retrofit.Builder()
                .baseUrl(BuildConfig.DOMAIN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        )
    }
}