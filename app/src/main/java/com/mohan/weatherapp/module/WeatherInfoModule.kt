package com.mohan.weatherapp.module

import com.mohan.weatherapp.utils.NetworkHelper
import com.mohan.weatherapp.repository.ApiProvider
import com.mohan.weatherapp.repository.ApiProviderImpl
import com.mohan.weatherapp.repository.RestApiServiceMapper
import com.mohan.weatherapp.data.manager.WeatherDataManager
import com.mohan.weatherapp.viewmodel.WeatherInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherInfoModule = module {
    single { RestApiServiceMapper() }
    single<ApiProvider> { ApiProviderImpl(get()) }
    single { WeatherDataManager(apiProvider = get()) }
    single { NetworkHelper(androidContext()) }

    viewModel {
        WeatherInfoViewModel(get())
    }
}