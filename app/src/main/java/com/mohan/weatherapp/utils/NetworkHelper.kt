package com.mohan.weatherapp.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkHelper(context: Context) {

    private var connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    fun hasInternet(onResult: (Boolean) -> Unit = {}) {
        try {
            if (hasNetworkAvailable()) {
                onResult(Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8").waitFor() == 0)
            } else {
                onResult(false)
            }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    private fun hasNetworkAvailable(): Boolean {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}


