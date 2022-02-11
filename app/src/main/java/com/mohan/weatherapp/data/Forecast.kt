package com.mohan.weatherapp.data

class Forecast(
    val temperature: Int,
    val minTemp: Int,
    val maxTemp: Int,
    val weatherUrl: String,
    val weather_state: String,
    val humidity: Int,
    val wind_speed: Int
)