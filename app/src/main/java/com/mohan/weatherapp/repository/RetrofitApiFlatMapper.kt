package com.mohan.weatherapp.repository

import io.reactivex.Single
import retrofit2.Retrofit
import io.reactivex.functions.Function

abstract class RetrofitApiFlatMapper<T> : Function<Retrofit, Single<T>> {

    private var cache: T? = null
    override fun apply(retrofit: Retrofit): Single<T> {
        return synchronized(this) {
            Single.just(if (cache != null) {
                cache!!
            } else {
                getServiceInstance(retrofit).also {
                    cache = it
                }
            })

        }
    }

    abstract fun getServiceInstance(retrofit: Retrofit): T

}