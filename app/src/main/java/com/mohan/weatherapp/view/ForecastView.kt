package com.mohan.weatherapp.view

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ArrayRes
import com.bumptech.glide.Glide
import com.mohan.weatherapp.R
import com.mohan.weatherapp.data.Country
import com.mohan.weatherapp.data.Forecast
import com.mohan.weatherapp.data.Weather

class ForecastView : LinearLayout {
    private var gradientPaint: Paint? = null
    private lateinit var currentGradient: IntArray
    private var weatherDescription: TextView? = null
    private var weatherTemperature: TextView? = null
    private var weatherMinTemp: TextView? = null
    private var weatherMaxTemp: TextView? = null
    private var weatherHumidity: TextView? = null
    private var weatherWind: TextView? = null
    private var weatherTomorrow: TextView? = null
    private var weatherImage: ImageView? = null
    private var evaluator: ArgbEvaluator? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun initGradient() {
        val centerX = width * 0.5f
        val gradient: Shader = LinearGradient(centerX, 0f, centerX, height.toFloat(),
            currentGradient, null,
            Shader.TileMode.MIRROR
        )
        gradientPaint!!.shader = gradient
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        currentGradient = colors(R.array.gradientPeriodicClouds)
        initGradient()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), gradientPaint!!)
        super.onDraw(canvas)
    }

    @SuppressLint("SetTextI18n")
    fun setForecast(forecast: Forecast) {
        val state: Weather = Weather.fromString(forecast.weather_state)
        currentGradient = weatherToGradient(state)
        if (width != 0 && height != 0) {
            initGradient()
        }
        weatherDescription!!.text = forecast.weather_state
        weatherTemperature!!.text = forecast.temperature + "\u2103"
        weatherMinTemp!!.text = "MinTemp: "+forecast.minTemp +"\u2103"
        weatherMaxTemp!!.text = "MaxTemp: "+forecast.maxTemp +"\u2103"
        weatherTomorrow!!.text = "Tomorrow's Weather"
        weatherHumidity!!.text = "Humidity: "+forecast.humidity +"%"
        weatherWind!!.text = "Wind Speed: "+forecast.wind_speed +" m/s"
        Glide.with(context).load(forecast.weatherUrl).into(weatherImage!!)
        invalidate()
        weatherImage!!.animate()
            .scaleX(1f).scaleY(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(300)
            .start()
    }

    fun onScroll(fraction: Float, oldF: Country, newF: Country) {
        weatherImage!!.scaleX = fraction
        weatherImage!!.scaleY = fraction
        currentGradient = mix(
            fraction,
            weatherToGradient(Weather.fromString("Clear")),
            weatherToGradient(Weather.fromString("Clear"))
        )
        initGradient()
        invalidate()
    }

    private fun mix(fraction: Float, c1: IntArray, c2: IntArray): IntArray {
        return intArrayOf(
            (evaluator!!.evaluate(fraction, c1[0], c2[0]) as Int),
            (evaluator!!.evaluate(fraction, c1[1], c2[1]) as Int),
            (evaluator!!.evaluate(fraction, c1[2], c2[2]) as Int)
        )
    }

    private fun weatherToGradient(weather: Weather): IntArray {
        return when (weather) {
            Weather.LIGHT_CLOUD -> colors(R.array.gradientPeriodicClouds)
            Weather.HEAVY_CLOUD -> colors(R.array.gradientCloudy)
            Weather.SHOWERS -> colors(R.array.gradientMostlyCloudy)
            Weather.LIGHT_RAIN -> colors(R.array.gradientPartlyCloudy)
            Weather.CLEAR -> colors(R.array.gradientClear)
            Weather.SNOW -> colors(R.array.gradientSnow)
            Weather.SLEET -> colors(R.array.gradientSleet)
            Weather.HAIL -> colors(R.array.gradientHail)
            Weather.THUNDERSTORM -> colors(R.array.gradientThunder)
            Weather.HEAVY_RAIN -> colors(R.array.gradientHeavyRain)
        }
    }

    private fun colors(@ArrayRes res: Int): IntArray {
        return context.resources.getIntArray(res)
    }

    init {
        evaluator = ArgbEvaluator()
        gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        setWillNotDraw(false)
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        inflate(context, R.layout.view_forecast, this)
        weatherDescription = findViewById(R.id.weather_description)
        weatherImage = findViewById(R.id.weather_image)
        weatherTemperature = findViewById(R.id.weather_temperature)
        weatherMinTemp = findViewById(R.id.weather_mintemp)
        weatherMaxTemp = findViewById(R.id.weather_maxtemp)
        weatherTomorrow = findViewById(R.id.weather_tomorrow)
        weatherHumidity = findViewById(R.id.weather_humidity)
        weatherWind = findViewById(R.id.weather_wind)
    }
}

operator fun <T> T.plus(str: String): CharSequence {
    return this.toString() + str
}
