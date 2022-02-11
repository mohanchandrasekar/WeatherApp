package com.mohan.weatherapp.utils

import com.mohan.weatherapp.data.WeatherInfo

object MockData {
    fun mockWeatherInfo(): WeatherInfo {
        return WeatherInfo(
            id = 6201949192978432,
            weather_state_name = "Showers",
            weather_state_abbr = "s",
            wind_direction_compass = "SSW",
            created = "2022-02-11",
            applicable_date = "2022-02-12",
            min_temp = 2.45,
            max_temp = 4.324999999999999,
            the_temp = 3.75,
            wind_speed = 11.88229141811819,
            wind_direction = 213.49999999999997,
            air_pressure = 1020.0,
            humidity = 87,
            visibility = 9.999726596675416,
            predictability = 73, weatherUrl = "https://www.metaweather.com/static/img/weather/png/s.png")
    }
}