package com.mohan.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mohan.weatherapp.adapter.ForecastAdapter
import com.mohan.weatherapp.data.Country
import com.mohan.weatherapp.data.CountryInfo
import com.mohan.weatherapp.data.Forecast
import com.mohan.weatherapp.utils.NetworkHelper
import com.mohan.weatherapp.view.ForecastView
import com.mohan.weatherapp.viewmodel.WeatherInfoViewModel
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.DiscreteScrollView.OnItemChangedListener
import com.yarolegovich.discretescrollview.DiscreteScrollView.ScrollStateChangeListener
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.coroutines.CoroutineContext


class WeatherActivity : AppCompatActivity(),
    ScrollStateChangeListener<ForecastAdapter.ViewHolder>,
    OnItemChangedListener<ForecastAdapter.ViewHolder?>, CoroutineScope {

    private val networkHelper: NetworkHelper by inject()

    private lateinit var job: Job
    private lateinit var forecastView: ForecastView
    private lateinit var cityPicker: DiscreteScrollView
    private lateinit var progressBar :ProgressBar
    private lateinit var parentLayout: View

    private var forecasts: MutableList<Forecast> = mutableListOf()
    private val weatherInfoViewModel by viewModel<WeatherInfoViewModel>()
    private lateinit var countryInfo: List<Country>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_weather)
        job = Job()
        countryInfo = CountryInfo.countryInfo()
        setUpUI(countryInfo)
    }

    private fun setUpUI(countryInfo: List<Country>) {
        parentLayout = findViewById(android.R.id.content)
        progressBar = findViewById(R.id.progress_circular)
        forecastView = findViewById(R.id.forecast_view)
        cityPicker = findViewById(R.id.forecast_city_picker)
        cityPicker.setSlideOnFling(true)
        cityPicker.adapter = ForecastAdapter(countryInfo)
        cityPicker.addOnItemChangedListener(this)
        cityPicker.addScrollStateChangeListener(this)
        cityPicker.scrollToPosition(DEFAULT_POSITION)
        cityPicker.setItemTransformer(
            ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build()
        )
    }

    override fun onCurrentItemChanged(holder: ForecastAdapter.ViewHolder?, position: Int) {
        forecasts.clear()
        if (holder != null) {
            progressBar.visibility = View.VISIBLE
            weatherApiCall(position)
            holder.showText()
        }
    }

    private fun weatherApiCall(position: Int) {
        val cityName = countryInfo[position].cityName
        networkHelper.hasInternet { isNetworkAvailable ->
            if (isNetworkAvailable){
                weatherInfoViewModel.locationSearch(cityName)
                runOnUiThread {
                    updateForecastView()
                }
            }else{
                Snackbar
                    .make(parentLayout, "No Internet Connection", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun updateForecastView(){
        launch {
            withContext(Dispatchers.Main) {
                delay(1000)
                weatherInfoObserver()
            }
        }
        progressBar.visibility = View.GONE
    }

    private fun weatherInfoObserver() {
        weatherInfoViewModel.weatherInfoLiveData.observe(this, {
            Timber.d("ForeCast data from api:  $it")
            forecasts.add(it).also {
                setUpForecastView()
            }
        })
    }

    private fun setUpForecastView() {
        forecastView.setForecast(forecasts[DEFAULT_POSITION])
    }

    override fun onScrollStart(holder: ForecastAdapter.ViewHolder, position: Int) {
        holder.hideText()
    }

    override fun onScroll(
        position: Float,
        currentIndex: Int, newIndex: Int,
        currentHolder: ForecastAdapter.ViewHolder?,
        newHolder: ForecastAdapter.ViewHolder?
    ) {
        val current = countryInfo[currentIndex]
        val adapter = cityPicker.adapter
        val itemCount = adapter?.itemCount ?: DEFAULT_POSITION
        if (newIndex in DEFAULT_POSITION until itemCount) {
            val next = countryInfo[newIndex]
            forecastView.onScroll(1f - Math.abs(position), current, next)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherInfoViewModel.destroy()
        job.cancel()
    }

    override fun onScrollEnd(holder: ForecastAdapter.ViewHolder, position: Int) {}
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    companion object{
        private const val DEFAULT_POSITION = 0
    }

}
