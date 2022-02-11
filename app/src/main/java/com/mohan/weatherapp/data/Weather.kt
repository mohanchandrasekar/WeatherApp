package com.mohan.weatherapp.data

import java.util.*


enum class Weather(private val displayName: String) {
    SNOW("Snow"), CLEAR("Clear"), SLEET("Sleet"), HAIL("Hail"), THUNDERSTORM("Thunderstorm"),
    HEAVY_RAIN("Heavy Rain"), LIGHT_RAIN("Light Rain"), SHOWERS("Showers"), HEAVY_CLOUD("Heavy Cloud"),
    LIGHT_CLOUD("Light Cloud");

    companion object {
        /**
         * @return the Enum representation for the given string.
         * @throws IllegalArgumentException if unknown string.
         */
        @Throws(IllegalArgumentException::class)
        fun fromString(s: String): Weather {
            return Arrays.stream(values())
                .filter { v -> v.displayName == s }
                .findFirst()
                .orElseThrow { IllegalArgumentException("unknown value: $s") }
        }
    }
}