package com.mohan.weatherapp.data

import com.mohan.weatherapp.R


object CountryInfo  {
    private val countryList: List<Country> = listOf(
        Country("Gothenburg", R.drawable.pisa),
        Country("Stockholm", R.drawable.paris),
        Country("Mountain View", R.drawable.washington),
        Country("London", R.drawable.london),
        Country("New York", R.drawable.new_york),
        Country("Berlin", R.drawable.rome)
    )

    fun countryInfo(): List<Country>{
        return countryList
    }
}