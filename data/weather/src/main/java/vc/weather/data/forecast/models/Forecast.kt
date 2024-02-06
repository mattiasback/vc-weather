package vc.weather.data.forecast.models

import vc.weather.data.forecast.remote.RemoteForecast
import kotlin.math.roundToInt

data class Forecast(
    val cityId: Int,
    val cityName: String,
    val weather: String,
    val description: String,
    val temperature: String,
    val feelsLike: String,
    val iconUrl: String,
)

internal fun RemoteForecast.toForecast(): Forecast {
    val primaryWeather = weather.firstOrNull()
    return Forecast(
        cityId = id ?: 0,
        cityName = name ?: "",
        weather = primaryWeather?.main ?: "",
        description = primaryWeather?.description ?: "",
        iconUrl = primaryWeather?.let { "https://openweathermap.org/img/wn/${it.icon}@2x.png" } ?: "",
        temperature = main?.temp?.roundToInt()?.toString() ?: "",
        feelsLike = main?.feelsLike?.roundToInt()?.toString() ?: "",
    )
}
